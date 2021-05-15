'''
    This is pseudocode for IPC socket communication

    Plan
    1. Make Each Control Programs with modular design
    2. In main server program, use multiprocess and fork child process
    3. In each child process, call each modules
    4. In each modules, communicate with server program using socket

    Problem
    1. Synchronization --> using mutex lock
'''
