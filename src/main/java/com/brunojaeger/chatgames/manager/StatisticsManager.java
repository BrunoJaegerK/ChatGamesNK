package com.brunojaeger.chatgames.manager;
import cn.nukkit.utils.Config; import com.brunojaeger.chatgames.ChatGamesNK; import java.io.File; import java.util.UUID;
/** Estadísticas ligeras persistidas localmente; no necesita un servicio de base de datos externo. */
public final class StatisticsManager {
 private final ChatGamesNK plugin; private Config data; private boolean enabled;
 public StatisticsManager(ChatGamesNK plugin){this.plugin=plugin;}
 public void load(){enabled=plugin.getConfig().getBoolean("statistics.enabled",true);data=new Config(new File(plugin.getDataFolder(),"stats.yml"),Config.YAML);}
 /** Registra una victoria y la cantidad que realmente se acreditó al ganador. */
 public void recordWin(UUID uuid,double reward){if(!enabled)return;String key=uuid.toString();data.set(key+".wins",data.getInt(key+".wins",0)+1);data.set(key+".earned",data.getDouble(key+".earned",0)+reward);data.save();}
 public int getWins(UUID uuid){return data==null?0:data.getInt(uuid+".wins",0);} public double getEarned(UUID uuid){return data==null?0:data.getDouble(uuid+".earned",0);} public boolean isEnabled(){return enabled;}
}