package com.thebkkingdom.Shotgun;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EntityListener implements Listener {

	
	@EventHandler
	public void ProjectileHit(ProjectileHitEvent event) {
		// if egg hits create explosion, snowball smoke
	}
}
