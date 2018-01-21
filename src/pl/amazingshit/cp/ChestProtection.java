package pl.amazingshit.cp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import pl.amazingshit.cp.listeners.Blocks;
import pl.amazingshit.cp.listeners.Explosions;
import pl.amazingshit.cp.listeners.Players;
import pl.amazingshit.cp.listeners.Plugins;
import pl.amazingshit.cp.managers.CommandManager;
import pl.amazingshit.cp.managers.CommandManager.Commands;
import pl.amazingshit.cp.managers.ConfigManager;
import pl.amazingshit.cp.managers.LanguageManager;
import pl.amazingshit.cp.util.Bukkit;
/**
 * Main ChestProtection class
 * @author DaAmazingShit
 */
public class ChestProtection extends JavaPlugin {
	// TODO: fix /cp removeplayer and addplayer with double chests

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

	/**
	 * Gets the version of the CPPlugin.
	 */
	public static String getVersion() {
		return instance.getDescription().getVersion();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdalias, String[] args) {
		for (Commands c: CommandManager.getCommands()) {
			c.execute(sender, cmd, cmdalias, args);
		}
		return true;
	}
}
