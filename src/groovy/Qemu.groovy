public class Qemu
{
  def vms = []

  def synchronized qProps = [
    qemu: "qemu",
    tcp: true,
    port: 2000
  ]

  Qemu()
  {
    // qemu with defaults
  }

  // change the properties of this qemu instance
  Qemu(props)
  {
    qProps += props

  }

  // TODO handle generic vm properties
  // def vm(props)
  def vm()
  {
    def vm = new Machine(this)

    vms.push(vm)

    return vm
  }

  def exec(cmd)
  {
    // TODO - errneous output not shown till vm quits
    // def out = new StringBuffer()

    "${qProps.qemu} $cmd".execute().consumeProcessOutput(qLog, qError)
    // println "${qProps.qemu} $cmd"
  }


  def synchronized start(vm)
  {
    // TODO restructure!

    def cmd = vm.makeArgs()

    checkTcp()
    if (qProps.tcp) 
      cmd += " -qmp tcp:localhost:${qProps.port},server"


    println cmd

    vm.qmp = qProps.port
    exec(cmd)
  }

  // find an available port
  def private checkTcp()
  {
    try
    {
      def s = new ServerSocket(++qProps.port)
      s.close()
    } catch (IOException io) {
      checkTcp()
    }
  }

  def qLog = 
  {
    append: 
        println "[log] $it"

  } as Appendable


  def qError = 
  {
    append:
        // { throw new QemuException("[qemu error] $it") }
        println it

  } as Appendable

  static class QemuException extends IllegalArgumentException
  {
    QemuException(String data) {super(data)} 
  }
}

