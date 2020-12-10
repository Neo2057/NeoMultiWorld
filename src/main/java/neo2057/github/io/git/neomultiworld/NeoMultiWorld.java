package neo2057.github.io.git.neomultiworld;


import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;



public final class NeoMultiWorld extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("[NMW]on");
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        Objects.requireNonNull(this.getCommand("nmw")).setExecutor(new NeoMultiWorldCommands(this));
        FileConfiguration config = getConfig();

        saveDefaultConfig();

        for(Player p:Bukkit.getOnlinePlayers())
            p.sendMessage("設定にあるワールドを読み込みをしています...");
        for(String name:config.getConfigurationSection("Worlds").getKeys(false)){
            WorldCreator wc = new WorldCreator(name);
            wc.createWorld();
        }
        for(Player p:Bukkit.getOnlinePlayers())
            p.sendMessage("ワールドの読み込みを終了いたしました");
        saveConfig();
        reloadConfig();
    }


    @Override
    public void onDisable() {
        getLogger().info("[NMW]off");
    }
}
