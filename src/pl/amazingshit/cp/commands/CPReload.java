package pl.amazingshit.cp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.amazingshit.cp.cp;
import pl.amazingshit.cp.managers.ConfigManager;
import pl.amazingshit.cp.managers.DatabaseManager;
import pl.amazingshit.cp.util.CPCommand;
import pl.amazingshit.cp.util.Permission;
import pl.amazingshit.cp.util.Permission.Perm;

public class CPReload extends CPCommand {

	public CPReload(CommandSender sender, Command cmd, String cmdalias, String[] args) {
		super(sender, cmd, cmdalias, args);
	}

	public void handle() {
		if (!cmd.getName().equalsIgnoreCase("cpreload")) {
			return;
		}
		if (!(sender instanceof Player)) {
			if (cmd.getName().equalsIgnoreCase("cpreload")) {
				DatabaseManager.config.load();
				cp.lang.setup();
				ConfigManager.config.load();
				sender.sendMessage("Reloaded ChestProtection.");
				return;
			}
			return;
		}
		Player p = (Player)sender;
		if (cmd.getName().equalsIgnoreCase("cpreload")) {
			if (!Permission.hasPlayer(p, Perm.RELOAD) && cp.pe) {
				p.sendMessage(ChatColor.RED + cp.lang.noPerm);
				return;
			}
			DatabaseManager.config.load();
			cp.lang.setup();
			ConfigManager.config.load();
			sender.sendMessage(ChatColor.GREEN + "Reloaded ChestProtection.");
			return;
		}
	}
}