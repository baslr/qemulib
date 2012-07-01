import groovy.util.GroovyTestCase

import Machine
import Qemu

public class MachineTests extends GroovyTestCase
{
  
  private vm
  private qemu

  void setUp()
  {
    qemu    = new Qemu([qemu: "qemu-system-x86_64"])
    vm  = qemu.vm()
  }

  void testKernelBoots()
  {
    vm.kernel('data/linux').initrd('data/initrd').append('appendd')

    assert vm.cmds.keySet().size() == 3
    assert vm.cmds.keySet().containsAll(['-kernel', '-initrd', '-append'])
    assert vm.cmds.values().containsAll(
           ['-kernel data/linux', '-initrd data/initrd', '-append appendd'])

    assert vm.makeArgs() == ' -kernel data/linux -initrd data/initrd -append appendd'
  }

  void testArgsSanity()
  {
    // assert vm.makeArgs(vm) == qemu.vms.first()
    assert vm.makeArgs() == ''

    vm.hda('driveA')
    assert vm.makeArgs() == ' -hda driveA'

    vm.hdb('driveB')
    assert vm.makeArgs() == ' -hda driveA -hdb driveB'
  }

  void testNetNic()
  {
    vm.nic([model: "i82551"])

    assert vm.nics.first() == "-net nic,model=i82551"
    shouldFail { vm.nic([model: "failnic"]) }

    vm.nic([model: "virtio"])
    assert vm.nics.get(1) == "-net nic,model=virtio"

    vm.nic([vlan: "7", model:"virtio"])
    assert vm.nics.get(2) == "-net nic,vlan=7,model=virtio"

    vm.nic([vlan: 7, model:"virtio"])
    shouldFail { vm.nic([vlan: "f"]) }
  }

  void testHDCommands()
  {
    vm.hda("testHda/s")
    vm.hdb("testHdb/s")
    vm.hdc("testHdc/s")
    vm.hdd("testHdd/s")

    vm.cdrom("testCd/s") // TODO qemu cannot use cdrom and hdc together

    assert vm.cmds.'-hda' == "-hda testHda/s"
    assert vm.cmds.'-hdb' == "-hdb testHdb/s"
    assert vm.cmds.'-hdc' == "-hdc testHdc/s"
    assert vm.cmds.'-hdd' == "-hdd testHdd/s"
    assert vm.cmds.'-cdrom' == "-cdrom testCd/s"
  }

  void testDriveArgs()
  {
    vm.drive(["if": "ide", "file":"s/p", "media":"disk"])
    assert vm.drives.first() == '-drive if=ide,file=s/p,media=disk'

    shouldFail { vm.drive(["if":""]) }
    shouldFail { vm.drive(["if":"ff"]) }
  }

  void testDriveArgsSaving()
  {
    vm.drive("if":"virtio")
    assert vm.drives.first() == "-drive if=virtio"

    vm.drive("file":"s/p")
    assert vm.drives.get(1) == "-drive file=s/p"

    vm.drive("if":"ide", "file":"s/p")
    assert vm.drives.get(2) == "-drive if=ide,file=s/p"
  }

  void testBootCmd()
  {
    vm.boot([order:'a', menu: 'off'])
    assert vm.cmds == ['-boot':'-boot order=a,menu=off']
  }
}
