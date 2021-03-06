package com.gmail.gogobebe2.shiftkill;

import com.gmail.gogobebe2.shiftkill.enhanceditems.ApollosBowItem;
import com.gmail.gogobebe2.shiftkill.enhanceditems.ChestplateOfAchillesItem;
import com.gmail.gogobebe2.shiftkill.enhanceditems.PotionsOfAchillesItem;
import com.gmail.gogobebe2.shiftkill.enhanceditems.WarHammerItem;
import com.gmail.gogobebe2.shiftkill.kits.*;
import com.gmail.gogobebe2.shiftspawn.api.events.PlayerShiftKilledEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Listeners implements Listener {
    private Map<UUID, Integer> killCounts = new HashMap<>();

    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = null;
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    damager = (Player) arrow.getShooter();
                }
            }
            else if (event.getDamager() instanceof Player) {
                damager = (Player) event.getDamager();
            }

            if (damager != null) {
                if (new ApollosBowItem().equals(damager.getItemInHand())) {
                    ApollosBowItem.hit(damager, player);
                }
                else if (new WarHammerItem().equals(damager.getItemInHand())) {
                    WarHammerItem.damageArmour(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerShiftKill(PlayerShiftKilledEvent event) {
        Player killer = event.getKiller();
        UUID killerID = killer.getUniqueId();
        int kills;
        Kit kit;

        if (killCounts.containsKey(killerID)) {
            kills = killCounts.get(killerID) + 1;
        }
        else {
            kills = 1;
        }
        killCounts.put(killerID, kills);
        switch (kills) {
            case 3: kit = new Milestone3Kit(); break;
            case 5: kit = new Milestone5Kit(); break;
            case 7: kit = new Milestone7Kit(); break;
            case 10: kit = new Milestone10Kit(); break;
            case 15: kit = new Milestone15Kit(); break;
            case 20: kit = new Milestone20Kit(); break;
            case 25: kit = new Milestone25Kit(); break;
            case 30: kit = new Milestone30Kit(); break;
            case 40: kit = new Milestone40Kit(); break;
            case 50: kit = new WarHammerKit(); break;
            case 75: kit = new ApollosBowKit(); break;
            case 100: kit = new ArmourOfAchillesKit(); break;
            default: kit = null;
        }
        if (kit != null) kit.giveSet(killer);
    }

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (new ChestplateOfAchillesItem().equals(player.getInventory().getChestplate())) {
                if (ChestplateOfAchillesItem.shouldDodge(player)) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "Dodged!");
                }
            }

        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        Entity thrownPotion = event.getEntity();
        if (new PotionsOfAchillesItem().equals(event.getEntity().getItem())) {
            event.setCancelled(true);
            PotionsOfAchillesItem.splash(thrownPotion.getLocation());
        }
    }
}
