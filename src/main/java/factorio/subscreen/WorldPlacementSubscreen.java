package factorio.subscreen;

/*
World Placement Subscreen

Used to place down a crafted item. It stores a Glyph[][] of
the world and then hovers a Glyph of the object above it.
*/

import asciiPanel.AsciiPanel;
import factorio.GameState;
import factorio.world.Tile;
import factorio.world.World;

import java.awt.*;

public class WorldPlacementSubscreen {

    private int width;
    private int height;
    private int xOff;
    private int yOff;

    private int xCord;
    private int yCord;
    private Tile placementTile;
    private Tile[][] worldSlice;
    private int radius;

    public WorldPlacementSubscreen (int width, int height, int xOff, int yOff) {
        this.width = width;
        this.height = height;

        this.xOff = xOff;
        this.yOff = yOff;

        xCord = width / 2;
        yCord = height / 2;
        radius = 3;

        updateLists();
    }

    /**
     * Sets xOff & yOff = 0 aka top-left corner
     */
    public WorldPlacementSubscreen (int width, int height) {
        this(width, height, 0, 0);
    }

    /**
     * Sets the tile to be placed
     */
    public void setActiveTile (Tile tile) {
        this.placementTile = tile;
    }

    //
    // Input Handling
    //

    /**
     * Returns true is successfully placed
     */
    public boolean placeItem () {
        World world = GameState.world;
        int worldX = xCord - (width / 2) + GameState.player.getX();
        int worldY = yCord - (height / 2) + GameState.player.getY();

        return world.placeCraftedTile(placementTile, worldX, worldY);
    }

    public void moveUp () {
        yCord--;
        int dY = yCord - (height / 2);

        if (dY < -radius || yCord < 0) yCord++;
    }

    public void moveDown () {
        yCord++;
        int dY = yCord - (height / 2);

        if (dY > radius || yCord >= height) yCord--;
    }

    public void moveLeft () {
        xCord--;
        int dX = xCord - (width / 2);

        if (dX < -radius || xCord < 0) xCord++;
    }

    public void moveRight () {
        xCord++;
        int dX = xCord - (width / 2);

        if (dX > radius || xCord >= width) xCord--;
    }

    //
    // Drawing Methods
    //

    public void drawSubscreen (AsciiPanel terminal) {
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                Tile t = worldSlice[x][y];
                terminal.write(t.getChar(), x + xOff, y + yOff, t.getColor());
            }
        }

        World world = GameState.world;
        int worldX = xCord - (width / 2) + GameState.player.getX();
        int worldY = yCord - (height / 2) + GameState.player.getY();
        Color displayColor = world.canPlaceCraftedTileAt(placementTile, worldX, worldY) ? Color.GREEN : Color.RED;
        terminal.write(placementTile.getChar(), xCord + xOff, yCord + yOff, displayColor);
    }

    // For now, there's no reason to actually track whether or not the subscreen is active
    // It's only necessary to update lists because of new player pos
    public void refresh () {
        updateLists();
        xCord = width / 2;
        yCord = height / 2;
    }

    private void updateLists () {
        World world = GameState.world;
        Tile[][] entireWorld = world.getWorld();
        worldSlice = new Tile[width][height];

        int startX = GameState.player.getX() - (width / 2);
        int startY = GameState.player.getY() - (height / 2);

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                int worldX = x+startX;
                int worldY = y+startY;

                if (worldX < 0 || worldX >= world.getWidth() ||
                        worldY < 0 || worldY >= world.getHeight())
                    worldSlice[x][y] = Tile.EMPTY;

                else
                    worldSlice[x][y] = entireWorld[worldX][worldY];
            }
        }
    }

}
