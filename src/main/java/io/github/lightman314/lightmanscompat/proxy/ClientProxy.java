package io.github.lightman314.lightmanscompat.proxy;

import com.google.common.base.Suppliers;
import io.github.lightman314.lightmanscurrency.api.config.ConfigFile;
import io.github.lightman314.lightmanscurrency.api.config.client.screen.builtin.ConfigSelectionScreen;
import io.github.lightman314.lightmanscurrency.api.config.client.screen.options.ConfigFileOption;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

import java.util.ArrayList;
import java.util.List;

public class ClientProxy extends CommonProxy {

    private final List<ConfigFile> configFiles = new ArrayList<>();

    @Override
    public void registerConfigFile(ConfigFile file) { this.configFiles.add(file); }

    @Override
    public void init() {
        if(!this.configFiles.isEmpty())
        {
            FMLModContainer container = FMLJavaModLoadingContext.get().getContainer();
            container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, Suppliers.memoize(() -> ConfigSelectionScreen.createFactory(container,this.configFiles.stream().map(ConfigFileOption::create).toList())));
        }
    }
}
