import groovy.util.GroovyTestSuite
import junit.textui.TestRunner

import QMPTests
import QemuTests
import VMTests

def tSuite = new GroovyTestSuite()

tSuite.addTestSuite(QemuTests.class)
tSuite.addTestSuite(VMTests.class)
tSuite.addTestSuite(QMPTests.class)

TestRunner.run(tSuite)


