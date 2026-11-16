package com.brunojaeger.chatgames;
import cn.nukkit.Player; import cn.nukkit.command.Command; import cn.nukkit.command.CommandSender; import cn.nukkit.plugin.PluginBase;
import com.brunojaeger.chatgames.listener.ChatListener; import com.brunojaeger.chatgames.manager.*;
import java.lang.reflect.Method; import java.util.*;
/** Clase principal: inicializa configuraciones, servicios, listener y ciclo de partidas. */
public final class ChatGamesNK extends PluginBase {
 private LanguageManager language; private GameManager games; private EconomyHook economy; private StatisticsManager statistics;
 @Override public void onEnable(){
  saveDefaultConfig();String[] resources={"lang/es_ES.yml","games/math.yml","games/trivia.yml","games/words.yml","games/multiple-choice.yml","games/reaction.yml"};for(String path:resources)saveResource(path,false);
  language=new LanguageManager(this);language.load();statistics=new StatisticsManager(this);statistics.load();economy=new EconomyHook(this);economy.hook();games=new GameManager(this);games.loadGames();
  getServer().getPluginManager().registerEvents(new ChatListener(this),this);getServer().getScheduler().scheduleRepeatingTask(this,()->games.tick(),20);
  getLogger().info(language.get("plugin-enabled",Map.of("count",String.valueOf(games.getGames().size()))).replace('&','§'));
  if(getConfig().getBoolean("economy.enabled",true)&&!economy.isAvailable())getLogger().warning("EconomyAPI no está disponible; no se podrán pagar recompensas.");
 }
 @Override public void onDisable(){if(games!=null)games.stopGame();if(language!=null)getLogger().info(language.get("plugin-disabled").replace('&','§'));}
 @Override public boolean onCommand(CommandSender sender,Command command,String label,String[] args){
  if(args.length==0){sender.sendMessage(color(language.get("usage")));return true;}String sub=args[0].toLowerCase(Locale.ROOT);
  if(sub.equals("stats")){if(!(sender instanceof Player)||!sender.hasPermission("chatgames.stats")){sender.sendMessage(color(language.get("no-permission")));return true;}if(args.length<2){sender.sendMessage(color(language.get("usage")));return true;}Player target=getServer().getPlayer(args[1]);if(target==null){language.send(sender,"player-not-found");return true;}showStats(sender,target);return true;}
  if(!sender.hasPermission("chatgames.admin")){sender.sendMessage(color(language.get("prefix")+language.get("no-permission")));return true;}
  switch(sub){
   case "start"->{if(args.length<2){sender.sendMessage(color(language.get("usage")));return true;}boolean started=args[1].equalsIgnoreCase("random")?games.startRandomGame():games.startGame(args[1]);if(!started){String message=games.isActive()?language.get("already-active"):language.get("unknown-game",Map.of("game",args[1]));sender.sendMessage(color(language.get("prefix")+message));}}
   case "stop"->{if(!games.isActive())language.send(sender,"no-active-game");else{games.stopGame();language.send(sender,"game-stopped");}}
   case "list"->{sender.sendMessage(color(language.get("games-header")));if(games.getGames().isEmpty())sender.sendMessage(color(language.get("games-empty")));for(var game:games.getGames())sender.sendMessage(color(language.get("games-entry").replace("{name}",game.getDisplayName()).replace("{type}",game.getType())));}
   case "reload"->reloadAll(sender);
   default->sender.sendMessage(color(language.get("usage")));
  }return true;
 }
 private void reloadAll(CommandSender sender){try{reloadConfig();language.load();statistics.load();economy.hook();games.loadGames();language.send(sender,"reload");}catch(Exception e){getLogger().warning("Error recargando configuraciones: "+e.getMessage());language.send(sender,"reload-error");}}
 private void showStats(CommandSender sender,Player target){if(!statistics.isEnabled()){language.send(sender,"stats-disabled");return;}sender.sendMessage(color(language.get("stats-header").replace("{player}",target.getName())));sender.sendMessage(color(language.get("stats-line").replace("{wins}",String.valueOf(statistics.getWins(target.getUniqueId()))).replace("{earned}",String.format(Locale.US,"%.2f",statistics.getEarned(target.getUniqueId())))));}
 public void broadcast(String message){getServer().broadcastMessage(color(language.get("prefix")+message));}
 /** Reproduce el sonido elegido en config.yml sin acoplar el código a una versión concreta del enum. */
 public void playCorrectSound(Player player){String name=getConfig().getString("sounds.correct","RANDOM_LEVELUP");try{Class<?> type=Class.forName("cn.nukkit.level.Sound");if(!type.isEnum())return;@SuppressWarnings({"rawtypes","unchecked"})Object sound=Enum.valueOf((Class<? extends Enum>)type,name);for(Method method:player.getLevel().getClass().getMethods())if(method.getName().equals("addSound")&&method.getParameterCount()==2&&method.getParameterTypes()[1].isAssignableFrom(type)&&method.getParameterTypes()[0].isInstance(player)){method.invoke(player.getLevel(),player,sound);return;}}catch(Exception e){if(getConfig().getBoolean("debug",false))getLogger().warning("No se pudo reproducir "+name+": "+e.getMessage());}}
 private String color(String text){return text==null?"":text.replace('&','§');}
 public LanguageManager getLanguage(){return language;} public GameManager getGames(){return games;} public EconomyHook getEconomy(){return economy;} public StatisticsManager getStatistics(){return statistics;}
}