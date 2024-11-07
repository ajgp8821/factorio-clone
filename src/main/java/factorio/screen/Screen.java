package factorio.screen;

import asciiPanel.AsciiPanel;

public interface Screen {
    public void displayOutput (AsciiPanel terminal);

    public Screen update ();
}
