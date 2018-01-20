package pl.amazingshit.cp.listeners;

import org.bukkit.event.server.PluginEvent;
import org.bukkit.event.server.ServerListener;

import pl.amazingshit.cp.cp;

public class Plugins extends ServerListener {

	@Override
	public void onPluginEnabled(PluginEvent e) {
		if (e.getPlugin().getDescription().getName().equals("Permissions")) {
			 cp.pe = true;
			 cp.instance.getServer().getLogger().info(cp.prefix + "Linked with Permissions.");
		}
	}
}