package pl.amazingshit.cp.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CPCommand {

	protected CommandSender sender;
	protected Command cmd;
	protected String alias;
	protected String[] args;

	public CPCommand(CommandSender sender, Command cmd, String cmdalias, String[] args) {
		this.sender = sender;
		this.cmd    = cmd;
		this.alias  = cmdalias;
		this.args   = args;
	}
}