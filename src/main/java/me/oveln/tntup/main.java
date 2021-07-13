package me.oveln.tntup;

import com.sun.org.apache.xpath.internal.operations.String;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Logger;

public class main extends JavaPlugin {

    private Logger logger;
    private static  main instance;
    private PluginDescriptionFile descriptionFile;
    private Set<Location> unboom;
    private ArrayList<Location> breaklist;
    private Map<Damageable,Double> dammagelist;
    private Map<Entity , Vector> velocitylist;
    private BukkitTask task;


    @Override
    public void onEnable() {
        descriptionFile = getDescription();
        instance = this;
        logger = getLogger();
        unboom = new HashSet<Location>();
        breaklist = new ArrayList<Location>();
        dammagelist = new HashMap<Damageable,Double>();
        velocitylist = new HashMap<Entity , Vector>();

        registerConfig();

        getServer().getPluginManager().registerEvents(new Blockplace(),this);
        getServer().getPluginManager().registerEvents(new Blockbreak(),this);

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule logAdminCommands false" );

        task = new Breaker(this,breaklist , dammagelist , velocitylist).runTaskTimer(this,0,1);
        logger.info("TNTUp启动成功 V1.0.0 作者Oveln");
    }
    public void onDisable() {
        task = null;
        logger.info("TNTUp关闭成功 V1.0.0");
        logger = null;
    }

    public void registerConfig() {
        saveDefaultConfig();
    }

    public static main getInstance() {
        return instance;
    }
    public boolean getset(Location x) {
        return unboom.contains(x);
    }
    public void addset(Location x) {
        unboom.add(x);
    }
    public void delset(Location x) {unboom.remove(x);}
    public void BreakBlock(Location x) {breaklist.add(x);}
    public void dammage(Damageable x,Double y) {dammagelist.put(x,y);}
    public void velocity(Entity x, Vector y) {velocitylist.put(x,y);}
}

