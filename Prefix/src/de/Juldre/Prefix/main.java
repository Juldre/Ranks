package de.Juldre.Prefix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {

	static {
		ConfigurationSerialization.registerClass(Rank.class, "Rank");
	}

	public YamlConfiguration config;
	public List<Rank> ranks;
	public final File configFolder = new File("plugins/" + getName());
	public final File configFile = new File("plugins/" + getName(), "config.yml");

	public boolean hasPermission(Player player, String permission) {
		boolean hasOp = false;
		boolean returnValue = false;
		if (player.isOp()) {
			player.setOp(false);
			hasOp = true;
		}

		if (player.hasPermission(permission))
			returnValue = true;

		if (hasOp)
			player.setOp(true);

		return returnValue;
	}

	public void save() {
		try {
			config.save(configFile);
		} catch (IOException e) {
		}
	}

	void generateConfig() {
		try {
			configFile.createNewFile();
			config = YamlConfiguration.loadConfiguration(configFile);
			config.options().copyHeader(true);
			config.options().header("Rangliste nach Priorität sortieren -> z.B. Spieler, dann Builder usw...\n"
					+ "Name ist nur zur übersichtlichkeit");
			ranks = new ArrayList<Rank>();
			ranks.add(new Rank("Default", "", "&8[&7Default&8] &a%player% &8> &7%message%"));
			ranks.add(new Rank("Admin", "Ranks.Admin", "&8[&cAdmin&8] &c%player% &8> &7%message%"));
			config.set("Ranks", ranks);
			save();
		} catch (IOException e) {
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		if (!configFolder.exists())
			configFolder.mkdir();

		if (!configFile.exists())
			generateConfig();
		else
			config = YamlConfiguration.loadConfiguration(configFile);

		if ((List<Rank>) config.get("Ranks") == null)
			generateConfig();

		ranks = (List<Rank>) config.get("Ranks");
		Collections.reverse(ranks);

		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChatMessage(AsyncPlayerChatEvent e) {
		for (Rank rank : ranks) {
			if (e.getPlayer().hasPermission(rank.permission) || rank.permission.isEmpty()) {
				// &8[&cAdmin&8] &c%player% &8> &7
				e.setFormat(rank.prefix.replaceAll("&", "§").replaceAll("%player%", e.getPlayer().getName())
						.replaceAll("%message%", e.getMessage()));
				return;
			}
		}
	}
}
