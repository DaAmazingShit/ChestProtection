package pl.amazingshit.cp;

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

public class Database {
	
	public static Configuration config = new Configuration(new File("plugins/ChestProtection", "database.yml"));
	// TODO check Javadocs grammar
	
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
	
	// TODO Separate Player from this method.
	/**
	 * Tries to add container with player to database.
	 * If operation wasn't successful or player already assigned to container it will return false.
	 * 
	 * @param p player
	 * @param loc location
	 * @return was operation successful
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
	 * Simple method, tries to return block from location. If operation wasn't successful it will return null.
	 * 
	 * @param loc location
	 * @return block
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
	 * @return was operation successful
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
	 * Tries to deny player from accessing the container. If operation was successful it will return true;
	 * 
	 * @param p player
	 * @param loc location
	 * @return was operation successful
	 */
	public static Boolean removePlayerFromContainer(Player p, Location loc) {
		try {
			config.load();
			List<String> players = new LinkedList<String>();
			players = config.getStringList(toString(loc), players);
			players.remove(p.getName());
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
	 * This method creates default database for saving protected blocks.
	 * 
	 */
    public static void createDatabase() {
    	config.load();
    	config.setHeader(
    			"# Info:",
    			"#     Author: DaAmazingShit",
    			"#     Contact:",
    			"#     - Github:  DaAmazingShit",
    			"#     - e-mail:  da.amazing.shit@interia.pl"
    			);
    	config.setProperty("version", ChestProtection.instance.getDescription().getVersion());
    	config.save();
    }
    
    /**
     * Do database exists? If no it will return false.
     * 
     * @return database exists
     */
    public static Boolean databaseExists() {
    	config.load();
    	if (config.getProperty("version") == null) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
	
    /**
     * Returns the string from Location in a format that configuration stores the location of protection.
     * 
     * @param loc location
     * @return configuration location format
     */
	public static String toString(Location loc) {
		String world = loc.getWorld().getName();
		String x = Integer.toString(loc.getBlockX());
		String y = Integer.toString(loc.getBlockY());
		String z = Integer.toString(loc.getBlockZ());
		return world+"x"+x+"y"+y+"z"+z;
	}
}
