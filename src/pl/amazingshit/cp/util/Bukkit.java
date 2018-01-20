package pl.amazingshit.cp.util;

import org.bukkit.Server;
/**
 * Shortcut for Server.class
 */
public class Bukkit {

	private static Server server;

	public static void set(Server serv) {
		server = serv;
	}

	public static Server get() {
		return server;
	}
}