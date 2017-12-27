package lols;

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
	
	public static Boolean czyPojemnikJestGracza(Player p, Location loc) {
		config.load();
		List<String> gracze = new LinkedList<String>();
		gracze = config.getStringList(toString(loc), gracze);
		if (gracze.contains(p.getName())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Boolean dodajPojemnikDoDatabase(Player p, Location loc) {
		try {
			config.load();
			List<String> gracze = new LinkedList<String>();
			gracze = config.getStringList(toString(loc), gracze);
			if (gracze.contains(p.getName())) {
				return false;
			}
			gracze.add(p.getName());
			config.setProperty(toString(loc), gracze);
			config.save();
			config.load();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	public static Block dawejTypPojemnikaZLokacji(Location loc) {
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
	
	public static Boolean usunPojemnikZDatabase(Location loc) {
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
	
	public static Boolean usunGraczaZPojemnika(Player p, Location loc) {
		try {
			config.load();
			List<String> gracze = new LinkedList<String>();
			gracze = config.getStringList(toString(loc), gracze);
			gracze.remove(p.getName());
			config.setProperty(toString(loc), gracze);
			config.save();
			config.load();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	public static Boolean czyPojemnikJestZabezpieczony(Location loc) {
		config.load();
		if (config.getProperty(toString(loc)) == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static Boolean czyBlokJestZabezpieczalny(Block b) {
		if (b.getType() == Material.CHEST || b.getType() == Material.JUKEBOX || b.getType() == Material.BURNING_FURNACE || 
				b.getType() == Material.FURNACE || b.getType() == Material.DISPENSER) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Boolean czyMaterialJestZabezpieczalny(Material b) {
		if (b == Material.CHEST || b == Material.JUKEBOX || b == Material.BURNING_FURNACE || 
				b == Material.FURNACE || b == Material.DISPENSER) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * 
	 * This method creates default database for saving protected blocks.
	 * 
	 * @category Database creation
	 */
    public static void stworzDatabase() {
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
    
    public static Boolean databaseIstnieje() {
    	config.load();
    	if (config.getProperty("version") == null) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
	
	public static String toString(Location loc) {
		String world = loc.getWorld().getName();
		String x = Integer.toString(loc.getBlockX());
		String y = Integer.toString(loc.getBlockY());
		String z = Integer.toString(loc.getBlockZ());
		return world+"x"+x+"y"+y+"z"+z;
	}
}
