package pl.amazingshit.cp.managers;

import java.io.File;

import pl.amazingshit.cp.util.ConfigUtil;
import pl.amazingshit.cp.util.Operation;
import pl.amazingshit.cp.util.cp;

public class ConfigManager {

	public static ConfigUtil config = new ConfigUtil(new File("plugins/ChestProtection", "config.yml"));

	/**
	 * Returns true if ops are allowed to modify protected containers.
	 * 
	 * @return access
	 */
	public static Boolean opAccess() {
		return config.getBoolean("allow-ops", false);
	}

	/**
	 * Do configuration exists? If no it will return false.
     * 
     * @return database exists
	 */
	public static Boolean configExists() {
		config.load();
		if (config.getKeys().isEmpty()) {
			return false;
		}
		else {
			return true;
		}
	}

	public static Operation createConfig() {
		try {
			System.out.print(cp.prefix + "Creating default configuration...");
			config.load();
			if (config.getProperty("version") != null || config.getHeader() != null || config.getProperty("allow-ops") != null) {
				return Operation.ALREADY_EXISTS;
			}
			config.setHeader(
					"# Info:",
					"#     Author: DaAmazingShit",
					"#     Contact:",
					"#     - Github:  DaAmazingShit",
					"#     - e-mail:  da.amazing.shit@interia.pl",
					"# Warning! ChestProtection becomes weird while using it in spawn-protection region"
			);
			config.setProperty("version", cp.instance.getDescription().getVersion());
			config.setProperty("allow-ops", false);
			config.save();
			System.out.print(cp.prefix + "Done.");
			return Operation.SUCCESS;
		}
		catch (Exception ex) {
			return Operation.FAIL;
		}
	}
}
