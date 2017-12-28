package pl.amazingshit.cp.managers;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

import pl.amazingshit.cp.ChestProtection;
import pl.amazingshit.cp.util.Operation;
import pl.amazingshit.cp.util.cp;
/**
 * Represents a database
 * @author DaAmazingShit
 */
public class DatabaseManager {
	
	// TODO Replace all Boolean with Operation
	public static Configuration config = new Configuration(new File("plugins/ChestProtection", "database.yml"));
	
	/**
	 * Returns true if player owns container at an location.
	 * 
	 * @param p player
	 * @param loc location
	 * @return player own container
	 */
	public static Boolean doesPlayerOwnContainer(Player p, Location loc) {
		config.load();
		List<String> players = new LinkedList<String>();
		players = config.getStringList(toString(loc), players);
		if (players.contains(p.getName())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Tries to add container with player to database.
	 * If operation wasn't successful or player already assigned to container it will return false.
	 * 
	 * @param p player
	 * @param loc location
	 * @return operation successful
	 */
	public static Boolean addContainerToDB(Player p, Location loc) {
		try {
			config.load();
			List<String> players = new LinkedList<String>();
			players = config.getStringList(toString(loc), players);
			if (players.contains(p.getName())) {
				return false;
			}
			players.add(p.getName());
			config.setProperty(toString(loc), players);
			config.save();
			config.load();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * Tries to add player to container.
	 * 
	 * @param p playername
	 * @param loc location
	 * @return operation
	 */
	public static Operation addPlayerToContainer(String p, Location loc) {
		try {
			config.load();
			if (config.getProperty(toString(loc)) != null) {
				
				List<String> players = new LinkedList<String>();
				players = config.getStringList(toString(loc), players);
				if (players.isEmpty()) {
					return Operation.NOT_PROTECTED;
				}
				if (players.contains(p)) {
					return Operation.ALREADY_EXISTS;
				}
				players.add(p);
				config.setProperty(toString(loc), players);
				config.save();
				return Operation.SUCCESS;
			}
			else {
				return Operation.NOT_PROTECTED;
			}
		}
		catch (Exception ex) {
			return Operation.FAIL;
		}
	}
	
	/**
	 * Simple method, tries to return block from location. If operation wasn't successful it will return null.
	 * 
	 * @param loc location
	 * @return block or null
	 */
	public static Block getBlockFromLocation(Location loc) {
		try {
			config.load();
			
			World w = Bukkit.getServer().getWorld(loc.getWorld().getName());
			
			Block toReturn = w.getBlockAt(loc);
			
			return toReturn;
		}
		catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Tries to remove container from database. If operation was successful it will return true.
	 * 
	 * @param loc location
	 * @return operation successful
	 */
	public static Boolean removeContainerFromDB(Location loc) {
		try {
			config.load();
			config.removeProperty(toString(loc));
			config.save();
			config.load();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * Tries to deny player from accessing the container.
	 * 
	 * @param p player
	 * @param loc location
	 * @return operation
	 */
	public static Operation removePlayerFromContainer(String p, Location loc) {
		try {
			config.load();
			List<String> players = new LinkedList<String>();
			players = config.getStringList(toString(loc), players);
			if (players.isEmpty()) {
				return Operation.NOT_PROTECTED;
			}
			if (!players.contains(p)) {
				return Operation.NOT_IN_LIST;
			}
			players.remove(p);
			config.setProperty(toString(loc), players);
			config.save();
			return Operation.SUCCESS;
		}
		catch (Exception ex) {
			return Operation.FAIL;
		}
	}
	
	/**
	 * Is the container protected? If no it will return false.
	 * 
	 * @param loc location
	 * @return container protected
	 */
	public static Boolean isContainerProtected(Location loc) {
		config.load();
		if (config.getProperty(toString(loc)) == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Is block protectable? If the block is not protectable it will return false.
	 * 
	 * @param b block
	 * @return block protectable
	 */
	public static Boolean isBlockProtectable(Block b) {
		if (b.getType() == Material.CHEST || b.getType() == Material.JUKEBOX || b.getType() == Material.BURNING_FURNACE || 
				b.getType() == Material.FURNACE || b.getType() == Material.DISPENSER) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Is material protectable? If the material is not protectable it will return false.
	 * 
	 * @param mat material
	 * @return material protectable
	 */
	public static Boolean isMaterialProtectable(Material mat) {
		if (mat == Material.CHEST || mat == Material.JUKEBOX || mat == Material.BURNING_FURNACE || 
				mat == Material.FURNACE || mat == Material.DISPENSER) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * This method tries to create default configuration.
	 * If operation was successful it will return true.
	 * 
	 * @return operation successful
	 */
    public static Boolean createConfig() {
    	try {
    		System.out.print(cp.prefix + "Creating default configuration...");
        	ChestProtection.config.load();
        	ChestProtection.config.setHeader(
        			"# Info:",
        			"#     Author: DaAmazingShit",
        			"#     Contact:",
        			"#     - Github:  DaAmazingShit",
        			"#     - e-mail:  da.amazing.shit@interia.pl",
        			"# Warning! ChestProtection becomes weird while using it in spawn-protection region"
        			);
        	ChestProtection.config.setProperty("version", ChestProtection.instance.getDescription().getVersion());
        	ChestProtection.config.save();
        	System.out.print(cp.prefix + "Done.");
        	return true;
    	}
    	catch (Exception ex) {
    		return false;
    	}
    }
    
    /**
     * Do database exists? If no it will return false.
     * 
     * @return database exists
     */
    public static Boolean databaseExists() {
    	config.load();
    	if (config.getKeys().isEmpty()) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    /**
     * Do configuration exists? If no it will return false.
     * 
     * @return database exists
     */
    public static Boolean configExists() {
    	ChestProtection.config.load();
    	if (ChestProtection.config.getKeys().isEmpty()) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
	
    /**
     * Returns database location format.
     * 
     * @param loc location
     * @return database location format
     */
	public static String toString(Location loc) {
		String world = loc.getWorld().getName();
		String x = Integer.toString(loc.getBlockX());
		String y = Integer.toString(loc.getBlockY());
		String z = Integer.toString(loc.getBlockZ());
		return world+"x"+x+"y"+y+"z"+z;
	}
}
