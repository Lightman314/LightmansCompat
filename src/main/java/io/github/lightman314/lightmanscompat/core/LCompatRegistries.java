package io.github.lightman314.lightmanscompat.core;

import io.github.lightman314.lightmanscompat.LCompat;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class LCompatRegistries {

    private static IEventBus modBus = null;
    private static final Map<ResourceLocation,DeferredRegister<?>> REGISTRIES = new HashMap<>();

    public static DeferredRegister<Item> ItemRegistry() { return getRegistry(BuiltInRegistries.ITEM); }
    public static DeferredRegister<Block> BlockRegistry() { return getRegistry(BuiltInRegistries.BLOCK); }
    public static DeferredRegister<BlockEntityType<?>> BlockEntityRegistry() { return getRegistry(BuiltInRegistries.BLOCK_ENTITY_TYPE); }

    public static <T> DeferredRegister<T> getRegistry(Registry<T> registry) { return getRegistry(registry.key()); }
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
