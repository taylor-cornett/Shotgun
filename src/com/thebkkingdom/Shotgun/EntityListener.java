package com.thebkkingdom.Shotgun;

import org.bukkit.Effect;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EntityListener implements Listener {

	@EventHandler
	public void ProjectileHit(ProjectileHitEvent event) {
		// if egg hits create explosion, snowball smoke
		Entity entity = event.getEntity();
		Player player = (Player) entity;

		if (entity instanceof Egg) {
			if (player.hasPermission("shotgun.grenade")) {
				entity.getWorld().createExplosion(entity.getLocation(), 5F);
				entity.getWorld().playEffect(entity.getLocation(),
						Effect.MOBSPAWNER_FLAMES, 100);
			}
		}

		if (entity instanceof Snowball) {
			if (player.hasPermission("shotgun.smoke")) {
				entity.getWorld().createExplosion(entity.getLocation(), -10F);
				for (int i = 0; i < 20; i++) {
					entity.getWorld().playEffect(entity.getLocation(),
							Effect.SMOKE, 1);
				}
			}
		}
	}
}
