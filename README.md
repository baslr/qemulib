Library to control qemu for groovy and nodejs
===

### Overview

The main goal was to have a consistent, simple api to launch virtual machines and control qemu. Here is the result:

```javascript
  var qemu  = require('qemu')

  var vm    = qemu.vm()
  vm.hda("data/disk.img")
  vm.kernel("data/vmlinuz").initrd("data/initrd")

  vm.start(function()
  {
    // post start stuff

    vm.quit()
  })


```

and using groovy:
```groovy
  def qemu  = new Qemu()
  def vm    = qemu.vm()

  vm.hda("data/disk.img").kernel("data/vmlinuz").initrd("data/initrd")

  vm.start()

  vm.quit()
```

Most functions return the created machine object so they can be chained as shown above. The language bindings are independent of one another - you dont need the groovy source when using nodejs and vice versa.

Use the mocha.sh script to run the node tests - you will need coffee-script and mocha installed. Mocha should be in your PATH. To run the tests in groovy:

```shell 
groovy -cp src/groovy/:tests/groovy test.groovy
```

### Details

Qemu has two ways to communicate and pass events - tcp or a unix domain socket. They are passed on startup and cannot be changed once the vm is running. TCP is the default and the library will try and handle that smoothly. Each qemu instance has a `qProps` map that allows tweaking:

```groovy
  qProps = [
    qemu: "qemu-system-x86_64",   // override the qemu executable here 
                                  // (default is "qemu")
    tcp: true,      // use tcp internally (recommended)
                    // alternative is to use domain sockets.
    port: 2000                    // starting port
  ]
```

The `port` variable will be incremented until an open port is found. Setting TCP to false is to switch to creating fifos instead of tcp (not implemented). To use domain sockets in java the excellent JUDS library will be required. 

### Todo

Probably the order in which they will be completed:

- Python port
- Unix domain socket support
- Event handling - listening of other vm events
- Full implementation of qemu's startup commands. 
  Use the qAppend("-serial fff etc") function in each library to add the more obscure switches as you may need them. 
- Wrapper for other tools such as qemu-img
