package com.carlos.plugins.tst.event;

import com.carlos.plugins.tst.WindChargePlugin;
import com.carlos.plugins.tst.config.WindConfig;
import com.carlos.plugins.tst.manager.WindChargeManager;
import com.carlos.plugins.tst.utils.CustomExplosion;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Listener para eventos relacionados a projéteis WindCharge.
 */
public class WindChargeListener implements Listener {

    private final WindChargeManager manager;
    private final Map<WindCharge, BukkitTask> taskMap = new HashMap<>();

    /**
     * Construtor para WindChargeListener.
     *
     * @param manager A instância do WindChargeManager para gerenciar as configurações do WindCharge.
     */
    public WindChargeListener(WindChargeManager manager) {
        this.manager = manager;
    }

    /**
     * Lida com o evento de lançamento de projétil para modificar o comportamento dos projéteis WindCharge.
     *
     * @param e O evento de lançamento de projétil.
     */
    @EventHandler
    public void onThrow(ProjectileLaunchEvent e) {
        Entity projectile = e.getEntity();
        if (projectile instanceof WindCharge windCharge) {
            WindConfig chargeConfig = manager.getWind();

            if (!chargeConfig.isEnabled()) return;

            if (chargeConfig.isVelocity()) {
                Vector currentVelocity = windCharge.getVelocity();
                double velocity = chargeConfig.getProjectVelocity();
                Vector reducedVelocity = currentVelocity.multiply(velocity);

                windCharge.setVelocity(reducedVelocity);

                if (chargeConfig.isParticle()) {
                    BukkitTask task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            Particle type = chargeConfig.getParticleType();
                            int amount = chargeConfig.getParticleAmount();
                            String colorHex = chargeConfig.getParticleColor();

                            Location loc = windCharge.getLocation();

                            if (type == Particle.DUST) {
                                java.awt.Color color = hexToColor(colorHex);
                                Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()), 1);

                                loc.getWorld().spawnParticle(type, loc, amount, 0, 0, 0, 0, dustOptions);
                            } else {
                                loc.getWorld().spawnParticle(type, loc, amount, 0, 0, 0, 1);
                            }
                        }
                    }.runTaskTimerAsynchronously(WindChargePlugin.getInstance(), 0, 1);

                    taskMap.put(windCharge, task);
                }
            }
        }
    }

    /**
     * Lida com o evento de explosão da entidade para cancelar a explosão dos projéteis WindCharge.
     *
     * @param e O evento de explosão da entidade.
     */
    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof WindCharge windCharge) {
            WindConfig chargeConfig = manager.getWind();

            if (!chargeConfig.isEnabled()) return;
            e.setCancelled(true);

            if (chargeConfig.isParticle()) {
                BukkitTask task = taskMap.remove(windCharge);
                if (task != null) {
                    task.cancel();
                }
            }
        }
    }

    /**
     * Lida com o evento de dano por entidade para cancelar o dano causado pelos projéteis WindCharge.
     *
     * @param e O evento de dano por entidade.
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof WindCharge) {
            WindConfig chargeConfig = manager.getWind();
            if (!chargeConfig.isEnabled()) return;
            e.setCancelled(true);
        }
    }

    /**
     * Lida com o evento de impacto do projétil para aplicar efeitos quando o projétil WindCharge atinge uma entidade ou o chão.
     *
     * @param event O evento de impacto do projétil.
     */
    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile instanceof WindCharge windCharge && projectile.getShooter() instanceof Player player) {
            if (!manager.getWind().isEnabled()) return;

            Location location = windCharge.getLocation();

            double power = manager.getWind().getExplosionPower();
            double radius = manager.getWind().getRadius();

            if (!manager.getWind().isExplosion()) return;
            CustomExplosion.windExplode(player, location, (float) power, radius, true);

            if (manager.getWind().isExplosionParticle()) {
                spawnParticles(location);
            }

            if (manager.getWind().isExplosionSound()) {
                playSound(location);
            }

            if (manager.getWind().isExplosionDamageable()) {
                damageArea(location);
            }
        }
    }

    /**
     * Converte uma cor hexadecimal para uma instância de {@link java.awt.Color}.
     *
     * @param hex A cor em formato hexadecimal.
     * @return A instância de {@link java.awt.Color}.
     */
    private java.awt.Color hexToColor(String hex) {
        return java.awt.Color.decode("#" + hex);
    }

    /**
     * Gera partículas na localização da explosão.
     *
     * @param location A localização onde as partículas serão geradas.
     */
    public void spawnParticles(Location location) {
        World world = location.getWorld();

        Particle particle = manager.getWind().getExplosionParticleType();
        int amount = manager.getWind().getExplosionParticleAmunt();
        double radius = manager.getWind().getRadius();

        for (int i = 0; i < amount; i++) {
            double theta = Math.random() * 2 * Math.PI;
            double phi = Math.acos(2 * Math.random() - 1);

            double offsetX = radius * Math.sin(phi) * Math.cos(theta);
            double offsetY = radius * Math.sin(phi) * Math.sin(theta);
            double offsetZ = radius * Math.cos(phi);

            Location particleLocation = location.clone().add(offsetX, offsetY, offsetZ);

            if (particle == Particle.DUST) {
                java.awt.Color color = hexToColor(manager.getWind().getExplosionParticleColor());
                Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()), 1);

                world.spawnParticle(particle, particleLocation, 1, dustOptions);
            } else {
                world.spawnParticle(particle, particleLocation, 1);
            }
        }
    }

    /**
     * Aplica dano a entidades dentro da área de efeito da explosão.
     *
     * @param location A localização da explosão.
     */
    public void damageArea(Location location) {
        double damage = manager.getWind().getExplosionDamage();
        double radius = manager.getWind().getRadius();
        Collection<LivingEntity> nearbyEntities = location.getNearbyLivingEntities(radius);

        for (LivingEntity entity : nearbyEntities) {
            if (entity instanceof Player player) {
                if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) continue;
            }

            entity.damage(damage);
        }
    }

    /**
     * Reproduz um som de explosão na localização especificada.
     *
     * @param location A localização onde o som será reproduzido.
     */
    public void playSound(Location location) {
        World world = location.getWorld();
        world.playSound(location, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1F, 1F);
    }
}
