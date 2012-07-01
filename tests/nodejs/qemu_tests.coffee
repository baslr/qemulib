qemu = require "qemu"
qemu.qProps.qemu = 'qemu-system-x86_64'

assert  = require "assert"
proc    = require "child_process"

suite 'Qemu generic', ->
  test 'qemu querying', ->
    vm = qemu.vm()
    vm.hda("data/hda.img").kernel("d/vmlinuz").initrd("d/initrd")
    # vm.start ->
    #   console.log "started"
    #   done()
  # test 'trying tcp port', (done) ->
  #   qemu.checkTcp()
  #   done()
  test 'qmp tcp socket', (done) ->
    vm = qemu.vm()

    vm.start ->
      vm.quit (resp) ->
        # console.log resp.toString()
        done()

  test 'qmp unix fifo socket'
  test 'json commands'
