qemu = require "qemu"

puts = (require "util").puts
assert = (require "assert")


suite 'Qemu processes', ->
  test 'qemu querying', ->
    vm = qemu.vm()
