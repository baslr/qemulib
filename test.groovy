def h = "localhost"
def p = 1231

def teh = 
{
  new Socket(h, p).withStreams 
  { input, output ->

	  output << '{"execute":"fail"}'

  	// start reading output
	  Thread.start 
	  {
      input.eachLine
      {
        println "[resp] ${it}"
      }

    }

  }

}

teh()