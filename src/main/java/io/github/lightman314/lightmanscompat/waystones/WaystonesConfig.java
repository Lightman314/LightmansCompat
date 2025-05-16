package io.github.lightman314.lightmanscompat.waystones;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscurrency.api.config.ConfigFile;
import io.github.lightman314.lightmanscurrency.api.config.SyncedConfigFile;
import io.github.lightman314.lightmanscurrency.api.config.options.basic.DoubleOption;
import io.github.lightman314.lightmanscurrency.api.config.options.builtin.MoneyValueOption;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class WaystonesConfig extends SyncedConfigFile {

    public static void init() {
        CONFIG.confirmSetup();
    }

    public static final WaystonesConfig CONFIG = new WaystonesConfig();

    private WaystonesConfig() { super(LCompat.MODID + "_waystones",VersionUtil.modResource(LCompat.MODID,"waystones")); }

    public final MoneyValueOption baseWarpPrice = MoneyValueOption.create(MoneyValue::empty);
    public final MoneyValueOption distanceScaledPrice = MoneyValueOption.create(MoneyValue::empty);
    public final DoubleOption distancePerScaledPrice = DoubleOption.create(100,0,Double.MAX_VALUE);
    public final MoneyValueOption dimensionalWarpPrice = MoneyValueOption.create(MoneyValue::empty);
    public final DoubleOption warpPlatePriceScale = DoubleOption.create(0,0,Double.MAX_VALUE);
    public final DoubleOption globalCostScale = DoubleOption.create(0,0,Double.MAX_VALUE);

    public final MoneyValueOption minWarpCost = MoneyValueOption.create(MoneyValue::empty);
    public final MoneyValueOption maxWarpCost = MoneyValueOption.create(MoneyValue::empty);

    @Override
    protected void setup(ConfigBuilder builder) {
        builder.comment("The base cost to warp using a Waystone")
                .add("baseWarpCost",this.baseWarpPrice);

        builder.comment("The distance scaled cost to warp using a Waystone")
                .add("distanceScaledCost",this.distanceScaledPrice);

        builder.comment("The distance per distance scaled cost in meters")
                .add("distancePerScaledCost",this.distancePerScaledPrice);

        builder.comment("The cost to warp from one dimension to another using a Waystone")
                .add("crossDimensionWarpCost",this.dimensionalWarpPrice);

        builder.comment("The warp cost scale when warping using a Warp Plate")
                .add("warpPlaceCostScale",this.warpPlatePriceScale);

        builder.comment("The warp cost scale when warping to a Global Waystone")
                .add("globalWaystoneCostScale",this.globalCostScale);

        builder.comment("The minimum warp cost regardless of distance, etc.")
                .add("minWarpCost",this.minWarpCost);

        builder.comment("The maximum warp cost regardless of distance, etc.")
                .add("maxWarpCost",this.maxWarpCost);
    }

}
