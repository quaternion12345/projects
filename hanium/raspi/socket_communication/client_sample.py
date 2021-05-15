import socket

HOST = '172.30.1.16'
# Enter IP or Hostname of your server
PORT = 1000
# Pick an open Port (1000+ recommended), must match the server port
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST,PORT))

#Lets loop awaiting for your input
while True:
        command = input('Enter your command: ').encode()
        s.send(command)
        reply = s.recv(1024)
        if reply == 'Terminate':
                break
        print (reply.decode())
