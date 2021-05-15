import cv2, time
import numpy as np

from bluetooth import * 

def correct_chk(coordinate, target):
    count=0
    for i in range(0,len(coordinate)):
        coordinate[i][0] += target_start_x
        coordinate[i][1] += target_start_y

        for j in range(0,len(coordinate)):
            if (abs(coordinate[i][0]-target[j][0]) < 20):
                if(abs(coordinate[i][1]-target[j][1]) < 20):
                    count+=1        
                    break
    if(count==4):
        #print('correct')
        return True
    else:
        #print('fail')
        return False
        

target = np.array([[200,190], [360, 190], [360,320], [200,320]], np.int32)
target_rect = np.array([[360, 320],[360, 190],[200,190], [200,320]], np.int32)

target_start_x = 200
target_start_y = 190
target_end_x = 360
target_end_y = 320


client_socket = BluetoothSocket( RFCOMM )
client_socket.connect(("98:D3:71:FD:79:10",1))

msg = '!'
if( msg == '!'):
    client_socket.send(msg)


cap = cv2.VideoCapture(0)
while(cap.isOpened()):    
    _, img = cap.read()
    
    if (_):
        coordinate = []
        #dst = img
        dst = img[target_start_y:target_end_y, target_start_x:target_end_x]
        gray = cv2.cvtColor(dst, cv2.COLOR_RGB2GRAY)
        corners = cv2.goodFeaturesToTrack(gray, 4, 0.1, 5, blockSize=3, useHarrisDetector=False, k=0.03)
        
        for corner in corners:
            coordinate.append(list(corner[0])) 

            x, y = list(corner[0])
            x = int(x + target_start_x)
            y = int(y + target_start_y)
            cv2.circle(img, (x,y), 3, (0, 0, 255), 10)
            
        if (correct_chk(coordinate, target)):
            print('signal')
            msg = 'A'
            if( msg == 'A'):
                client_socket.send(msg)
            msg = client_socket.recv(1024)
            client_socket.send('!')
            print(msg)
        
        img = cv2.polylines(img, [target_rect], True, (190, 252, 180), 5)
        cv2.imshow('Container CAM', img)       
        if cv2.waitKey(50) & 0xFF == ord('q'): break
    else:break

client_socket.close()
    
cap.release()
cv2.destroyAllWindows()