proc    = require "child_process"
machine = require "./machine"
net     = require "net"

qProps = 
{
    qemu: "qemu",
    tcp: true,
    port: 2000
}

vms = []

vm = ->
  new machine.vm(this)

start = (vm, callback) ->

  startVM = (port) ->
    qmpOpen()
    args = vm.makeArgs()
    vm.qmp = port
    args.push "-qmp"
    args.push "tcp:localhost:#{port},server"
    proc.spawn qProps.qemu, args, {stdio: 'inherit', detached: true}
    
  # bunch of extra code just to open the
  # socket, since qemu may not have bound yet
  qmpOpen = ->
    socket = net.connect ({port: vm.qmp})
    socket.on 'connect', ->
      callback()
      # socket.write('{"execute": "qmp_capabilities"}')
      socket.destroy()
    socket.on 'error', ->
      qmpOpen()

  checkTcp(startVM)
  vm


# find an available tcp conn
checkTcp = (startVM) ->
  socket = new net.Server()
  conn = socket.listen ++qProps.port

  conn.on 'error', (err) ->
    # this timeout is annoying but guess its required according to the api
    setTimeout ->
      checkTcp(startVM)
    , 10
  conn.on 'listening', ->
    conn.close()
    startVM(qProps.port)


exports.vm  = vm
exports.vms = vms
exports.qProps = qProps
exports.start = start
