import groovy.util.GroovyTestCase

import Qemu


public class QemuTests extends GroovyTestCase
{
  def qemu

  void setUp()
  {
    qemu = new Qemu([qemu: "qemu-system-x86_64"])
  }

  void testMachineDirSanity()
  {
  }

  void testProps()
  {
    def qemu1 = new Qemu()

    assert qemu.qProps.qemu == 'qemu-system-x86_64'
    assert qemu1.qProps.qemu == 'qemu'
  }

  void testMachineStoring()
  {

    def vm = qemu.vm()

    assert(qemu.vms.first() == vm)
  }

  

  void testMachineLaunch()
  {
    def vm = qemu.vm()

    // vm.hda('data/tinycore.img').cdrom('data/tc.iso')
    // vm.kernel('data/kboot/vmlinuz').initrd('data/kboot/tinycore.gz')

    // vm.start()

    // assert vm.cmds.keySet().contains('-qmp')

  }

  void testMachineQuit()
  {

  }

  // should override exception thats thrown in default qemulib
  void testOverrideLogging()
  {
    // def vm = qemu.vm()

    // shouldFail { qemu.exec('-append f')  }
    // qemu.metaClass.qError =
    // {
    //  append: {println "wnnar"}
    // } as Appendable

    // qemu.exec('-append f') 

  }
}
