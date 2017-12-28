package pl.amazingshit.cp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestProtection extends JavaPlugin {
	
	public static Plugin instance;
	public static LanguageManager lang;
	
	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		lang = new LanguageManager();
		instance = this;
		if (!DatabaseManager.databaseExists()) {
			DatabaseManager.createDatabase();
		}
		lang.setup();
		
		Bukkit.getServer().getLogger().info("ChestProtection enabled");
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Type.BLOCK_IGNITE,    new Blocks(),     Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_PLACE,     new Blocks(),     Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_BREAK,     new Blocks(),     Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_DAMAGE,    new Blocks(),     Priority.Normal, this);
		
		pm.registerEvent(Type.ENTITY_EXPLODE,  new Explosions(), Priority.Normal, this);
		
		pm.registerEvent(Type.PLAYER_INTERACT, new Players(),    Priority.Normal, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		
		if (!(sender instanceof Player)) {
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("cp")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + lang.helpAdd);
				sender.sendMessage(ChatColor.RED + lang.helpRemove);
				return true;
			}
			Player p = (Player)sender;
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
		}
		return true;
	}
}
