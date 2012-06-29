proc = require "child_process"
puts = (require "util").puts
machine = require "machine"

qProps = 
{
  qemu: "qemu-system-x86",
  tcp: true,
  port: 2000
}

vms = []

vm = ->
  new machine.vm()

exports.vm  = vm
exports.vms = vms
exports.qProps = qProps
