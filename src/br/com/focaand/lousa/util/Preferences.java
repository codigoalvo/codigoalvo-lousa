package br.com.focaand.lousa.util;

public class Preferences {
   
    private static Preferences instance = null;

    private int maxResolution;
    private int contraste;
    private boolean resultadoPretoBranco;
    private boolean showButtons;

    private Preferences() {
	this.setMaxResolution(800);
	this.setContraste(50);
	this.setResultadoPretoBranco(true);
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

    public int getContraste() {
	return contraste;
    }

    public void setContraste(int contraste) {
	this.contraste = contraste;
    }

    public boolean getResultadoPretoBranco() {
	return resultadoPretoBranco;
    }

    public void setResultadoPretoBranco(boolean resultadoPretoBranco) {
	this.resultadoPretoBranco = resultadoPretoBranco;
    }
   
}
