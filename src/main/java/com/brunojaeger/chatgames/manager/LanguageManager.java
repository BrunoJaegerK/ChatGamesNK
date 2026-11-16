package com.brunojaeger.chatgames.manager;
import cn.nukkit.command.CommandSender; import cn.nukkit.utils.Config; import com.brunojaeger.chatgames.ChatGamesNK; import java.io.File; import java.util.Map;
public final class LanguageManager {
 private final ChatGamesNK plugin; private Config language;
 public LanguageManager(ChatGamesNK plugin){this.plugin=plugin;}
 public void load(){String filename=plugin.getConfig().getString("language","es_ES")+".yml";File file=new File(plugin.getDataFolder(),"lang/"+filename);if(!file.exists())plugin.saveResource("lang/es_ES.yml",false);language=new Config(file,Config.YAML);}
 public String get(String key){return language==null?key:language.getString(key,key);}
 public String get(String key,Map<String,String> values){String out=get(key);if(values!=null)for(Map.Entry<String,String> e:values.entrySet())out=out.replace("{"+e.getKey()+"}",e.getValue());return out.replace('&','§');}
 public void send(CommandSender sender,String key){sender.sendMessage((get("prefix")+get(key)).replace('&','§'));}
}