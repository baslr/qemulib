Qemulib: library to control qemu for groovy and nodejs
===

### Overview

The main goal was to have a consistent, simple api to use qemu. Here is the result:

```javascript
  qemu  = require('qemu')

  vm    = qemu.vm()
  vm.hda("data/disk.img")
  vm.kernel("data/vmlinuz").initrd("data/initrd")

  vm.start(function()
  {
    // post start stuff
  })
```

and using groovy:
```groovy
  def qemu  = new Qemu()
  def vm    = qemu.vm()

  vm.hda("data/disk.img").kernel("data/vmlinuz")
  vm.initrd("data/initrd")

  vm.start()
```

Most commands return the created machine object so they can be chained.


### Details

Qemu has two ways to communicate and pass events - tcp or a unix domain socket. They are passed on startup and cannot be changed. TCP is the default and the library will try and handle that smoothly. Each `qemu` implementation has a `qPops` map that allows tweaking:

```groovy
  qProps = [
    qemu: "qemu-system-x86_64",   // override the qemu executable here
    tcp: true,      // use tcp internally (recommended)
                    // alternative is to use domain sockets.
    port: 2000                    // starting port
  ]
```

The `port` variable will be incremented until an open port is found. Setting TCP to false there will switch to creating fifos instead (not implemented, see below). To use domain sockets in java the excellent JUDS library will be required. 

### Todo

Probably the order in which they will be completed:

- Unix domain socket support
- Event handling 
- Full implementation of qemu's startup commands
  use the qAppend("-serial fff etc") function in each library to add the more obscure switches as you may need them. 
