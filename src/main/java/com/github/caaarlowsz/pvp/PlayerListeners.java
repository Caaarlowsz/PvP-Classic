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

import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public final class PlayerListeners implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	private void onPlayerJoin(PlayerJoinEvent event) {
		PvPPlayer player = PvPClassic.getPlayer(event.getPlayer());
		event.setJoinMessage("§7" + player.getDisplayName() + " entrou no servidor.");
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Bukkit.getScheduler().runTask(PvPClassic.getPlugin(), () -> ((CraftPlayer) player).getHandle().playerConnection
				.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN)));

		Player killer = player.getKiller();
		if (killer != null && killer != player)
			event.setDeathMessage("§e" + killer.getDisplayName() + " §ematou " + player.getDisplayName() + "§e.");
		else
			event.setDeathMessage("§e" + player.getDisplayName() + " §emorreu.");
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		PvPClassic.removePlayer(player);
		event.setQuitMessage("§7" + player.getDisplayName() + " saiu do servidor.");
	}
}