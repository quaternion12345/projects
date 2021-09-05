from threading import Thread
from socket import *
import sys

IP = sys.argv[1]
PORT = int(sys.argv[2])
user = 0 # number of users
users = [] # list for connection sockets descriptor

def sender(s, address, user):
    # first send enter message to other clients
    sentence = "> New user " + address[0] +":"+ str(address[1]) + " entered ("
    if user <= 1:
        sentence += str(user) + " user online)"
    else:
        sentence += str(user) + " users online)"
    print(sentence) # print to server side
    for sock in users: # send enter message to other client side
        if sock != s:
            sock.send(sentence.encode())

    while True: # do loop until connection is closed
        # print and send received chatting
        msg = s.recv(1024).decode()
        if msg == '': # connection is closed
            break
        sentence = "[" + address[0] +":"+ str(address[1]) + "] " + msg
        print(sentence) # print to server side
        for sock in users: # send received message to other client side
            if sock != s:
                sock.send(sentence.encode())

    # when connection is closed send left message to other clients
    sentence = "< The user " + address[0] +":"+ str(address[1]) + " left ("
    # clean up
    user -= 1
    users.remove(s)
    s.close()
    if user <= 1:
        sentence += str(user) + " user online)"
    else:
        sentence += str(user) + " users online)"
    print(sentence)  # print to server side
    for sock in users:  # send left message to other client side
        sock.send(sentence.encode())


if __name__ == '__main__':
    try:
        # open server socket
        serverSocket = socket(AF_INET, SOCK_STREAM)
        serverSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        serverSocket.bind((IP, PORT))
        serverSocket.listen(5)
        print("Chat Server started on port", PORT, end='')
        print('.')

        # open connection socket
        while True:
            connectionSocket, addr = serverSocket.accept()
            user += 1
            sentence = "> Connected to the chat server ("
            if user <= 1:
                sentence += str(user) + " user online)"
            else:
                sentence += str(user) + " users online)"
            connectionSocket.send(sentence.encode())
            t = Thread(target=sender, args=(connectionSocket, addr, user)) # make thread
            users.append(connectionSocket) # save socket descriptor
            t.start()

    except KeyboardInterrupt:
        # close server socket
        serverSocket.close()
        print()
        print("exit")
