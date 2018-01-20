package pl.amazingshit.cp.managers;

import java.io.File;

import pl.amazingshit.cp.cp;
import pl.amazingshit.cp.util.ConfigUtil;
import pl.amazingshit.cp.util.Operation;
/**
 * Manages configuration.
 */
public class ConfigManager {

	public static ConfigUtil config = new ConfigUtil(new File("plugins/ChestProtection", "config.yml"));

	/**
	 * Returns true if OPs are allowed to modify protected containers.
	 * 
	 * @return access
	 */
	public static Boolean opAccess() {
		config.load();
		return config.getBoolean("allow-ops", true);
	}

	/**
	 * Returns true if auto protection is enabled.
	 * 
	 * @return enabled
	 */
	public static Boolean autoProtection() {
		config.load();
		return config.getBoolean("auto-protection", true);
	}

	public static Boolean autoOPProtection() {
		config.load();
		return config.getBoolean("auto-protection-ops", true);
	}

	/**
	 * Do configuration exists? If no it will return false.
	 * 
	 * @return exists
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

	/**
	 * Creates default configuration.
	 * 
	 * @return operation - SUCCESS, ALREADY_EXISTS, FAIL
	 */
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
			config.setProperty("allow-ops", true);
			config.setProperty("auto-protection", true);
			config.setProperty("auto-protection-ops", true);
			config.save_();
			System.out.print(cp.prefix + "Done.");
			return Operation.SUCCESS;
		}
		catch (Exception ex) {
			return Operation.FAIL;
		}
	}
}