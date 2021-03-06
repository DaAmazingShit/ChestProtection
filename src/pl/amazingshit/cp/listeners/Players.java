package pl.amazingshit.cp.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerListener;

import pl.amazingshit.cp.cp;
import pl.amazingshit.cp.managers.DatabaseManager;
import pl.amazingshit.cp.util.Permission;
import pl.amazingshit.cp.util.Permission.Perm;
/**
 * Player listener.
 */
public class Players extends PlayerListener {

	public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent e) {
		if (e.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		Block block = e.getClickedBlock();
		if (!(e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.FURNACE || e.getClickedBlock().getType() == Material.DISPENSER || e.getClickedBlock().getType() == Material.JUKEBOX)) {
			return;
		}
		if (DatabaseManager.isContainerProtected(block.getLocation())) {
			if ((Permission.hasPlayer(e.getPlayer(), Perm.ACCESS_OTHER))) {
				e.getPlayer().sendMessage(ChatColor.YELLOW + cp.lang.accessProtContAdmin);
				return;
			}
			if (DatabaseManager.doesPlayerOwnContainer(e.getPlayer(), block.getLocation())) {
				return;
			}
		    
			e.getPlayer().sendMessage(ChatColor.RED + cp.lang.noAccess);
			e.setCancelled(true);
		}
	}
}