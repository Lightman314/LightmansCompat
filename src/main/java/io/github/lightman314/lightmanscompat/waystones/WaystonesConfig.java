package io.github.lightman314.lightmanscompat.waystones;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscurrency.api.config.SyncedConfigFile;
import io.github.lightman314.lightmanscurrency.api.config.options.basic.BooleanOption;
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

    public final BooleanOption invertMoneyCost = BooleanOption.createFalse();
    public final MoneyValueOption baseWarpPrice = MoneyValueOption.create(MoneyValue::empty);
    public final MoneyValueOption distanceScaledPrice = MoneyValueOption.create(MoneyValue::empty);
    public final DoubleOption distancePerScaledPrice = DoubleOption.create(100,0,Double.MAX_VALUE);
    public final MoneyValueOption leashedWarpPrice = MoneyValueOption.create(MoneyValue::empty);
    public final MoneyValueOption dimensionalWarpPrice = MoneyValueOption.create(MoneyValue::empty);

    public final DoubleOption globalCostScale = DoubleOption.create(0,0,Double.MAX_VALUE);
    public final DoubleOption warpStonePriceScale = DoubleOption.create(0,0,Double.MAX_VALUE);
    public final DoubleOption waystonePriceScale = DoubleOption.create(1,0,Double.MAX_VALUE);
    public final DoubleOption sharestonePriceScale = DoubleOption.create(0,0,Double.MAX_VALUE);
    public final DoubleOption portstonePriceScale = DoubleOption.create(0,0,Double.MAX_VALUE);
    public final DoubleOption warpPlatePriceScale = DoubleOption.create(0,0,Double.MAX_VALUE);
    public final DoubleOption inventoryButtonCostScale = DoubleOption.create(0,0,Double.MAX_VALUE);

    public final MoneyValueOption minWarpCost = MoneyValueOption.create(MoneyValue::empty);
    public final MoneyValueOption maxWarpCost = MoneyValueOption.create(MoneyValue::empty);

    @Override
    protected void setup(ConfigBuilder builder) {

        builder.comment("Set to true if the warp cost should be inverted, meaning the shorter the distance, the more expensive",
                        "This will be done by calculating the price normally and subtracting it from the max warp cost defined below")
                .add("invertWarpCost",this.invertMoneyCost);

        builder.comment("The base cost to warp using a Waystone")
                .add("baseWarpCost",this.baseWarpPrice);

        builder.comment("The distance scaled cost to warp using a Waystone")
                .add("distanceScaledCost",this.distanceScaledPrice);

        builder.comment("The distance per distance scaled cost in meters")
                .add("distancePerScaledCost",this.distancePerScaledPrice);

        builder.comment("How much is costs per leashed animal traveling with you")
                .add("warpCostPerLeashed",this.leashedWarpPrice);

        builder.comment("The cost to warp from one dimension to another using a Waystone")
                .add("crossDimensionWarpCost",this.dimensionalWarpPrice);

        builder.comment("The warp cost scale when warping to a Global Waystone")
                .add("globalWaystoneCostScale",this.globalCostScale);

        builder.comment("The warp cost scale when warping using a Warp Stone item")
                .add("warpStoneCostScale",this.warpStonePriceScale);

        builder.comment("The warp cost scale when warping from one waystone to another")
                .add("waystoneCostScale",this.waystonePriceScale);

        builder.comment("The warp cost scale when warping from one sharestone to another")
                .add("sharestoneCostScale",this.sharestonePriceScale);

        builder.comment("The warp cost scale when warping from a portstone")
                .add("portstoneCostScale",this.portstonePriceScale);

        builder.comment("The warp cost scale when warping using a Warp Plate")
                .add("warpPlateCostScale",this.warpPlatePriceScale);

        builder.comment("The warp cost scale when warping via the inventory button")
                .add("inventoryButtonCostScale",this.inventoryButtonCostScale);

        builder.comment("The minimum warp cost regardless of distance, etc. (may be subceeded by multipliers defined below)")
                .add("minWarpCost",this.minWarpCost);

        builder.comment("The maximum warp cost regardless of distance, etc. (may be exceeded by multipliers defined below)")
                .add("maxWarpCost",this.maxWarpCost);
    }

}
