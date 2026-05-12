package speiger.src.collections.shorts.sets;

import java.util.Set;

import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.collections.ShortIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractShortSet extends AbstractShortCollection implements ShortSet
{
	@Override
	public abstract ShortIterator iterator();
	@Override
	public AbstractShortSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		ShortIterator i = iterator();
		while(i.hasNext())
			hashCode += Short.hashCode(i.nextShort());
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
	
	public static class ReversedShortOrderedSet extends AbstractShortSet implements ShortOrderedSet {
		protected ShortOrderedSet set;
		
		public ReversedShortOrderedSet(ShortOrderedSet set) {
			this.set = set;
		}
		
		@Override
		public ReversedShortOrderedSet copy() { throw new UnsupportedOperationException(); }
		@Override
		public ShortBidirectionalIterator iterator(short fromElement) {
			return set.iterator(fromElement);
		}
		
		@Override
		public ShortBidirectionalIterator iterator() {
			return set.reverseIterator();
		}
		
		@Override
		public ShortBidirectionalIterator reverseIterator() {
			return set.iterator();
		}
		
		@Override
		public void addFirst(short o) {
			set.addLast(o);
		}
		
		@Override
		public void addLast(short o) {
			set.addFirst(o);
		}
		
		@Override
		public boolean remove(short o) {
			return set.remove(o);
		}
		
		@Override
		public boolean add(short o) {
			return set.add(o);
		}
		
		@Override
		public boolean addAndMoveToFirst(short o) {
			return set.addAndMoveToLast(o);
		}
		
		@Override
		public boolean addAndMoveToLast(short o) {
			return set.addAndMoveToFirst(o);
		}
		
		@Override
		public boolean moveToFirst(short o) {
			return set.moveToLast(o);
		}
		
		@Override
		public boolean moveToLast(short o) {
			return set.moveToFirst(o);
		}
		
		@Override
		public short getFirstShort() {
			return set.getLastShort();
		}
		
		@Override
		public short removeFirstShort() {
			return set.removeLastShort();
		}
		
		@Override
		public short getLastShort() {
			return set.getFirstShort();
		}
		
		@Override
		public short removeLastShort() {
			return set.removeFirstShort();
		}
		
		@Override
		public ShortOrderedSet reversed() {
			return set;
		}
		
		@Override
		public int size() {
			return set.size();
		}
	}
}