package com.brunojaeger.chatgames.manager;
import cn.nukkit.Player; import com.brunojaeger.chatgames.ChatGamesNK; import java.lang.reflect.Method;
/** Adaptador aislado de EconomyAPI: el plugin puede arrancar aunque la economía no esté instalada. */
public final class EconomyHook {
 private final ChatGamesNK plugin; private Object api; private Method addMoney; private boolean available;
 public EconomyHook(ChatGamesNK plugin){this.plugin=plugin;}
 public void hook(){available=false;if(!plugin.getConfig().getBoolean("economy.enabled",true))return;try{if(plugin.getServer().getPluginManager().getPlugin("EconomyAPI")==null)return;Class<?> type=Class.forName("onebone.economyapi.EconomyAPI");api=type.getMethod("getInstance").invoke(null);addMoney=type.getMethod("addMoney",Player.class,double.class);available=true;}catch(Exception e){plugin.getLogger().warning("No se pudo conectar con EconomyAPI: "+e.getMessage());}}
 public boolean isAvailable(){return available;}
 /** Ejecuta el método oficial addMoney(Player,double) de EconomyAPI. */
 public boolean reward(Player player,double amount){if(!available||amount<=0)return false;try{Object result=addMoney.invoke(api,player,amount);return !(result instanceof Number)||((Number)result).intValue()==1;}catch(Exception e){plugin.getLogger().warning("No se pudo pagar a "+player.getName()+": "+e.getMessage());return false;}}
}