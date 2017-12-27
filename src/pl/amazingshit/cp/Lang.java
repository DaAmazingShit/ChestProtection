package pl.amazingshit.cp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.bukkit.util.config.Configuration;

public class Lang {
	
	public Lang() {}
	
	private Configuration config        = null;
	private Configuration langSource    = null;
	
	public String noAccess              = null;
	public String protectionAdded       = null;
	public String protectionRemoved     = null;
	public String protectionCommand     = null;
	public String protectionArgRemove   = null;
	public String protectionArgAdd      = null;
	public String containerNotSelected  = null;
	public String helpAdd               = null;
	public String helpRemove            = null;
	
	public void setup() {
		File langFile = new File("plugins/ChestProtection", "lang.yml");
		config = new Configuration(langFile);
		config.load();
		if (config.getProperty("lang.custom") == null) {
		    BufferedWriter out;
			try {
				out = new BufferedWriter(new FileWriter("plugins/ChestProtection/lang.yml", true));
			    out.write("# You can select your custom lang.yml file which will be used to display custom messages in the game (if you have one)");
			    out.close();
			} catch (Exception ex) {
				// We don't actually need this annotation
			}
			config.setProperty("lang.custom", "off");
			config.save();
			this.Default();
		}
		if (((String) config.getProperty("lang.custom")).equalsIgnoreCase("off")) {
			this.Default();
		}
		File file = new File("plugins/ChestProtection", (String) config.getProperty("lang.custom"));
		if (file.exists()) {
			langSource = new Configuration(file);
			String main = "strings.";
			if (langSource.getProperty(main + ".no_access") == null || langSource.getProperty(main + ".protection_added") == null || 
					langSource.getProperty(main + ".protection_removed") == null|| langSource.getProperty(main + ".command_remove") == null||
					langSource.getProperty(main + ".command_add") == null || langSource.getProperty(main + ".container_is") == null ||
					langSource.getProperty(main + ".publicied") == null || langSource.getProperty(main + ".privated") == null ||
					langSource.getProperty(main + ".container_not_selected") == null || langSource.getProperty(main + ".help_add") == null ||
					langSource.getProperty(main + ".help_remove") == null) {
				// TODO Automatically create a fresh language file
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
	}
	
	private void Default() {
		this.noAccess             = "You cannot access this container.";
		this.protectionAdded      = "Added protection to your container.";
		this.protectionRemoved    = "Removed protection from your container.";
		this.protectionCommand    = "cp";
		this.protectionArgRemove  = "remove";
		this.protectionArgAdd     = "add";
		this.containerNotSelected = "You haven't selected a container.";
		this.helpAdd              = "/cp add    - Adds protection to left-clicked container";
		this.helpRemove           = "/cp remove - Removes protection from left-clicked container";
	}
}
