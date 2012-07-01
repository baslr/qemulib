puts = (require "util").puts
assert = (require "assert")

suite 'Vm tests', ->
  qemu  = require "qemu"

  test 'kerenel commands ', ->
    vm    = qemu.vm()

    vm.kernel "vmlinuz"
    assert.equal vm.cmds.kernel, "vmlinuz"

    vm.append "line"
    assert.equal vm.makeArgs().join(' '), "-kernel vmlinuz -append line"

    vm.initrd "initrd"
    assert.equal vm.makeArgs().join(' '), "-kernel vmlinuz -append line -initrd initrd"
  test 'various drive commands', ->
    vm    = qemu.vm()

    vm.hda "hda"
    assert.equal vm.cmds.hda, "hda"

    vm.hdc "hdc"
    vm.cdrom "cdrom"
    vm.hdd "hdd"
    assert.equal vm.makeArgs().join(' '), "-hda hda -hdc hdc -cdrom cdrom -hdd hdd"

