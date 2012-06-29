qemu = require "qemu"

puts = (require "util").puts
assert = (require "chai").assert


suite 'Qemu processes', ->
  test 'qemu startup', ->
    qemu.vm()
