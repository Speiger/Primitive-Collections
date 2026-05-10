package speiger.src.collections.doubles.sets;

import java.util.Set;

import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.collections.DoubleIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 */
public abstract class AbstractDoubleSet extends AbstractDoubleCollection implements DoubleSet
{
	@Override
	public abstract DoubleIterator iterator();
	@Override
	public AbstractDoubleSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		DoubleIterator i = iterator();
		while(i.hasNext())
			hashCode += Double.hashCode(i.nextDouble());
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
	
	public static class ReversedDoubleOrderedSet extends AbstractDoubleSet implements DoubleOrderedSet {
		protected DoubleOrderedSet set;
		
		public ReversedDoubleOrderedSet(DoubleOrderedSet set) {
			this.set = set;
		}
		
		@Override
		public ReversedDoubleOrderedSet copy() { throw new UnsupportedOperationException(); }
		@Override
		public DoubleBidirectionalIterator iterator(double fromElement) {
			return set.iterator(fromElement);
		}
		
		@Override
		public DoubleBidirectionalIterator iterator() {
			return set.reverseIterator();
		}
		
		@Override
		public DoubleBidirectionalIterator reverseIterator() {
			return set.iterator();
		}

		@Override
		public boolean remove(double o) {
			return set.remove(o);
		}
		
		@Override
		public boolean add(double o) {
			return set.add(o);
		}
		
		@Override
		public boolean addAndMoveToFirst(double o) {
			return set.addAndMoveToLast(o);
		}
		
		@Override
		public boolean addAndMoveToLast(double o) {
			return set.addAndMoveToFirst(o);
		}
		
		@Override
		public boolean moveToFirst(double o) {
			return set.moveToLast(o);
		}
		
		@Override
		public boolean moveToLast(double o) {
			return set.moveToFirst(o);
		}
		
		@Override
		public double getFirstDouble() {
			return set.getLastDouble();
		}
		
		@Override
		public double removeFirstDouble() {
			return set.removeLastDouble();
		}
		
		@Override
		public double getLastDouble() {
			return set.getFirstDouble();
		}
		
		@Override
		public double removeLastDouble() {
			return set.removeFirstDouble();
		}
		
		@Override
		public DoubleOrderedSet reversed() {
			return set;
		}
		
		@Override
		public int size() {
			return set.size();
		}
	}
}