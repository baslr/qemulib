import groovy.util.GroovyTestSuite
import junit.textui.TestRunner

import QMPTests
import QemuTests
import MachineTests

def tSuite = new GroovyTestSuite()

tSuite.addTestSuite(QemuTests.class)
tSuite.addTestSuite(MachineTests.class)
tSuite.addTestSuite(QMPTests.class)

TestRunner.run(tSuite)


