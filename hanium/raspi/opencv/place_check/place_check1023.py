import cv2, time
import numpy as np
import socket
import copy

# (HOST, PORT)
HOST = '172.30.1.55'
PORT = 4000

from multiprocessing import Process, Queue
from bluetooth import *


#firebase
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db


cred = credentials.Certificate('hanium-c1535-firebase-adminsdk-rkkeh-5ccf7fd2f1.json')
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://hanium-c1535.firebaseio.com/'
    })

loc_A = db.reference('place/A')
loc_B = db.reference('place/B')
loc_C = db.reference('place/C')



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


def send_coord(sock, conn, lst):
    '''
        send coordinate to arduino
    '''
    # check start
    msg = sock.recv(1024)
    if msg.decode() == 'Q':
        print('receive')
        qwe=copy.deepcopy(lst)
        print('len(qwe)',len(qwe))
        for i in range(len(qwe)):
            msg = conn.recv(1024)
            if msg.decode() == 'y':   
                sock.send('~')
                msg = sock.recv(1024)
                print("recv_upardu_~")
                if msg.decode() == '~':
                    print('send:' + qwe[i])
                    sock.send(qwe[i])
                    msg = sock.recv(1024)  # wait for arrival
                    # send to another raspberrypi
                    if(msg.decode() =='s'):
                        conn.send('start'.encode())
                        print('spreader start')
                        # wait for reply
                        msg = conn.recv(1024)
                        print("@form_downraps",msg)
                        if (msg.decode() == '@'):
                            print("Recv:" , msg)
                            # send signal to arduino
                            sock.send('}')
                            msg = sock.recv(1024)
                            print(msg, "1 }")
                            if msg.decode() == '}':
                                sock.send(qwe[i])
                                msg = sock.recv(1024)
                                conn.send(msg)
                                

    conn.close()  # close connection socket

try:
    #cap = cv2.VideoCapture(http://172.30.1.55:8091/stream_simple.html)
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
    send_coord(client_socket, conn, lst)  # sender function
    print(lst)
    finish()
except socket.error:
    print('error')
#except Exception as e:
#    print('error :', e)
#    finish()
