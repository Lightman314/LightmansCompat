package io.github.lightman314.lightmanscompat.core;

import io.github.lightman314.lightmanscompat.LCompat;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.Map;

public class LCompatRegistries {

    private static IEventBus modBus = null;
    private static final Map<ResourceLocation, DeferredRegister<?>> REGISTRIES = new HashMap<>();

    public static DeferredRegister<Item> ItemRegistry() { return getRegistry(ForgeRegistries.ITEMS); }
    public static DeferredRegister<Block> BlockRegistry() { return getRegistry(ForgeRegistries.BLOCKS); }
    public static DeferredRegister<BlockEntityType<?>> BlockEntityRegistry() { return getRegistry(ForgeRegistries.BLOCK_ENTITY_TYPES); }

    public static <T> DeferredRegister<T> getRegistry(IForgeRegistry<T> registry) { return getRegistry(registry.getRegistryKey()); }
    @SuppressWarnings("unchecked")
    public static <T> DeferredRegister<T> getRegistry(ResourceKey<? extends Registry<T>> key)
    {
        ResourceLocation id = key.location();
        if(!REGISTRIES.containsKey(id))
        {
            DeferredRegister<T> registry = DeferredRegister.create(key,LCompat.MODID);
            REGISTRIES.put(id,registry);
            if(modBus != null)
                registry.register(modBus);
        }
        try {
            return (DeferredRegister<T>)REGISTRIES.get(id);
        } catch (Exception e) { throw new RuntimeException(e); }

    }

    public static void init(IEventBus eventBus)
    {
        modBus = eventBus;
        for(DeferredRegister<?> registry : REGISTRIES.values())
            registry.register(modBus);
    }

}