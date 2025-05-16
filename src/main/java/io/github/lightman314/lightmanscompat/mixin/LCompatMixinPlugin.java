package io.github.lightman314.lightmanscompat.mixin;

import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class LCompatMixinPlugin implements IMixinConfigPlugin {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onLoad(String s) { }
    @Override
    public String getRefMapperConfig() { return null; }
    @Override
    public boolean shouldApplyMixin(String targetClass, String mixinClass) {
        try {
            String[] splits = mixinClass.split("\\.");
            String modid = splits[splits.length - 2];
            boolean enabled = FMLLoader.getLoadingModList().getMods().stream().anyMatch(mod -> mod.getModId().equals(modid));
            if(enabled)
                LOGGER.debug("Loaded {} as {} was loaded!", mixinClass, modid);
            else
                LOGGER.debug("Did not load {} as {} was not loaded!", mixinClass, modid);
            return enabled;
        } catch (Throwable e) { return false; }
    }
    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {}
    @Override
    public List<String> getMixins() { return null; }
    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) { }
    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) { }

}
