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
		Collection<T> newgroup = this.groups.remove(index_1);
		newgroup.addAll(this.groups.remove(index_2));
		this.groups.add(newgroup);
	}
	
	private int getIndex(T element) {
		int index=0;
		for(;index<this.groups.size(); ++index) {
			if(this.groups.get(index).contains(element)) {
				break;
			}
		}
		if(index >= this.groups.size()) throw new IllegalArgumentException("Argument not found in the DynamicGroup instance.");
		return index;
	}
}
