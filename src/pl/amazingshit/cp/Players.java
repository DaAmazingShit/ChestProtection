package pl.amazingshit.cp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class Players extends PlayerListener {
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		Block block = e.getClickedBlock();
		if (!(e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.FURNACE || e.getClickedBlock().getType() == Material.DISPENSER || e.getClickedBlock().getType() == Material.JUKEBOX)) {
			return;
		}
		if (Database.isContainerProtected(block.getLocation())) {
		    if (Database.doesPlayerOwnContainer(e.getPlayer(), block.getLocation())) {
			    return;
		    }
		    
			e.getPlayer().sendMessage(ChatColor.RED + ChestProtection.lang.noAccess);
			e.setCancelled(true);
		}
	}
}
