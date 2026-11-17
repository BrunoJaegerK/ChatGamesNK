package com.brunojaeger.chatgames.manager;

import cn.nukkit.Player;
import cn.nukkit.plugin.Plugin;
import com.brunojaeger.chatgames.ChatGamesNK;

import java.lang.reflect.Method;

/**
 * Adaptador aislado de EconomyAPI: el plugin puede arrancar aunque la economía
 * no esté instalada o no sea compatible.
 */
public final class EconomyHook {

    private static final String ECONOMY_CLASS = "me.onebone.economyapi.EconomyAPI";

    private final ChatGamesNK plugin;
    private Object api;
    private Method addMoney;
    private boolean available;

    public EconomyHook(ChatGamesNK plugin) {
        this.plugin = plugin;
    }

    public void hook() {
        available = false;
        api = null;
        addMoney = null;

        if (!plugin.getConfig().getBoolean("economy.enabled", true)) {
            return;
        }

        Plugin economyPlugin = plugin.getServer().getPluginManager().getPlugin("EconomyAPI");
        if (economyPlugin == null || !economyPlugin.isEnabled()) {
            plugin.getLogger().warning("EconomyAPI no está instalado o no está habilitado.");
            return;
        }

        try {
            // Cargar la clase desde el classloader de EconomyAPI evita problemas
            // de aislamiento entre los classloaders de los plugins.
            ClassLoader loader = economyPlugin.getClass().getClassLoader();
            Class<?> type = Class.forName(ECONOMY_CLASS, true, loader);

            api = type.getMethod("getInstance").invoke(null);
            if (api == null) {
                throw new IllegalStateException("EconomyAPI.getInstance() devolvió null.");
            }

            addMoney = type.getMethod("addMoney", Player.class, double.class);
            available = true;
            plugin.getLogger().info("Conexión con EconomyAPI establecida correctamente.");
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            plugin.getLogger().warning("No se pudo conectar con EconomyAPI: "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public boolean isAvailable() {
        return available;
    }

    /**
     * Ejecuta el método oficial addMoney(Player, double) de EconomyAPI.
     * EconomyAPI devuelve 1 cuando la operación se realiza correctamente.
     */
    public boolean reward(Player player, double amount) {
        if (!available || player == null || amount <= 0) {
            return false;
        }

        try {
            Object result = addMoney.invoke(api, player, amount);
            return result instanceof Number && ((Number) result).intValue() == 1;
        } catch (ReflectiveOperationException | RuntimeException e) {
            plugin.getLogger().warning("No se pudo pagar a " + player.getName() + ": "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
            return false;
        }
    }
}
