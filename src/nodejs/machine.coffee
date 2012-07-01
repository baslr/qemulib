# abstracts a virtual machine - use qemu.vm() to create
# a new vm rather than using this directly, otherwise
# the qemu ref wont be set. you can set it manually of course

net = require 'net'

cmdlist = {
    boot    : "-boot",
    drive   : "-drive",
    kernel  : "-kernel",
    append  : "-append",
    initrd  : "-initrd",
    cdrom   : "-cdrom",
    hda     : "-hda",
    hdb     : "-hdb",
    hdc     : "-hdc",
    hdd     : "-hdd",
    nic     : "-net nic,",
    net     : "-net user,"
}


# main object managed here
vm = (qemu) ->
  qemu      : qemu
  # qmp socket loc (tcp or fifo)
  qmp       : null
  appended  : ''

  start   : (cb) ->
    this.qemu.start(this, cb)

  # internal stuff
  cmds : {}
  cmdput : (cmd, arg) ->
    argMap = []
    argMap.push(arg)
    this.cmds[cmd] = argMap
  makeArgs : ->
    res = []
      # res.push ap for cmd, of this.appended.split(' ')
    res.push cmdlist[cmd],val for cmd, val of this.cmds
    if (this.appended)
      res.push this.appended
    res
  qAppend : (line) ->
    this.appended += ' ' + line



  # kernel related
  kernel  : (file) ->
    this.cmdput 'kernel', file
    this
  append  : (line) ->
    this.cmdput 'append', line
    this
  initrd  : (file) ->
    this.cmdput 'initrd', file
    this

  # drive related
  hda : (file) ->
    this.cmdput 'hda', file
    this
  hdb : (file) ->
    this.cmdput 'hdb', file
    this
  hdc : (file) ->
    this.cmdput 'hdc', file
    this
  hdd : (file) ->
    this.cmdput 'hdd', file
    this
  cdrom : (file) ->
    this.cmdput 'cdrom', file

  # qmp commands
  qmpSend : (data, callback) ->
    socket = net.connect({port: this.qmp})
    qemuOutput = null
    socket.on 'connect', ->
      socket.write('{"execute":"qmp_capabilities"}')
      socket.write(data)

    socket.on 'data', (resp) ->
      socket.end()
      qemuOutput = resp
    socket.on 'end', ->
      callback(qemuOutput)
  quit : (callback) ->
    this.qmpSend('{"execute":"quit"}', callback)

  drives  : []
  nics    : []

exports.vm    = vm
