package me.oveln.tntup;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Map;

public class Breaker extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final ArrayList<Location> breaklist;
    private final Map<Damageable,Double> dammagelist;
    private final Map<Entity , Vector> velocitylist;

    public Breaker(JavaPlugin plugin , ArrayList<Location> breaklist , Map<Damageable,Double> dammagelist, Map<Entity, Vector> velocitylist) {
        this.plugin = plugin;
        this.breaklist = breaklist;
        this.dammagelist = dammagelist;
        this.velocitylist = velocitylist;
    }

    @Override
    public void run() {
//        System.out.println("Check");
//        System.out.println(breaklist.isEmpty());
//        System.out.println(dammagelist.isEmpty());

        Location location;
        World world;
        while (!breaklist.isEmpty()) {
            location = breaklist.get(0);
            if (location == null) return;
            world = location.getWorld();
            Block block = world.getBlockAt(location);
            if (plugin.getConfig().getBoolean("EnableBlockDrop") && block.getType()!=Material.AIR && block.getType()!=Material.TNT) {
                ItemStack itemstack = new ItemStack(block.getType());
                world.dropItem(location, itemstack);
            }
            block.setType(Material.AIR);
            breaklist.remove(0);
        }
        try {
            for (Damageable i : dammagelist.keySet()) {
                if (i == null) return;
                try {
                    Double dam = dammagelist.get(i);
                    dammagelist.remove(i);
                    i.damage(dam);
                } catch (ConcurrentModificationException e) {

                }
                if (dammagelist.isEmpty()) return;
            }
            for (Entity i:velocitylist.keySet()) {
                if (i == null) return;
                try {
                    org.bukkit.util.Vector v=velocitylist.get(i);
                    velocitylist.remove(i);
                    i.setVelocity(v);
                }catch(ConcurrentModificationException e) {

                }
            }
        }catch (ConcurrentModificationException e) {

        }
    }
}
