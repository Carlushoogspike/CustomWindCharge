package com.carlos.plugins.tst;

import com.carlos.plugins.tst.command.WindCommand;
import com.carlos.plugins.tst.event.WindChargeListener;
import com.carlos.plugins.tst.manager.WindChargeManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Classe principal do plugin WindCharge.
 * Gerencia a inicialização e registro de comandos e eventos do plugin.
 */
@Getter
public class WindChargePlugin extends JavaPlugin {

    @Getter
    private static WindChargePlugin instance;

    private WindChargeManager manager;

    /**
     * Método chamado quando o plugin está carregando.
     * Salva a configuração padrão do plugin.
     */
    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    /**
     * Método chamado quando o plugin é ativado.
     * Inicializa as instâncias, registra eventos e comandos.
     */
    @Override
    public void onEnable() {
        instance = this;

        // Inicializa o gerenciador de WindCharge
        this.manager = new WindChargeManager(this);

        // Registra o listener de eventos do WindCharge
        getServer().getPluginManager().registerEvents(new WindChargeListener(manager), this);

        // Define o executor do comando "wind"
        getCommand("wind").setExecutor(new WindCommand());
    }
}
