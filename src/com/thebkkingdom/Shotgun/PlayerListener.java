package com.thebkkingdom.Shotgun;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
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

		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			if (player.getItemInHand().getType() == Material.BOOK) {
				if (player.hasPermission("shotgun.shotgun")) {

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

					
					// Checks if player has exactly five arrows. Needs to check if player has 5 or more arrows.
					if (player.getInventory().contains(
							new ItemStack(Material.ARROW, 5))) {
						player.getInventory().removeItem(
								new ItemStack(Material.ARROW, 5));
						
						//fire arrow now
					} else {
						player.sendMessage(ChatColor.BLUE
								+ "[Shotgun] You need at least 5 arrows to use the shotgun!");
					}
				}
			}

			if (player.getItemInHand().getType() == Material.SNOW_BALL) {
				player.getWorld().playEffect(playerLocation, Effect.EXTINGUISH,
						100);
			}

			if (event.getAction() == Action.RIGHT_CLICK_AIR) {
				if (player.getItemInHand().getType() == Material.BOOK) {

					//world.spawnCreature(playerLocation, EntityType.FIREBALL);

					/*
					Fireball fb = this.b.getEntity().launchProjectile(
							Fireball.class);
					fb.setYield(200);
					fb.setBounce(false);
					fb.setDirection(p
							.getPlayer()
							.getVelocity()
							.add(p.getPlayer()
									.getLocation()
									.toVector()
									.subtract(
											this.b.getEntity().getLocation()
													.toVector()).normalize()
									.multiply(Integer.MAX_VALUE)));
					*/				
				}
			}
		}
	}
}
