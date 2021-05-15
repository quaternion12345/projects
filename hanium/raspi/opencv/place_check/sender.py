import socket

def send_coord(sock, s, lst):
    '''
        send coordinate to arduino
    '''
    # open connection socket
    (conn, addr) = s.accept()
    print('Connected')
    # check start
    msg = sock.recv(1024)
    if msg.decode() == 'Q':
        print('receive')
        sock.sendall('~')
        # send coordinate
        for i in range(len(lst)):
            msg = sock.recv(1024)
            if msg.decode() == '~':                            
                print('send:'+lst[i])
                sock.sendall(lst[i])
                msg = sock.recv(1024) # wait for arrival
                # send to another raspberrypi
                conn.send('start'.encode())
                # wait for reply
                msg = conn.recv(1024)
                if (msg=='@'):
                    # send signal to arduino
                    sock.sendall('}')

    conn.close() # close connection socket                
