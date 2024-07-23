package com.carlos.plugins.tst.command;

import com.carlos.plugins.tst.WindChargePlugin;
import com.carlos.plugins.tst.manager.WindChargeManager;
import com.carlos.plugins.tst.utils.CustomTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Classe que implementa o comando /wind, permitindo aos jogadores recarregar as configurações ou obter o item WindCharge.
 */
public class WindCommand implements CommandExecutor {

    /**
     * Método executado quando o comando /wind é chamado.
     *
     * @param sender  O remetente do comando.
     * @param command O comando que foi executado.
     * @param label   O alias do comando que foi usado.
     * @param args    Os argumentos do comando.
     * @return true se o comando foi processado corretamente, false caso contrário.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Verifica se o remetente do comando é um jogador
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Comando apenas para jogadores");
            return true;
        }

        WindChargeManager manager = WindChargePlugin.getInstance().getManager();

        if (label.equalsIgnoreCase("wind")) {

            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /wind <reload/get>");
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    player.sendMessage(CustomTag.INFO + "Recarregando as configurações...");
                    manager.reloadConfig();
                    player.sendMessage(CustomTag.SUCCESS + "Configurações recarregadas com sucesso!");
                    return true;
                }

                if (args[0].equalsIgnoreCase("get")) {
                    ItemStack item = manager.getWind().getItem();
                    player.getInventory().addItem(item);
                    player.sendMessage(CustomTag.SUCCESS + "Item adicionado em seu inventário com sucesso!");
                    return true;
                }
            }
        }

        return false;
    }
}
