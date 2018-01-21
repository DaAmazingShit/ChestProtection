package pl.amazingshit.cp.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.amazingshit.cp.cp;
import pl.amazingshit.cp.listeners.Blocks;
import pl.amazingshit.cp.managers.DatabaseManager;
import pl.amazingshit.cp.util.CPCommand;
import pl.amazingshit.cp.util.Operation;
import pl.amazingshit.cp.util.Permission;
import pl.amazingshit.cp.util.Permission.Perm;

public class CP extends CPCommand {

	public CP(CommandSender sender, Command cmd, String cmdalias, String[] args) {
		super(sender, cmd, cmdalias, args);
	}

	public void handle() {
		if (!cmd.getName().equalsIgnoreCase("cp")) {
			return;
		}
		if (!(sender instanceof Player)) {
			if (cmd.getName().equalsIgnoreCase("cp")) {
				if (args.length == 0) {
					cp.lang.displayHelp(alias, sender);
					return;
				}
			}
			return;
		}
		Player p = (Player)sender;
		if (cmd.getName().equalsIgnoreCase("cp")) {
			if (args.length == 0) {
				cp.lang.displayHelp(alias, p);
				return;
			}
			if (args[0].equalsIgnoreCase(cp.lang.protectionArgInfo)) {
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location infoo = Blocks.lastClicked.get(p.getName());
					if (!DatabaseManager.doesPlayerOwnContainer(p, infoo) && !Permission.hasPlayer(p, Perm.SHOW_INFO_OTHER)) {
						p.sendMessage(ChatColor.RED + cp.lang.noPerm);
						return;
					}
					if (DatabaseManager.doesPlayerOwnContainer(p, infoo) && !Permission.hasPlayer(p, Perm.SHOW_INFO)) {
						p.sendMessage(ChatColor.RED + cp.lang.noPerm);
						return;
					}
					
					List<String> players = DatabaseManager.getPlayersOwning(infoo);
					if (players.isEmpty()) {
						p.sendMessage(ChatColor.YELLOW + cp.lang.notProtected);
						return;
					}
					p.sendMessage(cp.lang.playersOwningThis);
					for (String pl : players) {
						p.sendMessage(" - " + ChatColor.BLUE + pl);
					}
				}
				else {
					p.sendMessage(ChatColor.RED + cp.lang.containerNotSelected);
				}
				return;
			}
			if (args[0].equalsIgnoreCase(cp.lang.protectionArgRemove)) {
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location toRemove = Blocks.lastClicked.get(p.getName());
					World world = toRemove.getWorld();
					if (DatabaseManager.doesPlayerOwnContainer(p, toRemove)) {
						if (!Permission.hasPlayer(p, Perm.REMOVE) && cp.pe) {
							p.sendMessage(ChatColor.RED + cp.lang.noPerm);
							return;
						}
						
						if (world.getBlockAt(toRemove.getBlockX()+1, toRemove.getBlockY(), toRemove.getBlockZ()).getType() == 
								Material.CHEST) {
							DatabaseManager.removeContainerFromDB(world.getBlockAt(toRemove.getBlockX()+1, toRemove.getBlockY(), toRemove.getBlockZ()).getLocation());
						}
						if (world.getBlockAt(toRemove.getBlockX()-1, toRemove.getBlockY(), toRemove.getBlockZ()).getType() == 
								Material.CHEST) {
							DatabaseManager.removeContainerFromDB(world.getBlockAt(toRemove.getBlockX()-1, toRemove.getBlockY(), toRemove.getBlockZ()).getLocation());
						}
						if (world.getBlockAt(toRemove.getBlockX(), toRemove.getBlockY(), toRemove.getBlockZ()+1).getType() == 
								Material.CHEST) {
							DatabaseManager.removeContainerFromDB(world.getBlockAt(toRemove.getBlockX(), toRemove.getBlockY(), toRemove.getBlockZ()+1).getLocation());
						}
						if (world.getBlockAt(toRemove.getBlockX(), toRemove.getBlockY(), toRemove.getBlockZ()-1).getType() == 
								Material.CHEST) {
							DatabaseManager.removeContainerFromDB(world.getBlockAt(toRemove.getBlockX(), toRemove.getBlockY(), toRemove.getBlockZ()-1).getLocation());
						}
						DatabaseManager.removeContainerFromDB(toRemove);
						
						p.sendMessage(ChatColor.YELLOW + cp.lang.protectionRemoved);
						return;
					}
					if (!Permission.hasPlayer(p, Perm.REMOVE_OTHER) && cp.pe) {
						p.sendMessage(ChatColor.RED + cp.lang.noPerm);
						return;
					}
					if (world.getBlockAt(toRemove.getBlockX()+1, toRemove.getBlockY(), toRemove.getBlockZ()).getType() == 
							Material.CHEST) {
						DatabaseManager.removeContainerFromDB(world.getBlockAt(toRemove.getBlockX()+1, toRemove.getBlockY(), toRemove.getBlockZ()).getLocation());
					}
					if (world.getBlockAt(toRemove.getBlockX()-1, toRemove.getBlockY(), toRemove.getBlockZ()).getType() == 
							Material.CHEST) {
						DatabaseManager.removeContainerFromDB(world.getBlockAt(toRemove.getBlockX()-1, toRemove.getBlockY(), toRemove.getBlockZ()).getLocation());
					}
					if (world.getBlockAt(toRemove.getBlockX(), toRemove.getBlockY(), toRemove.getBlockZ()+1).getType() == 
							Material.CHEST) {
						DatabaseManager.removeContainerFromDB(world.getBlockAt(toRemove.getBlockX(), toRemove.getBlockY(), toRemove.getBlockZ()+1).getLocation());
					}
					if (world.getBlockAt(toRemove.getBlockX(), toRemove.getBlockY(), toRemove.getBlockZ()-1).getType() == 
							Material.CHEST) {
						DatabaseManager.removeContainerFromDB(world.getBlockAt(toRemove.getBlockX(), toRemove.getBlockY(), toRemove.getBlockZ()-1).getLocation());
					}
					DatabaseManager.removeContainerFromDB(toRemove);
					
					p.sendMessage(ChatColor.YELLOW + cp.lang.protectionRemoved);
					return;
				} else {
					p.sendMessage(ChatColor.RED + cp.lang.containerNotSelected);
					return;
				}
			}
			
			if (args[0].equalsIgnoreCase(cp.lang.protectionArgAdd)) {
				if (!Permission.hasPlayer(p, Perm.CREATE) && cp.pe) {
					p.sendMessage(ChatColor.RED + cp.lang.noPerm);
					return;
				}
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location toAdd = Blocks.lastClicked.get(p.getName());
					World world = toAdd.getWorld();
					if (world.getBlockAt(toAdd.getBlockX()+1, toAdd.getBlockY(), toAdd.getBlockZ()).getType() == 
							Material.CHEST) {
						DatabaseManager.addContainerToDB(p, world.getBlockAt(toAdd.getBlockX()+1, toAdd.getBlockY(), toAdd.getBlockZ()).getLocation());
					}
					if (world.getBlockAt(toAdd.getBlockX()-1, toAdd.getBlockY(), toAdd.getBlockZ()).getType() == 
							Material.CHEST) {
						DatabaseManager.addContainerToDB(p, world.getBlockAt(toAdd.getBlockX()-1, toAdd.getBlockY(), toAdd.getBlockZ()).getLocation());
					}
					if (world.getBlockAt(toAdd.getBlockX(), toAdd.getBlockY(), toAdd.getBlockZ()+1).getType() == 
							Material.CHEST) {
						DatabaseManager.addContainerToDB(p, world.getBlockAt(toAdd.getBlockX(), toAdd.getBlockY(), toAdd.getBlockZ()+1).getLocation());
					}
					if (world.getBlockAt(toAdd.getBlockX(), toAdd.getBlockY(), toAdd.getBlockZ()-1).getType() == 
							Material.CHEST) {
						DatabaseManager.addContainerToDB(p, world.getBlockAt(toAdd.getBlockX(), toAdd.getBlockY(), toAdd.getBlockZ()-1).getLocation());
					}
					
					Operation op = DatabaseManager.addContainerToDB(p, toAdd);
					if (op == Operation.SUCCESS) {
						p.sendMessage(ChatColor.GREEN + cp.lang.protectionAdded);
					}
					if (op == Operation.ALREADY_EXISTS) {
						p.sendMessage(ChatColor.RED + cp.lang.alreadyProtected);
					}
					if (op == Operation.FAIL) {
						p.sendMessage(ChatColor.RED + cp.lang.wrong);
					}
					if (op == Operation.NULL) {
						p.sendMessage(ChatColor.RED + cp.lang.noPlayer);
					}
					return;
				} else {
					p.sendMessage(ChatColor.RED + cp.lang.containerNotSelected);
					return;
				}
			}
			
			if (args[0].equalsIgnoreCase(cp.lang.assignToContainerArg)) {
				if (!Permission.hasPlayer(p, Perm.PLAYER_ADD) && cp.pe) {
					p.sendMessage(ChatColor.RED + cp.lang.noPerm);
					return;
				}
				if (args.length == 1) {
					cp.lang.displayHelp(alias, p);
					return;
				}
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location lc = Blocks.lastClicked.get(p.getName());
					String player = args[1];
					World world = lc.getWorld();
					
					if (world.getBlockAt(lc.getBlockX()+1, lc.getBlockY(), lc.getBlockZ()).getType() == 
							Material.CHEST) {
						DatabaseManager.addPlayerToContainer(player, world.getBlockAt(lc.getBlockX()+1, lc.getBlockY(), lc.getBlockZ()).getLocation());
					}
					if (world.getBlockAt(lc.getBlockX()-1, lc.getBlockY(), lc.getBlockZ()).getType() == 
							Material.CHEST) {
						DatabaseManager.addPlayerToContainer(player, world.getBlockAt(lc.getBlockX()-1, lc.getBlockY(), lc.getBlockZ()).getLocation());
					}
					if (world.getBlockAt(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()+1).getType() == 
							Material.CHEST) {
						DatabaseManager.addPlayerToContainer(player, world.getBlockAt(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()+1).getLocation());
					}
					if (world.getBlockAt(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()-1).getType() == 
							Material.CHEST) {
						DatabaseManager.addPlayerToContainer(player, world.getBlockAt(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()-1).getLocation());
					}
					Operation op = DatabaseManager.addPlayerToContainer(player, lc);
					
					if (op == Operation.SUCCESS) {
						p.sendMessage(ChatColor.GREEN + cp.lang.addedToSuccess);
					}
					if (op == Operation.ALREADY_EXISTS) {
						p.sendMessage(ChatColor.RED + cp.lang.alreadyAssigned);
					}
					if (op == Operation.NOT_PROTECTED) {
						p.sendMessage(ChatColor.RED + cp.lang.notProtected);
					}
					if (op == Operation.FAIL) {
						p.sendMessage(ChatColor.RED + cp.lang.wrong);
					}
					return;
				}
				else {
					p.sendMessage(ChatColor.RED + cp.lang.containerNotSelected);
					return;
				}
			}
			
			if (args[0].equalsIgnoreCase(cp.lang.removeFromContainerArg)) {
				if (!Permission.hasPlayer(p, Perm.PLAYER_REMOVE) && cp.pe) {
					p.sendMessage(ChatColor.RED + cp.lang.noPerm);
					return;
				}
				if (args.length == 1) {
					cp.lang.displayHelp(alias, p);
					return;
				}
				if (Blocks.lastClicked.get(p.getName()) != null) {
					Location lc = Blocks.lastClicked.get(p.getName());
					String player = args[1];
					World world = lc.getWorld();
					
					if (world.getBlockAt(lc.getBlockX()+1, lc.getBlockY(), lc.getBlockZ()).getType() == 
							Material.CHEST) {
						DatabaseManager.removePlayerFromContainer(player, world.getBlockAt(lc.getBlockX()+1, lc.getBlockY(), lc.getBlockZ()).getLocation());
					}
					if (world.getBlockAt(lc.getBlockX()-1, lc.getBlockY(), lc.getBlockZ()).getType() == 
							Material.CHEST) {
						DatabaseManager.removePlayerFromContainer(player, world.getBlockAt(lc.getBlockX()-1, lc.getBlockY(), lc.getBlockZ()).getLocation());
					}
					if (world.getBlockAt(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()+1).getType() == 
							Material.CHEST) {
						DatabaseManager.removePlayerFromContainer(player, world.getBlockAt(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()+1).getLocation());
					}
					if (world.getBlockAt(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()-1).getType() == 
							Material.CHEST) {
						DatabaseManager.removePlayerFromContainer(player, world.getBlockAt(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()-1).getLocation());
					}
					
					Operation op = DatabaseManager.removePlayerFromContainer(player, lc);
					if (op == Operation.SUCCESS) {
						p.sendMessage(ChatColor.GREEN + cp.lang.removedFromSuccess);
					}
					if (op == Operation.NOT_IN_LIST) {
						p.sendMessage(ChatColor.RED + cp.lang.notAssigned);
					}
					if (op == Operation.NOT_PROTECTED) {
						p.sendMessage(ChatColor.RED + cp.lang.notProtected);
					}
					if (op == Operation.FAIL) {
						p.sendMessage(ChatColor.RED + cp.lang.wrong);
					}
					return;
				}
				else {
					p.sendMessage(ChatColor.RED + cp.lang.containerNotSelected);
					return;
				}
			}
			else {
				cp.lang.displayHelp(alias, p);
			}
		}
	}
}