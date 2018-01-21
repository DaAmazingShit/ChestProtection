package pl.amazingshit.cp.util;

import org.bukkit.entity.Player;

import com.nijikokun.bukkit.Permissions.Permissions;

public class Permission {

	public static Boolean hasPlayer(Player p, Perm permission) {
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
			if (perm.equalsIgnoreCase("ACCESS_OTHER")) {
				return Perm.ACCESS_OTHER;
			}
			if (perm.equalsIgnoreCase("CREATE")) {
				return Perm.CREATE;
			}
			if (perm.equalsIgnoreCase("REMOVE")) {
				return Perm.REMOVE;
			}
			if (perm.equalsIgnoreCase("RELOAD")) {
				return Perm.RELOAD;
			}
			if (perm.equalsIgnoreCase("PLAYER_REMOVE")) {
				return Perm.PLAYER_REMOVE;
			}
			if (perm.equalsIgnoreCase("REMOVE_OTHER")) {
				return Perm.REMOVE_OTHER;
			}
			if (perm.equalsIgnoreCase("PLAYER_ADD")) {
				return Perm.PLAYER_ADD;
			}
			if (perm.equalsIgnoreCase("SHOW_INFO_OTHER")) {
				return Perm.SHOW_INFO_OTHER;
			}
			if (perm.equalsIgnoreCase("SHOW_INFO")) {
				return Perm.SHOW_INFO;
			}
			return null;
		}
	}
}