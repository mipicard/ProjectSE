package src;

import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;

public class DynamicGroup<T>{
	List<Collection<T>> groups;
	
	public DynamicGroup(Collection<T> elements) {
		this.groups = new LinkedList<>();
		for(T e : elements) {
			Collection<T> col = new HashSet<>();
			col.add(e);
			this.groups.add(col);
		}
	}
	
	public List<Collection<T>> getGroups(){
		return this.groups;
	}
	
	public void associate(T element1, T element2) {
		int index_1 = this.getIndex(element1),
			index_2 = this.getIndex(element2);
		if (index_1 > index_2) {
			Collection<T> newgroup = this.groups.remove(index_1);
			// addAll not implemented for set...
			for(T e : this.groups.remove(index_2)) {
				 newgroup.add(e);
			}
			this.groups.add(newgroup);
		} else if (index_1 < index_2) {
			Collection<T> newgroup = this.groups.remove(index_2);
			// addAll not implemented for set...
			for(T e : this.groups.remove(index_1)) {
				 newgroup.add(e);
			}
			this.groups.add(newgroup);
		}
		// We don't change group if 2 T are from the same group.
	}
	
	private int getIndex(T element) {
		int index=0;
		while(index<this.groups.size() && !this.contains(this.groups.get(index), element)) {
			++ index;
		}
		if(index >= this.groups.size()) throw new IllegalArgumentException("Argument not found in the DynamicGroup instance.");
		return index;
	}
	
	private boolean contains(Collection<T> collection, T element) {
		// Contains don't seem to work, so...
		for(T c : collection) {if(c.equals(element)) return true;}
		return false;
	}

}
