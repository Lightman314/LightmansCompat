package io.github.lightman314.lightmanscompat;

import io.github.lightman314.lightmanscurrency.api.config.ConfigFile;
import io.github.lightman314.lightmanscurrency.api.config.options.basic.BooleanOption;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class NodeConfig extends ConfigFile {

    public static final NodeConfig INSTANCE = new NodeConfig();

    private NodeConfig() { super(VersionUtil.modResource(LCompat.MODID,"nodes"),"lcompat-nodes",LoadPhase.NULL); }

    public final BooleanOption waystonesNode = BooleanOption.createTrue();
    public final BooleanOption ftbchunksNode = BooleanOption.createTrue();

    @Override
    protected void setup(ConfigBuilder builder) {

        builder.comment("Whether the Waystones compatibility node should be loaded during the boot sequence")
                .add("waystones",this.waystonesNode);

        builder.comment("Whether the FTB Chunks claim shop node should be loaded during the boot sequence")
                .add("ftbchunks",this.ftbchunksNode);

    }

}