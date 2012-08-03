package me.butkicker12.Shotgun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Shotgun extends JavaPlugin {

	File configFile;
	FileConfiguration config;

	public void onEnable() {
		//initialize File and FileConfiguration
        configFile = new File(getDataFolder(), "config.yml");
        
        try {
            firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //declare the FileConfigurations using YamlConfigurations
        config = new YamlConfiguration();
        
        loadYaml();
        
		this.registerEvents();
	}

	public void onDisable() {
		
	}

	private void registerEvents() {

		getServer().getPluginManager().registerEvents(new PlayerListener(),
				this);

		getServer().getPluginManager().registerEvents(new EntityListener(),
				this);
	}
	
	public void isWeaponEnabled() {
		//TODO
	}

	public boolean onCommand(CommandSender sender, Command command,
			String cmdLabel, String[] args) {

		// Player player = (Player) sender;
		Block target = ((Player) sender).getTargetBlock(null, 200);
		Location targetLocation = target.getLocation();

		/*
		 * Airstrike command
		 */
		if (command.getName().equalsIgnoreCase("airstrike")) {
			if (!(args.length == 0)) {
				sender.sendMessage(ChatColor.BLUE
						+ "[Shotgun] Did you mean /Airstrike");
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
						+ "[Shotgun] Did you mean /Nuke");
			}
			if (sender.hasPermission("shotgun.nuke")) {
				target.getWorld().createExplosion(targetLocation, 50F);
			} else {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission!");
			}
			return true;
		}
		return false;
	}

	/*
	 * if the yaml does not exists, we load the yaml located at your jar file, then save it in
     *  the File(/plugins/shotgun/*.yml)
	 * only needed at onEnable()
	 */
	private void firstRun() throws Exception {
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), configFile);
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
	 * Load yaml
	 * can be used anytime after first startup
	 */
	public void loadYaml() {
		try {
			config.load(configFile); // loads the contents of the File to its
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * can be called anywhere if you have *.set(path,value) on your
	 * methods
	 */
	public void saveYaml() {
		try {
			config.save(configFile); // saves the FileConfiguration to its File
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeYaml() {
		
		//TODO
		getConfig().set("weapon.cooldown.shotgun", "5");
		getConfig().set("weapon.cooldown.nuke", "20");
		getConfig().set("weapon.cooldown.smoke", "10");
		getConfig().set("weapon.cooldown.grenade", "10");
		getConfig().set("weapon.cooldown.grenade-launcher", "20");
		
		
		//TODO
		getConfig().set("weapon.enabled.shotgun", true);
		getConfig().set("weapon.enabled.nuke", true);
		getConfig().set("weapon.enabled.smoke", true);
		getConfig().set("weapon.enabled.grenade", true);
		getConfig().set("weapon.enabled..grenade-launcher", true);
		//not used
		//getConfig.set("log plugin use to file", false");
	}

}