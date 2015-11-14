package com.vauff.applecontrol.commands;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.vauff.applecontrol.core.Main;
import com.vauff.applecontrol.core.Util;

public class ClaimApples implements CommandExecutor
{
	private Main main;

	public ClaimApples(Main plugin)
	{
		main = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args)
	{
		if (commandlabel.equalsIgnoreCase("claimapples"))
		{
			if (sender instanceof Player)
			{
				if (sender.hasPermission("applecontrol.claimapples"))
				{
					if (main.getConfig().getInt("max-apple-amount") != 0)
					{
						Player player = (Player) sender;

						if (Main.storageConfig.getInt(player.getUniqueId().toString()) != 0)
						{
							int storedApples = Main.storageConfig.getInt(player.getUniqueId().toString());
							int applesInInv = Util.getAmount(player, Material.GOLDEN_APPLE, 1);
							int maxApples = main.getConfig().getInt("max-apple-amount");
							int claimedApples = 0;

							if (storedApples > maxApples)
							{
								claimedApples = maxApples - applesInInv;
							}
							else
							{
								if ((storedApples - applesInInv) > 0)
								{
									claimedApples = storedApples - applesInInv;
								}
							}

							if (claimedApples != 0)
							{
								String claimMessage = "";

								if (storedApples - claimedApples != 0)
								{
									claimMessage = " You can still claim " + (storedApples - claimedApples) + " more in max intervals of " + maxApples;
								}

								sender.sendMessage(ChatColor.GOLD + "You have just claimed " + claimedApples + " " + main.getConfig().getString("apple-name") + " apple(s)!" + claimMessage);
								System.out.println("[AppleControl] " + player.getName() + " just claimed " + claimedApples + " " + main.getConfig().getString("apple-name") + " apple(s)");
								Main.storageConfig.set(player.getUniqueId().toString(), storedApples - claimedApples);
								player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, claimedApples, (short) 1));

								try
								{
									Main.storageConfig.save(Main.storage);
								}
								catch (IOException e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								sender.sendMessage(ChatColor.DARK_PURPLE + "You already have too many " + main.getConfig().getString("apple-name") + " apples in your inventory to claim more!");
							}
						}
						else
						{
							sender.sendMessage(ChatColor.DARK_PURPLE + "You do not have any apples to claim!");
						}
					}
					else
					{
						sender.sendMessage(ChatColor.GOLD + "You cannot claim back apples because this server does not allow them at all!");
					}
				}
				else
				{
					sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
				}
			}

			if (sender instanceof ConsoleCommandSender)
			{
				sender.sendMessage("This command cannot be executed from the console!");
			}
		}
		return true;
	}
}