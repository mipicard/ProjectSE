package src;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RGB {
	private int red, green, blue;
	
	public RGB(int red, int green, int blue) {
		assert(0<=red && red<=255);
		assert(0<=green && green<=255);
		assert(0<=blue && blue<=255);
		
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public int toInt() {
		// https://stackoverflow.com/a/4801397
		int ret = this.red;
		ret = (ret << 8) + this.green;
		ret = (ret << 8) + this.blue;
		return ret;
	}

	static final public RGB Red = new RGB(255,0,0);
	static final public RGB Green = new RGB(0,255,0);
	static final public RGB Blue = new RGB(0,0,255);
	static final public RGB White = new RGB(255,255,255);
	static final public RGB Black = new RGB(0,0,0);
	static final public RGB Purple = new RGB(128,0,128);
	static final public RGB Magenta = new RGB(255,0,255);
	static final public RGB Indigo = new RGB(75,0,130);
	static final public RGB Cyan = new RGB(0,255,255);
	static final public RGB Chartreuse = new RGB(127,255,0);
	static final public RGB Brown = new RGB(165,42,42);
	static final public RGB Crimson = new RGB(220,20,60);
	static final public RGB Orange = new RGB(255,140,0);
	static final public RGB Gold = new RGB(255,215,0);
	static final public RGB Pink = new RGB(255,20,147);
	static final public RGB Yellow = new RGB(255,255,0);
	static final public RGB Salmon = new RGB(250,128,114);
	
	static final public List<RGB> staticColors = Collections.unmodifiableList(Arrays.asList(
			RGB.Red,
			RGB.Green,
			RGB.Blue,
			RGB.White,
			RGB.Purple,
			RGB.Magenta,
			RGB.Indigo,
			RGB.Cyan,
			RGB.Chartreuse,
			RGB.Brown,
			RGB.Crimson,
			RGB.Orange,
			RGB.Gold,
			RGB.Pink,
			RGB.Yellow,
			RGB.Salmon
			));
}
