=begin

	

=end


require 'java'


def info(msg)
	Log.info(msg)
end

def warn(msg)
	Log.warn(msg)
end

def debug(msg)
	Log.debug(msg)
end

def testFunc
	info("test message from Ruby Method Wrapper")
end
