package com.stupidbot.universalannouncer;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.stupidbot.universalannouncer.listeners.OnCommand;
import com.stupidbot.universalannouncer.utils.Announcer;

public class Main extends JavaPlugin implements Listener {
	private static Main instance;

	public void onDisable() {
		FileConfiguration config = instance.getConfig();
		config.set("HiddenAnnouncements.Players", Announcer.hiddenAnnoucements);
		instance.saveConfig();
		instance = null;
		System.out.println(getName() + " Is now disabled!");
	}

	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(this, this);
		instance.getCommand("reloadannouncements").setExecutor(new OnCommand());
		instance.getCommand("toggleannouncements").setExecutor(new OnCommand());
		instance.getCommand("testannouncements").setExecutor(new OnCommand());
		setupConfig();
		Announcer.setup();
		System.out.println(getName() + " Is now enabled!");
	}

	private void setupConfig() {
		FileConfiguration config = instance.getConfig();

		if (!(new File(getDataFolder() + File.separator + "config.yml").exists())) {
			config.set("Timer", 1200);
			config.set("Announcements.0.JSON", true);
			config.set("Announcements.0.Message",
					"[\"\",{\"text\":\"[ANNOUNCEMENT] \",\"color\":\"dark_red\"},{\"text\":\"This plugin supports JSON text, hover over me!\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://d1wn0q81ehzw6k.cloudfront.net/additional/thul/media/0eaa14d11e8930f5?w=400&h=400\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"CLICK ME FOR A CAT!\",\"color\":\"gray\"}]}}}]");
			config.set("Announcements.1.JSON", false);
			config.set("Announcements.1.Message", "&4[ANNOUNCEMENT] &6Or you can use normal text!");
			config.set("HiddenAnnouncements.Players", new ArrayList<String>());
		}

		config.options().copyDefaults(true);
		instance.saveConfig();
		instance.reloadConfig();
	}

	public static Main getInstance() {
		return instance;
	}
}