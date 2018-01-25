package com.stupidbot.universalannouncer.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.stupidbot.universalannouncer.Main;

import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class Announcer {
	public static List<String> hiddenAnnoucements;

	public static void setup() {
		Main instance = Main.getInstance();
		FileConfiguration config = instance.getConfig();
		int animationSpeed = config.getInt("Timer");

		if (hiddenAnnoucements == null)
			if (config.getStringList("HiddenAnnouncements.Players") != null)
				hiddenAnnoucements = config.getStringList("HiddenAnnouncements.Players");

		new BukkitRunnable() {
			ArrayList<String> announcementSegments = new ArrayList<String>(
					config.getConfigurationSection("Announcements").getKeys(false));

			public void run() {
				announce(announcementSegments.get(new Random().nextInt(announcementSegments.size())));
			}
		}.runTaskTimer(instance, animationSpeed, animationSpeed);
	}

	public static void announce(String messageId) {
		Main instance = Main.getInstance();
		FileConfiguration config = instance.getConfig();

		for (Player all : Bukkit.getOnlinePlayers())
			if (!(hiddenAnnoucements.contains(all.getUniqueId().toString())))
				if (config.getBoolean("Announcements." + messageId + ".JSON"))
					sendJSON(ChatColor.translateAlternateColorCodes('&',
							config.getString("Announcements." + messageId + ".Message")), all);
				else
					all.sendMessage(ChatColor.translateAlternateColorCodes('&',
							config.getString("Announcements." + messageId + ".Message")));
	}

	private static void sendJSON(String message, Player player) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(message));
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
}