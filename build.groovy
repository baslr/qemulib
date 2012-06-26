import groovy.util.GroovyTestSuite
import junit.framework.Test
import junit.textui.TestRunner
import org.codehaus.groovy.runtime.ScriptTestAdapter

import tests.groovy.QMPSocketTests

final testPath = 'src/tests/'

tests = []

// grab all the tests in testPath and populate in tests
// unused
filter = 
{
	it.eachDir(filter)
	it.eachFileMatch(~/.+Test[s]?\.groovy/)
	{
		tests += it
	}
}

// filter(new File(testPath))

def tSuite = new GroovyTestSuite()
tSuite.addTestSuite(QMPSocketTests.class)

TestRunner.run(tSuite)


runTests()