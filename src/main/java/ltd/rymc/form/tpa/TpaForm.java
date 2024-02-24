package ltd.rymc.form.tpa;

import co.aikar.commands.PaperCommandManager;
import fun.xiantiao.utils.interfaces.redis.Redis;
import fun.xiantiao.utils.interfaces.redis.RedisHandler;
import ltd.rymc.form.tpa.commands.TpaFormCommand;
import ltd.rymc.form.tpa.config.ConfigManager;
import ltd.rymc.form.tpa.configs.LangConfig;
import ltd.rymc.form.tpa.configs.TpaConfig;
import ltd.rymc.form.tpa.listener.TpaCommandListener;
import ltd.rymc.form.tpa.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TpaForm extends JavaPlugin {

    private static TpaForm instance;
    private static ConfigManager<TpaConfig> tpaConfigManager;
    private static ConfigManager<LangConfig> langConfigManager;
    private static PaperCommandManager commandManager;

    private static Redis pool;

    @Override
    public void onEnable() {
        instance = this;
        tpaConfigManager = ConfigManager.create(getDataFolder().toPath(),"config.yml", TpaConfig.class);
        langConfigManager = ConfigManager.create(getDataFolder().toPath(), "lang.yml", LangConfig.class);
        reload();

        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new TpaFormCommand());

        Bukkit.getPluginManager().registerEvents(new TpaCommandListener(), this);

        if (Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("XTLib")).isEnabled()) {
            pool = new RedisHandler();
            String redisHost = tpaConfigManager.getConfigData().redisHost();
            int redisPort = tpaConfigManager.getConfigData().redisPort();
            int redisMaxTotal = tpaConfigManager.getConfigData().redisMaxTotal();
            int redisMaxIdle = tpaConfigManager.getConfigData().redisMaxIdle();
            String redisUser = tpaConfigManager.getConfigData().redisUser();
            String redisPassword = tpaConfigManager.getConfigData().redisPassword();
            int redisDB = tpaConfigManager.getConfigData().redisDB();
            pool.createRedisPool(redisHost,redisPort,redisMaxTotal,redisMaxIdle,redisUser,redisPassword,redisDB);
            pool.setKey("fun.xiantiao.test","Dx");
            if ("Dx".equalsIgnoreCase(pool.getKey("fun.xiantiao.test"))) {
                getLogger().info("HuskHomes多端启用");
            } else {
                getLogger().warning("无法使用Redis，HuskHomes多端未启用");
            }
        } else {
            getLogger().warning("未找到XTLib，HuskHomes多端未启用");
        }

        if (config().metrics()){
            new Metrics(this, 16561);
        }

    }

    public static void reload(){
        tpaConfigManager.reloadConfig();
        langConfigManager.reloadConfig();
        pool.closeRedisPool();
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
    public static Redis getRedis() {
        return pool;
    }

}
