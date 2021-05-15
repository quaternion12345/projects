# import packages
from multiprocessing import Process
import time
import os

start = time.time() # start clock

def counter(cnt):
    pid = os.getpid()    
    for i in range(cnt):
        print("Process ID : ", pid, " -- ", i)

# main Function
if __name__ == '__main__':

    case_size = [100000, 10000000, 1000000, 1000000]
    p = []

    # Run Test Cases
    for index, number in enumerate(case_size):
        # Make Child Process for Function 'count(number)'
        process = Process(target=counter, args=(number,))
        p.append(process)
        process.start() # Fork

    # Wait for synchronization
    for proc in p:
        proc.join() # Wait for Process
        temp = time.time()
        print(proc, " joined at", temp-start) # show process finished time

end = time.time() # end clock

print(end-start, "seconds taken")

