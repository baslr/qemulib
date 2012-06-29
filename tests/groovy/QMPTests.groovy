import groovy.util.GroovyTestCase

import VM
import Qemu


public class QMPTests extends GroovyTestCase
{
	def vm
	def qemu

	void setUp()
	{
		qemu = new Qemu([qemu: 'qemu-system-x86_64'])

		vm = qemu.vm()
	}

	void testQMPSocket()
	{

		def fc = 
		{
			def vm = qemu.vm()
			// vm.start()
		}

	// for (i in 1..20)
	// fc()


	}

}
