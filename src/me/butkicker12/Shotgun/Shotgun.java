package me.butkicker12.Shotgun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

	File configFile;
	FileConfiguration config;

	public void onEnable() {
		// config below
		// initialize File and FileConfiguration
		configFile = new File(getDataFolder(), "config.yml");

		try {
			firstRun();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// declare the FileConfigurations using YamlConfigurations
		config = new YamlConfiguration();

		this.loadYaml();

		// check for updates
		checkUpdate();

		this.registerEvents();
	}

	public void onDisable() {
		saveYaml();
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
						if (getConfig().getBoolean(
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

	/*
	 * if the yaml does not exists, we load the yaml located at your jar file,
	 * then save it in the File(/plugins/shotgun/*.yml) then populate and save
	 * only needed at onEnable()
	 */
	private void firstRun() throws Exception {
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), configFile);

			// populate and save
			writeYaml();
			saveYaml();
			getLogger().log(Level.INFO,
					"[Shotgun] Configuration sucessfully populated!");
		}
	}

	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Load yaml can be used anytime after first startup
	 */
	public void loadYaml() {
		try {
			config.load(configFile); // loads the contents of the File to its
			getLogger().log(Level.INFO, "[Shotgun] Configuration loaded!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * can be called anywhere if you have *.set(path,value) on your methods
	 */
	public void saveYaml() {
		try {
			config.save(configFile); // saves the FileConfiguration to its File
		} catch (IOException e) {
			getLogger().log(
					Level.WARNING,
					"[Shotgun] Unable to save config at: " + getDataFolder()
							+ "config.yml");
			e.printStackTrace();

		}
	}

	private void writeYaml() {

		getConfig().set("options.update-checker", true);
		getConfig().set("weapon.shotgun.fire-via-command", false);

		// TODO
		getConfig().set("weapon.cooldown.shotgun", "5");
		getConfig().set("weapon.cooldown.nuke", "20");
		getConfig().set("weapon.cooldown.smoke", "10");
		getConfig().set("weapon.cooldown.grenade", "10");
		getConfig().set("weapon.cooldown.grenade-launcher", "20");

		// TODO
		getConfig().set("weapon.enabled.shotgun", true);
		getConfig().set("weapon.enabled.nuke", true);
		getConfig().set("weapon.enabled.smoke", true);
		getConfig().set("weapon.enabled.grenade", true);
		getConfig().set("weapon.enabled..grenade-launcher", true);
		// not used
		// getConfig.set("log plugin use to file", false");
	}

	private void checkUpdate() {
		if (getConfig().getBoolean(
				"options.update-checker") == true) {
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
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
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
