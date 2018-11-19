package src;

import java.io.IOException;
import java.util.List;
import java.util.Collection;
import java.util.LinkedList;

public class Main {
	
	public static void firstStep(List<BooleanImage> frames) {
		Collection<Coordinate> firstFrameTruePixel = frames.get(0).collectionOfTruePixel(),
							   motionlessPixel = new LinkedList<>();
		List<Collection<Coordinate>> otherFramesTruePixel = new LinkedList<>();
		for(int i=1; i<frames.size(); ++i) {
			otherFramesTruePixel.add(frames.get(i).collectionOfTruePixel());
		}
		for(Coordinate cPix : firstFrameTruePixel) {
			int i = 0;
			while(i<otherFramesTruePixel.size() && otherFramesTruePixel.get(i).contains(cPix)) {
				++i;
			}
			if(i == otherFramesTruePixel.size()) {
				motionlessPixel.add(cPix);
			}
		}
		frames.forEach((BooleanImage frame) -> frame.setCollectionPixel(motionlessPixel, false));
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		System.out.println("Source Directory = " + args[0]);
		System.out.println("Reading image from Directory...");
		List<BooleanImage> frames = BooleanImage.readFromDirectory(args[0]);
		System.out.println("Done.");
		System.out.println();
		System.out.println("First step : remove motionless edge.");
		System.out.println("Processing...");
		firstStep(frames);
		System.out.println("Done.");
		System.out.println();
		System.out.println("Second step : grouping edge.");
		System.out.println("Processing...");
		// TODO
		System.out.println("NOT IMPLEMENTED YET.");
		
		System.out.println("Done.");
		System.out.println();
		System.out.println("Third step : compute mapping.");
		System.out.println("Processing...");
		// TODO
		System.out.println("NOT IMPLEMENTED YET.");
		
		System.out.println("Done.");
		
	}

}
