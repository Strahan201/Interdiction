package com.sylvcraft;

import org.bukkit.plugin.java.JavaPlugin;

import com.sylvcraft.commands.Interdict;
import com.sylvcraft.events.CraftItem;
import com.sylvcraft.events.PlayerInteract;
import com.sylvcraft.events.PlayerItemHeld;
import com.sylvcraft.events.PlayerPickupItem;
import com.sylvcraft.events.VehicleCreate;

import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class Interdiction extends JavaPlugin {
  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(new CraftItem(this), this);
    getServer().getPluginManager().registerEvents(new PlayerItemHeld(this), this);
    getServer().getPluginManager().registerEvents(new VehicleCreate(this), this);
    getServer().getPluginManager().registerEvents(new PlayerPickupItem(this), this);
    getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
    getCommand("interdict").setExecutor(new Interdict(this));
    saveDefaultConfig();
  }
  
  public void msg(String msgCode, CommandSender sender) {
  	String tmp = getConfig().getString("messages." + msgCode, msgCode) + ' ';
  	if (tmp.trim().equals("")) return;
  	
  	for (String m : tmp.split("%br%")) {
  		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', tokens(m)));
  	}
  }

  public void msg(String msgCode, CommandSender sender, Map<String, String> data) {
  	String tmp = getConfig().getString("messages." + msgCode, msgCode) + ' ';
  	if (tmp.trim().equals("")) return;
  	for (Map.Entry<String, String> mapData : data.entrySet()) {
  	  tmp = tmp.replace(mapData.getKey(), mapData.getValue());
  	}
  	msg(tmp, sender);
  }
  
  String tokens(String input) {
  	ConfigurationSection tokens = getConfig().getConfigurationSection("tokens");
  	if (tokens == null) return input;
  	for (String token : tokens.getKeys(false)) {
  		input = input.replace("#" + token + "#", getConfig().getString("tokens." + token, token));
  	}
  	return input;
  }
}