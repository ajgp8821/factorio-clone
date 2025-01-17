package factorio.factory;

import factorio.inventory.ItemIndex;
import factorio.world.Tile;

public class FactoryAutoMiningDrill implements FacData, FacProducer {

    private final ItemIndex PRODUCT;
    private final int TICKS_PER_TAKE;

    private int ticksSinceLastTake;

    public Tile getTile () { return Tile.AUTO_MINING_UPGRADE; }

    public FactoryAutoMiningDrill (ItemIndex product) {
        if (product == null)
            throw new NullPointerException("Product must be an actual item");

        this.PRODUCT = product;
        TICKS_PER_TAKE = 10;
    }

    public boolean canTakeProduct () {
        return ticksSinceLastTake >= TICKS_PER_TAKE;
    }

    public ItemIndex takeProduct () {
        ticksSinceLastTake = 0;
        return PRODUCT;
    }

    public void tickUpdate () {
        if (ticksSinceLastTake < TICKS_PER_TAKE)
            ticksSinceLastTake++;
    }
}
