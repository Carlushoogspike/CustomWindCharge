package com.carlos.plugins.tst.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe de configuração para o item WindCharge.
 */
@Log4j2
@Data
public class WindConfig {

    private boolean enabled;
    private String customName;
    private List<String> lore;
    private boolean explosion;
    private double explosionPower;
    private boolean explosionDamageable;
    private double explosionDamage;
    private boolean explosionParticle;
    private Particle explosionParticleType;
    private String explosionParticleColor;
    private int explosionParticleAmunt;
    private boolean explosionSound;
    private double radius;
    private boolean velocity;
    private double projectVelocity;
    private boolean particle;
    private Particle particleType;
    private String particleColor;
    private int particleAmount;

    /**
     * Construtor que carrega a configuração do arquivo de configuração.
     *
     * @param config O arquivo de configuração.
     */
    public WindConfig(FileConfiguration config) {
        enabled = config.getBoolean("enabled");

        customName = config.getString("custom.name");
        lore = config.getStringList("custom.lore");

        explosion = config.getBoolean("explosion.enabled");
        if (explosion) {
            explosionPower = config.getDouble("explosion.power");

            explosionParticle = config.getBoolean("explosion.particle.enabled");
            explosionParticleType = translate(config.getString("explosion.particle.key"));
            explosionParticleColor = config.getString("explosion.particle.color");
            explosionParticleAmunt = config.getInt("explosion.particle.amount");

            explosionDamageable = config.getBoolean("explosion.damage.enabled");
            explosionDamage = config.getDouble("explosion.damage.damage");

            radius = config.getDouble("explosion.radius");

            explosionSound = config.getBoolean("explosion.sound");
        }

        velocity = config.getBoolean("velocity.enabled");
        if (velocity) {
            projectVelocity = config.getDouble("velocity.velocity");
        }

        particle = config.getBoolean("particle.enabled");
        if (particle) {
            particleType = translate(config.getString("particle.key"));
            particleColor = config.getString("particle.color");
            particleAmount = config.getInt("particle.amount");
        }
    }

    /**
     * Cria um ItemStack com as propriedades configuradas do WindCharge.
     *
     * @return O ItemStack configurado.
     */
    public ItemStack getItem() {
        ItemStack stack = new ItemStack(Material.WIND_CHARGE);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(getCustomName().replaceAll("&", "§")));
        meta.lore(loreComponent());
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Converte a lore configurada para uma lista de Componentes.
     *
     * @return A lista de Componentes da lore.
     */
    private List<Component> loreComponent() {
        return lore.stream()
                .map(lore -> Component.text(lore.replace("&", "§")))
                .collect(Collectors.toList());
    }

    /**
     * Traduz uma string para um valor de Particle correspondente.
     *
     * @param name O nome da partícula em string.
     * @return A partícula correspondente ou null se não for encontrada.
     */
    private Particle translate(String name) {
        return Arrays.stream(Particle.values())
                .filter(a -> a.name().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);
    }
}
