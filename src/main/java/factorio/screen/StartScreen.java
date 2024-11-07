package factorio.screen;

import asciiPanel.AsciiPanel;
import factorio.Controls;
import factorio.GameState;
import factorio.PressState;
import factorio.subscreen.GridSubscreen;

import java.util.ArrayList;

public class StartScreen implements Screen {

    private ArrayList<String> options;

    private final int titleTextY;
    private final int titleTextX;

    GridSubscreen optionsSubscreen;

    public StartScreen() {
        options = new ArrayList<String>();
        options.add("Play Game");
        options.add("Quit");

        titleTextX = 25;
        titleTextY = 8;

        final int TITLE_TEXT_HEIGHT = 8 + 8; // 8 = height of text, 4 = 2 padding on top & bottom
        int xOff = GameState.windowWidth / 2 - 5;

        optionsSubscreen = new GridSubscreen(GameState.windowWidth - xOff, GameState.windowHeight - TITLE_TEXT_HEIGHT, xOff, TITLE_TEXT_HEIGHT + 7);

        optionsSubscreen.setColumns(1);
        optionsSubscreen.setOptions(options);
        optionsSubscreen.setActive(true);
        optionsSubscreen.setPad(2);
        optionsSubscreen.refresh();

    }

    public void displayOutput(AsciiPanel terminal) {
        // Escape sequences screw up some of the lines in the the terminal.write(...) part, so here is the string in comment form
        /*
             /$$$$$$$$                   /$$                         /$$
            | $$_____/                  | $$                        |__/
            | $$    /$$$$$$   /$$$$$$$ /$$$$$$    /$$$$$$   /$$$$$$  /$$  /$$$$$$
            | $$$$$|____  $$ /$$_____/|_  $$_/   /$$__  $$ /$$__  $$| $$ /$$__  $$
            | $$__/ /$$$$$$$| $$        | $$    | $$  \ $$| $$  \__/| $$| $$  \ $$
            | $$   /$$__  $$| $$        | $$ /$$| $$  | $$| $$      | $$| $$  | $$
            | $$  |  $$$$$$$|  $$$$$$$  |  $$$$/|  $$$$$$/| $$      | $$|  $$$$$$/
            |__/   \_______/ \_______/   \___/   \______/ |__/      |__/ \______/
        */
        terminal.write(" /$$$$$$$$                   /$$                         /$$          ", titleTextX, titleTextY);
        terminal.write("| $$_____/                  | $$                        |__/          ", titleTextX, titleTextY + 1);
        terminal.write("| $$    /$$$$$$   /$$$$$$$ /$$$$$$    /$$$$$$   /$$$$$$  /$$  /$$$$$$ ", titleTextX, titleTextY + 2);
        terminal.write("| $$$$$|____  $$ /$$_____/|_  $$_/   /$$__  $$ /$$__  $$| $$ /$$__  $$", titleTextX, titleTextY + 3);
        terminal.write("| $$__/ /$$$$$$$| $$        | $$    | $$  \\ $$| $$  \\__/| $$| $$  \\ $$", titleTextX, titleTextY + 4);
        terminal.write("| $$   /$$__  $$| $$        | $$ /$$| $$  | $$| $$      | $$| $$  | $$", titleTextX, titleTextY + 5);
        terminal.write("| $$  |  $$$$$$$|  $$$$$$$  |  $$$$/|  $$$$$$/| $$      | $$|  $$$$$$/", titleTextX, titleTextY + 6);
        terminal.write("|__/   \\_______/ \\_______/   \\___/   \\______/ |__/      |__/ \\______/ ", titleTextX, titleTextY + 7);

        optionsSubscreen.drawSubscreen(terminal);
    }

    public Screen update () {
        // Current option selected
        if (GameState.inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            switch (optionsSubscreen.getSelectedIndex()) {
                // "Play Game"
                case 0:
                    return new WorldScreen();
                // "Quit"
                case 1:
                    System.exit(0);
            }

        } else if (GameState.inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            optionsSubscreen.moveUp();
        else if (GameState.inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            optionsSubscreen.moveDown();

        return this;
    }
}
