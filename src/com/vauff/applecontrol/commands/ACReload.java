package com.vauff.applecontrol.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.vauff.applecontrol.core.Main;

public class ACReload implements CommandExecutor
{
	private Main main;

	public ACReload(Main plugin)
	{
		main = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args)
	{
		if (commandlabel.equalsIgnoreCase("acreload"))
		{
			if (sender.hasPermission("applecontrol.reload"))
			{
				main.reloadConfig();
				sender.sendMessage(ChatColor.AQUA + "AppleControl configuration file reloaded successfully!");
			}
			else
			{
				sender.sendMessage(ChatColor.DARK_RED + "You do not have acccess to that command.");
			}
		}
		return true;
	}
}