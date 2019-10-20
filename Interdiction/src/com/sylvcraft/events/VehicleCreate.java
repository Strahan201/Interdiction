package com.sylvcraft.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import com.sylvcraft.Interdiction;

public class VehicleCreate implements Listener {
	Interdiction plugin;
	
	public VehicleCreate(Interdiction plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onVehicleCreate(VehicleCreateEvent e) {
		String materialName = e.getVehicle().getType().name().toLowerCase();
		if (!plugin.getConfig().getBoolean("interdict." + materialName + ".create")) return;
		
		e.getVehicle().remove();
	}
}
