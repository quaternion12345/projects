import socket
import random
# (HOST, PORT) example
# HOST = '172.30.1.2' 
# PORT = 1000

def socket_communication(HOST, PORT):
        
        '''Function for TCP way of server socket communication'''
        
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        print ('Socket created')

        # handling error exception
        try:
                s.bind((HOST, PORT))
        except socket.error:
                print ('Bind failed ')

        s.listen(5) # listening socket
        print ('Socket is awaiting client')
        (conn, addr) = s.accept()
        print ('Connected')

        # wait for message
        terminate = False # flag for terminate
        while not terminate:
                # receive message from client
                data = conn.recv(1024)
                data = data.decode()
                print('I sent a message back in response to: ')
                print(data)
                reply = ''

                # process given message from client at each cases
                # Case 1
                if data == 'Hello':
                        reply = 'Hi, back!'
                # Case 2
                elif data == 'Should we have to stop?':
                        if random.randint(1) == True:
                                reply = 'yes!'
                        else:
                                reply = 'no!'

                # Terminate
                elif data == 'quit':
                        reply = 'Terminate'
                        terminate = True
                # Unknown message
                else:
                        reply = 'Unknown command'

                # send reply to client
                conn.send(reply.encode())
                
        conn.close() # close connections
