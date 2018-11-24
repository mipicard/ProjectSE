package src;

import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
		//*/
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
	
	public static void thirdStep(List<BooleanImage> frames) throws InterruptedException {
		/*
		Collection<Thread> threads = new LinkedList<>();
		for(int i=0; i<(frames.size()-1); ++i){
			BooleanImage frame1 = frames.get(i), frame2 = frames.get(i+1);
			Thread t = new Thread(()->runManhattanAssociation(frame1, frame2));
			t.start();
			threads.add(t);
		}
		for(Thread t : threads) {
			t.join();
		}
		//*/
		/*
			We use only the number of core for the number of thread running at the same time, to ensure
			that we will not overflow max thread memory.
		*/
		//*
		int cores = Runtime.getRuntime().availableProcessors(); // >=1 or the world gonna end
		int frames_done=0, threadId;
		Thread[] threads = new Thread[cores];
		while(frames_done<(frames.size()-1)) {
			threadId=0;
			while(threadId<cores && (frames_done+threadId)<(frames.size()-1)) {
				BooleanImage frame1 = frames.get(frames_done+threadId);
				BooleanImage frame2 = frames.get(frames_done+threadId+1);
				threads[threadId] = new Thread(()->runManhattanAssociation(frame1, frame2));
				System.out.println("	Begin processing of frame "+(frames_done+threadId)+" and frame "+(frames_done+threadId+1));
				threads[threadId].start();
				++threadId;
			}
			threadId=0;
			while(threadId<cores && (frames_done+threadId)<(frames.size()-1)){
				threads[threadId].join();
				System.out.println("	Done processing of frame "+(frames_done+threadId)+" and frame "+(frames_done+threadId+1));
				++threadId;
			}
			frames_done += threadId;
		}
		//*/
	}
	
	private static void runManhattanAssociation(BooleanImage frame1, BooleanImage frame2) {
		List<Collection<Coordinate>> groupFrame1 = frame1.getGroupCopy(),
									 groupFrame2 = frame2.getGroupCopy();
		int before_group1_size;
		int index_group1;
		do {
			before_group1_size = groupFrame1.size();
			index_group1=0;
			
			while(index_group1<groupFrame1.size() && 0<groupFrame2.size()) {
				List<Collection<Coordinate>> association = possibleGroupAssociationManhattan(groupFrame1.get(index_group1), groupFrame2);
				if(association.size()==1) {
					frame1.putAssociationGroup(frame1.getIndexGroup(groupFrame1.get(index_group1)), frame2.getIndexGroup(association.get(0)));
					groupFrame1.remove(index_group1);
					groupFrame2.remove(association.get(0));
				}else {
					++index_group1;
				}
			}
		}while( 0<groupFrame1.size() && 0<groupFrame2.size() && groupFrame1.size()<before_group1_size);
		while(0<groupFrame1.size() && 0<groupFrame2.size()) {
			List<Collection<Coordinate>> association = possibleGroupAssociationManhattan(groupFrame1.get(0), groupFrame2);
			frame1.setAssociationGroup(frame1.getIndexGroup(groupFrame1.get(0)), frame2.getIndexGroup(association.get(0)));
			groupFrame1.remove(0);
			groupFrame2.remove(association.get(0));
		}
	}
	
	private static List<Collection<Coordinate>> possibleGroupAssociationManhattan(Collection<Coordinate> groupSource, List<Collection<Coordinate>> possibleGroups){
		assert(!possibleGroups.isEmpty());
		
		List<Collection<Coordinate>> possible = new LinkedList<>();
		possible.add(possibleGroups.get(0));
		int minSumManhattan = groupManhattanComputing(groupSource, possibleGroups.get(0));
		int tmpSumManhattan;
		for(int i=1; i<possibleGroups.size(); ++i) {
			tmpSumManhattan = groupManhattanComputing(groupSource, possibleGroups.get(i));
			if (minSumManhattan == tmpSumManhattan) {
				possible.add(possibleGroups.get(i));
			} else if (minSumManhattan > tmpSumManhattan) {
				possible.clear();
				minSumManhattan = tmpSumManhattan;
				possible.add(possibleGroups.get(i));
			}
		}
		return possible;
	}
	
	private static int groupManhattanComputing(Collection<Coordinate> group1, Collection<Coordinate> group2) {
		int manhattanSum = 0;
		for(Coordinate pixel : group1) {
			manhattanSum += pixelWithGroupManhattanComputing(pixel, group2);
		}
		return manhattanSum;
	}
	
	
	private static int pixelWithGroupManhattanComputing(Coordinate pixel, Collection<Coordinate> group) {
		assert(!group.isEmpty());
		
		Iterator<Coordinate> itPix = group.iterator();
		int manhattanMin = pixel.manhattanDistance(itPix.next());
		int manhattanTmp = 0;
		while(itPix.hasNext()) {
			manhattanTmp = pixel.manhattanDistance(itPix.next());
			if(manhattanTmp < manhattanMin) {
				manhattanMin = manhattanTmp;
			}
		}
		return manhattanMin;
	}
	

	public static void export(List<BooleanImage> frames, String directory) throws IOException {
		(new File(directory)).mkdir();
		List<RGB> colors = RGB.staticColors;
		Map<Integer, RGB> assocColorBefore = new HashMap<>();
		Map<Integer, RGB> assocColorNext = assocColorBefore;
		for(int i=0, size=frames.get(0).getGroupCopy().size(); i<size; ++i) {
			assocColorNext.put(Integer.valueOf(i), colors.get(i%colors.size()));
		}
		frames.get(0).exportToFile(assocColorNext, directory);
		
		for(int frame=1; frame<frames.size(); ++frame) {
			assocColorBefore = assocColorNext;
			assocColorNext = new HashMap<>();
			Map<Integer, Integer> associationGroup = frames.get(frame-1).getAssociationGroup();
			for(Integer key : associationGroup.keySet()) {
				assocColorNext.put(associationGroup.get(key), assocColorBefore.get(key));
			}
			for(int i=0, size=frames.get(frame).getGroupCopy().size(); i<size; ++i) {
				if(!assocColorNext.containsKey(Integer.valueOf(i))) {
					if(assocColorNext.values().containsAll(colors)){
						assocColorNext.put(Integer.valueOf(i), colors.get(i%colors.size()));
					}else{
						assocColorNext.put(Integer.valueOf(i), getAvailableColor(assocColorNext.values()));
					}
				}
			}
			
			frames.get(frame).exportToFile(assocColorNext, directory);
		}
	}
	
	private static RGB getAvailableColor(Collection<RGB> colorsTaken) {
		for(RGB color : RGB.staticColors) {
			if(!colorsTaken.contains(color)) {
				return color;
			}
		}
		// Never happen in our code.
		return null;
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
		thirdStep(frames);
		System.out.println("Done.");
		System.out.println();
		System.out.println("Exporting frames...");
		export(frames, args[1]);
		System.out.println("Done.");
		
	}

}
