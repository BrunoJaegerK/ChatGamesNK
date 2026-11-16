package com.brunojaeger.chatgames.manager;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import com.brunojaeger.chatgames.ChatGamesNK;

import java.io.File;
import java.util.Map;

/** Carga el idioma seleccionado y recurre al español si el archivo no existe. */
public final class LanguageManager {
    private final ChatGamesNK plugin;
    private Config language;

    public LanguageManager(ChatGamesNK plugin) {
        this.plugin = plugin;
    }

    public void load() {
        String selected = plugin.getConfig().getString("language", "es_ES");
        File file = new File(plugin.getDataFolder(), "lang/" + selected + ".yml");

        if (!file.isFile()) {
            plugin.getLogger().warning("No existe lang/" + selected + ".yml; se usará es_ES.yml.");
            plugin.saveResource("lang/es_ES.yml", false);
            file = new File(plugin.getDataFolder(), "lang/es_ES.yml");
        }

        language = new Config(file, Config.YAML);
    }

    public String get(String key) {
        return language == null ? key : language.getString(key, key);
    }

    public String get(String key, Map<String, String> values) {
        String output = get(key);
        if (values != null) {
            for (Map.Entry<String, String> entry : values.entrySet()) {
                output = output.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return output.replace('&', '§');
    }

    public void send(CommandSender sender, String key) {
        sender.sendMessage((get("prefix") + get(key)).replace('&', '§'));
    }
}
