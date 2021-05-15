# import packages
from multiprocessing import Process
import time
import os
from server_code import socket_communication # import server file

start = time.time() # start clock

def child(ip_address, port_num):
    '''
        function for tcp server
    '''
    
    pid = os.getpid()    
    print("Process ID : ", pid)    
    socket_communication(ip_address, port_num)
    

# Parent server forks child servers
if __name__ == '__main__':

    ip = '172.30.1.2'
    port_number = [1000, 2000]
    child_processes = []    # container for child process

    # Fork child servers
    for index, port in enumerate(port_number):
        # Make Child Process for Function 'child(ip, port)'
        process = Process(target=child, args=(ip, port,))
        child_processes.append(process)
        process.start() # Fork
    
    # Wait for parent-child synchronization
    for proc in child_processes:
        proc.join() # Wait for Process
        temp = time.time() # Check current time
        print(proc, " joined at", temp-start) # show process finished time

end = time.time() # end clock

print(end-start, "seconds taken")
