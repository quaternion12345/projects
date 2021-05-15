import RPi.GPIO as GPIO
import time, socket, cv2
from pyzbar import pyzbar
from bluetooth import *
from spreader_motor import *

#firebase
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db


HOST = '172.30.1.44'
PORT = 8000

d_flag = 0

grab_flag = False

container_num = -1

cred = credentials.Certificate('hanium-c1535-firebase-adminsdk-rkkeh-5ccf7fd2f1.json')
firebase_admin.initialize_app(cred,{'databaseURL' : 'https://hanium-c1535.firebaseio.com/'})

class Container(object) :
    def __init__(self):
        self.Cid = []
        self.classification = []
        self.corp = []
        self.weight = []
        
    def add(self,Cid , classification, corp, weight):
        self.Cid.append(Cid)
        self.classification.append(classification)
        self.corp.append(corp)
        self.weight.append(weight)

container = Container()
ref = db.reference('/container')

# Create the client socket
client_socket=BluetoothSocket( RFCOMM )
#client_socket.close()
client_socket.connect(("98:D3:71:F9:B7:A2", 1))

# Connect to the server
conn = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('3333333333')
conn.connect((HOST,PORT))

GPIO.setwarnings(False) 
GPIO.setmode(GPIO.BCM) #Pin mode setup

pressure = 17
press_flag = False
GPIO.setup(pressure, GPIO.IN)
##################
color_g_on = (0,255,0)
color_y_on = (0,255,255)
color_r_on = (0,0,255)

color_g_off = (0,60,0)
color_y_off = (0,60,60)
color_r_off = (0,0,60)

light_g_center = (580, 240)
light_y_center = (580, 330)
light_r_center = (580, 420)

def QR_light(img,x,y, qr_flag):
    if((250<x and x<390) and (200<y and y<290)): # green
        cv2.circle(img, light_g_center, 40, color_g_on,-1)
        cv2.circle(img, light_y_center, 40, color_y_off,-1)
        cv2.circle(img, light_r_center, 40, color_r_off,-1)
        qr_flag = False
        
    elif((180<x and x<460) and (100<y and y<380)): # yellow
        cv2.circle(img, light_g_center, 40, color_g_off,-1)
        cv2.circle(img, light_y_center, 40, color_y_on,-1)
        cv2.circle(img, light_r_center, 40, color_r_off,-1)
        qr_flag = True

    else: # red
        cv2.circle(img, light_g_center, 40, color_g_off,-1)
        cv2.circle(img, light_y_center, 40, color_y_off,-1)
        cv2.circle(img, light_r_center, 40, color_r_on,-1)
        qr_flag = True
    
    return qr_flag
    
def QR_light_red(img):
    cv2.circle(img, light_g_center, 40, color_g_off,-1)
    cv2.circle(img, light_y_center, 40, color_y_off,-1)
    cv2.circle(img, light_r_center, 40, color_r_on,-1)
        
def barcode_detect():
    global container_num

    print("[INFO] starting video stream...")
    found = set()
    cap = cv2.VideoCapture(0)

    qr_flag = True
    while (qr_flag):
        _, frame = cap.read()
        barcodes = pyzbar.decode(frame)
        QR_light_red(frame)
        for barcode in barcodes:
            (x, y, w, h) = barcode.rect
            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
            barcodeData = barcode.data.decode("utf-8")
            barcodeType = barcode.type
            text = "{} ({})".format(barcodeData, barcodeType)
            
            barcode_center_x = x + w/2
            barcode_center_y = y + h/2
            qr_flag = QR_light(frame, barcode_center_x, barcode_center_y, qr_flag)
            
            cv2.putText(frame, text, (x, y - 10),
            cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
            
            if (barcodeData not in found) and (not qr_flag):
                #######firebase_db
                container_num = container_num + 1
                Cid = text[0:3]
                corp = text[3:5]
                weight = text[5:7]
                classification = text[7:9]
                container.add(Cid=Cid, corp=corp, weight=weight,classification=classification)
                ref.update({container_num:{'Cid': container.Cid[container_num], 'corp':container.corp[container_num], 'weight':container.weight[container_num], 'classification':container.classification[container_num]}})
                #s.send('c ready'.encode()) # container ready
                #################
                
                found.add(barcodeData)
                print(barcodeData)
                break
            
        cv2.imshow("Barcode Scanner", frame)
        key = cv2.waitKey(1) & 0xFF
        if key == ord("q"):break

    print("[INFO] cleaning up...")
    time.sleep(2)
    conn.send('w'.encode())
    cv2.destroyAllWindows()
    return True
    

def release():
    spreader_open()
    time.sleep(1.4)
    spreader_stop()
    conn.send('r'.encode())
    return True
    
def release_init():
    spreader_open()
    time.sleep(1.0)
    spreader_stop()
    conn.send('r'.encode())
    return True

def grab_init():
    spreader_close()
    time.sleep(2.7)
    spreader_stop()
    conn.send('g'.encode())
    return True

def grab():
    spreader_close()
    time.sleep(1.6)
    spreader_stop()
    conn.send('g'.encode())
    return True
    
def down(floor):
    if floor == 1:
        height = '-'
    elif floor == 2:
        height = '$'
    else:
        print('error')
    client_socket.send('D')
    client_socket.send(height)
    msg = client_socket.recv(1024)
    print('down msg',msg)
    if msg.decode() == 'q':
        print('down recv : q')
        conn.send('d'.encode())
        return True
        
                
def up(floor):
    if floor == 1:
        height = '-'
    elif floor == 2:
        height = '$'
    else:
        print('error')
        
    client_socket.send('U')
    client_socket.send(height)
    msg = client_socket.recv(1024)
    if msg.decode() == 'r':
        conn.send('u'.encode())
        return True
   


index = 0
try:
    client_socket.send('U')
    client_socket.send('.')
    msg = client_socket.recv(1024)
    print('start blt msg',msg)
    while(True):
        msg = conn.recv(1024)
        print('main msg :',msg)
        
        if msg.decode()=='up1':
            up(1)
        elif msg.decode()=='up2':
            up(2)
        elif msg.decode()=='down1':
            down(1)
        elif msg.decode()=='down2':
            down(2)
        elif msg.decode()=='Grab':
            grab()
        elif msg.decode()=='Grab_init':
            grab_init()
        elif msg.decode()=='release':
            release()
        elif msg.decode()=='release_init':
            release_init()
        elif msg.decode()=='qr':
            barcode_detect()
        
        
except KeyboardInterrupt:
    spreader_stop()
    pass

finally:
    GPIO.cleanup()