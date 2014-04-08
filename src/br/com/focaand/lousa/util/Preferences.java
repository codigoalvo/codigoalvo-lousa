package br.com.focaand.lousa.util;

public class Preferences {
   
    private static Preferences instance = null;

    private int maxResolution;
    private boolean showButtons;

    private Preferences() {
	this.setMaxResolution(800);
	this.setShowButtons(true);
    }

    public static Preferences getInstance() {
	if (instance == null)
	    instance = new Preferences();
	return instance;
    }

    public int getMaxResolution() {
	return maxResolution;
    }

    public void setMaxResolution(int maxResolution) {
	this.maxResolution = maxResolution;
    }

    public boolean getShowButtons() {
	return showButtons;
    }

    public void setShowButtons(boolean showButtons) {
	this.showButtons = showButtons;
    }
   
}
