import cv2, time
import numpy as np

from multiprocessing import Process, Queue
from bluetooth import * 

target = np.array([[210,195], [370, 195], [370,315], [210,315]], np.int32)
target_rect = np.array([[370, 315],[370, 195],[210,195], [210,315]], np.int32)

target_start_x = 200
target_start_y = 195
target_end_x = 380
target_end_y = 315

overlap_check = True
overlap_check_tmp = 0
overlap_index = 0

client_socket = BluetoothSocket( RFCOMM )
client_socket.connect(("98:D3:71:FD:79:10",1))
#client_socket.close()
q = Queue()
lst = []

def correct_chk(coordinate, target):
    count=0
    if len(coordinate)!= 4:
        return False
    tmp = target.tolist()
    for i in range(4):
        coordinate[i][0] += target_start_x
        coordinate[i][1] += target_start_y
        
        for j in range(len(tmp)):            
            if (abs(coordinate[i][0]-tmp[j][0]) < 33):
                if(abs(coordinate[i][1]-tmp[j][1]) < 33):
                    count+=1
                    tmp.remove(tmp[j])
                    break
    if(count==4):
        #print('correct')
        return True
    else:
        #print('fail')
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
        cv2.circle(img, (x,y), 3, (0, 0, 255), 10)

    return img, coordinate        


def Bluetooth_init():
    msg = '!'
    client_socket.send(msg)
    client_socket.sendall("he")

def Bluetooth_send(msg):
    client_socket.sendall(msg)

def Bluetooth_recv(q):
    msg =  client_socket.recv(1024)
    q.put(msg.decode())
    print("recv : ",end='')
    print(msg)
    
    if (q.qsize() == 3):               
        Bluetooth_send('#')                      

def finish():
    client_socket.close()    
    cap.release()    

cap = cv2.VideoCapture(0)
Bluetooth_init()

while(cap.isOpened()):    
    _, img = cap.read()
    
    if (_):
        img, coordinate= draw_vertex(img)
        
        if (correct_chk(coordinate, target) and overlap_check == True):
            overlap_check_tmp = overlap_index
            overlap_check = False
            print('signal')
            Bluetooth_send('A')
            p = Process(target = Bluetooth_recv, args = (q,))
            p.start()
            
        if((not overlap_check) and overlap_index == (overlap_check_tmp + 10)):
                overlap_check = True
                overlap_index = 0                

        overlap_index += 1
        img = cv2.polylines(img, [target_rect], True, (190, 252, 180), 5)
        cv2.imshow('Container CAM', img)

        if (q.qsize() == 3):                               
            for i in range(3):
                lst.append(q.get())
            cv2.destroyAllWindows()
            break
        
        if cv2.waitKey(50) & 0xFF == ord('q'): break
    else:break
p.join()
msg =  client_socket.recv(1024)
if msg.decode() == 'Q':
    print('receive')
    Bluetooth_send('&')
    time.sleep(1)
    Bluetooth_send(lst[0])

print(lst)
finish()
