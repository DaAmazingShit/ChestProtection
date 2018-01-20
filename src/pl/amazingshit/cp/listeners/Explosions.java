package pl.amazingshit.cp.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import pl.amazingshit.cp.managers.DatabaseManager;
/**
 * Entity listener
 */
public class Explosions extends EntityListener {

	@Override
	public void onEntityExplode(EntityExplodeEvent e) {
		if (e.isCancelled()) {
			return;
		}
		int count = e.blockList().size();
		for (int x = 0; x < count; x++) {
			Block block = (Block)e.blockList().get(x);
			if (DatabaseManager.isContainerProtected(block.getLocation())) {
				e.setCancelled(true);
				return;
			}
		}
	}
}