package lols;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class lystener extends PlayerListener {
	
	@Override public void onPlayerInteract(PlayerInteractEvent e) {
		// to jes kiedy gracz zaznacza
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		Block block = e.getClickedBlock();
		if (!(e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.FURNACE || e.getClickedBlock().getType() == Material.DISPENSER || e.getClickedBlock().getType() == Material.JUKEBOX)) {
			return;
		}
		// jezeli pojemnik jest zabezpieczony
		if (Database.czyPojemnikJestZabezpieczony(block.getLocation())) {
			// jezeli pojemnik nalezy do gracza
		    if (Database.czyPojemnikJestGracza(e.getPlayer(), block.getLocation())) {
			    return;
		    }
		    
			e.getPlayer().sendMessage(ChatColor.RED + "Nie twój pojemnik.");
			e.setCancelled(true);
		}
	}
}
