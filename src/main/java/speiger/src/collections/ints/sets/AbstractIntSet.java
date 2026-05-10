package speiger.src.collections.ints.sets;

import java.util.Set;

import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.collections.IntIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractIntSet extends AbstractIntCollection implements IntSet
{
	@Override
	public abstract IntIterator iterator();
	@Override
	public AbstractIntSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		IntIterator i = iterator();
		while(i.hasNext())
			hashCode += Integer.hashCode(i.nextInt());
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
	
	public static class ReversedIntOrderedSet extends AbstractIntSet implements IntOrderedSet {
		protected IntOrderedSet set;
		
		public ReversedIntOrderedSet(IntOrderedSet set) {
			this.set = set;
		}
		
		@Override
		public ReversedIntOrderedSet copy() { throw new UnsupportedOperationException(); }
		@Override
		public IntBidirectionalIterator iterator(int fromElement) {
			return set.iterator(fromElement);
		}
		
		@Override
		public IntBidirectionalIterator iterator() {
			return set.reverseIterator();
		}
		
		@Override
		public IntBidirectionalIterator reverseIterator() {
			return set.iterator();
		}

		@Override
		public boolean remove(int o) {
			return set.remove(o);
		}
		
		@Override
		public boolean add(int o) {
			return set.add(o);
		}
		
		@Override
		public boolean addAndMoveToFirst(int o) {
			return set.addAndMoveToLast(o);
		}
		
		@Override
		public boolean addAndMoveToLast(int o) {
			return set.addAndMoveToFirst(o);
		}
		
		@Override
		public boolean moveToFirst(int o) {
			return set.moveToLast(o);
		}
		
		@Override
		public boolean moveToLast(int o) {
			return set.moveToFirst(o);
		}
		
		@Override
		public int getFirstInt() {
			return set.getLastInt();
		}
		
		@Override
		public int removeFirstInt() {
			return set.removeLastInt();
		}
		
		@Override
		public int getLastInt() {
			return set.getFirstInt();
		}
		
		@Override
		public int removeLastInt() {
			return set.removeFirstInt();
		}
		
		@Override
		public IntOrderedSet reversed() {
			return set;
		}
		
		@Override
		public int size() {
			return set.size();
		}
	}
}