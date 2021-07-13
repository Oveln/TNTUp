package me.oveln.tntup;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Date;

public class Blockbreak implements Listener {

    @EventHandler
    public boolean onBlockbreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.TNT) {
            Location block = event.getBlock().getLocation();
            World world = block.getWorld();
//            Player pl =event.getPlayer();
            if (main.getInstance().getset(block)) {
//                Firework fw = (Firework) block.getWorld().spawnEntity(block.clone().add(0.5,-0.1,0.5), EntityType.FIREWORK);
//                FireworkMeta fwm = fw.getFireworkMeta();
//                fwm.addEffect(FireworkEffect.builder().withColor(Color.GREEN).flicker(true).build());
//                fwm.setPower(0);
//                fw.setFireworkMeta(fwm);
                String cmd = "summon minecraft:fireworks_rocket "+block.getBlockX()+" "+(block.getBlockY()+1)+" "+block.getBlockZ()+" {FireworksItem:{tag:{Fireworks:{Flight:1,Explosions:[{Type:0,Colors:[I;65289],FadeColors:[I;8769280]}]}},id:\"minecraft:fireworks\",Count:1},Life:1,LifeTime:0}";
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd );
                main.getInstance().delset(block);
                event.setDropItems(false);
            }
        }
        return true;
    }
}
