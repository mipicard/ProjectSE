package src;

import java.util.Collection;
import java.util.LinkedList;

public class Coordinate {
	public int x, y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null) return false;
		if(this==o) return true;
		if(!(o instanceof Coordinate)) return false;
		Coordinate c = (Coordinate)o;
		return this.x==c.x && this.y==c.y;
	}
	
	public Collection<Coordinate> getNeighboors(int width, int height){
		LinkedList<Coordinate> neighboors = new LinkedList<>();
		boolean x0 = (this.x==0),
				y0 = (this.y==0),
				xmax = ((this.x+1) == width),
				ymax = ((this.y+1) == height);
		
		// 1 | 2 | 3
		// - - - - -
		// 8 | X | 4
		// - - - - -
		// 7 | 6 | 5
		
		if(!y0 && !x0) neighboors.add(new Coordinate(this.x-1, this.y-1)); // 1
		if(!y0) neighboors.add(new Coordinate(this.x, this.y-1)); // 2
		if(!xmax && !y0) neighboors.add(new Coordinate(this.x+1, this.y-1)); // 3
		if(!xmax) neighboors.add(new Coordinate(this.x+1, this.y)); // 4
		if(!ymax && !xmax) neighboors.add(new Coordinate(this.x+1, this.y+1)); // 5
		if(!ymax) neighboors.add(new Coordinate(this.x, this.y+1)); // 6
		if(!x0 && !ymax) neighboors.add(new Coordinate(this.x-1, this.y+1)); // 7
		if(!x0) neighboors.add(new Coordinate(this.x-1, this.y)); // 8
		
		return neighboors;
	}
	
	public int manhattanDistance(Coordinate c) {
		return Math.abs(this.x - c.x) + Math.abs(this.y - c.y);
	}
}
