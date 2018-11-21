package src;

import java.io.IOException;
import java.util.List;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Main {
	
	public static void firstStep(List<BooleanImage> frames) {
		Collection<Coordinate> firstFrameTruePixel = frames.get(0).getPixels(),
							   motionlessPixel = new LinkedList<>();
		List<Collection<Coordinate>> otherFramesTruePixel = new LinkedList<>();
		for(int i=1; i<frames.size(); ++i) {
			otherFramesTruePixel.add(frames.get(i).getPixels());
		}
		for(Coordinate cPix : firstFrameTruePixel) {
			int i = 0;
			while(i<otherFramesTruePixel.size() && contains(otherFramesTruePixel.get(i),cPix)) {
				++i;
			}
			if(i == otherFramesTruePixel.size()) {
				motionlessPixel.add(cPix);
			}
		}
		frames.forEach((BooleanImage frame) -> frame.getPixels().removeAll(motionlessPixel));
	}
	
	private static boolean contains(Collection<Coordinate> collection, Coordinate element) {
		for(Coordinate c : collection) {
			if(element.equals(c)) {
				return true;
			}
		}
		return false;
	}
	
	public static void secondStep(List<BooleanImage> frames) throws InterruptedException {
		/*
		Collection<Thread> threads = new LinkedList<>();
		for(BooleanImage frame : frames) {
			Thread t = new Thread(()->runDetermineGroup(frame));
			t.start();
			threads.add(t);
		}
		for(Thread t : threads) {
			t.join();
		}
		*/
		/*
			We use only the number of core for the number of thread running at the same time, to ensure
			that we will not overflow max thread memory.
		*/
		//*
		int cores = Runtime.getRuntime().availableProcessors(); // >=1 or the world gonna end
		int frames_done=0, threadId;
		Thread[] threads = new Thread[cores];
		while(frames_done<frames.size()) {
			threadId=0;
			while(threadId<cores && (frames_done+threadId)<frames.size()) {
				BooleanImage frame = frames.get(frames_done+threadId);
				threads[threadId] = new Thread(()->runDetermineGroup(frame));
				threads[threadId].start();
				System.out.println("	Begin processing of frame "+(frames_done+threadId));
				++threadId;
			}
			threadId=0;
			while(threadId<cores && (frames_done+threadId)<frames.size()){
				threads[threadId].join();
				System.out.println("	Done processing of frame "+(frames_done+threadId));
				++threadId;
			}
			frames_done += threadId;
		}
		//*/
	}
	
	private static void runDetermineGroup(BooleanImage frame) {
		Collection<Coordinate> pixels = frame.getPixels();
		DynamicGroup<Coordinate> groupBuilder = new DynamicGroup<>(pixels);
		
		for(Coordinate pixel : pixels) {
			for(Coordinate neighboor : pixel.getNeighboors(frame.getWidth(), frame.getHeight())) {
				if(pixels.contains(neighboor)) {
					groupBuilder.associate(pixel, neighboor);
				}
			}
		}
		
		frame.setGroupsAndRemovePixels(groupBuilder.getGroups());
	}
	
	
	
	
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
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
		secondStep(frames);
		System.out.println("Done.");
		System.out.println();
		System.out.println("Third step : compute mapping.");
		System.out.println("Processing...");
		// TODO
		System.out.println("NOT IMPLEMENTED YET.");
		
		System.out.println("Done.");
		
	}

}
