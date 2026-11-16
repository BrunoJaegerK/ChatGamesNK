package com.brunojaeger.chatgames.listener;
import cn.nukkit.event.EventHandler; import cn.nukkit.event.Listener; import cn.nukkit.event.player.PlayerChatEvent; import com.brunojaeger.chatgames.ChatGamesNK;
public final class ChatListener implements Listener {
 private final ChatGamesNK plugin; public ChatListener(ChatGamesNK plugin){this.plugin=plugin;}
 /** Solo cancela mensajes que son respuestas válidas o intentos de opción múltiple; el chat normal sigue intacto. */
 @EventHandler public void onPlayerChat(PlayerChatEvent event){if(plugin.getGames().handleAnswer(event.getPlayer(),event.getMessage()))event.setCancelled(true);}
}