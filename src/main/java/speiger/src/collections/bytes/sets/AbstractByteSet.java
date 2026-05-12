package speiger.src.collections.bytes.sets;

import java.util.Set;

import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.collections.ByteIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractByteSet extends AbstractByteCollection implements ByteSet
{
	@Override
	public abstract ByteIterator iterator();
	@Override
	public AbstractByteSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		ByteIterator i = iterator();
		while(i.hasNext())
			hashCode += Byte.hashCode(i.nextByte());
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
	
	public static class ReversedByteOrderedSet extends AbstractByteSet implements ByteOrderedSet {
		protected ByteOrderedSet set;
		
		public ReversedByteOrderedSet(ByteOrderedSet set) {
			this.set = set;
		}
		
		@Override
		public ReversedByteOrderedSet copy() { throw new UnsupportedOperationException(); }
		@Override
		public ByteBidirectionalIterator iterator(byte fromElement) {
			return set.iterator(fromElement);
		}
		
		@Override
		public ByteBidirectionalIterator iterator() {
			return set.reverseIterator();
		}
		
		@Override
		public ByteBidirectionalIterator reverseIterator() {
			return set.iterator();
		}
		
		@Override
		public void addFirst(byte o) {
			set.addLast(o);
		}
		
		@Override
		public void addLast(byte o) {
			set.addFirst(o);
		}
		
		@Override
		public boolean remove(byte o) {
			return set.remove(o);
		}
		
		@Override
		public boolean add(byte o) {
			return set.add(o);
		}
		
		@Override
		public boolean addAndMoveToFirst(byte o) {
			return set.addAndMoveToLast(o);
		}
		
		@Override
		public boolean addAndMoveToLast(byte o) {
			return set.addAndMoveToFirst(o);
		}
		
		@Override
		public boolean moveToFirst(byte o) {
			return set.moveToLast(o);
		}
		
		@Override
		public boolean moveToLast(byte o) {
			return set.moveToFirst(o);
		}
		
		@Override
		public byte getFirstByte() {
			return set.getLastByte();
		}
		
		@Override
		public byte removeFirstByte() {
			return set.removeLastByte();
		}
		
		@Override
		public byte getLastByte() {
			return set.getFirstByte();
		}
		
		@Override
		public byte removeLastByte() {
			return set.removeFirstByte();
		}
		
		@Override
		public ByteOrderedSet reversed() {
			return set;
		}
		
		@Override
		public int size() {
			return set.size();
		}
	}
}