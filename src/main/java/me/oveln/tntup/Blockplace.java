package me.oveln.tntup;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Blockplace implements Listener {

    @EventHandler
    public boolean onBlockplace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.getType() == Material.TNT && item.getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE)) {
            FileConfiguration config = main.getInstance().getConfig();
            int sec = config.getInt("TNTtime"+item.getItemMeta().getEnchantLevel(Enchantment.ARROW_DAMAGE));
            int pct = config.getInt("SpawnPointProtection");
            Location location = event.getBlock().getLocation() , loc;
            loc = location.clone().subtract(event.getBlock().getWorld().getSpawnLocation());

            if (Math.abs(loc.getBlockX())<pct && Math.abs(loc.getBlockZ())<pct) return true;
            main.getInstance().addset(location);
            Timer timer = new Timer();
            Player player = event.getPlayer();
            for (int i = 1;i<sec;i++) {
                timer.schedule(new shining_Task(main.getInstance(),location, config.getInt("Soundrange")),1000*i);
            }
            timer.schedule(new Boom_Task(main.getInstance(),
                                        location,
                                        item.getItemMeta().getEnchantLevel(Enchantment.ARROW_DAMAGE),
                                        config)
                    ,1000*sec);
            timer = null;
        }
        return true;
    }
    class shining_Task extends TimerTask {
        private final Location location;
        private final main ins;
        private final World world;
        private final int r;

        public shining_Task(main ins , Location location,int r) {
            this.location = location;
            this.ins = ins;
            this.world = location.getWorld();
            this.r = r;
        }

        @Override
        public void run() {
            if (ins.getset(location)) {
//                System.out.print("red");
                Collection<Entity> entities = world.getNearbyEntities(location,r,r,r);
                for (Entity i:entities) {
                    if (i instanceof Player) {
                        ((Player)i).playSound(location,Sound.ENTITY_CREEPER_HURT,1,1);
                    }
                }
                location.getWorld().spawnParticle(Particle.FLAME, location.clone().add(0.5, 0.5, 0.5), 50, 0, 0, 0, 0.1);
            }
        }

    }
    class Boom_Task extends TimerTask {
        private final Location location;
        private final main ins;
        private final int level;
        private final FileConfiguration config;

        public Boom_Task(main ins , Location location,int level,FileConfiguration config) {
            this.location = location;
            this.ins = ins;
            this.level = level;
            this.config = config;
        }

        @Override
        public void run() {
            if (ins.getset(location)) {//Boom~!
//                System.out.print("Boom");
                int r = level *3;
                double dam = config.getDouble("TNTDamage"+level);
                World world = location.getWorld();



                world.spawnParticle(Particle.CLOUD,location,300,0.5,0.5,0.5,0.9);
                world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE,1,0);

                Collection<Entity> entities = world.getNearbyEntities(location,r,r,r);
                for (Entity i:entities) {
                    Vector direction = i.getLocation().toVector().subtract(location.toVector());
                    main.getInstance().velocity(i,i.getVelocity().add(direction.normalize().multiply(3)));
                    if (i instanceof Damageable) main.getInstance().dammage((Damageable)(i),dam);
                }

                for (int i = -r;i<=r;i++)
                    for (int j=-r;j<=r;j++)
                        for (int k=-r;k<=r;k++) {
                            Location loc = location.clone().add(i,j,k);
                            if (loc.distance(location)<=r&&loc.getBlock().getType()!=Material.AIR) {
                                main.getInstance().BreakBlock(loc);
                            }
                        }
                main.getInstance().delset(location);
            }
        }
    }
}
