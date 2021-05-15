import cv2,time
from pyzbar import pyzbar

cap = cv2.VideoCapture(0)
found = set()

color_g_on = (0,255,0)
color_y_on = (0,255,255)
color_r_on = (0,0,255)

color_g_off = (0,60,0)
color_y_off = (0,60,60)
color_r_off = (0,0,60)

light_g_center = (580, 240)
light_y_center = (580, 330)
light_r_center = (580, 420)
def QR_light(img,x,y):
    if((230<x and x<410) and (180<y and y<310)): # green
        cv2.circle(img, light_g_center, 40, color_g_on,-1)
        cv2.circle(img, light_y_center, 40, color_y_off,-1)
        cv2.circle(img, light_r_center, 40, color_r_off,-1)

    elif((180<x and x<460) and (100<y and y<380)): # yellow
        cv2.circle(img, light_g_center, 40, color_g_off,-1)
        cv2.circle(img, light_y_center, 40, color_y_on,-1)
        cv2.circle(img, light_r_center, 40, color_r_off,-1)

    else: # red
        cv2.circle(img, light_g_center, 40, color_g_off,-1)
        cv2.circle(img, light_y_center, 40, color_y_off,-1)
        cv2.circle(img, light_r_center, 40, color_r_on,-1)
        

while(cap.isOpened()): 
    
    _, img = cap.read()
    if _ is not None:
        barcodes = pyzbar.decode(img)
        for barcode in barcodes:
            (x, y, w, h) = barcode.rect
            barcode_center_x = x + w/2
            barcode_center_y = y + h/2
            QR_light(img, barcode_center_x, barcode_center_y)
            
            
            cv2.rectangle(img, (x, y), (x + w, y + h), (0, 0, 255), 2)
            barcodeData = barcode.data.decode("utf-8")
            barcodeType = barcode.type            
            text = "{} ({})".format(barcodeData, barcodeType)
            cv2.putText(img, text, (x, y - 10),
            cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
            if barcodeData not in found:
                found.add(barcodeData)
        
        cv2.imshow("img", img)
        if cv2.waitKey(50) & 0xFF == ord('q'): break

cap.release()
cv2.destroyAllWindows()
