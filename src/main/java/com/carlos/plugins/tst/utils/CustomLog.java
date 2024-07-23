package com.carlos.plugins.tst.utils;

import org.bukkit.Bukkit;

/**
 * Classe utilitária para criar mensagens personalizaveis
 */
public class CustomLog {

    /**
     * Mensagem de inforamção
     * @param msg Mensagem a ser enviada
     */
    public static void info(String msg) {
        msg("§a[WindItem] §2" + msg);
    }

    /**
     * Mensagem de aviso
     * @param msg Mensagem a ser enviada
     */
    public static void warn(String msg){
        msg("§4[WindItem] §c" + msg);
    }

    /**
     * Mensagem de alerta
     * @param msg Mensagem a ser enviada
     */
    public static void alert(String msg){
        msg("§e[WindItem] §e" + msg);
    }

    /**
     * Metodo responsavel por enviar a mensagem
     * @param msg Mensagem de entrada
     */
    private static void msg(String msg){
        Bukkit.getConsoleSender().sendMessage(msg);
    }

}
