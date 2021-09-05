from threading import Thread
from socket import *
import sys

IP = sys.argv[1]
PORT = int(sys.argv[2])
def recv_data_from_server(s):
    while True:
        sentence = s.recv(1024).decode()
        print(sentence)

def send_data_to_server(s):
    while True:
        sentence = input('[You] ')
        s.send(sentence.encode())

if __name__ == '__main__':
    try:
        clientSocket = socket(AF_INET, SOCK_STREAM)
        clientSocket.connect((IP, PORT))
        rthread = Thread(target=recv_data_from_server, args=(clientSocket,)) # thread for receive
        sthread = Thread(target=send_data_to_server, args=(clientSocket,)) # thread for send
        # make thread as daemon
        # thread terminated also, when parent is terminated
        rthread.daemon = True
        sthread.daemon = True
        # start thread
        rthread.start()
        sthread.start()
        # wait thread
        rthread.join()
        sthread.join()
        # close socket
        clientSocket.close()
    except KeyboardInterrupt:
        clientSocket.close()
        print()
        print("exit")
