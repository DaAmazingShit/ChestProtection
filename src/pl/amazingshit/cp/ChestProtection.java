package pl.amazingshit.cp;

import java.lang.reflect.Field;

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
		if (this.getServerVersion() == "1.3" || this.getServerVersion() == "1.3_01" ||
				this.getServerVersion() == "1.5_01" || this.getServerVersion() == "1.6.1" ||
				this.getServerVersion() == "1.6.3" || this.getServerVersion() == "1.6.4" ||
				this.getServerVersion() == "1.6.5" || this.getServerVersion() == "1.6.6" ||
				this.getServerVersion() == "1.7_01" || this.getServerVersion() == "1.7.2" ||
				this.getServerVersion() == "1.7.3") {
			pm.registerEvent(Type.BLOCK_IGNITE,    new Blocks(),     Priority.Normal,  this);
			pm.registerEvent(Type.BLOCK_PLACE,     new Blocks(),     Priority.Normal,  this);
			pm.registerEvent(Type.BLOCK_DAMAGE,    new Blocks(),     Priority.Normal,  this);
			pm.registerEvent(Type.BLOCK_BREAK,     new Blocks(),     Priority.Normal,  this);
			
			pm.registerEvent(Type.PLAYER_INTERACT, new Players(),    Priority.Normal,  this);
			
			pm.registerEvent(Type.ENTITY_EXPLODE,  new Explosions(), Priority.Normal,  this);
			
			pm.registerEvent(Type.PLUGIN_ENABLE,   new Plugins(),    Priority.Monitor, this);
		}
		if (this.getServerVersion() == "1.2" || this.getServerVersion() == "1.2_01" ||
				this.getServerVersion() == "1.2_02" || this.getServerVersion() == "1.1_02" ||
				this.getServerVersion() == "1.1" || this.getServerVersion() == "1.1_01") {
			pm.registerEvent(Type.BLOCK_IGNITE,    new Blocks(),     Priority.Normal,  this);
			pm.registerEvent(Type.BLOCK_PLACED,    new Blocks(),     Priority.Normal,  this);
			pm.registerEvent(Type.BLOCK_DAMAGED,   new Blocks(),     Priority.Normal,  this);
			pm.registerEvent(Type.BLOCK_BREAK,     new Blocks(),     Priority.Normal,  this);
			
			pm.registerEvent(Type.BLOCK_INTERACT,  new Blocks(),     Priority.Normal,  this);
			
			pm.registerEvent(Type.ENTITY_EXPLODE,  new Explosions(), Priority.Normal,  this);
			
			pm.registerEvent(Type.PLUGIN_ENABLE,   new Plugins(),    Priority.Monitor, this);
		}
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

	public String getServerVersion() {
		CraftServer cs = server;
		
		Field f;
		try {
			f = CraftServer.class.getDeclaredField("protocolVersion");
		}
		catch (Exception ex) {
			return null;
		}
		
		String version;
		try {
			f.setAccessible(true);
			version = (String) f.get(cs);
		}
		catch (Exception ex) {
			return null;
		}
		return version;
	}
}