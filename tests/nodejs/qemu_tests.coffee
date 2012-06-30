qemu = require "qemu"

puts    = (require "util").puts
assert  = (require "assert")
proc    = require "child_process"


suite 'Qemu generic', ->
  test 'qemu querying', ->
    vm = qemu.vm()
    # vm.hda("data/hda.img").kernel("d/vmlinuz").initrd("d/initrd")
    # vm.start()
  # test 'trying tcp port', (done) -> 
  #   qemu.checkTcp()
  #   done()
  test 'qmp tcp socket', ->
    vm = qemu.vm()
    vm.start()
  test 'qmp unix fifo socket'
  test 'json commands'


