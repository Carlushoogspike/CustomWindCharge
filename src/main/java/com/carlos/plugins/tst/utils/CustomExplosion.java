package com.carlos.plugins.tst.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

/**
 * Classe utilitária para criar explosões personalizadas com efeitos de vento.
 */
public class CustomExplosion {

    /**
     * Método que aplica uma explosão de vento, afetando entidades dentro de um raio especificado.
     *
     * @param source        A entidade que causa a explosão, pode ser nula.
     * @param location      A localização da explosão.
     * @param power         A força da explosão.
     * @param radius        O raio de efeito da explosão.
     * @param sourceBounce  Se a entidade que causa a explosão deve ser afetada por ela.
     */
    public static void windExplode(@Nullable Player source, Location location, float power, double radius, boolean sourceBounce) {
        List<Entity> entities = new ArrayList<>(location.getWorld().getNearbyEntities(location, radius, radius, radius));

        for (Entity entity : entities) {
            if (entity instanceof Snowball) continue;
            if (entity instanceof Player && ((Player) entity).getGameMode() == GameMode.SPECTATOR) continue;

            Location entityLoc = entity.getLocation();
            boolean flag = source != null && source.getUniqueId().equals(entity.getUniqueId());

            if (flag && !sourceBounce) continue;

            double distance = entity.getLocation().distanceSquared(entityLoc) / power;
            double eyeHeight = entity instanceof LivingEntity ? ((LivingEntity) entity).getEyeHeight() : 1.53D;
            double dx = entityLoc.getX() - location.getX();
            double dy = entityLoc.getY() + eyeHeight - location.getY();
            double dz = entityLoc.getZ() - location.getZ();
            double dq = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dq != 0.0D) {
                dx /= dq;
                dy /= dq;
                dz /= dq;
                double face = (1.0D - distance) * power;
                Vector vec = new Vector(dx * face, dy * face, dz * face);

                entity.setVelocity(entity.getVelocity().add(vec).multiply(power));
            }
        }
    }
}
