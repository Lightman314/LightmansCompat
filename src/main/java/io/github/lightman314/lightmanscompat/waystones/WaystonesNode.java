package io.github.lightman314.lightmanscompat.waystones;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.WarpMode;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaystonesNode {

    //public static final String WAYSTONES_MODID = "waystones";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void setup()
    {
        //Register teleport event listener
        MinecraftForge.EVENT_BUS.register(WaystonesEventListener.class);
        //Force instantiate the config files
        WaystonesConfig.init();
    }

    public static MoneyValue calculatePrice(Player player, IWaystone target, WarpMode warpMode, int leashed)
    {
        return calculatePrice(target,warpMode,leashed, new WarpDistanceCache(player,target));
    }
    public static MoneyValue calculatePrice(IWaystone target, WarpMode mode, int leashed, WarpDistanceCache distanceData)
    {
        //These modes are hard-coded as free, so I'll follow their lead
        if(mode == WarpMode.BOUND_SCROLL || mode == WarpMode.RETURN_SCROLL || mode == WarpMode.WARP_SCROLL)
            return MoneyValue.empty();

        MoneyValue price;
        if(target.getDimension().location().equals(distanceData.originalDimension))
        {
            price = WaystonesConfig.CONFIG.baseWarpPrice.get();
            //LOGGER.debug("Same dimension warp detected. Base warp price is {}", price.getString("Empty"));
            //Calculate distance scaled price
            MoneyValue distancePrice = WaystonesConfig.CONFIG.distanceScaledPrice.get();
            double distancePerPrice = WaystonesConfig.CONFIG.distancePerScaledPrice.get();
            if(distancePerPrice > 0 && !distancePrice.isEmpty())
            {
                double mult = distanceData.distance / distancePerPrice;
                MoneyValue addPrice = distancePrice.multiplyValue(mult);
                if(WaystonesConfig.CONFIG.invertMoneyCost.get())
                {
                    //Subtract from max price
                    addPrice = WaystonesConfig.CONFIG.maxWarpCost.get().subtractValue(addPrice);
                    if(addPrice == null)
                        addPrice = MoneyValue.empty();
                }
                if(price.isEmpty())
                    price = addPrice;
                else
                    price = price.addValue(addPrice);
                //LOGGER.debug("Distance scaled warp is valid. New price to warp {}m ({} * {} multiplier) is {} ({} was added)", distanceData.distance, distancePrice.getString("Empty"), mult, price.getString("Empty"), addPrice.getString("Empty"));
            }
        }
        else
        {
            price = WaystonesConfig.CONFIG.dimensionalWarpPrice.get();
            //LOGGER.debug("Cross dimension warp detected. Warp price is {}", price.getString("Empty"));
        }


        if(leashed > 0)
        {
            MoneyValue leashedPrice = WaystonesConfig.CONFIG.leashedWarpPrice.get();
            if(!leashedPrice.isEmpty())
            {
                leashedPrice = leashedPrice.multiplyValue(leashed);
                if(price.isEmpty())
                    price = leashedPrice;
                else
                    price = price.addValue(leashedPrice);
                LOGGER.debug("{} leashed animals detected. Price is now {}",leashed,price.getString("Empty"));
            }

        }

        //Apply limits
        MoneyValue minCost = WaystonesConfig.CONFIG.minWarpCost.get();
        MoneyValue maxCost = WaystonesConfig.CONFIG.maxWarpCost.get();
        //Invert the min/max if they are configured backwards
        if(minCost.sameType(maxCost) && minCost.getCoreValue() > maxCost.getCoreValue())
        {
            MoneyValue temp = minCost;
            minCost = maxCost;
            maxCost = temp;
        }
        if(minCost.sameType(price) && minCost.getCoreValue() > price.getCoreValue())
            price = minCost;
        if(maxCost.sameType(price) && !maxCost.isEmpty() && maxCost.getCoreValue() < price.getCoreValue())
            price = maxCost;

        //LOGGER.debug("Price after applying limits is {}", price.getString("Empty"));

        //Apply multipliers
        if(mode == WarpMode.INVENTORY_BUTTON)
            price = price.multiplyValue(WaystonesConfig.CONFIG.inventoryButtonCostScale.get());
        if(mode == WarpMode.WARP_STONE)
            price = price.multiplyValue(WaystonesConfig.CONFIG.warpStonePriceScale.get());
        if(mode == WarpMode.WAYSTONE_TO_WAYSTONE)
            price = price.multiplyValue(WaystonesConfig.CONFIG.waystonePriceScale.get());
        if(mode == WarpMode.SHARESTONE_TO_SHARESTONE)
            price = price.multiplyValue(WaystonesConfig.CONFIG.sharestonePriceScale.get());
        if(mode == WarpMode.PORTSTONE_TO_WAYSTONE)
            price = price.multiplyValue(WaystonesConfig.CONFIG.portstonePriceScale.get());
        if(mode == WarpMode.WARP_PLATE)
            price = price.multiplyValue(WaystonesConfig.CONFIG.warpPlatePriceScale.get());
        if(target.isGlobal())
            price = price.multiplyValue(WaystonesConfig.CONFIG.globalCostScale.get());

        //LOGGER.debug("Price after applying multipliers is {}", price.getString("Empty"));

        return price;
    }

}
