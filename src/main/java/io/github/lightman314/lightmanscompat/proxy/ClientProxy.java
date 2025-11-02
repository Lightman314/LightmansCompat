package io.github.lightman314.lightmanscompat.proxy;

import io.github.lightman314.lightmanscurrency.api.config.ConfigFile;
import io.github.lightman314.lightmanscurrency.api.config.client.screen.builtin.ConfigSelectionScreen;
import io.github.lightman314.lightmanscurrency.api.config.client.screen.options.ConfigFileOption;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.ArrayList;
import java.util.List;

public class ClientProxy extends CommonProxy {

    private final List<ConfigFile> configFiles = new ArrayList<>();

    @Override
    public void registerConfigFile(ConfigFile file) {
        configFiles.add(file);
    }

    @Override
    public void init(ModContainer container, IEventBus modBus) {
        if(!this.configFiles.isEmpty())
            container.registerExtensionPoint(IConfigScreenFactory.class, ConfigSelectionScreen.createFactory(this.configFiles.stream().map(ConfigFileOption::create).toList()));
    }
}
