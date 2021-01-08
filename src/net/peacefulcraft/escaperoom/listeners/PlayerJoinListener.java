package net.peacefulcraft.escaperoom.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.peacefulcraft.escaperoom.EscapeRoom;

public class PlayerJoinListener implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent ev) {
    ev.getPlayer().sendMessage(EscapeRoom.messagingPrefix + "Welcome to the server! -Templateus");
  }
}