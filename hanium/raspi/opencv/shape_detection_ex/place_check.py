import cv2
import numpy as np

def move_down(img):
    height, width = img.shape[:2]
    M = np.float32([[1, 0, 0], [0, 1, -25]]) # 25 down
    img_translation = cv2.warpAffine(img, M, (width,height))
    #cv2.imshow("translation", img_translation)
    return img_translation



def img_init(img):
    height, width = img.shape[:2]
    M = np.float32([[1, 0, 0], [0, 1, 125]]) # 25 up
    img_translation = cv2.warpAffine(img, M, (width,height))
    #cv2.imshow("translation", img_translation)
    return img_translation

target = np.array([[497, 317], [226, 317], [497, 129], [226, 129]], np.int32)
target_rect = np.array([[497, 317],[497, 129],[226, 129], [226, 317]], np.int32)
src = cv2.imread("area_fill.png")
src = img_init(src)

for ii in range(10):
    dst = src.copy()
    gray = cv2.cvtColor(dst, cv2.COLOR_RGB2GRAY)
    corners = cv2.goodFeaturesToTrack(gray, 4, 0.1, 5, blockSize=3, useHarrisDetector=False, k=0.03)
    #print('corners',corners)
    for i in corners:
        target_index=0
        if((tuple(i[0])==target[target_index]).min()): # tuple(i[0])= corner(x,y)
            print('Correct!!! Save(x,y)')
            
        target_index = target_index + 1
        
        cv2.circle(dst, tuple(i[0]), 3, (0, 0, 255-ii*15), 10)
        
    dst = cv2.polylines(dst, [target_rect], True, (190,252,180),5)
    
    cv2.imshow("display", dst)
    cv2.waitKey(0)
    
    src = move_down(src)
cv2.destroyAllWindows()

'''
#cap = cv2.VideoCapture(0)
while(True): 
    _, img = cap.read()
    if _ is not None:
    
        cv2.imshow('img', img)
        if cv2.waitKey(50) & 0xFF == ord('q'): break
'''
