package com.sylvcraft.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sylvcraft.Interdiction;

public class PlayerInteract implements Listener {
	Interdiction plugin;
	
	public PlayerInteract(Interdiction plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getItem() == null) return;

		String materialName = e.getItem().getType().name().toLowerCase();
		if (!plugin.getConfig().getBoolean("interdict." + materialName + ".interact")) return;
		if (e.getPlayer().hasPermission("interdiction.bypass")) return;
		if (e.getPlayer().hasPermission("interdiction.bypass.interact")) return;
		plugin.getLogger().info("boo");

		Map<String, String> data = new HashMap<String, String>();
		data.put("%item%", materialName);
		plugin.msg("interact", e.getPlayer());
		e.setCancelled(true);
	}
}
