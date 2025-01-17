package factorio.screen;

import asciiPanel.AsciiPanel;
import factorio.Controls;
import factorio.GameState;
import factorio.InputBuffer;
import factorio.PressState;
import factorio.factory.FacData;
import factorio.subscreen.CraftingSubscreen;
import factorio.subscreen.PlayerInventorySubscreen;

public class CraftScreen implements Screen {

    private PlayerInventorySubscreen inventorySubscreen;
    private CraftingSubscreen craftingSubscreen;

    private ScreenState screenState;

    public CraftScreen (FacData adjacentCrafting) {
        int screenWidth = GameState.windowWidth;
        int screenHeight = GameState.windowHeight;

        inventorySubscreen = new PlayerInventorySubscreen(screenWidth / 2, screenHeight);
        craftingSubscreen = new CraftingSubscreen(screenWidth / 2, screenHeight, (screenWidth+1) / 2, 0, adjacentCrafting);

        screenState = ScreenState.LOOKING_AT_CRAFTING_PANEL;
        inventorySubscreen.setActive(false);
        craftingSubscreen.setActive(true);
    }

    enum ScreenState {
        LOOKING_AT_INVENTORY,
        LOOKING_AT_CRAFTING_PANEL
    }

    //
    // Input handling
    //

    public Screen update () {
        switch (screenState) {
            case LOOKING_AT_CRAFTING_PANEL:
                return craftingInput();

            case LOOKING_AT_INVENTORY:
                return inventoryInput();
        }

        return this;
    }

    private Screen craftingInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

            // Go to inventory subscreen
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            inventorySubscreen.setActive(true);
            craftingSubscreen.setActive(false);
            screenState = ScreenState.LOOKING_AT_INVENTORY;

            // Craft Something
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED)
            craftingSubscreen.craftSelection();

            // Scrolling
        else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            craftingSubscreen.selectionUp();
        else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            craftingSubscreen.selectionDown();

        return this;
    }

    private Screen inventoryInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

            // Go to inventory subscreen
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            craftingSubscreen.setActive(true);
            inventorySubscreen.setActive(false);
            screenState = ScreenState.LOOKING_AT_CRAFTING_PANEL;

            // Scrolling
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            inventorySubscreen.scrollUp();
        else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            inventorySubscreen.scrollDown();

        return this;
    }

    //
    // Display logic
    //

    public void displayOutput (AsciiPanel terminal) {
        inventorySubscreen.drawSubscreen(terminal);
        craftingSubscreen.drawSubscreen(terminal);
    }

}
