package src;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
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
	private final String name;
	
	private BooleanImage(boolean[][] image, int width, int height, String name) {
		this.width = width;
		this.height = height;
		this.pixels = this.collectionOfTruePixel(image);
		this.groups = null;
		this.associationWithNextFrame = new HashMap<>(); // Current frame's groups => Next frame's groups
		this.name = name;
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
	
	public String getName() {
		return this.name;
	}
	
	public void setGroupsAndRemovePixels(List<Collection<Coordinate>> groups) {
		this.groups = groups;
		this.pixels = null;
	}
	
	public List<Collection<Coordinate>> getGroupCopy(){
		return new LinkedList<>(this.groups);
	}
	
	public void setAssociationGroup(int origin, int next) {
		if(this.getGroupCopy() != null)
			this.associationWithNextFrame.put(Integer.valueOf(origin), Integer.valueOf(next));
	}
	
	public int getIndexGroup(Collection<Coordinate> group) {
		int retour = this.groups.indexOf(group);
		assert(retour != -1);
		return retour;
	}
	
	public Map<Integer, Integer> getAssociationGroup() {
		return Collections.unmodifiableMap(this.associationWithNextFrame);
	}
	
	public void exportToFile(Map<Integer, RGB> associationGroupColor, String directory) throws IOException {
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		for(int x=0; x<this.width; ++x) {
			for(int y=0; y<this.width; ++y) {
				int group = this.pixelInGroup(new Coordinate(x,y));
				if(group == -1) {
					image.setRGB(x, y, RGB.Black.toInt());
				}else {
					image.setRGB(x, y, associationGroupColor.get(Integer.valueOf(group)).toInt());
				}
			}
		}
		// TODO
		ImageIO.write(image, "BMP", new File(directory+"/"+this.getName()));
	}
	
	/*
	 * Return -1 if not found.
	 */
	private int pixelInGroup(Coordinate pixel) {
		for(int i=0; i<this.groups.size(); ++i) {
			if(contains(this.groups.get(i), pixel)) {
				return i;
			}
		}
		return -1;
	}
	
	private boolean contains(Collection<Coordinate> collection, Coordinate element) {
		// Contains don't seem to work, so...
		for(Coordinate c : collection) {if(c.equals(element)) return true;}
		return false;
	}
	
	static public BooleanImage readFromBmpFile(String file) throws IOException {
		File image = new File(file);
		BufferedImage bmp = ImageIO.read(image);
		boolean[][] imageBoolean = new boolean[bmp.getWidth()][bmp.getHeight()];
		for(int w=0; w<bmp.getWidth(); ++w) {
			for(int h=0; h<bmp.getHeight(); ++h) {
				imageBoolean[w][h] = bmp.getRGB(w, h) == -1;
			}
		}
		return new BooleanImage(imageBoolean, bmp.getWidth(), bmp.getHeight(), image.getName());
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
