package pl.amazingshit.cp.managers;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.amazingshit.cp.util.ConfigUtil;
import pl.amazingshit.cp.util.Permission;
import pl.amazingshit.cp.util.Permission.Perm;
/**
 * Manages language files.
 */
public class LanguageManager {

	public LanguageManager() {}

	public ConfigUtil config              = null;
	public ConfigUtil langSource          = null;

	public String helpAdd                 = null;
	public String helpRemove              = null;
	public String helpAddPlayer           = null;
	public String helpInfo                = null;
	public String helpRemovePlayer        = null;

	public String noAccess                = null;
	public String noPerm                  = null;
	public String noPlayer                = null;
	public String containerNotSelected    = null;
	public String notAssigned             = null;
	public String notProtected            = null;
	public String wrong                   = null;

	public String protectionAdded         = null;
	public String protectionRemoved       = null;
	public String addedToSuccess          = null;
	public String removedFromSuccess      = null;
	public String accessProtContAdmin     = null;
	public String removedProtContAdmin    = null;

	public String alreadyProtected        = null;
	public String alreadyAssigned         = null;

	public String protectionArgRemove     = null;
	public String protectionArgAdd        = null;
	public String protectionArgInfo       = null;
	public String assignToContainerArg    = null;
	public String removeFromContainerArg  = null;
	
	public String playersOwningThis        = null;

	/**
	 * Reloads language files or setups them
	 */
	public void setup() {
		File langFile = new File("plugins/ChestProtection", "lang.yml");
		config = new ConfigUtil(langFile);
		config.load();
		if (config.getProperty("lang.custom") == null) {
			config.setHeader("# You can select your custom lang.yml file which will be used to display custom messages in the game (if you have one)");
			config.setProperty("lang.custom", "");
			config.save();
			this.Default();
		}
		String isOff = null;
		isOff = config.getString("lang.custom", isOff);
		if (isOff == null) {
			this.Default();
			return;
		}
		File file = new File("plugins/ChestProtection", isOff);
		if (file.exists()) {
			langSource = new ConfigUtil(file);
			langSource.load();
			String main = "strings";
			if (langSource.getProperty(main) == null) {
				this.createLangFile(file);
			}
			
			this.noAccess               = langSource.getString(main + ".no_access", this.noAccess);
			this.noPerm                 = langSource.getString(main + ".no_perm", this.noPerm);
			this.protectionAdded        = langSource.getString(main + ".protection_added", this.protectionAdded);
			this.protectionRemoved      = langSource.getString(main + ".protection_removed", this.protectionRemoved);
			this.protectionArgRemove    = langSource.getString(main + ".command_remove", this.protectionArgRemove);
			this.protectionArgAdd       = langSource.getString(main + ".command_add", this.protectionArgAdd);
			this.protectionArgInfo      = langSource.getString(main + ".command_info", this.protectionArgInfo);
			this.containerNotSelected   = langSource.getString(main + ".container_not_selected", this.containerNotSelected);
			this.helpAdd                = langSource.getString(main + ".help_add", this.helpAdd);
			this.helpInfo               = langSource.getString(main + ".help_info", this.helpInfo);
			this.helpRemove             = langSource.getString(main + ".help_remove", this.helpRemove);
			this.helpAddPlayer          = langSource.getString(main + ".help_addplayer", this.helpAddPlayer);
			this.helpRemovePlayer       = langSource.getString(main + ".help_removeplayer", this.helpRemovePlayer);
			this.assignToContainerArg   = langSource.getString(main + ".command_addplayer", this.assignToContainerArg);
			this.removeFromContainerArg = langSource.getString(main + ".command_removeplayer", this.removeFromContainerArg);
			// 1.1
			this.removedProtContAdmin   = langSource.getString(main + ".admin.removed_container", "Removed protected container.");
			this.accessProtContAdmin    = langSource.getString(main + ".admin.access_container", "Accessing protected container.");
			this.wrong                  = langSource.getString(main + ".went_wrong", "Something went wrong!");
			this.notProtected           = langSource.getString(main + ".not_protected", "This container is not protected.");
			this.removedFromSuccess     = langSource.getString(main + ".removed_from", "Successfully removed player from your container!");
			this.addedToSuccess         = langSource.getString(main + ".added_to", "Successfully added player to your container!");
			this.notAssigned            = langSource.getString(main + ".not_assigned", "This player is not assigned to your container!");
			this.alreadyAssigned        = langSource.getString(main + ".already_assigned", "This player is already assigned to your container!");
			this.alreadyProtected       = langSource.getString(main + ".already_protected", "This container is already protected!");
			this.noPlayer               = langSource.getString(main + ".no_player", "It seems that you don't exist...");
			this.playersOwningThis      = langSource.getString(main + ".players_owning", "Players that own this container:");
		}
		else {
			this.Default();
			this.createLangFile(file);
		}
	}

	protected void Default() {
		this.noAccess               = "You cannot access this container.";
		this.noPerm                 = "You don't have permission to do this!";
		this.protectionAdded        = "This container is now protected.";
		this.protectionRemoved      = "This container is no longer protected.";
		this.protectionArgRemove    = "remove";
		this.protectionArgAdd       = "add";
		this.protectionArgInfo      = "info";
		this.containerNotSelected   = "You haven't selected a container.";
		this.helpAdd                = " add - Adds protection to left-clicked container";
		this.helpInfo               = " info - Shows information about protected container";
		this.helpRemove             = " remove - Removes protection from left-clicked container";
		this.helpAddPlayer          = " addplayer <player> - Allows the <player> to access the container";
		this.helpRemovePlayer       = " removeplayer <player> - Denies the <player> to access the container";
		this.assignToContainerArg   = "addplayer";
		this.removeFromContainerArg = "removeplayer";
		// 1.1
		this.removedProtContAdmin   = "Removed protected container.";
		this.accessProtContAdmin    = "Accessing protected container.";
		this.wrong                  = "Something went wrong!";
		this.notProtected           = "This container is not protected.";
		this.removedFromSuccess     = "Successfully removed player from your container!";
		this.addedToSuccess         = "Successfully added player to your container!";
		this.notAssigned            = "This player is not assigned to your container!";
		this.alreadyAssigned        = "This player is already assigned to your container!";
		this.alreadyProtected       = "This container is already protected!";
		this.noPlayer               = "It seems that you don't exist...";
		this.playersOwningThis      = "Players that own this container:";
	}

	protected void createLangFile(File file) {
		String main = "strings";
		ConfigUtil use;
		use = new ConfigUtil(file);
		use.load();
		use.setProperty(main + ".no_access", "You cannot access this container.");
		use.setProperty(main + ".no_perm", "You don't have permission to do this!");
		use.setProperty(main + ".protection_added", "Added protection to your container.");
		use.setProperty(main + ".protection_removed", "Removed protection from your container.");
		use.setProperty(main + ".command_remove", "remove");
		use.setProperty(main + ".command_add", "add");
		use.setProperty(main + ".command_info", "info");
		use.setProperty(main + ".command_addplayer", "addplayer");
		use.setProperty(main + ".command_removeplayer", "removeplayer");
		use.setProperty(main + ".container_not_selected", "You haven't selected a container.");
		use.setProperty(main + ".help_add", " add - Adds protection to left-clicked container");
		use.setProperty(main + ".help_info", " info - Shows information about protected container");
		use.setProperty(main + ".help_remove", " remove - Removes protection from left-clicked container");
		use.setProperty(main + ".help_addplayer", " addplayer <player> - Allows the <player> to access the container");
		use.setProperty(main + ".help_removeplayer", " removeplayer <player> - Denies the <player> to access the container");
		// 1.1
		use.setProperty(main + ".admin.removed_container", "Removed protected container.");
		use.setProperty(main + ".admin.access_container", "Accessing protected container.");
		use.setProperty(main + ".went_wrong", "Something went wrong!");
		use.setProperty(main + ".not_protected", "This container is not protected.");
		use.setProperty(main + ".removed_from", "Successfully removed player from your container!");
		use.setProperty(main + ".added_to", "Successfully added player to your container!");
		use.setProperty(main + ".not_assigned", "This player is not assigned to your container!");
		use.setProperty(main + ".already_assigned", "This player is already assigned to your container!");
		use.setProperty(main + ".already_protected", "This container is already protected!");
		use.setProperty(main + ".no_player", "It seems that you don't exist...");
		use.setProperty(main + ".players_owning", "Players that own this container:");
		use.save_();
	}

	/**
	 * Displays help to the player
	 * 
	 * @param cmd what player typed
	 * @param p player
	 */
	public void displayHelp(String cmd, CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Pomoc | Help:");
			sender.sendMessage("/cpreload - Reloads the plugin");
			return;
		}
		Player p = (Player)sender;
		if (!Permission.hasPlayer(p, Perm.CREATE) && !Permission.hasPlayer(p, Perm.REMOVE) && !Permission.hasPlayer(p, Perm.PLAYER_ADD) && !Permission.hasPlayer(p, Perm.PLAYER_REMOVE)) {
			p.sendMessage("You have no permission to any of ChestProtection command features.");
		}
		p.sendMessage(ChatColor.BLUE + "Pomoc | Help:");
		if (Permission.hasPlayer(p, Perm.CREATE)) {
			p.sendMessage(ChatColor.YELLOW + "/" + cmd + this.helpAdd);
		}
		if (Permission.hasPlayer(p, Perm.REMOVE)) {
			p.sendMessage(ChatColor.YELLOW + "/" + cmd + this.helpRemove);
		}
		if (Permission.hasPlayer(p, Perm.PLAYER_ADD)) {
			p.sendMessage(ChatColor.YELLOW + "/" + cmd + this.helpAddPlayer);
		}
		if (Permission.hasPlayer(p, Perm.PLAYER_REMOVE)) {
			p.sendMessage(ChatColor.YELLOW + "/" + cmd + this.helpRemovePlayer);
		}
		if (Permission.hasPlayer(p, Perm.SHOW_INFO)) {
			p.sendMessage(ChatColor.YELLOW + "/" + cmd + this.helpInfo);
		}
		if (Permission.hasPlayer(p, Perm.RELOAD)) {
			p.sendMessage(ChatColor.YELLOW + "/cpreload - Reloads the plugin");
		}
	}
}