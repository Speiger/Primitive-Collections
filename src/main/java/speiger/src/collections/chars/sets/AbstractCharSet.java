package speiger.src.collections.chars.sets;

import java.util.Set;

import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.collections.CharIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractCharSet extends AbstractCharCollection implements CharSet
{
	@Override
	public abstract CharIterator iterator();
	@Override
	public AbstractCharSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		CharIterator i = iterator();
		while(i.hasNext())
			hashCode += Character.hashCode(i.nextChar());
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
	
	public static class ReversedCharOrderedSet extends AbstractCharSet implements CharOrderedSet {
		protected CharOrderedSet set;
		
		public ReversedCharOrderedSet(CharOrderedSet set) {
			this.set = set;
		}
		
		@Override
		public ReversedCharOrderedSet copy() { throw new UnsupportedOperationException(); }
		@Override
		public CharBidirectionalIterator iterator(char fromElement) {
			return set.iterator(fromElement);
		}
		
		@Override
		public CharBidirectionalIterator iterator() {
			return set.reverseIterator();
		}
		
		@Override
		public CharBidirectionalIterator reverseIterator() {
			return set.iterator();
		}
		
		@Override
		public void addFirst(char o) {
			set.addLast(o);
		}
		
		@Override
		public void addLast(char o) {
			set.addFirst(o);
		}
		
		@Override
		public boolean remove(char o) {
			return set.remove(o);
		}
		
		@Override
		public boolean add(char o) {
			return set.add(o);
		}
		
		@Override
		public boolean addAndMoveToFirst(char o) {
			return set.addAndMoveToLast(o);
		}
		
		@Override
		public boolean addAndMoveToLast(char o) {
			return set.addAndMoveToFirst(o);
		}
		
		@Override
		public boolean moveToFirst(char o) {
			return set.moveToLast(o);
		}
		
		@Override
		public boolean moveToLast(char o) {
			return set.moveToFirst(o);
		}
		
		@Override
		public char getFirstChar() {
			return set.getLastChar();
		}
		
		@Override
		public char removeFirstChar() {
			return set.removeLastChar();
		}
		
		@Override
		public char getLastChar() {
			return set.getFirstChar();
		}
		
		@Override
		public char removeLastChar() {
			return set.removeFirstChar();
		}
		
		@Override
		public CharOrderedSet reversed() {
			return set;
		}
		
		@Override
		public int size() {
			return set.size();
		}
	}
}