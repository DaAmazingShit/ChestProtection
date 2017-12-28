package pl.amazingshit.cp.managers;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;
/**
 * Manages language files.
 * @author DaAmazingShit
 */
public class LanguageManager {
	
	public LanguageManager() {}
	
	private Configuration config          = null;
	private Configuration langSource      = null;
	
	public String noAccess                = null;
	public String protectionAdded         = null;
	public String protectionRemoved       = null;
	public String protectionArgRemove     = null;
	public String protectionArgAdd        = null;
	public String containerNotSelected    = null;
	public String helpAdd                 = null;
	public String helpRemove              = null;
	public String helpAddPlayer           = null;
	public String helpRemovePlayer        = null;
	public String assignToContainerArg    = null;
	public String removeFromContainerArg  = null;
	
	/**
	 * Reloads language files or setups them
	 * 
	 */
	public void setup() {
		File langFile = new File("plugins/ChestProtection", "lang.yml");
		config = new Configuration(langFile);
		config.load();
		if (config.getProperty("lang.custom") == null) {
			config.setHeader("# You can select your custom lang.yml file which will be used to display custom messages in the game (if you have one)");
			config.setProperty("lang.custom", null);
			config.save();
			this.Default();
		}
		String isOff = null;
		isOff = config.getString("lang.custom", isOff);
		if (isOff == null) {
			this.Default();
			return;
		}
		String custom = null;
		custom = config.getString("lang.custom", custom);
		File file = new File("plugins/ChestProtection", custom);
		if (file.exists()) {
			langSource = new Configuration(file);
			langSource.load();
			String main = "strings";
			if (langSource.getProperty(main) == null) {
				this.createLangFile(file);
			}
			
			this.noAccess               = langSource.getString(main + ".no_access", this.noAccess);
			this.protectionAdded        = langSource.getString(main + ".protection_added", this.protectionAdded);
			this.protectionRemoved      = langSource.getString(main + ".protection_removed", this.protectionRemoved);
			this.protectionArgRemove    = langSource.getString(main + ".command_remove", this.protectionArgRemove);
			this.protectionArgAdd       = langSource.getString(main + ".command_add", this.protectionArgAdd);
			this.containerNotSelected   = langSource.getString(main + ".container_not_selected", this.containerNotSelected);
			this.helpAdd                = langSource.getString(main + ".help_add", this.helpAdd);
			this.helpRemove             = langSource.getString(main + ".help_remove", this.helpRemove);
			this.helpAddPlayer          = langSource.getString(main + ".help_addplayer", this.helpAddPlayer);
			this.helpRemovePlayer       = langSource.getString(main + ".help_removeplayer", this.helpRemovePlayer);
			this.assignToContainerArg   = langSource.getString(main + ".command_addplayer", this.assignToContainerArg);
			this.removeFromContainerArg = langSource.getString(main + ".command_removeplayer", this.removeFromContainerArg);
		}
		else {
			this.createLangFile(file);
		}
	}
	
	private void Default() {
		this.noAccess               = "You cannot access this container.";
		this.protectionAdded        = "This container is now protected.";
		this.protectionRemoved      = "This container is no longer protected.";
		this.protectionArgRemove    = "remove";
		this.protectionArgAdd       = "add";
		this.containerNotSelected   = "You haven't selected a container.";
		this.helpAdd                = " add    - Adds protection to left-clicked container";
		this.helpRemove             = " remove - Removes protection from left-clicked container";
		this.helpAddPlayer          = " addplayer <player> - Allows the <player> to access the container";
		this.helpRemovePlayer       = " removeplayer <player> - Denies the <player> to access the container";
		this.assignToContainerArg   = "addplayer";
		this.removeFromContainerArg = "removeplayer";
	}
	
	private void createLangFile(File file) {
		String main = "strings";
		Configuration use;
		use = new Configuration(file);
		use.load();
		use.setProperty(main + ".no_access", "You cannot access this container.");
		use.setProperty(main + ".protection_added", "Added protection to your container.");
		use.setProperty(main + ".protection_removed", "Removed protection from your container.");
		use.setProperty(main + ".command_remove", "remove");
		use.setProperty(main + ".command_add", "add");
		use.setProperty(main + ".command_addplayer", "addplayer");
		use.setProperty(main + ".command_removeplayer", "removeplayer");
		use.setProperty(main + ".container_not_selected", "You haven't selected a container.");
		use.setProperty(main + ".help_add", " add      - Adds protection to left-clicked container");
		use.setProperty(main + ".help_remove", " remove - Removes protection from left-clicked container");
		use.setProperty(main + ".help_addplayer", " addplayer <player> - Allows the <player> to access the container");
		use.setProperty(main + ".help_removeplayer", " removeplayer <player> - Denies the <player> to access the container");
		use.save();
	}
	
	/**
	 * Displays help to the player
	 * 
	 * @param cmd what player typed
	 * @param p player
	 */
	public void displayHelp(String cmd, Player p) {
		p.sendMessage(ChatColor.RED + "/" + cmd + this.helpAdd);
		p.sendMessage(ChatColor.RED + "/" + cmd + this.helpRemove);
		p.sendMessage(ChatColor.RED + "/" + cmd + this.helpAddPlayer);
		p.sendMessage(ChatColor.RED + "/" + cmd + this.helpRemovePlayer);
	}
}
