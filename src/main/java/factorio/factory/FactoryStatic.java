package factorio.factory;

import factorio.world.Tile;

public class FactoryStatic implements FacData {

    private Tile tile;

    public FactoryStatic (Tile tile) {
        this.tile = tile;
    }

    public Tile getTile () {
        return tile;
    }

}
