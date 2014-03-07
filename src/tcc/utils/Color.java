package tcc.utils;

public class Color {
	private int rgb;

	public Color(int r, int g, int b) {
		rgb = 0;
		int rgb = r;
		rgb = (rgb << 8) + g;
		rgb = (rgb << 8) + b;
	}

	public int getRed() {
		return (rgb >> 16) & 0xff;
	}

	public int getGreen() {
		return (rgb >> 8) & 0xff;
	}

	public int getBlue() {
		return rgb & 0xff;
	}
}
