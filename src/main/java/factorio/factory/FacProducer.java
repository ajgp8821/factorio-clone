package factorio.factory;

import factorio.inventory.ItemIndex;

public interface FacProducer {
    public void tickUpdate ();
    public boolean canTakeProduct ();
    public ItemIndex takeProduct ();
}
