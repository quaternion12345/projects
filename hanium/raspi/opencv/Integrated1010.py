import RPi.GPIO as GPIO
import time, argparse, socket, datetime, cv2
from pyzbar import pyzbar
from bluetooth import *
from spreader_motor import *

HOST = '172.30.1.52'
PORT = 1000

d_flag = 0


grab_flag = False

spreader_open()
#spreader_close()
time.sleep(1.95)
#time.sleep(0.4)
spreader_stop()

# Create the client socket
client_socket=BluetoothSocket( RFCOMM )
client_socket.connect(("98:D3:71:F9:B7:A2", 1))

# Connect to the server
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST,PORT))

GPIO.setwarnings(False) 
GPIO.setmode(GPIO.BCM) #Pin mode setup

pressure = 17
press_flag = False
GPIO.setup(pressure, GPIO.IN)

client_socket.send('U')
client_socket.recv(1024)

ap = argparse.ArgumentParser()
ap.add_argument("-o", "--output", type=str, default="barcodes.csv",
help="path to output CSV file containing barcodes")

args = vars(ap.parse_args())
print("[INFO] starting video stream...")

csv = open(args["output"], "w")

found = set()
cap = cv2.VideoCapture(0)

qr_flag = True
while (qr_flag):
    _, frame = cap.read()
    barcodes = pyzbar.decode(frame)

    for barcode in barcodes:
        (x, y, w, h) = barcode.rect
        cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)

        barcodeData = barcode.data.decode("utf-8")
        barcodeType = barcode.type
 
        text = "{} ({})".format(barcodeData, barcodeType)
        cv2.putText(frame, text, (x, y - 10),
        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)

        if barcodeData not in found:
            csv.write("{},{}\n".format(datetime.datetime.now(), barcodeData))
            csv.flush()
            found.add(barcodeData)
            print(barcodeData)
            qr_flag = False
            client_socket.send('D')
            client_socket.recv(1024)
            break
        
    cv2.imshow("Barcode Scanner", frame)
    key = cv2.waitKey(1) & 0xFF
    if key == ord("q"):break

print("[INFO] cleaning up...")
csv.close()
cv2.destroyAllWindows()


try:
    while True:
        
        distance = distance_measurement()
        #print(distance)
    
        # wave grab
        if (distance < 3.5):
            if(not grab_flag):
                grab_flag=True
                spreader_close()
                time.sleep(1)
                client_socket.send('U')
                msg = client_socket.recv(1024)
                print(msg)
                if (msg.decode()=='r'):
                    d_flag = 1
                    print(msg.decode())
                    s.send('y'.encode())
        
        #
        prss_sensor = GPIO.input(pressure)
        
        if(not(press_flag) and prss_sensor): # 
            press_flag = True
            client_socket.send('U')
            msg = client_socket.recv(1024)
            if (msg.decode()=='r'):
                d_flag = 1
                s.send('y'.encode())
            
        
        if(d_flag==1):
            rec = s.recv(1024)      
            if(s.recv(1024).decode() == 'start'):
                client_socket.send('D')
                msg = client_socket.recv(1024)
                if(msg.decode()=='q'):
                    grab_flag = False
                    spreader_open()
                    time.sleep(1.85)
                    spreader_stop()
                    client_socket.send('U')
                    msg2 = client_socket.recv(1024)
                    if (msg2.decode() =='r'):
                        s.send('@'.encode())
                        d_flag = 0
                
          
        time.sleep(0.1)
       
except KeyboardInterrupt:
    spreader_stop()
    pass
finally:
    GPIO.cleanup()