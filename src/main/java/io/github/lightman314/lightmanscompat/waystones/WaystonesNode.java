package io.github.lightman314.lightmanscompat.waystones;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.WaystoneTypes;
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

    public static MoneyValue calculatePrice(Player player, IWaystone target)
    {
        return calculatePrice(target,new WarpDistanceCache(player,target));
    }
    public static MoneyValue calculatePrice(IWaystone target, WarpDistanceCache distanceData)
    {
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

        //Apply multipliers

        if(target.getWaystoneType().equals(WaystoneTypes.WARP_PLATE))
            price = price.multiplyValue(WaystonesConfig.CONFIG.warpPlatePriceScale.get());
        if(target.isGlobal())
            price = price.multiplyValue(WaystonesConfig.CONFIG.globalCostScale.get());

        //LOGGER.debug("Price after applying multipliers is {}", price.getString("Empty"));

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
        return price;
    }

}
