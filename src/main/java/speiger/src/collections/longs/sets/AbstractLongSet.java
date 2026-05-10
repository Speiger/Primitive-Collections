package speiger.src.collections.longs.sets;

import java.util.Set;

import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.collections.LongIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractLongSet extends AbstractLongCollection implements LongSet
{
	@Override
	public abstract LongIterator iterator();
	@Override
	public AbstractLongSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		LongIterator i = iterator();
		while(i.hasNext())
			hashCode += Long.hashCode(i.nextLong());
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
	
	public static class ReversedLongOrderedSet extends AbstractLongSet implements LongOrderedSet {
		protected LongOrderedSet set;
		
		public ReversedLongOrderedSet(LongOrderedSet set) {
			this.set = set;
		}
		
		@Override
		public ReversedLongOrderedSet copy() { throw new UnsupportedOperationException(); }
		@Override
		public LongBidirectionalIterator iterator(long fromElement) {
			return set.iterator(fromElement);
		}
		
		@Override
		public LongBidirectionalIterator iterator() {
			return set.reverseIterator();
		}
		
		@Override
		public LongBidirectionalIterator reverseIterator() {
			return set.iterator();
		}

		@Override
		public boolean remove(long o) {
			return set.remove(o);
		}
		
		@Override
		public boolean add(long o) {
			return set.add(o);
		}
		
		@Override
		public boolean addAndMoveToFirst(long o) {
			return set.addAndMoveToLast(o);
		}
		
		@Override
		public boolean addAndMoveToLast(long o) {
			return set.addAndMoveToFirst(o);
		}
		
		@Override
		public boolean moveToFirst(long o) {
			return set.moveToLast(o);
		}
		
		@Override
		public boolean moveToLast(long o) {
			return set.moveToFirst(o);
		}
		
		@Override
		public long getFirstLong() {
			return set.getLastLong();
		}
		
		@Override
		public long removeFirstLong() {
			return set.removeLastLong();
		}
		
		@Override
		public long getLastLong() {
			return set.getFirstLong();
		}
		
		@Override
		public long removeLastLong() {
			return set.removeFirstLong();
		}
		
		@Override
		public LongOrderedSet reversed() {
			return set;
		}
		
		@Override
		public int size() {
			return set.size();
		}
	}
}