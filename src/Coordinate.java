package src;

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
}
