package com.sylvcraft.events;

import org.bukkit.event.inventory.CraftItemEvent;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.sylvcraft.Interdiction;

public class CraftItem implements Listener {
	Interdiction plugin;
	
	public CraftItem(Interdiction plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onCraftItem(CraftItemEvent e) {
		String materialName = e.getCurrentItem().getType().name().toLowerCase();
		if (!plugin.getConfig().getBoolean("interdict." + materialName + ".craft")) return;
		if (e.getWhoClicked().hasPermission("interdiction.bypass")) return;
		if (e.getWhoClicked().hasPermission("interdiction.bypass.craft")) return;
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("%item%", materialName);
		plugin.msg("craft", e.getWhoClicked());
		e.setCancelled(true);
	}
}
