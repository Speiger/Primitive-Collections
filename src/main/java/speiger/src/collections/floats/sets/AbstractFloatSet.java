package speiger.src.collections.floats.sets;

import java.util.Set;

import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatBidirectionalIterator;
import speiger.src.collections.floats.collections.FloatIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractFloatSet extends AbstractFloatCollection implements FloatSet
{
	@Override
	public abstract FloatIterator iterator();
	@Override
	public AbstractFloatSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		FloatIterator i = iterator();
		while(i.hasNext())
			hashCode += Float.hashCode(i.nextFloat());
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Set))
			return false;
		Set<?> l = (Set<?>)o;
		if(l.size() != size()) return false;
		try {
			return containsAll(l);
		} catch (ClassCastException | NullPointerException unused) {
			return false;
		}
	}
	
	public static class ReversedFloatOrderedSet extends AbstractFloatSet implements FloatOrderedSet {
		protected FloatOrderedSet set;
		
		public ReversedFloatOrderedSet(FloatOrderedSet set) {
			this.set = set;
		}
		
		@Override
		public ReversedFloatOrderedSet copy() { throw new UnsupportedOperationException(); }
		@Override
		public FloatBidirectionalIterator iterator(float fromElement) {
			return set.iterator(fromElement);
		}
		
		@Override
		public FloatBidirectionalIterator iterator() {
			return set.reverseIterator();
		}
		
		@Override
		public FloatBidirectionalIterator reverseIterator() {
			return set.iterator();
		}
		
		@Override
		public void addFirst(float o) {
			set.addLast(o);
		}
		
		@Override
		public void addLast(float o) {
			set.addFirst(o);
		}
		
		@Override
		public boolean remove(float o) {
			return set.remove(o);
		}
		
		@Override
		public boolean add(float o) {
			return set.add(o);
		}
		
		@Override
		public boolean addAndMoveToFirst(float o) {
			return set.addAndMoveToLast(o);
		}
		
		@Override
		public boolean addAndMoveToLast(float o) {
			return set.addAndMoveToFirst(o);
		}
		
		@Override
		public boolean moveToFirst(float o) {
			return set.moveToLast(o);
		}
		
		@Override
		public boolean moveToLast(float o) {
			return set.moveToFirst(o);
		}
		
		@Override
		public float getFirstFloat() {
			return set.getLastFloat();
		}
		
		@Override
		public float removeFirstFloat() {
			return set.removeLastFloat();
		}
		
		@Override
		public float getLastFloat() {
			return set.getFirstFloat();
		}
		
		@Override
		public float removeLastFloat() {
			return set.removeFirstFloat();
		}
		
		@Override
		public FloatOrderedSet reversed() {
			return set;
		}
		
		@Override
		public int size() {
			return set.size();
		}
	}
}