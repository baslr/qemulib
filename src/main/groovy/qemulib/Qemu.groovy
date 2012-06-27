package qemulib

import qemulib.VM

public class Qemu
{
	def vms = []
	def qProps = [
		qemu: "qemu",
		tcp: true,
		port: 1231
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
		def vm = new VM(this)

		vms.push(vm)

		return vm
	}

	def exec(cmd)
	{
		"${qProps.qemu} $cmd".execute().consumeProcessOutput(qLog, qError)
	}

	def qLog = 
	{
		append: 
			if (it.trim().length() > 0) // squash blank lines
				println "[log] $it"

	} as Appendable


	def qError = 
	{
		append:
				{ throw new QemuException("[qemu error] $it") }

	} as Appendable

	static class QemuException extends IllegalArgumentException
	{
		QemuException(String data) {super(data)} 
	}
}

