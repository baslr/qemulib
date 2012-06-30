proc = require "child_process"
puts = (require "util").puts
machine = require "machine"
net = require "net"

qProps = 
{
  qemu: "qemu-system-x86_64",
  tcp: true,
  port: 2000
}

vms = []

vm = ->
  new machine.vm(this)

start = (vm) ->
  args = vm.makeArgs()
  checkTcp()
  args.push "-qmp"
  args.push "tcp:localhost:" + qProps.port + ",server"
  vmproc = proc.spawn (qProps.qemu), args, {stdio: 'inherit'}

checkTcp = ->
  socket = new net.Server()
  conn = socket.listen ++qProps.port
  conn.on 'error', (err) ->
    # this timeout is annoying but guess its required according to the api
    setTimeout ->
      console.log qProps.port + " " + err 
      checkTcp()
    , 10
  conn.on 'listening', ->
    puts "port " + qProps.port
    conn.close()


exports.vm  = vm
exports.vms = vms
exports.qProps = qProps
exports.start = start
exports.checkTcp = checkTcp
