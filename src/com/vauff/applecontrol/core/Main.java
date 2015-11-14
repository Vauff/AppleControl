package com.vauff.applecontrol.core;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;

import com.vauff.applecontrol.commands.ACReload;
import com.vauff.applecontrol.commands.CheckApples;
import com.vauff.applecontrol.commands.ClaimApples;

public class Main extends JavaPlugin
{
	public static String version = "1.0";
	public static File storage;
	public static FileConfiguration storageConfig;

	public void onEnable()
	{
		storage = new File(getDataFolder(), "storage.yml");
		storageConfig = YamlConfiguration.loadConfiguration(storage);

		getConfig().options().copyDefaults(true);
		saveConfig();
		getCommand("claimapples").setExecutor(new ClaimApples(this));
		getCommand("checkapples").setExecutor(new CheckApples(this));
		getCommand("acreload").setExecutor(new ACReload(this));

		if (getConfig().getInt("dont-ever-change-this") != 1)
		{
			ConsoleCommandSender console = getServer().getConsoleSender();
			File config = new File(getDataFolder(), "config.yml");
			File configBackup = new File(getDataFolder(), "config-backup.yml");

			console.sendMessage("[AppleControl]" + ChatColor.DARK_RED + " IMPORTANT:");
			console.sendMessage("[AppleControl]" + ChatColor.RED + " Your configuration has been reset due to an update that added new configuration options");
			console.sendMessage("[AppleControl]" + ChatColor.RED + " Your old configuration file is stored in config-backup.yml");
			console.sendMessage("[AppleControl]" + ChatColor.RED + " You can use this file as reference to get back your config to how it was before");

			try
			{
				Files.copy(config, configBackup);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			config.delete();
			saveDefaultConfig();
		}

		getServer().getScheduler().runTaskTimer(this, new Runnable()
		{
			int appleCheckTime = 2;

			public void run()
			{
				{
					if (appleCheckTime != 1)
					{
						appleCheckTime--;
					}
					else
					{
						for (Player player : Bukkit.getOnlinePlayers())
						{
							String playerName = player.getName();
							int appleNumber = Util.getAmount(player, Material.GOLDEN_APPLE, 1);
							int applesTaken = appleNumber - getConfig().getInt("max-apple-amount");

							if (appleNumber > getConfig().getInt("max-apple-amount") && !player.hasPermission("applecontrol.skipapplecheck"))
							{
								String claimMessage = "";

								if (getConfig().getInt("max-apple-amount") != 0)
								{
									claimMessage = " You can claim back your apples in intervals of " + getConfig().getInt("max-apple-amount") + " by using /claimapples";
								}

								player.sendMessage("" + ChatColor.RED + applesTaken + " " + getConfig().getString("apple-name") + " apple(s) were taken from your inventory because you had too many!" + claimMessage);
								System.out.println("[AppleControl] Took " + applesTaken + " " + getConfig().getString("apple-name") + " apple(s) from " + playerName + "'s inventory");
								player.getInventory().removeItem(new ItemStack(Material.GOLDEN_APPLE, applesTaken, (short) 1));
								player.updateInventory();

								if (player.getGameMode() != GameMode.CREATIVE && getConfig().getBoolean("store-creative-apples") == false)
								{
									storageConfig.set(player.getUniqueId().toString(), applesTaken + storageConfig.getInt(player.getUniqueId().toString()));
								}

								if (getConfig().getBoolean("store-creative-apples") == true)
								{
									storageConfig.set(player.getUniqueId().toString(), applesTaken + storageConfig.getInt(player.getUniqueId().toString()));
								}

								try
								{
									storageConfig.save(storage);
								}
								catch (IOException e)
								{
									e.printStackTrace();
								}
							}
						}
						appleCheckTime = 2;
					}
				}
			}
		}, 0L, 20L);
	}
}