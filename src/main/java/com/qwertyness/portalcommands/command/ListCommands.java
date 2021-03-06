package com.qwertyness.portalcommands.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.qwertyness.interactables.command.CommandLabel;
import com.qwertyness.portalcommands.PortalCommands;
import com.qwertyness.portalcommands.portal.Portal;

public class ListCommands extends CommandLabel {

	public ListCommands(PortalCommands plugin) {
		super("listportalcommands", "Lists the commands of a specified portal.", "<portal_name>", plugin);
	}

	public void run(Player player, String[] args) {
		if (args.length < 2) {
			player.sendMessage(ChatColor.RED + "Too few arguments! /interactable " + this.name + " " + this.syntax);
			return;
		}
		if (!this.plugin.getInteractablesAPI().getInteractableManager().isRegistered(args[1])) {
			player.sendMessage(ChatColor.RED + "That portal doe not exist!");
			return;
		}
		if (!(this.getPlugin().getInteractablesAPI().getInteractableManager().getInteractable(args[1]) instanceof Portal)) {
			player.sendMessage(ChatColor.RED + "Specified interactable is not a Portal!");
			return;
		}
		
		Portal portal = (Portal) this.getPlugin().getInteractablesAPI().getInteractableManager().getInteractable(args[1]);
		player.sendMessage(ChatColor.GREEN + "------- " + ChatColor.GOLD + portal.getName() + " - Commands" + ChatColor.GREEN + " -------");
		for (int counter = 0;counter < portal.getCommands().size();counter++) {
			player.sendMessage(ChatColor.GREEN + new Integer(counter + 1).toString() + ": " + ChatColor.GOLD + portal.getCommands().get(counter).getCommand() + ChatColor.GREEN + ":" + ChatColor.GOLD + portal.getCommands().get(counter).getSender());
		}
	}
}
