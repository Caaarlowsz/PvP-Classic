package com.github.caaarlowsz.pvp;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public final class PlayerListeners implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	private void onPlayerJoin(PlayerJoinEvent event) {
		PvPPlayer player = PvPClassic.getPlayer(event.getPlayer());
		if (player.teleportToSpawn())
			player.giveKit();

		event.setJoinMessage(Strings.getJoinMessage().replace("{player_name}", player.getDisplayName()));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Bukkit.getScheduler().runTask(PvPClassic.getPlugin(), () -> ((CraftPlayer) player).getHandle().playerConnection
				.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN)));

		Player killer = player.getKiller();
		if (killer != null && killer != player)
			event.setDeathMessage(Strings.getKillMessage().replace("{killer_name}", killer.getDisplayName())
					.replace("{player_name}", player.getDisplayName()));
		else
			event.setDeathMessage(Strings.getDeathMessage().replace("{player_name}", player.getDisplayName()));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onPlayerRespawn(PlayerRespawnEvent event) {
		PvPPlayer player = PvPClassic.getPlayer(event.getPlayer());
		Bukkit.getScheduler().runTask(PvPClassic.getPlugin(), () -> {
			if (player.teleportToSpawn())
				player.giveKit();
		});
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		PvPClassic.removePlayer(player);
		event.setQuitMessage(Strings.getQuitMessage().replace("{player_name}", player.getDisplayName()));
	}
}