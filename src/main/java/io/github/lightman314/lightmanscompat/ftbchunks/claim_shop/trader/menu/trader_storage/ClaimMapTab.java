package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.trader_storage.ClaimMapClientTab;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ClaimMapTab extends TraderStorageTab {

    public ClaimMapTab(@Nonnull ITraderStorageMenu menu) { super(menu); }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public Object createClientTab(@Nonnull Object screen) { return new ClaimMapClientTab(screen,this); }

    @Override
    public boolean canOpen(Player player) { return this.menu.hasPermission(Permissions.EDIT_TRADES); }

    public boolean isNotLocked() {
        if(this.menu.getTrader() instanceof ClaimShopData shop)
            return !shop.isActive() && !shop.isLocked();
        return false;
    }

    public void DeselectChunk(ChunkPos chunk)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && shop.hasPermission(this.menu.getPlayer(),Permissions.EDIT_TRADES))
        {
            shop.removeChunkTarget(chunk);
            if(this.isClient())
                this.menu.SendMessage(this.builder().setFlag("RemoveChunk").setInt("ChunkX",chunk.x).setInt("ChunkZ",chunk.z));
        }
    }

    public void SelectChunk(ChunkPos chunk)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && shop.hasPermission(this.menu.getPlayer(),Permissions.EDIT_TRADES))
        {
            shop.addChunkTarget(chunk);
            if(this.isClient())
                this.menu.SendMessage(this.builder().setFlag("AddChunk").setInt("ChunkX",chunk.x).setInt("ChunkZ",chunk.z));
        }
    }

    public void TryQuickClaim()
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop)
        {
            if(shop.isActive() || shop.isLocked())
                return;

            if(this.isClient())
                this.menu.SendMessage(this.builder().setFlag("TryQuickClaim"));
            else //Only claim on the logical server
                shop.reclaimLand(shop.getOwner().getValidOwner().asPlayerReference(),null);
        }
    }

    @Override
    public void receiveMessage(LazyPacketData message) {
        if(message.contains("AddChunk") || message.contains("RemoveChunk"))
        {
            ChunkPos chunk = new ChunkPos(message.getInt("ChunkX"),message.getInt("ChunkZ"));
            if(message.contains("AddChunk"))
                this.SelectChunk(chunk);
            else
                this.DeselectChunk(chunk);
        }
        if(message.contains("TryQuickClaim"))
            this.TryQuickClaim();
    }

}