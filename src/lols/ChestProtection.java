package lols;

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
	
	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		instance = this;
		if (!Database.databaseIstnieje()) {
			Database.stworzDatabase();
		}
		
		Bukkit.getServer().getLogger().info("ChestProtection enabled");
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Type.BLOCK_IGNITE,    new blokiXD(),  Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_CHANGE,    new blokiXD(),  Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_PLACE,     new blokiXD(),  Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_BREAK,     new blokiXD(),  Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_DAMAGE,    new blokiXD(),  Priority.Normal, this);
		
		pm.registerEvent(Type.ENTITY_EXPLODE,  new tienti(),   Priority.Normal, this);
		
		pm.registerEvent(Type.PLAYER_INTERACT, new lystener(), Priority.Normal, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		
		if (!(sender instanceof Player)) {
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("ochrona")) {
			if (args.length == 0) {
				sender.sendMessage("/ochrona usun  - Usuwa ochrone z wczesniej zaznaczonego pojemnika (LPM)");
				sender.sendMessage("/ochrona dodaj - Dodaje ochrone do wczesniej zaznaczonego pojemnika (LPM)");
				return true;
			}
			Player p = (Player)sender;
			if (args[0].equalsIgnoreCase("usun")) {
				if (blokiXD.ostatnioklik.get(p.getName()) != null) {
					Location toRemove = blokiXD.ostatnioklik.get(p.getName());
					
					Database.usunPojemnikZDatabase(toRemove);
					
					p.sendMessage(ChatColor.YELLOW + "Pojemnik jest teraz publiczny.");
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "Nie zaznaczyles pojemnika!");
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("dodaj")) {
				if (blokiXD.ostatnioklik.get(p.getName()) != null) {
					Location toAdd = blokiXD.ostatnioklik.get(p.getName());
					
					Database.dodajPojemnikDoDatabase(p, toAdd);
					
					p.sendMessage(ChatColor.YELLOW + "Pojemnik jest teraz prywatny.");
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "Nie zaznaczyles pojemnika!");
					return true;
				}
			}
		}
		return true;
	}
}
