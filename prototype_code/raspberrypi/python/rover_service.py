import atexit, threading, time, web
from rover import *

t = None
state = 'stop'

urls = (
	'/(.*)', 'servicehandler'
)
app = web.application(urls, globals())

class servicehandler:
	def GET(self, action):
		global state
		reset_safety()
		if not action:
			action = 'status'
		if action == 'forward':
			forward()
			state = action
		elif action == 'backward':
			backward()
			state = action
		elif action == 'left':
			left()
			state = action
		elif action == 'right':
			right()
			state = action
		elif action == 'stop':
			safety_stop()
		return state

def cleanup_rover():
	cleanup()

def reset_safety():
	global t
	if t:
		t.cancel()
	t = threading.Timer(5, safety_stop)
	t.start()

def safety_stop():
	global state
	stop()
	state = 'stop'
	t.cancel()

if __name__ == "__main__":
	atexit.register(cleanup_rover)
	init()
	app.run()
