package qemulib

import groovy.json.JsonBuilder

import qemulib.Qemu

public class VM 
{
	def qemu

	// map of processed command line args
	def cmds 		= [:]

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


	VM(qemu)
	{
		this.qemu = qemu
	}

	def start()
	{
		qemu.exec(makeArgs())
	}

	def makeArgs()
	{
		VM.makeArgs(this)
	}

	def private static makeArgs =
	{ vm ->
		def result = ''

		vm.cmds.keySet().each
		{
			result += ' ' + vm.cmds[it]
		}

		return result
	}

	// QMP stuff
	//

	def json = 
	{
		return new JsonBuilder(it).toString()
	}

	def quit =
	{
		return json([execute: 'quit'])
	}


	def eject =
	{ dev ->
		return json([execute: 'eject', arguments: ['device': dev]])
	}

	//
	// -- End QMP stuff


	def boot(params)
	{
		// TODO
	}

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

	}

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

