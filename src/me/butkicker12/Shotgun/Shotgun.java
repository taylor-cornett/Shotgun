package me.butkicker12.Shotgun;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Shotgun extends JavaPlugin {

	private File configFile = null;
	private FileConfiguration config = null;

	public void onEnable() {

		this.loadConfig();
		this.saveConfig();

		// check for updates
		checkUpdate();

		this.registerEvents();
	}

	public void onDisable() {
		saveConfig();
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerListener(),
				this);
		getServer().getPluginManager().registerEvents(new EntityListener(),
				this);
	}

	public void isWeaponEnabled() {
		// TODO
	}

	public boolean onCommand(CommandSender sender, Command command,
			String cmdLabel, String[] args) {

		// Player player = (Player) sender;
		Block target = ((Player) sender).getTargetBlock(null, 200);
		Location targetLocation = target.getLocation();

		/*
		 * Check if sender is player
		 */
		if ((sender instanceof Player)) {
			/*
			 * Airstrike command
			 */
			if (command.getName().equalsIgnoreCase("airstrike")) {
				if (!(args.length == 0)) {
					sender.sendMessage(ChatColor.BLUE
							+ "[Shotgun] Did you mean /airstrike?");
				}
				if (sender.hasPermission("shotgun.airstrike")) {
					target.getWorld().strikeLightning(targetLocation);
					target.getWorld().createExplosion(targetLocation, 5);
					sender.sendMessage(ChatColor.BLUE
							+ "[Shotgun] Airstrike called at your crosshairs");
				} else {
					sender.sendMessage(ChatColor.RED
							+ "You do not have permission!");
				}
				return true;
			}

			/*
			 * Nuke command
			 */

			if (command.getName().equalsIgnoreCase("nuke")) {
				if (!(args.length == 0)) {
					sender.sendMessage(ChatColor.BLUE
							+ "[Shotgun] Did you mean /nuke?");
				}
				if (sender.hasPermission("shotgun.nuke")) {
					target.getWorld().createExplosion(targetLocation, 50F);
				} else {
					sender.sendMessage(ChatColor.RED
							+ "You do not have permission!");
				}
				return true;
			}

			/*
			 * Shotgun stick fire alternative
			 */
			if (command.getName().equalsIgnoreCase("shotgun")) {
				if (args[0].equalsIgnoreCase("fire")) {
					if (((Player) sender).hasPermission("shotgun.shotgun")) {
						if (getCustomConfig().getBoolean(
								"weapons.shotgun.fire-via-command") == true) {
							/*
							 * Checks if player has 5 arrows. If they do then it
							 * fires.
							 */
							if (((Player) sender).getInventory().contains(
									Material.ARROW, 5)) {

								((Player) sender).getInventory().removeItem(
										new ItemStack(Material.ARROW, 5));

								((Player) sender).getWorld().playEffect(
										((Player) sender).getLocation(),
										Effect.BOW_FIRE, 50);
								((Player) sender).getWorld().playEffect(
										((Player) sender).getLocation(),
										Effect.SMOKE, 105);

								// run task twice
								for (int i = 0; i < 2; i++) {
									((Player) sender)
											.getWorld()
											.createExplosion(
													((Player) sender)
															.getLocation(),
													-1);
								}

								// Runs the task 5 times
								for (int i = 0; i < 5; i++) {
									((Player) sender)
											.launchProjectile(Arrow.class);
								}
							} else {
								sender.sendMessage(ChatColor.BLUE
										+ "[Shotgun] You need at least 5 arrows to use the shotgun!");
							}
						} else {
							sender.sendMessage("[Shotgun] enable: 'weapons.shotgun.fire-via-command' for this command to work");
							return false;
						}
					}
				}
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.RED
					+ "[Shotgun] You must be a player to use that command!");
		}
		return false;
	}

	public void loadConfig() {
		if (configFile == null) {
			configFile = new File(getDataFolder(), "config.yml");
		}
		config = YamlConfiguration.loadConfiguration(configFile);

		writeYaml();
	}

	/*
	 * can be called anywhere if you have *.set(path,value) on your methods
	 */
	public void saveConfig() {
		if (config == null || configFile == null) {
			return;
		}
		try {
			getCustomConfig().save(configFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE,
					"Could not save config to " + configFile, ex);
		}
	}

	public FileConfiguration getCustomConfig() {
		if (config == null) {
			this.loadConfig();
		}
		return config;
	}

	private void writeYaml() {

		getCustomConfig().set("options.update-checker", true);
		getCustomConfig().set("weapon.shotgun.fire-via-command", false);

		// TODO
		getCustomConfig().set("weapon.cooldown.shotgun", "5");
		getCustomConfig().set("weapon.cooldown.nuke", "20");
		getCustomConfig().set("weapon.cooldown.smoke", "10");
		getCustomConfig().set("weapon.cooldown.grenade", "10");
		getCustomConfig().set("weapon.cooldown.grenade-launcher", "20");

		// TODO
		getCustomConfig().set("weapon.enabled.shotgun", true);
		getCustomConfig().set("weapon.enabled.nuke", true);
		getCustomConfig().set("weapon.enabled.smoke", true);
		getCustomConfig().set("weapon.enabled.grenade", true);
		getCustomConfig().set("weapon.enabled..grenade-launcher", true);
		// not used
		// getCustomConfig().set("log plugin use to file", false");
	}

	private void checkUpdate() {
		if (getCustomConfig().getBoolean("options.update-checker") == true) {
			// state checking for updates
			getLogger()
					.log(Level.INFO, "[Shotgun] Checking for updates.......");

			URL url;
			URLConnection connection;
			InputStreamReader inputstream = null;
			// BufferedReader reader;

			try {
				url = new URL(
						"https://dl.dropbox.com/u/39012172/Bukkit/Shotgun/version.txt");
				connection = url.openConnection();
				inputstream = new InputStreamReader(connection.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			BufferedReader reader = new BufferedReader(inputstream);
			String remoteVersion = "";
			String pluginVersion = this.getDescription().getVersion();

			try {
				remoteVersion = reader.readLine();
				connection = null;
				inputstream = null;
				reader.close();
				reader = null;
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (!(remoteVersion.equalsIgnoreCase(pluginVersion))) {
				getLogger()
						.log(Level.INFO,
								"[Shotgun] You have updates! Please download version:"
										+ remoteVersion
										+ "from the plugin page (http://dev.bukkit.org/server-mods/shotgun/files/)");
			} else {
				getLogger()
						.log(Level.INFO, "[Shotgun] You gave no updates! :D");
			}
		}
	}
}
