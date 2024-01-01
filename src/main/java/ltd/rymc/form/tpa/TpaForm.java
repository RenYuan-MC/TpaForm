package ltd.rymc.form.tpa;

import co.aikar.commands.PaperCommandManager;
import ltd.rymc.form.tpa.commands.TpaFormCommand;
import ltd.rymc.form.tpa.config.ConfigManager;
import ltd.rymc.form.tpa.configs.LangConfig;
import ltd.rymc.form.tpa.configs.TpaConfig;
import ltd.rymc.form.tpa.listener.TpaCommandListener;
import ltd.rymc.form.tpa.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TpaForm extends JavaPlugin {

    private static TpaForm instance;
    private static ConfigManager<TpaConfig> tpaConfigManager;
    private static ConfigManager<LangConfig> langConfigManager;
    private static PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        tpaConfigManager = ConfigManager.create(getDataFolder().toPath(),"config.yml", TpaConfig.class);
        langConfigManager = ConfigManager.create(getDataFolder().toPath(), "lang.yml", LangConfig.class);
        reload();

        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new TpaFormCommand());

        Bukkit.getPluginManager().registerEvents(new TpaCommandListener(), this);

        if (config().metrics()){
            new Metrics(this, 16561);
        }

    }

    public static void reload(){
        tpaConfigManager.reloadConfig();
        langConfigManager.reloadConfig();
    }

    public static TpaConfig config(){
        return tpaConfigManager.getConfigData();
    }

    public static LangConfig lang(){
        return langConfigManager.getConfigData();
    }

    public static TpaForm getInstance() {
        return instance;
    }

}
