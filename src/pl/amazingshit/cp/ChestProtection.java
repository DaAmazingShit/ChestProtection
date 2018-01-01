package pl.amazingshit.cp;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import pl.amazingshit.cp.listeners.Blocks;
import pl.amazingshit.cp.listeners.Explosions;
import pl.amazingshit.cp.listeners.Players;
import pl.amazingshit.cp.managers.DatabaseManager;
import pl.amazingshit.cp.managers.LanguageManager;
import pl.amazingshit.cp.util.Bukkit;
import pl.amazingshit.cp.util.ConfigUtil;
import pl.amazingshit.cp.util.Operation;
import pl.amazingshit.cp.util.cp;
/**
 * Main ChestProtection class
 * @author DaAmazingShit
 */
public class ChestProtection extends JavaPlugin {
	
	public static Plugin instance;
	public static LanguageManager lang;
	public static ConfigUtil config;
	public static String prefix = "[ChestProtection] ";
	public static CraftServer server;
	
	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		server = (CraftServer)this.getServer();
		Bukkit.set(server);
		config = new ConfigUtil(new File(this.getDataFolder(), "config.yml"));
		lang   = new LanguageManager();
		instance = this;
		if (!DatabaseManager.configExists()) {
			DatabaseManager.createConfig();
		}
		lang.setup();
		
		this.getServer().getLogger().info("ChestProtection enabled");
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Type.BLOCK_IGNITE,    new Blocks(),     Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_PLACED,     new Blocks(),     Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_DAMAGED,    new Blocks(),     Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_BREAK,     new Blocks(),     Priority.Normal, this);
		
		pm.registerEvent(Type.BLOCK_INTERACT, new Players(),    Priority.Normal, this);
		
		pm.registerEvent(Type.ENTITY_EXPLODE,  new Explosions(), Priority.Normal, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdalias, String[] args) {
		
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player)sender;
		if (cmd.getName().equalsIgnoreCase("cp")) {
			if (args.length == 0) {
				lang.displayHelp(cmdalias, p);
				return true;
			}
			if (args[0].equalsIgnoreCase(lang.protectionArgRemove)) {
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location toRemove = Blocks.lastClicked.get(p.getName());
					
					DatabaseManager.removeContainerFromDB(toRemove);
					
					p.sendMessage(ChatColor.YELLOW + ChestProtection.lang.protectionRemoved);
					return true;
				} else {
					p.sendMessage(ChatColor.RED + ChestProtection.lang.containerNotSelected);
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase(lang.protectionArgAdd)) {
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location toAdd = Blocks.lastClicked.get(p.getName());
					
					DatabaseManager.addContainerToDB(p, toAdd);
					
					p.sendMessage(ChatColor.YELLOW + ChestProtection.lang.protectionAdded);
					return true;
				} else {
					p.sendMessage(ChatColor.RED + ChestProtection.lang.containerNotSelected);
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase(lang.assignToContainerArg)) {
				if (args.length == 1) {
					lang.displayHelp(cmdalias, p);
					return true;
				}
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location lc = Blocks.lastClicked.get(p.getName());
					String player = args[1];
					Operation op = DatabaseManager.addPlayerToContainer(player, lc);
					if (op == Operation.SUCCESS) {
						p.sendMessage(ChatColor.GREEN + "Successfully removed player from your container!");
					}
					if (op == Operation.ALREADY_EXISTS) {
						p.sendMessage(ChatColor.RED + "A player is already assigned to this container!");
					}
					if (op == Operation.NOT_PROTECTED) {
						p.sendMessage(ChatColor.RED + "This container is not protected.");
					}
					if (op == Operation.FAIL) {
						p.sendMessage(ChatColor.RED + "Something went wrong!");
					}
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + lang.containerNotSelected);
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase(lang.removeFromContainerArg)) {
				if (args.length == 1) {
					lang.displayHelp(cmdalias, p);
					return true;
				}
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location lc = Blocks.lastClicked.get(p.getName());
					String player = args[1];
					Operation op = DatabaseManager.removePlayerFromContainer(player, lc);
					if (op == Operation.SUCCESS) {
						p.sendMessage(ChatColor.GREEN + "Successfully added player to your container!");
					}
					if (op == Operation.NOT_IN_LIST) {
						p.sendMessage(ChatColor.RED + "This player is not assigned to your container!");
					}
					if (op == Operation.NOT_PROTECTED) {
						p.sendMessage(ChatColor.RED + "This container is not protected.");
					}
					if (op == Operation.FAIL) {
						p.sendMessage(ChatColor.RED + "Something went wrong!");
					}
					return true;
				}
				else {
					p.sendMessage(ChatColor.RED + lang.containerNotSelected);
					return true;
				}
			}
			else {
				lang.displayHelp(cmdalias, p);
			}
		}
		if (cmd.getName().equalsIgnoreCase("reloadcp")) {
			DatabaseManager.config.load();
			lang.setup();
			config.load();
			cp.cnfutil.load();
			sender.sendMessage(ChatColor.GREEN + "Reloaded ChestProtection.");
			return true;
		}
		return true;
	}
}
