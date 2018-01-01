package pl.amazingshit.cp.util;

import java.io.File;

import pl.amazingshit.cp.ChestProtection;
/**
 * Shortcut for ChestProtection
 */
public class cp extends ChestProtection {
	// A little test here
	public static ConfigUtil cnfutil = new ConfigUtil(new File("plugins/ChestProtection", "config.yml"));
	/**
	 * Returns header
	 * @return
	 */
    public static String test() {
	    return cnfutil.getHeader();
    }
}
