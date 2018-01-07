package pl.amazingshit.cp.managers;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import pl.amazingshit.cp.util.ConfigUtil;
import pl.amazingshit.cp.util.Operation;
import pl.amazingshit.cp.util.cp;
/**
 * Manages database
 */
public class DatabaseManager {

	public static ConfigUtil config = new ConfigUtil(new File("plugins/ChestProtection", "database.yml"));

	/**
	 * Returns true if player owns container at an location.
	 * 
	 * @param p player
	 * @param loc location
	 * @return
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
	 * 
	 * @param p player
	 * @param loc location
	 * @return operation - NULL, ALREADY_EXISTS, SUCCESS, FAIL
	 */
	public static Operation addContainerToDB(Player p, Location loc) {
		try {
			if (p == null) {
				return Operation.NULL;
			}
			config.load();
			List<String> players = new LinkedList<String>();
			players = config.getStringList(toString(loc), players);
			if (players.contains(p.getName())) {
				return Operation.ALREADY_EXISTS;
			}
			players.add(p.getName());
			config.setProperty(toString(loc), players);
			config.save();
			return Operation.SUCCESS;
		}
		catch (Exception ex) {
			return Operation.FAIL;
		}
	}

	/**
	 * Returns player assigned to container.
	 * 
	 * @param loc location
	 * @return
	 */
	public static List<String> getPlayersOwning(Location loc) {
		config.load();
		List<String> ret = new LinkedList<String>();
		try {
			ret = config.getStringList(toString(loc), ret);
			if (ret.isEmpty()) {
				return null;
			}
			return ret;
		}
		catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Tries to add player to container.
	 * 
	 * @param p playername
	 * @param loc location
	 * @return operation - NOT_PROTECTED, NULL, ALREADY_EXISTS, SUCCESS, FAIL
	 */
	public static Operation addPlayerToContainer(String p, Location loc) {
		try {
			config.load();
			if (config.getProperty(toString(loc)) != null) {
				
				List<String> players = new LinkedList<String>();
				players = config.getStringList(toString(loc), players);
				if (players.isEmpty()) {
					removeContainerFromDB(loc);
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
	 * Simple method, tries to return block from location. If block doesn't exists it will return null.
	 * 
	 * @param loc location
	 * @return block or null
	 */
	public static Block getBlockFromLocation(Location loc) {
		try {
			config.load();
			
			World w = cp.instance.getServer().getWorld(loc.getWorld().getName());
			
			Block toReturn = w.getBlockAt(loc);
			
			return toReturn;
		}
		catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Tries to remove container from database.
	 * 
	 * @param loc location
	 * @return operation - NULL, NOT_PROTECTED, SUCCESS, FAIL
	 */
	public static Operation removeContainerFromDB(Location loc) {
		try {
			config.load();
			if (loc == null) {
				return Operation.NULL;
			}
			if (config.getProperty(toString(loc)) == null) {
				return Operation.NOT_PROTECTED;
			}
			config.removeProperty(toString(loc));
			config.save();
			return Operation.SUCCESS;
		}
		catch (Exception ex) {
			return Operation.FAIL;
		}
	}

	/**
	 * Tries to deny player from accessing the container.
	 * 
	 * @param p player
	 * @param loc location
	 * @return operation - NOT_PROTECTED, NOT_IN_LIST, SUCCESS, FAIL
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
	 * @return
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
	 * @return
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
	 * @return
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
