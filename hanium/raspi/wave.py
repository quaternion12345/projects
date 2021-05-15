import RPi.GPIO as GPIO # import module
import time

GPIO.setwarnings(False) 
GPIO.setmode(GPIO.BCM) #Pin mode setup

# pin definition
trig=2
echo=3

GPIO.setup(trig, GPIO.OUT)
GPIO.setup(echo, GPIO.IN)

def distance_measurement():
    try:
        GPIO.output(trig, False)
        time.sleep(0.001)
        GPIO.output(trig, True)
        time.sleep(0.000001)
        GPIO.output(trig, False)
        while GPIO.input(echo)==0:
            pulse_start=time.time()
        while GPIO.input(echo)==1:
            pulse_end=time.time()
        pulse_duration=pulse_end-pulse_start
        distance=pulse_duration*17000
        distance=round(distance,2)
        return distance
    except:
        pass



'''
while True:
    dt=distance_measurement()
    print("Distance: ", dt, "cm")
    time.sleep(0.05)
'''
