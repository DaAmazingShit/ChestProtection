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
		if (permission.isDefault()) {
			return true;
		}
		return Permissions.Security.has(p, permission.getName());
	}

	public enum Perm {

		// Admin
		SHOW_INFO_OTHER("cp.admin.info", false),

		ACCESS_OTHER("cp.admin.access", false),

		RELOAD("cp.admin.reload", false),

		REMOVE_OTHER("cp.admin.remove.others", false),

		// Default
		SHOW_INFO("cp.use.info", true),

		PLAYER_REMOVE("cp.use.player.remove", true),

		PLAYER_ADD("cp.use.player.add", true),

		REMOVE("cp.use.remove.self", true),

		CREATE("cp.use.create", true);

		private String perm;
		private static HashMap<String, Perm> schowek = new HashMap<String, Perm>();
		private final boolean defaultAccess;

		private Perm(String perm, boolean da) {
			this.perm = perm;
			this.defaultAccess = da;
		}

		/**
		 * Returns the permission node
		 * @return node
		 */
		public String getName() {
			return perm;
		}

		/**
		 * Returns true if permission is accessible for everyone.
		 */
		public boolean isDefault() {
			return this.defaultAccess;
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