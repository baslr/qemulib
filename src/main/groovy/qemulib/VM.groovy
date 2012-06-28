package qemulib

import groovy.json.JsonBuilder

import qemulib.Qemu

public class VM 
{
	def qemu

	// map of processed command line args
	def cmds 		= [:]

	// handle to this vm's qmp
	def qmp

	def drives 	= []
	def nics		= []

	private static final cmdList = 
	[
		boot		: "-boot",
		drive 	: "-drive",
		kernel	: "-kernel",
		append	: "-append",
		initrd	: "-initrd",
		cdrom		: "-cdrom",
		hda   	: "-hda",
		hdb			: "-hdb",
		hdc			: "-hdc",
		hdd			: "-hdd",
		nic			: "-net nic,",
		net 	  : "-net user,"
	]

	private static final args = 
	[
		// -boot
		order		: ~/[a-p]/,
		once		: ~/[a-p]/,
		menu		: ~/on|off/,

		// -drive
		// TODO ‘cyls=c,heads=h,secs=s[,trans=t]’
		file		: null,
		'if'		: ~/ide|scsi|sd|mtd|floppy|pflash|virtio/,
		bus			: null,
		nit		: null,
		index		: null,
		media		: ~/disk|cdrom/,
		snapshot: ~/on|off/,
		cache		: ~/none|writeback|writetrough/,
		aio			: ~/threads|native/,
		serial	: null,

		// networking 
		name		: null, // device name in monitor commands
		addr		: null, // device addr for PCI cards only
		net 		: null,
		vlan		: ~/[0-9]+/,
		macaddr	: null,
		model		: ~/virtio|i82551|i82557b|i82559er|ne2k_pci|ne2k_isa|pcnet|rtl8139|e1000|smc91c111|lance|mcf_fec/,
		vectors : null
	]

	// TODO Valid drive letters depend on the target achitecture. 
	// map to convenience bootcodes - used in bootOrder 
	// TODO use with -boot once
	private static bootCmd =
	[
		d 		: ~/c|cd|cdrom/,
		c 		: ~/disk|hda/,
		n 		: ~/net|eth|eth0/,
		l 		: ~/eth1/,
		o 		: ~/eth2/,
		p 		: ~/eth3/
	]

	VM(qemu)
	{
		this.qemu = qemu
	}

	def start()
	{
		qemu.start(this)
		openSocket()
	}

	def openTries = 0
	def openSocket =
	{
		Thread.start 
		{
			try {
					def s = new Socket('localhost', qmp)
					s.close()				                            

					capabilities()
			} catch(IOException e) {
				if (++openTries == 10)
				{
					println "giving up on $qmp"
					return
				}
				println "Failed opening QMP socket, retrying: $qmp"
				sleep 100
				openSocket()
			}	
		}
	}

	// create the qmp socket


	def makeArgs()
	{
		def result = ''

		this.cmds.keySet().each
		{
			result += ' ' + this.cmds[it]
		}

		return result
	}

	// QMP stuff
	//

	def qmpSend(data) 
	{
		try {

			def s = new Socket('localhost', qmp)

			def ins = s.getOutputStream()
			ins << data
			ins.flush()

			s.getInputStream().eachLine()
			{
				println "[vm $qmp] $it"
				return
			}

			ins.close()

		} catch (IOException io) {
			println 'Failed writing to QMP socket.'
		}
	}

	def json = 
	{
		return new JsonBuilder(it).toString()
	}

	def capabilities =
	{
		qmpSend(json([execute: 'qmp_capabilities']))

		[this]
	}

	def quit =
	{
		qmpSend(json([execute: 'quit']))

		qmp.close()
		[this]
	}


	def eject =
	{ dev ->
		return json([execute: 'eject', arguments: ['device': dev]])
	}

	//
	// -- End QMP stuff


	def private cmdput =
	{ command, arg ->
		cmds.put(cmdList[command], cmdList[command] + " $arg")
	}

	def kernel(file)
	{
		cmdput("kernel", file)
		return this
	}

	def append(line)
	{
		cmdput("append", line)
		return this
	}

	def initrd(file)
	{
		cmdput("initrd", file)
		return this
	}

	// qemu's user mode networking
	def net(props)
	{
		// TODO
	}

	def boot(props)
	{
		def cmd = ''

		props.keySet().each
		{
			if (args[it])
				if (!args[it].matcher(props[it] as String).matches())
					throw new IllegalArgumentException("boot $it=${props[it]}")

			cmd += "$it=${props[it]},"
		}

		// strip trailing comma
		cmd = cmd[0 .. cmd.length()-2]

		cmdput("boot", cmd)

		return this
	}

	// def bootMenu(bool)
	// {
	// 	(bool as Boolean) ? boot([menu: 'on']) : boot([menu: 'off'])
	// 	return this
	// }

	// // convenience wrapper for boot order
	// def bootOrder(props)
	// {

	// }

	// wrapper for qemu -net nic
	def nic(props)
	{
		def cmd = cmdList.nic

		props.keySet().each
		{
			if (args[it])
				if (!args[it].matcher(props[it] as String).matches())
					throw new IllegalArgumentException("$cmd ${props[it]}")

			cmd += "$it=${props[it]},"
		}

		// strip trailing comma
		cmd = cmd[0 .. cmd.length()-2]

		nics.push(cmd)
		return this
	}

	// creates a new drive
	def drive(props)
	{
		def cmd = cmdList.drive + ' '

		props.keySet().each
		{
			if (args[it])
				if (!args[it].matcher(props[it]).matches())
					throw new IllegalArgumentException("$cmd ${props[it]} Invalid")

			cmd += "$it=${props[it]},"
		}

		// strip trailing comma
		cmd = cmd[0 .. cmd.length()-2]

		drives.push(cmd)

		return this
	}

	def cdrom(file)
	{
		cmdput("cdrom", file)
		return this
	}

	def hda(file)
	{
		cmdput("hda", file)
		return this
	}

	def hdb(file)
	{
		cmdput("hdb", file)
		return this
	}

	def hdc(file)
	{
		cmdput("hdc", file)
		return this
	}

	def hdd(file)
	{
		cmdput("hdd", file)
		return this
	}

}
