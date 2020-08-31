package me.opd02.sandframe;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class main extends JavaPlugin implements Listener {
	//When player leaves, remove enchanting tables and frames
	public HashMap<Block, UUID> frames = new HashMap<Block, UUID>();
	public HashMap<Block, UUID> rframes = new HashMap<Block, UUID>();
	public void onEnable(){
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		//getConfig().options().copyDefaults();
	//	saveConfig();
		runnablerunner();
	}
	public void runnablerunner() {
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for(Block b : frames.keySet()){
					if(!(b.getType()==Material.ENDER_PORTAL_FRAME)){
						UUID uuid = frames.get(b);
						frames.remove(b, uuid);
						return;
					}
					if(b.getWorld().getBlockAt(new Location(b.getWorld(), b.getX(), b.getY() - 1, b.getZ())).getType()==Material.AIR){
						b.getWorld().getBlockAt(new Location(b.getWorld(), b.getX(), b.getY() - 1, b.getZ())).setType(Material.SAND);
					}
				}
				for(Block b : rframes.keySet()){
					if(!(b.getType()==Material.ENCHANTMENT_TABLE)){
						UUID uuid = frames.get(b);
						frames.remove(b, uuid);
						return;
					}
					if(b.getWorld().getBlockAt(new Location(b.getWorld(), b.getX(), b.getY() - 1, b.getZ())).getType()==Material.AIR){
						b.getWorld().getBlockAt(new Location(b.getWorld(), b.getX(), b.getY() - 1, b.getZ())).setType(Material.SAND);
						b.getWorld().getBlockAt(new Location(b.getWorld(), b.getX(), b.getY() - 1, b.getZ())).setData((byte) 1);;
					}
				}
			}
		}.runTaskTimer(this, 60, 4);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
		if(cmd.getName().equalsIgnoreCase("tpup")){
			if(sender instanceof Player){
				sender.sendMessage("This command is for CONSOLE use only!");
				return true;
			}
			Player p = Bukkit.getPlayer(args[0]);
			Location ploc = p.getLocation();
			p.teleport(new Location(p.getWorld(), ploc.getX(), ploc.getY() + 5, ploc.getZ(), ploc.getYaw(), ploc.getPitch()));
		}
		if(cmd.getName().equalsIgnoreCase("tpdown")){
			if(sender instanceof Player){
				sender.sendMessage("This command is for CONSOLE use only!");
				return true;
			}
			Player p = Bukkit.getPlayer(args[0]);
			Location ploc = p.getLocation();
			p.teleport(new Location(p.getWorld(), ploc.getX(), ploc.getY() - 5, ploc.getZ(), ploc.getYaw(), ploc.getPitch()));
		}
		return true;
	}
	@EventHandler
	public void onPortalThrow(PotionSplashEvent e){
		e.setCancelled(true);
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		if(!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
			return;
		}
		if(e.getItem().getType().equals(Material.FIREWORK) || e.getItem().getType().equals(Material.SIGN) || e.getItem().getType().equals(Material.MINECART) || e.getItem().getType().equals(Material.COMMAND_MINECART) || e.getItem().getType().equals(Material.HOPPER_MINECART) || e.getItem().getType().equals(Material.STORAGE_MINECART) || e.getItem().getType().equals(Material.POWERED_MINECART)){
			e.setCancelled(true);
			e.getPlayer().sendMessage("§7[§b§lSno§c§lMC§7]§c That item is not allowed on this server!");	
		}
	}
	@EventHandler
	public void onPlayerBlockBreak(BlockBreakEvent e){
		Block block = e.getBlock();
		if(frames.containsKey(block)){
			UUID uuid = frames.get(block);
			frames.remove(block, uuid);
			//getConfig().set("FramesPlaced." + uuid.toString(), getConfig().getInt("FramesPlaced." + uuid.toString()) - 1);
			//saveConfig();
		//	return;
		}
		if(rframes.containsKey(block)){
			UUID uuid = rframes.get(block);
			rframes.remove(block, uuid);
			//getConfig().set("FramesPlaced." + uuid.toString(), getConfig().getInt("FramesPlaced." + uuid.toString()) - 1);
			//saveConfig();
		//	return;
		}
		//return;
	}
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent e){
		Block block = e.getBlock();
		Player p = e.getPlayer();
		if(block.getType().equals(Material.ENDER_PORTAL_FRAME)){
			frames.put(block, p.getUniqueId());
			return;
		}
		if(block.getType().equals(Material.ENCHANTMENT_TABLE)){
			rframes.put(block, p.getUniqueId());
		}
		switch(block.getType()){
		case FURNACE:
			e.setCancelled(true);
			p.sendMessage("§7[§b§lSno§c§lMC§7]§c That block is not allowed on this server!");
			break;
		case ANVIL:
			e.setCancelled(true);
			p.sendMessage("§7[§b§lSno§c§lMC§7]§c That block is not allowed on this server!");
			break;
		case SIGN:
			e.setCancelled(true);
			p.sendMessage("§7[§b§lSno§c§lMC§7]§c That block is not allowed on this server!");
			break;
		default:
			break;
		}
	}
}
