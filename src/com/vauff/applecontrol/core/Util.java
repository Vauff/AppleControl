package com.vauff.applecontrol.core;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Util
{
	public static int getAmount(Player p, Material mat, int damage)
	{
		int amount = 0;

		for (ItemStack stack : p.getInventory().getContents())
		{
			if (stack != null && stack.getType() == mat && stack.getDurability() == damage)
				amount += stack.getAmount();
		}

		return amount;
	}
}
