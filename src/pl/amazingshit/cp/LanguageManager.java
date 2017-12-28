package pl.amazingshit.cp;

import java.io.File;
import org.bukkit.util.config.Configuration;

/**
 * Manages language files.
 * @author DaAmazingShit
 *
 */
public class LanguageManager {
	
	public LanguageManager() {}
	
	private Configuration config        = null;
	private Configuration langSource    = null;
	
	public String noAccess              = null;
	public String protectionAdded       = null;
	public String protectionRemoved     = null;
	public String protectionArgRemove   = null;
	public String protectionArgAdd      = null;
	public String containerNotSelected  = null;
	public String helpAdd               = null;
	public String helpRemove            = null;
	
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
			
			this.noAccess             = langSource.getString(main + ".no_access", this.noAccess);
			this.protectionAdded      = langSource.getString(main + ".protection_added", this.protectionAdded);
			this.protectionRemoved    = langSource.getString(main + ".protection_removed", this.protectionRemoved);
			this.protectionArgRemove  = langSource.getString(main + ".command_remove", this.protectionArgRemove);
			this.protectionArgAdd     = langSource.getString(main + ".command_add", this.protectionArgAdd);
			this.containerNotSelected = langSource.getString(main + ".container_not_selected", this.containerNotSelected);
			this.helpAdd              = langSource.getString(main + ".help_add", this.helpAdd);
			this.helpRemove           = langSource.getString(main + ".help_remove", this.helpRemove);
		}
		else {
			this.createLangFile(file);
		}
	}
	
	private void Default() {
		this.noAccess             = "You cannot access this container.";
		this.protectionAdded      = "This container is now protected.";
		this.protectionRemoved    = "This container is no longer protected.";
		this.protectionArgRemove  = "remove";
		this.protectionArgAdd     = "add";
		this.containerNotSelected = "You haven't selected a container.";
		this.helpAdd              = " add    - Adds protection to left-clicked container";
		this.helpRemove           = " remove - Removes protection from left-clicked container";
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
		use.setProperty(main + ".container_not_selected", "You haven't selected a container.");
		use.setProperty(main + ".help_add", " add      - Adds protection to left-clicked container");
		use.setProperty(main + ".help_remove", " remove - Removes protection from left-clicked container");
		use.save();
	}
}
