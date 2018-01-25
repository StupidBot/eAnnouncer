package com.stupidbot.universalannouncer.listeners;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.stupidbot.universalannouncer.Main;
import com.stupidbot.universalannouncer.utils.Announcer;

public class OnCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("reloadannouncements"))
			if (sender.hasPermission(new Permission("UniversalAnnouncer.Admin"))) {
				Main.getInstance().reloadConfig();
				Bukkit.getScheduler().cancelAllTasks();
				Announcer.setup();
				sender.sendMessage("§aReloaded announcements.");
			} else
				sender.sendMessage("§cYou don't have permission to do use this command.");
		else if (cmd.getName().equalsIgnoreCase("toggleannouncements"))
			if (sender.hasPermission(new Permission("UniversalAnnouncer.Hide")))
				if (sender instanceof Player) {
					Player player = (Player) sender;

					if (Announcer.hiddenAnnoucements == null
							|| !(Announcer.hiddenAnnoucements.contains(player.getUniqueId().toString()))) {
						Announcer.hiddenAnnoucements.add(player.getUniqueId().toString());
						player.sendMessage("§cAnnouncements toggled off.");
					} else {
						Announcer.hiddenAnnoucements.remove(player.getUniqueId().toString());
						player.sendMessage("§aAnnouncements toggled on.");
					}
				} else
					sender.sendMessage("§cThis command can only be run by a player.");
			else
				sender.sendMessage("§cYou don't have permission to do use this command.");
		else if (cmd.getName().equalsIgnoreCase("testannouncements"))
			if (sender.hasPermission(new Permission("UniversalAnnouncer.Admin"))) {
				if (args.length > 0) {
					Main instance = Main.getInstance();
					FileConfiguration config = instance.getConfig();
					Set<String> announcementSegments = config.getConfigurationSection("Announcements").getKeys(false);

					if (announcementSegments.contains(args[0])) {
						Announcer.announce(args[0]);
						sender.sendMessage("§aTested announcement §e" + args[0] + "§a.");
					} else {
						StringBuilder sb = new StringBuilder();

						for (String argsHelp : announcementSegments)
							sb.append(argsHelp).append(" ");

						String allArgs = sb.toString().trim();

						sender.sendMessage("§cInvalid args. Supported args: §e" + allArgs + "§c.");
					}
				} else
					sender.sendMessage("§cIncorrect usage. /" + label + " <announcement id>.");
			} else
				sender.sendMessage("§cYou don't have permission to do use this command.");
		return true;
	}
}