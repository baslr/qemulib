def h = "localhost"
def p = 1231

def s = new Socket(h, p)

def qmpOut = s.getOutputStream()
s = s.getInputStream()

Thread.start 
{
	s.eachLine()
	{
		println it
	}
}

qmpOut << '{"execute": "fail"}'