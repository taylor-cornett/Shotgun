package me.butkicker12.Shotgun;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

	private Shotgun plugin;

	public PlayerListener(Shotgun instance) {
		plugin = instance;
	}

	@EventHandler
	public void playerInteract(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Location playerLocation = player.getLocation();
		World world = player.getWorld();
		GameMode gamemode = player.getGameMode();

		/*
		 * Shotgun gun
		 */
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
				&& (player.getItemInHand().getType() == Material.BOOK)) {
			if (plugin.getCustomConfig().getBoolean("weapon.enabled.shotgun",
					true)) {
				if (player.hasPermission("shotgun.shotgun")) {
					/*
					 * Checks if player has 5 arrows. If they do then it fires.
					 * If in creative mode then don't take arrows
					 */
					switch (gamemode) {
					case SURVIVAL:
						if (player.getInventory().contains(Material.ARROW, 5)) {

							Inventory inv = player.getInventory();
							Material type = Material.ARROW;
							//int amount = 5;
							int amount = plugin.getCustomConfig().getInt("options.weapon.shotgun.inventory-amount");
							
							if (amount > 0) {
								amount = 0;
							}

							for (ItemStack is : inv.getContents()) {
								if (is != null && is.getType() == type) {
									int newamount = is.getAmount() - amount;
									if (newamount > 0) {
										is.setAmount(newamount);
										break;
									} else {
										inv.remove(is);
										amount = -newamount;
										if (amount == 0)
											break;
									}
								}
							}

							world.playEffect(playerLocation, Effect.BOW_FIRE,
									50);
							world.playEffect(playerLocation, Effect.SMOKE, 105);

							// run task twice
							for (int i = 0; i < 2; i++) {
								world.createExplosion(playerLocation, -1);
							}

							// Runs the task 5 times
							for (int i = 0; i < 5; i++) {
								player.launchProjectile(Arrow.class);
							}

						} else {
							player.sendMessage(ChatColor.BLUE
									+ "[Shotgun] You need at least 5 arrows to use the shotgun!");
						}
						break;

					case CREATIVE:

						world.playEffect(playerLocation, Effect.BOW_FIRE, 50);
						world.playEffect(playerLocation, Effect.SMOKE, 105);

						// run task twice
						for (int i = 0; i < 2; i++) {
							world.createExplosion(playerLocation, -1);
						}

						// Runs the task 5 times
						for (int i = 0; i < 5; i++) {
							player.launchProjectile(Arrow.class);
						}
						break;
					default:
						break;
					}
				}
			}
		}

		/*
		 * Nuke gun
		 */
		if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			if (plugin.getCustomConfig()
					.getBoolean("weapon.enabled.nuke", true)) {
				if (player.getItemInHand().getType() == Material.BOOK
						&& player.hasPermission("shotgun.nuke")) {

					player.sendMessage(ChatColor.BLUE
							+ "[Shotgun] Sorry this feature does not work right now. :( It will be back soon!");
					// player.launchProjectile(Fireball.class);
				}
			}
		}
	}
}
