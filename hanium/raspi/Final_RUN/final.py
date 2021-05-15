import cv2, time
import numpy as np
import socket
import copy
from weight_algorithm_1024 import *

# (HOST, PORT)
HOST = '172.30.1.44'
PORT = 8000

from multiprocessing import Process, Queue
from bluetooth import *


#firebase
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db


cred = credentials.Certificate('hanium-c1535-firebase-adminsdk-rkkeh-5ccf7fd2f1.json')
firebase_admin.initialize_app(cred,{'databaseURL' : 'https://hanium-c1535.firebaseio.com/'})

loc_A = db.reference('place/A')
loc_B = db.reference('place/B')
loc_C = db.reference('place/C')

container_num = 0

# from sender import send_coord

target_start_x = 230
target_start_y = 195
target_end_x = 395
target_end_y = 315

target = np.array([[target_start_x, 195], [target_end_x, 195], [target_end_x, 315], [target_start_x, 315]], np.int32)
target_rect = np.array([[target_end_x, 315], [target_end_x, 195], [target_start_x, 195], [target_start_x, 315]], np.int32)


overlap_check = True
overlap_check_tmp = 0
overlap_index = 0

try:# handling error exception
    client_socket = BluetoothSocket(RFCOMM)
    client_socket.connect(("98:D3:71:FD:79:10", 1))
    # client_socket.close()
    q = Queue()
    lst = []

    # open server socket
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR,1)
    print('Socket created')
    s.bind((HOST, PORT))
    s.listen(5)  # listening maximum 5 connection socket
    print('Socket is awaiting client')
except Exception as e:
    print('Bind failed.', e)


def correct_chk(coordinate, target):
    count = 0
    if len(coordinate) != 4:
        return False
    tmp = target.tolist()
    for i in range(4):
        coordinate[i][0] += target_start_x
        coordinate[i][1] += target_start_y

        for j in range(len(tmp)):
            if (abs(coordinate[i][0] - tmp[j][0]) < 30):
                if (abs(coordinate[i][1] - tmp[j][1]) < 30):
                    count += 1
                    tmp.remove(tmp[j])
                    break
    if (count == 4):
        # print('correct')
        return True
    else:
        # print('fail')
        return False


def draw_vertex(img):
    coordinate = []
    dst = img[target_start_y:target_end_y, target_start_x:target_end_x]
    gray = cv2.cvtColor(dst, cv2.COLOR_RGB2GRAY)
    corners = cv2.goodFeaturesToTrack(gray, 4, 0.1, 5, blockSize=3, useHarrisDetector=False, k=0.03)

    for corner in corners:
        coordinate.append(list(corner[0]))
        x, y = list(corner[0])
        x = int(x + target_start_x)
        y = int(y + target_start_y)
        cv2.circle(img, (x, y), 3, (0, 0, 255), 10)

    return img, coordinate


def Bluetooth_init():
    msg = '?'
    client_socket.send(msg)


def Bluetooth_send(msg):
    client_socket.sendall(msg)


def Bluetooth_recv(q):
    msg = client_socket.recv(1024)
    q.put(msg.decode())
    print("recv : ", end='')
    print(msg)

    if (q.qsize() == 3):
        Bluetooth_send('{')
        


def finish():
    client_socket.close()
    cap.release()

def move_forward(ble,location):
    ble.send('~')
    msg= ble.recv(1024)
    if msg.decode()=='~':
        ble.send(location)
        msg = ble.recv(1024)
        if msg.decode()=='s':
            return True
    else:
        return False

def move_back(ble,location):
    ble.send('}')
    msg= ble.recv(1024)
    if msg.decode()=='}':
        ble.send(location)
        msg = ble.recv(1024)
        if msg.decode()=='z':
            return True
    else:
        return False

def release_init(conn):
    conn.send('release_init'.encode())
    print('release_init')
    msg = conn.recv(1024)
    print(msg)
    if msg.decode()=='r':
        return True


def grab_init(conn_init):
    conn.send('Grab_init'.encode())
    print('Grab_init')
    msg = conn.recv(1024)
    if msg.decode()=='g':
        return True

def release(conn):
    conn.send('release'.encode())
    print('release')
    msg = conn.recv(1024)
    print(msg)
    if msg.decode()=='r':
        return True


def grab(conn):
    conn.send('Grab'.encode())
    print('Grab')
    msg = conn.recv(1024)
    if msg.decode()=='g':
        return True


def up(conn, floor):
    if floor == 1:
        string = 'up1'
    elif floor == 2:
        string = 'up2'
    conn.send(string.encode())
    print("spreader_up")
    msg = conn.recv(1024)
    if msg.decode()=='u':
        return True

def down(conn, floor):
    if floor == 1:
        string = 'down1'
    elif floor == 2:
        string = 'down2'
    conn.send(string.encode())
    print('spreader_down',floor)
    msg = conn.recv(1024)
    if msg.decode()=='d':
        return True

def qr(conn):
    conn.send('qr'.encode())
    msg = conn.recv(1024)
    print('qr',msg)
    if msg.decode()=='w':
        print('qr finish')
        return True
    else:
        return False
    

def db_container_num_plus(location):
    if location == 1:
        loc_num = db.reference('place/A')
        tmp_num = loc_num.get()
    elif location == 2:
        loc_num = db.reference('place/B')
        tmp_num = loc_num.get()
    else :
        loc_num = db.reference('place/C')
        tmp_num = loc_num.get()
    loc_num.update({'num':str(int(tmp_num['num'])+1)})


def db_container_num_minus(location):
    if location == 1:
        loc_num = db.reference('place/A')
        tmp_num = loc_num.get()
    elif location == 2:
        loc_num = db.reference('place/B')
        tmp_num = loc_num.get()
    else :
        loc_num = db.reference('place/C')
        tmp_num = loc_num.get()
    loc_num.update({'num':str(int(tmp_num['num'])-1)})

def db_container_weight_update(w_lst, order, flag):
    '''
        w_lst : weight list / order : order list / flag : need rearrange
        w_lst[i] element is placed to order[i]
    '''
    a_weight = 0
    b_weight = 0
    c_weight = 0
    length = 0 # number of cargos
    
    if flag == True: # rearrange case
        length = len(w_lst) - 1
    else: # normal case
        length = len(w_lst)

    for i in range(length):
        if order[i] == 1: # A
            a_weight += w_lst[i]
        elif order[i] == 2: # B
            b_weight += w_lst[i]
        elif order[i] == 3: # C
            c_weight += w_lst[i]
        
    loc_num_A = db.reference('place/A')
    loc_num_A.update({'weight':str(a_weight)})
    loc_num_B = db.reference('place/B')
    loc_num_B.update({'weight':str(b_weight)})
    loc_num_C = db.reference('place/C')
    loc_num_C.update({'weight':str(c_weight)})
                

def send_func(sock, conn, lst):
    global container_num
    # check start
    b_msg = sock.recv(1024)
    if b_msg.decode() == 'Q':
        #up_line_finish
        print('receive')
        i=0
        while(True):
            
            if qr(conn) :
                print('qr success')               
                ref = db.reference('/container'+'/'+str(container_num))
                tmp = ref.get()
                # print("tmp['weight']",type(tmp['weight']))
                weight = int(tmp['weight'])
                container_num += 1
                order, w_lst, change = algorithm(weight)
                floor = ((len(order)-1) // 3) + 1 # floor for normal
                print('floor',floor)
                location = lst[order[i]-1]


                if len(change) > 0: # rearrange
                    print('rearrange')
                    grab(conn) # close
                    pre_order = ord(lst[change[0]-1]) # distance of orignal position
                    post_order = ord(lst[change[1]-1]) # distance of changed position
                    print('pre_order',pre_order)
                    print('post_order',post_order)
                    move_forward(sock, chr(pre_order)) # move to original position
                    release(conn) # open
                    down(conn, floor)
                    grab(conn) # grab
                    up(conn, floor)
                    # move to target location / release
                    if pre_order > post_order:
                        move_back(sock, chr(pre_order - post_order + 20))
                        down(conn, floor)
                        release(conn)
                        up(conn, floor)
                    else:
                        move_forward(sock, chr(post_order - pre_order + 20))
                        down(conn, floor)
                        release(conn)   #release
                        up(conn, floor)
                    db_container_num_plus(change[1])
                    db_container_num_minus(change[0])
                    db_container_weight_update(w_lst, order, True)
                    grab(conn) # close
                    move_back(sock, chr(post_order)) # comeback                
                
                # load start
                release(conn) # open
                print('release success')
                down(conn, 1)# down
                print('down success')
                grab(conn)# grab
                print('grab success')
                up(conn, 1)# up
                print('up success')
                if move_forward(sock, location):
                    down(conn, floor)
                    db_container_num_plus(order[i]) #container_num_plus
                    db_container_weight_update(w_lst, order, False) #container weight
                    if release_init(conn):
                        up(conn, floor)
                        grab_init(conn) # close
                        if not move_back(sock,location): # send signal to arduino
                            print('error')
                        #release(conn) # open                                                      

                i += 1
                print(i,'th iteration')
                
    conn.close()  # close connection socket


try:
    #cap = cv2.VideoCapture('http://172.30.1.44:8091/stream_simple.html')
    cap = cv2.VideoCapture(0)
    Bluetooth_init()
    time.sleep(3)
    while (cap.isOpened()):
        _, img = cap.read()

        if (_):
            img, coordinate = draw_vertex(img)

            if (correct_chk(coordinate, target) and overlap_check == True):
                overlap_check_tmp = overlap_index
                overlap_check = False
                print('signal')
                Bluetooth_send('A')
                p = Process(target=Bluetooth_recv, args=(q,))
                p.start()

            if ((not overlap_check) and overlap_index == (overlap_check_tmp + 10)):
                overlap_check = True
                overlap_index = 0

            overlap_index += 1
            img = cv2.polylines(img, [target_rect], True, (190, 252, 180), 5)
            cv2.imshow('Container CAM', img)

            if (q.qsize() == 3):
                for i in range(3):
                    lst.append(q.get())
                loc_A.update({'location':[lst[0]]})
                loc_B.update({'location':[lst[1]]})
                loc_C.update({'location':[lst[2]]})
                print('scan finish')
                cv2.destroyAllWindows()
                break

            if cv2.waitKey(50) & 0xFF == ord('q'): break
        else:
            break
    p.join()
    (conn, addr) = s.accept()
    print('Connected')

    send_func(client_socket, conn, lst)  # sender function
    
    print(lst)
    finish()
except socket.error:
    print('error')
#except Exception as e:
#    print('error :', e)
#    finish()
