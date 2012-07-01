puts = (require "util").puts
assert = (require "assert")

qemu  = require "qemu"
qemu.qProps.qemu = 'qemu-system-x86_64'

suite 'Vm tests', ->

  test 'kerenel commands ', ->
    vm = qemu.vm()

    vm.kernel "vmlinuz"
    assert.equal vm.cmds.kernel, "vmlinuz"

    vm.append "line"
    assert.equal vm.makeArgs().join(' '), "-kernel vmlinuz -append line"

    vm.initrd "initrd"
    assert.equal vm.makeArgs().join(' '), "-kernel vmlinuz -append line -initrd initrd"
  test 'various drive commands', ->
    vm = qemu.vm()

    vm.hda "hda"
    assert.equal vm.cmds.hda, "hda"

    vm.hdc "hdc"
    vm.cdrom "cdrom"
    vm.hdd "hdd"
    assert.equal vm.makeArgs().join(' '), "-hda hda -hdc hdc -cdrom cdrom -hdd hdd"
  test 'append qemu args function', ->
    vm = qemu.vm()

    vm.qAppend('-hda hda')
    vm.start (resp) ->

