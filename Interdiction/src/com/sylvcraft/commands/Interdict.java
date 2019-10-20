package com.sylvcraft.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.sylvcraft.Interdiction;

public class Interdict implements CommandExecutor {
	Interdiction plugin;
	
	public Interdict(Interdiction plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("interdiction.admin")) {
			plugin.msg("access-denied", sender);
			return true;
		}
		
		if (args.length == 0) {
			plugin.msg("help", sender);
			return true;
		}
		
		String feature = args[0].toLowerCase();
		String target = (args.length > 1)?args[1].toLowerCase():"";
		if (!new ArrayList<String>(Arrays.asList(new String[] {"cfg","show","listent","listmat","reload","create","craft","hold","pickup","interact"})).contains(feature)) {
			plugin.msg("help", sender);
			return true;
		}

		switch (feature) {
		case "reload":
			plugin.reloadConfig();
			plugin.msg("reloaded", sender);
			return true;

		case "show":
			showInterdictions(sender);
			return true;

		case "cfg":
			if (args.length < 2) {
				plugin.msg("help-cfg", sender);
				return true;
			}
			switch (target) {
			case "remove":
				toggleRemoval(sender);
				break;
			default:
				plugin.msg("help", sender);
			}
			return true;
		
		case "listent":
			listObjects(sender, "ent");
			return true;
		
		case "listmat":
			listObjects(sender, "mat");
			return true;
		
		case "create":
			if (args.length < 2) {
				plugin.msg("help", sender);
				return true;
			}
			try {
				EntityType.valueOf(target.toUpperCase());
			} catch (IllegalArgumentException ex) {
				plugin.msg("invalid", sender);
				return true;
			}
			break;
		
		default:
			if (args.length < 2 && !(sender instanceof Player)) {
				plugin.msg("help", sender);
				return true;
			}
			if (args.length == 1) {
				Player p = (Player)sender;
				if (p.getInventory().getItemInHand().getType() == Material.AIR) {
					plugin.msg("hold-item", sender);
					return true;
				}
				target = p.getInventory().getItemInHand().getType().name().toLowerCase();
			}
			Material m = Material.matchMaterial(target);
			if (m == null) {
				plugin.msg("invalid", sender);
				return true;
			}
			break;
		}
		
		toggleInterdiction(sender, feature, target);
		return true;
	}
	
	void showInterdictions(CommandSender sender) {
		Map<String, String> data = new HashMap<String, String>();
		plugin.msg("show-header", sender);
		ConfigurationSection cfg = plugin.getConfig().getConfigurationSection("interdict");
		if (cfg == null) { 
			plugin.msg("show-none", sender);
			return;
		}

		boolean itemDisplayed = false;
		for (String item : cfg.getKeys(false)) {
			for (String function : new String[] {"create","craft","hold","pickup","interact"}) {
				if (!plugin.getConfig().getBoolean("interdict." + item + "." + function)) continue;
				data.put("%item%", item);
				data.put("%function%", "#" + function + "#");
				plugin.msg("show-data", sender, data);
				itemDisplayed = true;
			}
		}
		if (!itemDisplayed) plugin.msg("show-none", sender);
	}
	
	void listObjects(CommandSender sender, String type) {
		Map<String, String> data = new HashMap<String, String>();
		plugin.msg("list" + type + "-header", sender);
		if (type.equals("ent")) {
			for (EntityType et : EntityType.values()) {
				data.put("%entity%", et.name());
				plugin.msg("listent-data", sender, data);
			}
		} else {
			for (Material m : Material.values()) {
				data.put("%material%", m.name());
				plugin.msg("listmat-data", sender, data);
			}
		}
	}
	
	void toggleRemoval(CommandSender sender) {
		Map<String, String> data = new HashMap<String, String>();
		plugin.getConfig().set("config.remove", !plugin.getConfig().getBoolean("config.remove"));
		plugin.saveConfig();
		data.put("%status%", plugin.getConfig().getBoolean("config.remove")?"#enabled#":"#disabled#");
		plugin.msg("remove", sender, data);
	}
	
	void toggleInterdiction(CommandSender sender, String feature, String target) {
		Map<String, String> data = new HashMap<String, String>();
		String configPath = "interdict." + target + "." + feature;
		plugin.getConfig().set(configPath, !plugin.getConfig().getBoolean(configPath));
		plugin.saveConfig();

		data.put("%item%", StringUtils.capitalize(target));
		data.put("%feature%", "#" + feature + "#");
		data.put("%status%", plugin.getConfig().getBoolean(configPath)?"#enabled#":"#disabled#");
		plugin.msg("toggle", sender, data);
	}
}
