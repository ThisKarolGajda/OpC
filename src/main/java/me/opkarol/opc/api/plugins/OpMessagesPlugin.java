package me.opkarol.opc.api.plugins;

import me.opkarol.opc.api.file.ConfigurationMap;

public class OpMessagesPlugin extends OpPlugin implements SendMessages {
    private ConfigurationMap configurationMap;

    @Override
    public void onEnable() {
        configurationMap = new ConfigurationMap(getInstance(), "messages");
        if (!configurationMap.getConfiguration().createConfig()) {
            disablePlugin("Messages file created. Restart the server now, in order to allow plugin load messages.");
        }
        super.onEnable();
    }

    @Override
    public ConfigurationMap getConfigurationMap() {
        return configurationMap;
    }
}
