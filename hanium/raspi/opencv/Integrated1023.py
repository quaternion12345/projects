import RPi.GPIO as GPIO
import time, argparse, socket, datetime, cv2
from pyzbar import pyzbar
from bluetooth import *
from spreader_motor import *

HOST = '172.30.1.55'
PORT = 4000

d_flag = 0

grab_flag = False


# Create the client socket
client_socket=BluetoothSocket( RFCOMM )
#client_socket.close()
client_socket.connect(("98:D3:71:F9:B7:A2", 1))

# Connect to the server
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#s.close()
print('3333333333')
s.connect((HOST,PORT))

GPIO.setwarnings(False) 
GPIO.setmode(GPIO.BCM) #Pin mode setup

pressure = 17
press_flag = False
GPIO.setup(pressure, GPIO.IN)

client_socket.sendall('U')
client_socket.sendall('#')
msg = client_socket.recv(1024)
print(msg)

def  barcode_detect():
    
    spreader_open()
    #spreader_close()
    time.sleep(1.3)
    #time.sleep(0.4)
    spreader_stop()

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
                client_socket.send('!')
                break
            
        cv2.imshow("Barcode Scanner", frame)
        key = cv2.waitKey(1) & 0xFF
        if key == ord("q"):break

    print("[INFO] cleaning up...")
    csv.close()
    time.sleep(2)
    cv2.destroyAllWindows()
#client_socket.recv(1024)

def down():
    global grab_flag, d_flag
    
    while True :
        distance = distance_measurement()
        #print('distance',distance)
        # wave grab
        msg = client_socket.recv(1024)
        print('msg : ',msg.decode())
        if msg.decode() =='q':
            #client_socket.send('G')
            #print('distance',distance)
            if(not grab_flag):
                grab_flag=True
                print('spreader_close')
                spreader_close()
                time.sleep(1.4)
                spreader_stop()
                client_socket.send('U')
                client_socket.send('!')
                   
                msg = client_socket.recv(1024)
                # Grap_up_finish
                if (msg.decode()=='r'):
                    d_flag = 1
                    print('msg.decode()',msg.decode())
                    s.send('y'.encode())
                    print("send_to_uprasp_y")
                    break
                
                
def up():
    global grab_flag, d_flag
    
    rec = s.recv(1024)
    print("recv_from_uprasph_start")
    if(rec.decode() == 'start'):
        client_socket.send('D')
        client_socket.send('!')
        msg = client_socket.recv(1024)
        print("recv : q ?" ,msg)
        if(msg.decode()=='q'):
            grab_flag = False
            spreader_open()
            time.sleep(1.3)
            spreader_stop()
            client_socket.send('U')
            client_socket.send('!')
            d_flag=d_flag+1
            msg2 = client_socket.recv(1024)
            print("recv : r ?" , msg2)
            if (msg2.decode() =='r'):
                spreader_close()
                time.sleep(1.4)
                spreader_stop()
                s.send('@'.encode())
                msg = s.recv(1024)
                if msg =='z':
                    return

index = 0
try:
    for i in range(3):
        barcode_detect()
        down()
        up()
        
except KeyboardInterrupt:
    spreader_stop()
    pass

finally:
    GPIO.cleanup()