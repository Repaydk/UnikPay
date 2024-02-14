package dk.manaxi.unikpay.plugin;

import dk.manaxi.unikpay.plugin.commands.CommandManager;
import dk.manaxi.unikpay.plugin.enums.Hook;
import dk.manaxi.unikpay.plugin.fetch.Payments;
import dk.manaxi.unikpay.plugin.hooks.SkriptHook;
import dk.manaxi.unikpay.plugin.interfaces.IHook;
import dk.manaxi.unikpay.plugin.listeners.OnSync;
import dk.manaxi.unikpay.plugin.utils.ColorUtils;
import dk.manaxi.unikpay.plugin.utils.Config;
import dk.manaxi.unikpay.plugin.websocket.Console;
import dk.manaxi.unikpay.plugin.websocket.IoSocket;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;

public final class Main extends JavaPlugin {
    private static final String url = dk.manaxi.unikpay.api.Config.MAINURL + "request";
    private BukkitAudiences adventure;
    private static Main instance;
    public static Config config;
    public static FileConfiguration configYML;
    public static ConsoleCommandSender log;
    private static final HashMap<Hook, Boolean> HOOKS = new HashMap<>();
    private static String APIKEY;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        log = Bukkit.getConsoleSender();
        log.sendMessage(ColorUtils.getColored("&8&m---------------------------------&r", "", "  &2Enabling &aUnikPay &fv" + getDescription().getVersion()));
        long timestampBeforeLoad = System.currentTimeMillis();
        instance = this;
        initialiseConfigs();

        //Commands
        log.sendMessage(ColorUtils.getColored("", "  &2Hooking into Commands"));
        CommandManager.initialise(this);
        //hooks
        log.sendMessage(ColorUtils.getColored("", "  &2Hooking into integrations"));
        initialiseHooks();
        IoSocket.connectSocket();
        if(Main.configYML.getBoolean("update-notify", true)) {
            Bukkit.getServer().getPluginManager().registerEvents(new OnSync(), this);
        }

        log.sendMessage(ColorUtils.getColored("", " &2Hooking into console"));
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addAppender(new Console());

        log.sendMessage(ColorUtils.getColored("", "  &fUnikPay has been enabled!", "    &aVersion: &f" +
                        getDescription().getVersion(), "    &aAuthors: &f" +
                        getDescription().getAuthors(), "",
                "  &2Took &a" + ( System.currentTimeMillis() - timestampBeforeLoad) + " millis &2to load!", "", "&8&m---------------------------------&r"));
        (new BukkitRunnable() {
            @Override
            public void run() {
                Payments.fetchPayments();
            }
        }).runTaskTimerAsynchronously(Main.getInstance(), 20L, 600L);
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        log.sendMessage(ColorUtils.getColored("&8&m---------------------------------&r", "", "  &4UnikPay Disabled!", "    &cVersion: &f" + getDescription().getVersion(), "    &cAuthors: &f" + getDescription().getAuthors(), "", "&8&m---------------------------------&r"));
        IoSocket.getSocket().disconnect();
    }

    private void initialiseConfigs() {
        saveDefaultConfig();
        if (!(new File(getDataFolder(), "config.yml")).exists())saveResource("config.yml", false);
        config = new Config(this, null, "config.yml");
        configYML = config.getConfig();
        log.sendMessage(ColorUtils.getColored("", "  &2Getting your apikey"));
        dk.manaxi.unikpay.plugin.configuration.Config.loadALl();
        if (configYML.getString("Api-key").isEmpty() || configYML.getString("Api-key").equals("KEY HER")) {
            log.sendMessage(ColorUtils.getColored("", "", " &c- Du mangler at putte din apikey ind i config.yml"));
            APIKEY = null;
            return;
        }
        log.sendMessage(ColorUtils.getColored("", "", " &a- Fandt din api-key"));
        APIKEY = configYML.getString("Api-key");
    }



    //TO EVERYTHING THERE NEED TO BE RELOADED
    public void reload() {
        initialiseConfigs();
        if(IoSocket.getSocket().connected()) {
            IoSocket.getSocket().disconnect();
        }
        IoSocket.connectSocket();
    }

    public static String getAPIKEY(){
        return APIKEY;
    }


    public static Main getInstance() {
        return instance;
    }

    public File getFile() {
        return super.getFile();
    }

    private void initialiseHooks() {
        IHook[] hooks = {
                new SkriptHook()
        };
        for (IHook hook : hooks) {
            HOOKS.put(hook.getEnum(), hook.init(this));
        }
    }

    public static boolean isHookInitialised(Hook paramHook) {
        return HOOKS.getOrDefault(paramHook, Boolean.FALSE);
    }


}