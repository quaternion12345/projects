import cv2

cap = cv2.VideoCapture(0)

while(cap.isOpened()): 
    _, img = cap.read()
    if _ is not None:
        cv2.imshow("img", img)
        if cv2.waitKey(50) & 0xFF == ord('q'): break

cap.release()
cv2.destroyAllWindows()
