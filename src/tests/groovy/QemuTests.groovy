import groovy.util.GroovyTestCase

import qemulib.Qemu


public class QemuTests extends GroovyTestCase
{
	def qemu

	void setUp()
	{
		qemu = new Qemu([qemu: "qemu-system-x86_64"])
	}

	void testVMDirSanity()
	{
	}

	void testProps()
	{
		def qemu1 = new Qemu()

		assert qemu.qProps.qemu == 'qemu-system-x86_64'
		assert qemu1.qProps.qemu == 'qemu'
	}

	void testVMStoring()
	{

		def vm = qemu.vm()

		assert(qemu.vms.first() == vm)
	}

	

	void testVMLaunch()
	{
		def vm = qemu.vm()

		// vm.hda('test_data/tinycore.img').cdrom('test_data/tc.iso')
		// vm.kernel('test_data/kboot/vmlinuz').initrd('test_data/kboot/tinycore.gz')

		// vm.start()

	}

	void testVMQuit()
	{

	}

	// should override exception thats thrown in default qemulib
	void testOverrideLogging()
	{
		// def vm = qemu.vm()

		// shouldFail { qemu.exec('-append f')  }
		// qemu.metaClass.qError =
		// {
		// 	append: {println "wnnar"}
		// } as Appendable

		// qemu.exec('-append f') 

	}
}