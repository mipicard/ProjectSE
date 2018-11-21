package src;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Wrapper class for reading an image of boolean, with some convenience method.
 * @author mpicard
 */
public class BooleanImage {
	private int width, height;
	private Collection<Coordinate> pixels;
	private List<Collection<Coordinate>> groups;
	private Map<Integer, Integer> associationWithNextFrame;
	
	private BooleanImage(boolean[][] image, int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = this.collectionOfTruePixel(image);
		this.groups = null;
		this.associationWithNextFrame = new HashMap<>();
	}
	
	private Collection<Coordinate> collectionOfTruePixel(boolean[][] image){
		Collection<Coordinate> retour = new LinkedList<>();
		for(int x=0; x<this.width; ++x) {
			for(int y=0; y<this.height; ++y) {
				if(image[x][y]) {
					retour.add(new Coordinate(x, y));
				}
			}
		}
		return retour;
	}
	
	public Collection<Coordinate> getPixels(){
		return this.pixels;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setGroupsAndRemovePixels(List<Collection<Coordinate>> groups) {
		this.groups = groups;
		this.pixels = null;
	}
	
	public List<Collection<Coordinate>> getGroup(){
		return this.groups;
	}
	
	public void setAssociationGroup(int origin, int next) {
		if(this.getGroup() != null)
			this.associationWithNextFrame.put(Integer.valueOf(origin), Integer.valueOf(next));
	}
	
	
	
	
	
	
	
	
	static public BooleanImage readFromBmpFile(String file) throws IOException {
		BufferedImage bmp = ImageIO.read(new File(file));
		boolean[][] imageBoolean = new boolean[bmp.getWidth()][bmp.getHeight()];
		for(int w=0; w<bmp.getWidth(); ++w) {
			for(int h=0; h<bmp.getHeight(); ++h) {
				imageBoolean[w][h] = bmp.getRGB(w, h) == -1;
			}
		}
		return new BooleanImage(imageBoolean, bmp.getWidth(), bmp.getHeight());
	}
	
	static public List<BooleanImage> readFromDirectory(String directory) throws IOException{
		// Get all files, in order.
		List<String> files_path = new LinkedList<>();
		for(File f: (new File(directory)).listFiles()) {
			if (f.isFile()) {
				files_path.add(f.getPath());
			}
		}
		files_path.sort(Comparator.comparing((String x) -> x));
		
		// Read all files
		List<BooleanImage> retour = new LinkedList<>();
		for(String path : files_path) {
			retour.add(readFromBmpFile(path));
		}
		return retour;
	}
}
