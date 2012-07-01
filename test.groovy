import groovy.util.GroovyTestSuite
import junit.textui.TestRunner

import QemuTests
import MachineTests

def tSuite = new GroovyTestSuite()

tSuite.addTestSuite(QemuTests.class)
tSuite.addTestSuite(MachineTests.class)

TestRunner.run(tSuite)


