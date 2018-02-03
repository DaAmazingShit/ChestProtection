package pl.amazingshit.cp.util;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.nijikokun.bukkit.Permissions.Permissions;

import pl.amazingshit.cp.cp;

public class Permission {

	public static Boolean hasPlayer(Player p, Perm permission) {
		if (!cp.pe) {
			return p.isOp();
		}
		return Permissions.Security.has(p, permission.getName());
	}

	public enum Perm {

		SHOW_INFO_OTHER("cp.admin.info"),

		ACCESS_OTHER("cp.admin.access"),

		RELOAD("cp.admin.reload"),

		SHOW_INFO("cp.use.info"),

		PLAYER_REMOVE("cp.use.player.remove"),

		PLAYER_ADD("cp.use.player.add"),

		REMOVE_OTHER("cp.use.remove.others"),

		REMOVE("cp.use.remove.self"),

		CREATE("cp.use.create");

		private String perm;
		private static HashMap<String, Perm> schowek = new HashMap<String, Perm>();

		private Perm(String perm) {
			this.perm = perm;
		}

		/**
		 * Returns the permission node
		 * @return node
		 */
		public String getName() {
			return perm;
		}

		/**
		 * Returns Perm.class
		 * 
		 * @param node
		 * @return Perm.class
		 */
		public static Perm getPerm(String perm) {
			return schowek.get(perm);
		}

		static {
			for (Perm everyperm: Perm.values()) {
				schowek.put(everyperm.name(), everyperm);
			}
		}
	}
}