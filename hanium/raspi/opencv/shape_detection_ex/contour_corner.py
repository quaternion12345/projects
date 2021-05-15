import cv2, sys
from matplotlib import pyplot as plt
import numpy as np
import time

flag = True
cap = cv2.VideoCapture(0)

while(flag):
    flag=False
    _, image = cap.read()
    #time.sleep(1)
    if _ is not None:
        #image = cv2.resize(image, (720, 480), interpolation=cv2.INTER_AREA)
        image = cv2.imread('nemo.png')
        dst = image.copy()
        image_gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY) # 그레이스케일
        b,g,r = cv2.split(image)
        image2 = cv2.merge([r,g,b])

        blur = cv2.GaussianBlur(image_gray, ksize=(3,3), sigmaX=0)
        ret, thresh1 = cv2.threshold(blur, 170, 255, cv2.THRESH_BINARY)
        
        edged = cv2.Canny(blur, 10, 250)
        #kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (7,7))
        #closed = cv2.morphologyEx(edged, cv2.MORPH_CLOSE, kernel)
        
        
        __, contours,_ = cv2.findContours(edged.copy(),cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        total = 0
        # 외곽선 그리는 용도. 이미지에 그리기 때문에 이 코드 적용하면 원래 이미지에
        # 초록색 선 생김
        contours_image = cv2.drawContours(image, contours, -1, (0,255,0), 3)
        
        
        '''
        contours_xy = np.array(contours)
        contours_xy.shape # =8
    
    
        
        # x의 min과 max 찾기
        x_min, x_max = 0,0
        value = list()
        for i in range(len(contours_xy)):
            for j in range(len(contours_xy[i])):
                value.append(contours_xy[i][j][0][0]) #네번째 괄호가 0일때 x의 값
                x_min = min(value)
                x_max = max(value)
        print("x_min :",x_min)
        print("x_max :",x_max)
         
        # y의 min과 max 찾기
        y_min, y_max = 0,0
        value = list()
        for i in range(len(contours_xy)):
            for j in range(len(contours_xy[i])):
                value.append(contours_xy[i][j][0][1]) #네번째 괄호가 0일때 x의 값
                y_min = min(value)
                y_max = max(value)
        print("y_min :",y_min)
        print("y_max :",y_max)
        
        # 이미지 가로, 세로 값
        x = x_min
        y = y_min
        w = x_max-x_min
        h = y_max-y_min
        
        #https://youbidan.tistory.com/19
        #==========================================================================================
        #==========================================================================================
        
        
        corners = cv2.goodFeaturesToTrack(image_gray, 100, 0.01, 5, blockSize=3, useHarrisDetector=True, k=0.03)
        corners = np.int0(corners) # 우측하단->좌측하단->우측상단->좌측상단
        
        #print(corners)
        
        p1_x=corners[0][0][0]   # p4--------------------p3
        p1_y=corners[0][0][1]   # |                     |
        p2_x=corners[1][0][0]   # |                     |
        p2_y=corners[1][0][1]   # p2--------------------p1
        p3_x=corners[2][0][0]
        p3_y=corners[2][0][1]
        p4_x=corners[3][0][0]
        p4_y=corners[3][0][1]
        
        
        for i in corners:
            cv2.circle(contours_image, tuple(i[0]), 3, (0, 0, 255), 5)
        '''
        cv2.imshow('contours_image', contours_image)                
        if cv2.waitKey(50) & 0xFF == ord('q'): break
#plt.imshow(image)
#plt.show()

#cv2.destroyAllWindows()
#https://076923.github.io/posts/Python-opencv-23/
