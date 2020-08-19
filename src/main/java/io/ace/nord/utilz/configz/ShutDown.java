package io.ace.nord.utilz.configz;

import io.ace.nord.NordClient;

public class ShutDown extends Thread {
    @Override
    public void run(){
        saveConfig();
    }

    public static void saveConfig(){
        NordClient.INSTANCE.configUtils.saveMods();
        NordClient.INSTANCE.configUtils.saveBinds();
        NordClient.INSTANCE.configUtils.saveDrawn();
        NordClient.INSTANCE.configUtils.savePrefix();

    }
}