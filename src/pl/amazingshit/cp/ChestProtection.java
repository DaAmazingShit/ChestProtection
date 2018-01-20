package pl.amazingshit.cp;

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
import pl.amazingshit.cp.listeners.Plugins;
import pl.amazingshit.cp.managers.ConfigManager;
import pl.amazingshit.cp.managers.DatabaseManager;
import pl.amazingshit.cp.managers.LanguageManager;
import pl.amazingshit.cp.util.Bukkit;
import pl.amazingshit.cp.util.Operation;
import pl.amazingshit.cp.util.Permission;
import pl.amazingshit.cp.util.Permission.Perm;
/**
 * Main ChestProtection class
 * @author DaAmazingShit
 */
public class ChestProtection extends JavaPlugin {
	// TODO: */cp INFO!!!!!!!!!!!!!!!!!!!!!!*
	public static Plugin instance;
	public static LanguageManager lang;
	public static String prefix = "[ChestProtection] ";
	public static CraftServer server;
	public static boolean pe = false;
	
	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		server = (CraftServer)this.getServer();
		Bukkit.set(server);
		lang   = new LanguageManager();
		instance = this;
		if (!ConfigManager.configExists()) {
			ConfigManager.createConfig();
		}
		lang.setup();
		
		this.getServer().getLogger().info(prefix + "Enabled, v" + getVersion() + " by AmazingShit.");
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Type.BLOCK_IGNITE,    new Blocks(),     Priority.Normal,  this);
		pm.registerEvent(Type.BLOCK_PLACE,     new Blocks(),     Priority.Normal,  this);
		pm.registerEvent(Type.BLOCK_DAMAGE,    new Blocks(),     Priority.Normal,  this);
		pm.registerEvent(Type.BLOCK_BREAK,     new Blocks(),     Priority.Normal,  this);
		
		pm.registerEvent(Type.PLAYER_INTERACT, new Players(),    Priority.Normal,  this);
		
		pm.registerEvent(Type.ENTITY_EXPLODE,  new Explosions(), Priority.Normal,  this);
		
		pm.registerEvent(Type.PLUGIN_ENABLE,   new Plugins(),    Priority.Monitor, this);
	}

	public static String getVersion() {
		return "1.2";
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
					if (DatabaseManager.doesPlayerOwnContainer(p, toRemove)) {
						if (!Permission.hasPlayer(p, Perm.REMOVE) && pe) {
							p.sendMessage(ChatColor.RED + lang.noPerm);
							return true;
						}
						DatabaseManager.removeContainerFromDB(toRemove);
						
						p.sendMessage(ChatColor.YELLOW + lang.protectionRemoved);
						return true;
					}
					if (!Permission.hasPlayer(p, Perm.REMOVE_OTHER) && pe) {
						p.sendMessage(ChatColor.RED + lang.noPerm);
						return true;
					}
					DatabaseManager.removeContainerFromDB(toRemove);
					
					p.sendMessage(ChatColor.YELLOW + lang.protectionRemoved);
					return true;
				} else {
					p.sendMessage(ChatColor.RED + lang.containerNotSelected);
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase(lang.protectionArgAdd)) {
				if (!Permission.hasPlayer(p, Perm.CREATE) && pe) {
					p.sendMessage(ChatColor.RED + lang.noPerm);
					return true;
				}
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location toAdd = Blocks.lastClicked.get(p.getName());
					
					Operation op = DatabaseManager.addContainerToDB(p, toAdd);
					
					if (op == Operation.SUCCESS) {
						p.sendMessage(ChatColor.GREEN + lang.protectionAdded);
					}
					if (op == Operation.ALREADY_EXISTS) {
						p.sendMessage(ChatColor.RED + "This container is already protected!");
					}
					if (op == Operation.FAIL) {
						p.sendMessage(ChatColor.RED + "Something went wrong!");
					}
					if (op == Operation.NULL) {
						p.sendMessage(ChatColor.RED + "It seems that you don't exist...");
					}
					
					return true;
				} else {
					p.sendMessage(ChatColor.RED + lang.containerNotSelected);
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase(lang.assignToContainerArg)) {
				if (!Permission.hasPlayer(p, Perm.PLAYER_ADD) && pe) {
					p.sendMessage(ChatColor.RED + lang.noPerm);
					return true;
				}
				if (args.length == 1) {
					lang.displayHelp(cmdalias, p);
					return true;
				}
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location lc = Blocks.lastClicked.get(p.getName());
					String player = args[1];
					
					Operation op = DatabaseManager.addPlayerToContainer(player, lc);
					
					if (op == Operation.SUCCESS) {
						p.sendMessage(ChatColor.GREEN + "Successfully added player to your container!");
					}
					if (op == Operation.ALREADY_EXISTS) {
						p.sendMessage(ChatColor.RED + "This player is already assigned to your container!");
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
				if (!Permission.hasPlayer(p, Perm.PLAYER_REMOVE) && pe) {
					p.sendMessage(ChatColor.RED + lang.noPerm);
					return true;
				}
				if (args.length == 1) {
					lang.displayHelp(cmdalias, p);
					return true;
				}
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location lc = Blocks.lastClicked.get(p.getName());
					String player = args[1];
					Operation op = DatabaseManager.removePlayerFromContainer(player, lc);
					if (op == Operation.SUCCESS) {
						p.sendMessage(ChatColor.GREEN + "Successfully removed player from your container!");
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
		if (cmd.getName().equalsIgnoreCase("cpreload")) {
			if (!Permission.hasPlayer(p, Perm.RELOAD) && pe) {
				p.sendMessage(ChatColor.RED + lang.noPerm);
				return true;
			}
			DatabaseManager.config.load();
			lang.setup();
			ConfigManager.config.load();
			sender.sendMessage(ChatColor.GREEN + "Reloaded ChestProtection.");
			return true;
		}
		return true;
	}
}
