puts = (require "util").puts

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
  qemu : qemu

  start   : -> 
    this.qemu.start(this)

  # internal stuff
  cmds : {}
  cmdput : (cmd, arg) ->
    argMap = []
    argMap.push(arg)
    this.cmds[cmd] = argMap
  makeArgs : ->
    res = []
    res.push cmdlist[cmd],val for cmd, val of this.cmds
    
    res

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
    this


  drives  : []
  nics    : []

exports.vm    = vm
