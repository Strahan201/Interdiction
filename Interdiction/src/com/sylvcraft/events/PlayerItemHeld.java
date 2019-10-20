package com.sylvcraft.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.sylvcraft.Interdiction;

public class PlayerItemHeld implements Listener {
	Interdiction plugin;
	
	public PlayerItemHeld(Interdiction plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {
		ItemStack heldItem = e.getPlayer().getInventory().getItem(e.getNewSlot());
		if (heldItem == null) return;
		
		String materialName = heldItem.getType().name().toLowerCase();
		if (!plugin.getConfig().getBoolean("interdict." + materialName + ".hold")) return;
		if (e.getPlayer().hasPermission("interdiction.bypass")) return;
		if (e.getPlayer().hasPermission("interdiction.bypass.hold")) return;
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("%item%", materialName);
		if (!plugin.getConfig().getBoolean("config.remove", false)) {
			plugin.msg("hold-retain", e.getPlayer(), data);
		} else {
			e.getPlayer().getInventory().setItem(e.getNewSlot(), null);
			plugin.msg("hold-remove", e.getPlayer(), data);			
		}
	}
}
