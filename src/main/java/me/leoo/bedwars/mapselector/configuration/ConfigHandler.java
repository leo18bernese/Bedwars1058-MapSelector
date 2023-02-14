package me.leoo.bedwars.mapselector.configuration;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ConfigHandler {
	private YamlConfiguration yml;
	private File config;
	private String name;

	public ConfigHandler(Plugin plugin, String name, String dir) {
		File d = new File(dir);
		if (!d.exists() && !d.mkdirs()) {
			plugin.getLogger().log(Level.SEVERE, "Could not create " + d.getPath());
		} else {
			this.name = name;
			this.config = new File(dir, name + ".yml");
			if (!this.config.exists()) {
				plugin.getLogger().log(Level.INFO, "Creating " + this.config.getPath());

				try {
					if (!this.config.createNewFile()) {
						plugin.getLogger().log(Level.SEVERE, "Could not create " + this.config.getPath());
						return;
					}
				} catch (IOException var6) {
					var6.printStackTrace();
				}
			}

			this.yml = YamlConfiguration.loadConfiguration(this.config);
			this.yml.options().copyDefaults(true);
		}
	}

	public void reload() {
		this.yml = YamlConfiguration.loadConfiguration(this.config);
	}

	public YamlConfiguration getYml() {
		return this.yml;
	}

	public void save() {
		try {
			this.yml.save(this.config);
		} catch (IOException var2) {
			var2.printStackTrace();
		}

	}

	public List<String> getList(String path) {
		return this.yml.getStringList(path).stream().map((s) -> PlaceholderAPI.setPlaceholders(null, ChatColor.translateAlternateColorCodes('&', s))).collect(Collectors.toList());
	}

	public boolean getBoolean(String path) {
		return this.yml.getBoolean(path);
	}

	public int getInt(String path) {
		return this.yml.getInt(path);
	}

	public String getString(String path) {
		String s = this.yml.getString(path);
		if (s == null) {
			System.out.println("[Guilds] String with the path " + path + " not found in the config " + name);
			return "String not found";
		}
		return PlaceholderAPI.setPlaceholders(null, ChatColor.translateAlternateColorCodes('&', s));
	}
}
