package com.carlos.plugins.tst.manager;

import com.carlos.plugins.tst.WindChargePlugin;
import com.carlos.plugins.tst.config.WindConfig;
import com.carlos.plugins.tst.utils.CustomLog;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Classe gerenciadora para o WindCharge, responsável por carregar e recarregar a configuração.
 */
@Getter
public class WindChargeManager {

    private WindConfig wind;
    private final WindChargePlugin plugin;

    /**
     * Construtor que inicializa o gerenciador com a instância do plugin e carrega a configuração inicial.
     *
     * @param plugin Instância do plugin WindChargePlugin.
     */
    public WindChargeManager(WindChargePlugin plugin) {
        this.plugin = plugin;
        loadConfig(plugin.getConfig());
    }

    /**
     * Carrega a configuração do arquivo de configuração fornecido.
     *
     * @param config O arquivo de configuração.
     */
    private void loadConfig(FileConfiguration config) {
        wind = new WindConfig(config);
    }

    /**
     * Recarrega a configuração do plugin.
     */
    public void reloadConfig() {
        if (plugin != null) {
            plugin.reloadConfig();
            loadConfig(plugin.getConfig());
            CustomLog.info("Configuração recarregada.");
        } else {
            throw new IllegalStateException("O plugin não foi inicializado.");
        }
    }
}
