package com.vauff.applecontrol.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.vauff.applecontrol.core.Main;

public class CheckApples implements CommandExecutor
{
	private Main main;

	public CheckApples(Main plugin)
	{
		main = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args)
	{
		if (commandlabel.equalsIgnoreCase("checkapples"))
		{
			if (sender instanceof Player)
			{
				if (sender.hasPermission("applecontrol.checkapples"))
				{
					Player player = (Player) sender;
					String claimMessage = " They cannot be claimed back though, because this server does not allow them at all!";

					if (main.getConfig().getInt("max-apple-amount") != 0)
					{
						claimMessage = " You can claim back your apples in intervals of " + main.getConfig().getInt("max-apple-amount") + " by using /claimapples";
					}

					sender.sendMessage(ChatColor.GREEN + "You currently have " + Main.storageConfig.getInt(player.getUniqueId().toString()) + " " + main.getConfig().getString("apple-name") + " apple(s) in storage!" + claimMessage);
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