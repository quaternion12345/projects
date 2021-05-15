import cv2, time
import numpy as np


def correct_chk(correct_check):
    if ((correct_check[0] and correct_check[1] and 
    correct_check[2] and correct_check[3]) == True):
        print('True')
        return True
    else:
        #print('F')
        return False
        

target = np.array([[400, 280], [226, 280], [400, 200], [226, 200]], np.int32)
target_rect = np.array([[400, 280],[400, 200],[226, 200], [226, 280]], np.int32)
target_start_x = 226
target_start_y = 200
target_end_x = 400
target_end_y = 280

cap = cv2.VideoCapture(0)
while(cap.isOpened()):    
    _, img = cap.read()
    #fps = cap.get(cv2.CAP_PROP_FPS)
    #delay = cap.get(cv2.CAP_PROP_POS_MSEC)
    #print('delay',delay)
    if (_):
        dst = img[target_start_y:target_end_y, target_start_x:target_end_x]
        gray = cv2.cvtColor(dst, cv2.COLOR_RGB2GRAY)
        corners = cv2.goodFeaturesToTrack(gray, 4, 0.1, 5, blockSize=3, useHarrisDetector=False, k=0.03)
        correct_check = [False, False, False, False]
        target_index = 0
        for corner in corners:
            if((tuple(corner[0]) == target[target_index]).all()):  # tuple(corner[0])= corner(x,y)
                correct_check[target_index]=True

            target_index = target_index + 1

            x, y = tuple(corner[0])
            x = int(x + target_start_x)
            y = int(y + target_start_y)
            
            cv2.circle(img, (x,y), 3, (0, 0, 255), 10)

        if(correct_chk(correct_check)==True):
            print('Save motor steps') # multiprocess steps count OR motor active time
            cv2.putText(img, "Save motor steps", (10, 30),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 0), 2)
        
        img = cv2.polylines(img, [target_rect], True, (190, 252, 180), 5)
        cv2.imshow('img', img)       
        if cv2.waitKey(50) & 0xFF == ord('q'): break
    else:break
    
cap.release()
cv2.destroyAllWindows()