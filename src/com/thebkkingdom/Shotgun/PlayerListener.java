package com.thebkkingdom.Shotgun;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

	@EventHandler
	public void playerInteract(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Location playerLocation = player.getLocation();
		World world = player.getWorld();
		ItemStack arrow = new ItemStack(Material.ARROW, 5);

		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			if (player.getItemInHand().getType() == Material.BOOK) {
				if (player.hasPermission("s.shotgun") || player.isOp()) {
					// Deprecated :(
					player.shootArrow();
					player.shootArrow();
					player.shootArrow();
					player.shootArrow();
					player.shootArrow();
					world.playEffect(playerLocation, Effect.BOW_FIRE, 50);
					world.createExplosion(playerLocation, -1);
					world.createExplosion(playerLocation, -1);
					world.playEffect(playerLocation, Effect.SMOKE, 105);

					player.getInventory().removeItem(arrow);
				}
			}

			if (player.getItemInHand().getType() == Material.SNOW_BALL) {
				player.getWorld().playEffect(playerLocation, Effect.EXTINGUISH,
						100);
			}

			if (event.getAction() == Action.RIGHT_CLICK_AIR) {
				if (player.getItemInHand().getType() == Material.BOOK) {
					// rocket launcher/fireball code here
				}
			}
		}
	}
}
