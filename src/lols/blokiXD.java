package lols;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockChangeEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class blokiXD extends BlockListener {
	//                    gracz   skrzynka
	public static HashMap<String, Location> ostatnioklik = new HashMap<String, Location>();
		@Override
		public void onBlockPlace(BlockPlaceEvent e) {
			if (!(e.getBlockPlaced().getType() == Material.CHEST || e.getBlockPlaced().getType() == Material.FURNACE || e.getBlockPlaced().getType() == Material.DISPENSER || e.getBlockPlaced().getType() == Material.JUKEBOX || e.getBlockPlaced().getType() == Material.BURNING_FURNACE)) {
				return;
			}
			World world = Bukkit.getServer().getWorld(e.getBlockPlaced().getLocation().getWorld().getName());
			if (e.getBlockPlaced().getType() == Material.CHEST) {
				
				int x      = e.getBlockPlaced().getLocation().getBlockX();
				int xminus = e.getBlockPlaced().getLocation().getBlockX()-1;
				int x1     = e.getBlockPlaced().getLocation().getBlockX()+1;
				
				int y      = e.getBlockPlaced().getLocation().getBlockY();
				
				int z      = e.getBlockPlaced().getLocation().getBlockZ();
				int zminus = e.getBlockPlaced().getLocation().getBlockZ()-1;
				int z1     = e.getBlockPlaced().getLocation().getBlockZ()+1;
				
				if (world.getBlockAt(xminus, y, z).getType() == Material.CHEST && Database.czyPojemnikJestZabezpieczony(world.getBlockAt(xminus, y, z).getLocation())) {
					if (!Database.czyPojemnikJestGracza(e.getPlayer(), world.getBlockAt(xminus, y, z).getLocation())) {
						e.getPlayer().sendMessage(ChatColor.RED + "Brak dostepu.");
						e.setCancelled(true);
						return;
					}
				}
				if (world.getBlockAt(x, y, zminus).getType() == Material.CHEST && Database.czyPojemnikJestZabezpieczony(world.getBlockAt(x, y, zminus).getLocation())) {
					if (!Database.czyPojemnikJestGracza(e.getPlayer(), world.getBlockAt(x, y, zminus).getLocation())) {
						e.getPlayer().sendMessage(ChatColor.RED + "Brak dostepu.");
						e.setCancelled(true);
						return;
					}
				}
				if (world.getBlockAt(x1, y, z).getType() == Material.CHEST && Database.czyPojemnikJestZabezpieczony(world.getBlockAt(x1, y, z).getLocation())) {
					if (!Database.czyPojemnikJestGracza(e.getPlayer(), world.getBlockAt(x1, y, z).getLocation())) {
						e.getPlayer().sendMessage(ChatColor.RED + "Brak dostepu.");
						e.setCancelled(true);
						return;
					}
				}
				if (world.getBlockAt(x, y, z1).getType() == Material.CHEST && Database.czyPojemnikJestZabezpieczony(world.getBlockAt(x, y, z1).getLocation())) {
					if (!Database.czyPojemnikJestGracza(e.getPlayer(), world.getBlockAt(x, y, z1).getLocation())) {
						e.getPlayer().sendMessage(ChatColor.RED + "Brak dostepu.");
						e.setCancelled(true);
						return;
					}
				}
				Database.dodajPojemnikDoDatabase(e.getPlayer(), e.getBlockPlaced().getLocation());
				e.getPlayer().sendMessage(ChatColor.YELLOW + "Pojemnik automatycznie zabezpieczony.");
				return;
			}
			Database.dodajPojemnikDoDatabase(e.getPlayer(), e.getBlockPlaced().getLocation());
			e.getPlayer().sendMessage(ChatColor.YELLOW + "Pojemnik automatycznie zabezpieczony.");
		}
		
		@Override
		public void onBlockBreak(BlockBreakEvent e) {
			if (!(e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.FURNACE || e.getBlock().getType() == Material.DISPENSER || e.getBlock().getType() == Material.JUKEBOX || e.getBlock().getType() == Material.BURNING_FURNACE)) {
				return;
			}
			if (Database.czyPojemnikJestZabezpieczony(e.getBlock().getLocation()) ) {
				if (Database.czyPojemnikJestGracza(e.getPlayer(), e.getBlock().getLocation()) == false) {
					e.getPlayer().sendMessage(ChatColor.RED + "Brak dostepu.");
					e.setCancelled(true);
					return;
				}
				Database.usunPojemnikZDatabase(e.getBlock().getLocation());
				e.getPlayer().sendMessage(ChatColor.YELLOW + "Ochrona usunieta.");
			}
		}
		
		@Override
		public void onBlockIgnite(BlockIgniteEvent e) {
			if (!(e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.FURNACE || e.getBlock().getType() == Material.DISPENSER || e.getBlock().getType() == Material.JUKEBOX || e.getBlock().getType() == Material.BURNING_FURNACE)) {
				return;
			}
			if (Database.czyPojemnikJestZabezpieczony(e.getBlock().getLocation())) {
				e.setCancelled(true);
			}
		}
		
		@Override
		public void onBlockChange(BlockChangeEvent e) {
			if (!(e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.FURNACE || e.getBlock().getType() == Material.DISPENSER || e.getBlock().getType() == Material.JUKEBOX || e.getBlock().getType() == Material.BURNING_FURNACE)) {
				return;
			}
			if (Database.czyPojemnikJestZabezpieczony(e.getBlock().getLocation())) {
				e.setCancelled(true);
			}
		}
		
		@Override
		public void onBlockDamage(BlockDamageEvent e) {
			if (e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.FURNACE || e.getBlock().getType() == Material.DISPENSER || e.getBlock().getType() == Material.JUKEBOX || e.getBlock().getType() == Material.BURNING_FURNACE) {
				if (Database.czyPojemnikJestZabezpieczony(e.getBlock().getLocation())) {
					if (Database.czyPojemnikJestGracza(e.getPlayer(), e.getBlock().getLocation())) {
						if (ostatnioklik.get(e.getPlayer().getName()) != null) {
						    ostatnioklik.remove(e.getPlayer().getName());
					    }
						ostatnioklik.put(e.getPlayer().getName(), e.getBlock().getLocation());
					}
					else {
						e.getPlayer().sendMessage(ChatColor.RED + "Nie twój pojemnik.");
						e.setCancelled(true);
					}
				}
				else {
					if (ostatnioklik.get(e.getPlayer().getName()) != null) {
					    ostatnioklik.remove(e.getPlayer().getName());
				    }
					ostatnioklik.put(e.getPlayer().getName(), e.getBlock().getLocation());
				}
			}
			else {
				return;
			}
		}
	}