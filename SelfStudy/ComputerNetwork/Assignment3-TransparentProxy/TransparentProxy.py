from threading import Thread
from socket import *
import sys
import queue

PORT = int(sys.argv[1]) # custom port

# flag for filter options
url_filter_on = True
image_filter_on = False

# Thread id
tid_pool = queue.Queue() # tid pools
max_tid = 20 # maximum thread number --> constant
for i in range(max_tid):
    tid_pool.put(i+1)
# iteration number
iteration = 0

def convert_flag_to_str(flag):
    if(flag):
        return "O"
    else:
        return "X"

def manage_socket(socket_client, address, req ,tid):
    '''
    Manage client--proxy--server
    '''
    # two sockets, one for client, another for main server
    # socket_client and socket_server
    log = []  # save log sentence
    global iteration, image_filter_on
    iteration += 1

    # Append log for connection between CLI and PRX
    log.append('-' * 47)
    log.append(str(iteration)+'  [Conn:     '+str(tid)+'/   '+str(max_tid - tid_pool.qsize()) + ']')
    log.append('[ ' + convert_flag_to_str(url_filter_on) + ' ] URL filter | [ ' + convert_flag_to_str(image_filter_on) + ' ] Image filter')
    log.append('')

    # 0x0D : CR, 0x0A : LF

    # CLI ==> PRX
    end_header = 0
    for index in range(len(req)):
        if req[index] == 0x0d and req[index+1] == 0x0a and req[index+2] == 0x0d and req[index+3] == 0x0a:
            end_header = index + 3
            break

    receive = req[:end_header+1].decode()

    request_line_end = receive.find("\r\n")
    request_line = receive[:request_line_end]
    host_line = ''
    user_agent_line = ''

    for line in receive.split('\n'):
        if "Host:" in line:
            host_line = line[6:-1]
        if "User-Agent:" in line:
            user_agent_line = line[12:-1]

    log.append('[CLI connected to ' + address[0]+':'+str(address[1]) + ']')
    log.append('[CLI ==> PRX --- SRV]')
    log.append('  > ' + request_line)
    log.append('  > ' + user_agent_line)

    # check image filter is activated
    if "?image_off" in request_line:
        image_filter_on = True
        log[2] = '[ ' + convert_flag_to_str(url_filter_on) + ' ] URL filter | [ ' + convert_flag_to_str(image_filter_on) + ' ] Image filter'
    if "?image_on" in request_line:
        image_filter_on = False
        log[2] = '[ ' + convert_flag_to_str(url_filter_on) + ' ] URL filter | [ ' + convert_flag_to_str(image_filter_on) + ' ] Image filter'

    # PRX ==> SRV

    # URL Filter
    # Change "receive" "request_line" "host_line"
    receive, request_line, host_line = url_filter(receive, request_line, host_line)
    req = receive.encode() + req[end_header+1:] # change request packet

    # Make PRX-SRV connection socket
    socket_server = socket(AF_INET, SOCK_STREAM)
    ip = ''
    port = 80
    parser = host_line.find(":")
    if parser == -1:
        ip = host_line
        port = 80 # default port
    else:
        ip = host_line[:parser]
        port = int(host_line[parser+1:])

    socket_server.connect((ip, port))

    socket_server.send(req) # send HTTP request to server

    log.append('[SRV connected to ' + ip+':'+str(port) + ']')
    log.append('[CLI --- PRX ==> SRV]')
    log.append('  > ' + request_line)
    log.append('  > ' + user_agent_line)

    first = True
    response_line = ''
    content_type = ''
    content_length = ''

    while True:
        resp = socket_server.recv(4096) # receive HTTP response from server
        if len(resp) == 0: # finished
            break

        if first: # first iteration --> read headers
            first = False

            end_header = 0
            for index in range(len(resp)):
                if resp[index] == 0x0d and resp[index + 1] == 0x0a and resp[index + 2] == 0x0d and resp[index + 3] == 0x0a:
                    end_header = index + 3
                    break

            receive2 = resp[:end_header+1].decode()

            response_line = receive2.split('\n')[0]
            content_type = ''
            content_length = ''

            for line in receive2.split('\n'):
                if "Content-Type" in line:
                    content_type = line[14:-1]
                    continue
                if "Content-Length" in line:
                    content_length = line[16:-1]
                    continue

            drop = image_filter(content_type)
            if drop: # drop image packet
                log.append('[CLI --- PRX <== SRV]')
                log.append('  > ' + response_line)
                log.append('  > ' + content_type + ' ' + content_length + 'bytes')
                log.append('[CLI <== PRX --- SRV]')
                log.append('  > ' + '')
                log.append('  > ' + '')
                log.append('[CLI disconnected]')
                log.append('[SRV disconnected]')
                log.append('-' * 47)
                socket_client.close()
                socket_server.close()
                tid_pool.put(tid)
                for content in log:  # print log
                    print(content)
                return

        socket_client.send(resp)

    if content_type == '':
        content_type = "Not Specified"
    if content_length == '':
        content_length = '0'

    log.append('[CLI --- PRX <== SRV]')
    log.append('  > ' + response_line)
    log.append('  > ' + content_type + ' ' + content_length + 'bytes')

    # send HTTP response to client

    log.append('[CLI <== PRX --- SRV]')
    log.append('  > ' + response_line)
    log.append('  > ' + content_type + ' ' + content_length + 'bytes')

    socket_client.close()
    socket_server.close()

    log.append('[CLI disconnected]')
    log.append('[SRV disconnected]')
    log.append('-'*47)

    tid_pool.put(tid) # return thread id
    for content in log: # print log
        print(content)
    return

# URL Filtering
def url_filter(rec, req_line, h_line):
    '''
    Substitue original URL to designated URL
    '''
    if not url_filter_on:
        return rec, req_line, h_line
    if "yonsei" in h_line:
        new_req = req_line.replace(h_line, "www.linuxhowtos.org", 1)
        new_h = "www.linuxhowtos.org"
        rec = rec.replace(req_line, new_req, 1)
        rec = rec.replace(h_line, new_h, 1)
        req_line = new_req
        h_line = new_h
    return rec, req_line, h_line

# Image Filtering
def image_filter(content):
    '''
    Drop image
    '''
    if not image_filter_on:
        return False
    if "image" in content:
        return True
    return False


# main function
if __name__ == '__main__':
    # Managing sockets with multi-thread
    # One for receiving client request
    # Another for fetching response of the main server
    serverSocket = socket(AF_INET, SOCK_STREAM)
    serverSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
    serverSocket.bind(('localhost', PORT))
    serverSocket.listen(20)  # maximum 20 connection sockets

    print('Starting proxy server on port '+str(PORT))
    try:
        while True:
            conn, addr = serverSocket.accept()
            request = conn.recv(4096) # receive HTTP request from client
            td = Thread(target=manage_socket, args=(conn, addr, request, tid_pool.get()), daemon=True)
            td.start()
            #manage_socket(conn, addr, request, tid_pool.get())
    except KeyboardInterrupt:
        serverSocket.close()
