import RPi.GPIO as GPIO

def init():
  GPIO.setmode(GPIO.BCM)
  GPIO.setup(18, GPIO.OUT)
  GPIO.setup(23, GPIO.OUT)
  GPIO.setup(24, GPIO.OUT)
  GPIO.setup(25, GPIO.OUT)
  GPIO.output(18, True)

def forward():
  GPIO.output(23, True)
  GPIO.output(24, False)
  GPIO.output(25, False)

def backward():
  GPIO.output(23, False)
  GPIO.output(24, True)
  GPIO.output(25, False)

def left():
  GPIO.output(23, True)
  GPIO.output(24, True)
  GPIO.output(25, False)

def right():
  GPIO.output(23, False)
  GPIO.output(24, False)
  GPIO.output(25, True)

def stop():
  GPIO.output(23, False)
  GPIO.output(24, False)
  GPIO.output(25, False)

def cleanup():
  GPIO.output(18, False)
  GPIO.cleanup()

