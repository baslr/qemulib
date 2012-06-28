import groovy.util.GroovyTestCase

import VM
import Qemu

public class VMTests extends GroovyTestCase
{
	
	private testVM
	private qemu

	void setUp()
	{
		qemu 		= new Qemu([qemu: "qemu-system-x86_64"])
		testVM 	= qemu.vm()
	}

	void testKernelBoots()
	{
		testVM.kernel('data/linux').initrd('data/initrd').append('appendd')

		assert testVM.cmds.keySet().size() == 3
		assert testVM.cmds.keySet().containsAll(['-kernel', '-initrd', '-append'])
		assert testVM.cmds.values().containsAll(
		       ['-kernel data/linux', '-initrd data/initrd', '-append appendd'])

		assert testVM.makeArgs() == ' -kernel data/linux -initrd data/initrd -append appendd'
	}

	void testArgsSanity()
	{
		// assert testVM.makeArgs(testVM) == qemu.vms.first()
		assert testVM.makeArgs() == ''

		testVM.hda('driveA')
		assert testVM.makeArgs() == ' -hda driveA'

		testVM.hdb('driveB')
		assert testVM.makeArgs() == ' -hda driveA -hdb driveB'
	}

	void testNetNic()
	{
		testVM.nic([model: "i82551"])

		assert testVM.nics.first() == "-net nic,model=i82551"
		shouldFail { testVM.nic([model: "failnic"]) }

		testVM.nic([model: "virtio"])
		assert testVM.nics.get(1) == "-net nic,model=virtio"

		testVM.nic([vlan: "7", model:"virtio"])
		assert testVM.nics.get(2) == "-net nic,vlan=7,model=virtio"

		testVM.nic([vlan: 7, model:"virtio"])
		shouldFail { testVM.nic([vlan: "f"]) }
	}

	void testHDCommands()
	{
		testVM.hda("testHda/s")
		testVM.hdb("testHdb/s")
		testVM.hdc("testHdc/s")
		testVM.hdd("testHdd/s")

		testVM.cdrom("testCd/s") // TODO qemu cannot use cdrom and hdc together

		assert testVM.cmds.'-hda' == "-hda testHda/s"
		assert testVM.cmds.'-hdb' == "-hdb testHdb/s"
		assert testVM.cmds.'-hdc' == "-hdc testHdc/s"
		assert testVM.cmds.'-hdd' == "-hdd testHdd/s"
		assert testVM.cmds.'-cdrom' == "-cdrom testCd/s"
	}

	void testDriveArgs()
	{
		testVM.drive(["if": "ide", "file":"s/p", "media":"disk"])
		assert testVM.drives.first() == '-drive if=ide,file=s/p,media=disk'

		shouldFail { testVM.drive(["if":""]) }
		shouldFail { testVM.drive(["if":"ff"]) }
	}

	void testDriveArgsSaving()
	{
		testVM.drive("if":"virtio")
		assert testVM.drives.first() == "-drive if=virtio"

		testVM.drive("file":"s/p")
		assert testVM.drives.get(1) == "-drive file=s/p"

		testVM.drive("if":"ide", "file":"s/p")
		assert testVM.drives.get(2) == "-drive if=ide,file=s/p"
	}

	void testBootCmd()
	{
		testVM.boot([order:'a', menu: 'off'])
		assert testVM.cmds == ['-boot':'-boot order=a,menu=off']
	}
}
