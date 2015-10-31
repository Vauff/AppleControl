package me.tehkitti.AppleControl;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static String v = "1.0.0";

	public static void pluginInfo(String message) {

		System.out.println("[AppleControl] Version " + v + " " + message);
	}

	@Override
	public void onDisable() {
		pluginInfo("of AppleControl Disabled!");
	}

	@Override
	public void onEnable() {
		pluginInfo("of AppleControl Enabled!");
		getConfig().options().copyDefaults(true);
		saveConfig();
		if ((getConfig().getInt("dont-ever-change-this") != 1)) {
			ConsoleCommandSender console = getServer().getConsoleSender();
			console.sendMessage("[AppleControl]" + ChatColor.DARK_RED
					+ " IMPORTANT:");
			console.sendMessage("[AppleControl]"
					+ ChatColor.RED
					+ " Configuration has been reset due to a update that added new configuration options.");
			File file = new File(this.getDataFolder(), "config.yml");
			file.delete();
			this.saveDefaultConfig();
		}
		this.getServer().getScheduler()
				.runTaskTimerAsynchronously(this, new Runnable() {
					public int applechecktime = 2;

					public void run() {
						{
							if (applechecktime != 1) {
								applechecktime--;
							} else {

								ItemStack item = new ItemStack(Material.GOLDEN_APPLE,1, (short)1);
								for (Player player : Bukkit.getOnlinePlayers()) {
									String playerName = player.getName();
									if (player.getInventory().contains(item))

										{

											{
												player.sendMessage(ChatColor
														.translateAlternateColorCodes(
																'&',
																getConfig()
																		.getString(
																				"excessive-apple-message")));
											}
											break;
										}
								}
								applechecktime = 2;
							}
						}
					}
				}, 0L, 20L);

	}

}
