package com.sylvcraft.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.sylvcraft.Interdiction;

public class PlayerPickupItem implements Listener {
	Interdiction plugin;
	
	public PlayerPickupItem(Interdiction plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		String materialName = e.getItem().getItemStack().getType().name().toLowerCase();
		if (!plugin.getConfig().getBoolean("interdict." + materialName + ".pickup")) return;
		if (e.getPlayer().hasPermission("interdiction.bypass")) return;
		if (e.getPlayer().hasPermission("interdiction.bypass.pickup")) return;

		e.setCancelled(true);
	}
}
