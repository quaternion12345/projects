from bluetooth import *
# Create the client socket
client_socket=BluetoothSocket( RFCOMM )
client_socket.connect(("98:D3:31:FC:AF:A5", 1))


while True:
    
    msg = input("Send : ")
    print (msg)
    client_socket.send(msg)
    
    
    msg2=client_socket.recv(1024)
    print("received message:{}".format(msg2))
    
    
    
print ("Finished")

