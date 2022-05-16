package speiger.src.collections.doubles.utils;

import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.utils.DoubleCollections.EmptyCollection;
import speiger.src.collections.doubles.utils.DoubleCollections.SynchronizedCollection;
import speiger.src.collections.doubles.utils.DoubleCollections.UnmodifiableCollection;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Helper class for sets
 */
public class DoubleSets
{
	/**
	 * Empty Set Variable
	 */
	public static final DoubleSet EMPTY = new EmptySet();
	
	/**
	 * EmptySet getter
	 * @return a EmptySet
	 */
	public static DoubleSet empty() {
		return EMPTY;
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static DoubleSet synchronize(DoubleSet s) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s) : new SynchronizedSet(s));
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static DoubleSet synchronize(DoubleSet s, Object mutex) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s, mutex) : new SynchronizedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static DoubleSortedSet synchronize(DoubleSortedSet s) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s) : new SynchronizedSortedSet(s));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static DoubleSortedSet synchronize(DoubleSortedSet s, Object mutex) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s, mutex) : new SynchronizedSortedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static DoubleOrderedSet synchronize(DoubleOrderedSet s) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s) : new SynchronizedOrderedSet(s));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static DoubleOrderedSet synchronize(DoubleOrderedSet s, Object mutex) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s, mutex) : new SynchronizedOrderedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static DoubleNavigableSet synchronize(DoubleNavigableSet s) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s) : new SynchronizedNavigableSet(s));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static DoubleNavigableSet synchronize(DoubleNavigableSet s, Object mutex) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s, mutex) : new SynchronizedNavigableSet(s, mutex));
	}
	
	/**
	 * Creates Unmodifyable Set wrapper
	 * @param s set that should be made unmodifiable
	 * @return a UnmodifyableSet, if the set is already unmodifiable then it returns itself
	 */
	public static DoubleSet unmodifiable(DoubleSet s) {
		return s instanceof UnmodifiableSet ? s : new UnmodifiableSet(s);
	}
	
	/**
	 * Creates Unmodifyable SortedSet wrapper
	 * @param s sortedSet that should be made unmodifiable
	 * @return a UnmodifyableSortedSet, if the set is already unmodifiable then it returns itself
	 */
	public static DoubleSortedSet unmodifiable(DoubleSortedSet s) {
		return s instanceof UnmodifiableSortedSet ? s : new UnmodifiableSortedSet(s);
	}
	
	/**
	 * Creates Unmodifyable OrderedSet wrapper
	 * @param s OrderedSet that should be made unmodifiable
	 * @return a UnmodifyableOrderedSet, if the set is already unmodifiable then it returns itself
	 */
	public static DoubleOrderedSet unmodifiable(DoubleOrderedSet s) {
		return s instanceof UnmodifiableOrderedSet ? s : new UnmodifiableOrderedSet(s);
	}
	
	/**
	 * Creates Unmodifyable NavigableSet wrapper
	 * @param s navigableSet that should be made unmodifiable
	 * @return a UnmodifyableNavigableSet, if the set is already unmodifiable then it returns itself
	 */
	public static DoubleNavigableSet unmodifiable(DoubleNavigableSet s) {
		return s instanceof UnmodifiableNavigableSet ? s : new UnmodifiableNavigableSet(s);
	}
	
	/**
	 * Creates a Singleton set of a given element
	 * @param element the element that should be converted into a singleton set
	 * @return a singletonset of the given element
	 */
	public static DoubleSet singleton(double element) {
		return new SingletonSet(element);
	}
	
	private static class SingletonSet extends AbstractDoubleSet
	{
		double element;
		
		SingletonSet(double element) {
			this.element = element;
		}
		
		@Override
		public boolean remove(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean add(double o) { throw new UnsupportedOperationException(); }
		@Override
		public DoubleIterator iterator()
		{
			return new DoubleIterator() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next = false; }
				@Override
				public double nextDouble() {
					if(!next) throw new IllegalStateException();
					next = false;
					return element;
				}
			};
		}
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonSet copy() { return new SingletonSet(element); }
	}
	
	private static class EmptySet extends EmptyCollection implements DoubleSet
	{
		@Override
		public boolean remove(double o) { throw new UnsupportedOperationException(); }
		@Override
		public EmptySet copy() { return this; }
	}
	
	private static class UnmodifiableNavigableSet extends UnmodifiableSortedSet implements DoubleNavigableSet
	{
		DoubleNavigableSet n;
		
		UnmodifiableNavigableSet(DoubleNavigableSet c) {
			super(c);
			n = c;
		}
		
		@Override
		public boolean contains(double o) { return n.contains(o); }

		@Override
		@Deprecated
		public boolean contains(Object o) { return n.contains(o); }
		
		@Override
		public double lower(double e) { return n.lower(e); }
		
		@Override
		public double floor(double e) { return n.floor(e); }
		
		@Override
		public double ceiling(double e) { return n.ceiling(e); }
		
		@Override
		public double higher(double e) { return n.higher(e); }
		
		@Override
		public void setDefaultMaxValue(double e) { throw new UnsupportedOperationException(); }
		
		@Override
		public double getDefaultMaxValue() { return n.getDefaultMaxValue(); }
		
		@Override
		public void setDefaultMinValue(double e) { throw new UnsupportedOperationException(); }
		
		@Override
		public double getDefaultMinValue() { return n.getDefaultMinValue(); }
		
		@Override
		public DoubleNavigableSet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleNavigableSet subSet(double fromElement, boolean fromInclusive, double toElement, boolean toInclusive) { return DoubleSets.unmodifiable(n.subSet(fromElement, fromInclusive, toElement, toInclusive)); }
		
		@Override
		public DoubleNavigableSet headSet(double toElement, boolean inclusive) { return DoubleSets.unmodifiable(n.headSet(toElement, inclusive)); }

		@Override
		public DoubleNavigableSet tailSet(double fromElement, boolean inclusive) { return DoubleSets.unmodifiable(n.tailSet(fromElement, inclusive)); }

		@Override
		public DoubleBidirectionalIterator descendingIterator() { return DoubleIterators.unmodifiable(n.descendingIterator()); }

		@Override
		public DoubleNavigableSet descendingSet() { return DoubleSets.unmodifiable(n.descendingSet()); }
		
		@Override
		public DoubleNavigableSet subSet(double fromElement, double toElement) { return DoubleSets.unmodifiable(n.subSet(fromElement, toElement)); }
		
		@Override
		public DoubleNavigableSet headSet(double toElement) { return DoubleSets.unmodifiable(n.headSet(toElement)); }
		
		@Override
		public DoubleNavigableSet tailSet(double fromElement) { return DoubleSets.unmodifiable(n.tailSet(fromElement)); }
	}
	
	private static class UnmodifiableOrderedSet extends UnmodifiableSet implements DoubleOrderedSet
	{
		DoubleOrderedSet s;
		UnmodifiableOrderedSet(DoubleOrderedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public boolean addAndMoveToFirst(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(double o) { throw new UnsupportedOperationException(); }
		@Override
		public DoubleBidirectionalIterator iterator() { return DoubleIterators.unmodifiable(s.iterator()); }
		@Override
		public DoubleBidirectionalIterator iterator(double fromElement) { return DoubleIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public DoubleOrderedSet copy() { return s.copy(); }
		@Override
		public double firstDouble() { return s.firstDouble(); }
		@Override
		public double pollFirstDouble() { throw new UnsupportedOperationException(); }
		@Override
		public double lastDouble() { return s.lastDouble(); }
		@Override
		public double pollLastDouble() { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableSortedSet extends UnmodifiableSet implements DoubleSortedSet
	{
		DoubleSortedSet s;
		UnmodifiableSortedSet(DoubleSortedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public DoubleComparator comparator() { return s.comparator(); }
		@Override
		public DoubleBidirectionalIterator iterator() { return DoubleIterators.unmodifiable(s.iterator()); }
		@Override
		public DoubleBidirectionalIterator iterator(double fromElement) { return DoubleIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public DoubleSortedSet copy() { return s.copy(); }
		@Override
		public DoubleSortedSet subSet(double fromElement, double toElement) { return DoubleSets.unmodifiable(s.subSet(fromElement, toElement)); }
		@Override
		public DoubleSortedSet headSet(double toElement) { return DoubleSets.unmodifiable(s.headSet(toElement)); }
		@Override
		public DoubleSortedSet tailSet(double fromElement) { return DoubleSets.unmodifiable(s.tailSet(fromElement)); }
		@Override
		public double firstDouble() { return s.firstDouble(); }
		@Override
		public double pollFirstDouble() { throw new UnsupportedOperationException(); }
		@Override
		public double lastDouble() { return s.lastDouble(); }
		@Override
		public double pollLastDouble() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * Unmodifyable Set wrapper that helps is used with unmodifyableSet function
	 */
	public static class UnmodifiableSet extends UnmodifiableCollection implements DoubleSet
	{
		DoubleSet s;
		
		protected UnmodifiableSet(DoubleSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public DoubleSet copy() { return s.copy(); }
		
		@Override
		public boolean remove(double o) { throw new UnsupportedOperationException(); }
	}
	
	private static class SynchronizedNavigableTrimSet extends SynchronizedNavigableSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedNavigableTrimSet(DoubleNavigableSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedNavigableTrimSet(DoubleNavigableSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedNavigableSet extends SynchronizedSortedSet implements DoubleNavigableSet
	{
		DoubleNavigableSet n;
		
		SynchronizedNavigableSet(DoubleNavigableSet c) {
			super(c);
			n = c;
		}
		
		SynchronizedNavigableSet(DoubleNavigableSet c, Object mutex) {
			super(c, mutex);
			n = c;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) { synchronized(mutex) { return n.contains(o); } }

		@Override
		public boolean contains(double o) { synchronized(mutex) { return n.contains(o); } }
		
		@Override
		public double lower(double e) { synchronized(mutex) { return n.lower(e); } }
		
		@Override
		public double floor(double e) { synchronized(mutex) { return n.floor(e); } }
		
		@Override
		public double ceiling(double e) { synchronized(mutex) { return n.ceiling(e); } }
		
		@Override
		public double higher(double e) { synchronized(mutex) { return n.higher(e); } }
		
		@Override
		public void setDefaultMaxValue(double e) { synchronized(mutex) { n.setDefaultMaxValue(e); } }
		
		@Override
		public double getDefaultMaxValue() { synchronized(mutex) { return n.getDefaultMaxValue(); } }
		
		@Override
		public void setDefaultMinValue(double e) { synchronized(mutex) { n.setDefaultMinValue(e); } }
		
		@Override
		public double getDefaultMinValue() { synchronized(mutex) { return n.getDefaultMinValue(); } }
		
		@Override
		public DoubleNavigableSet copy() { synchronized(mutex) { return n.copy(); } }
		
		@Override
		public DoubleNavigableSet subSet(double fromElement, boolean fromInclusive, double toElement, boolean toInclusive) { synchronized(mutex) { return DoubleSets.synchronize(n.subSet(fromElement, fromInclusive, toElement, toInclusive), mutex); } }

		@Override
		public DoubleNavigableSet headSet(double toElement, boolean inclusive) { synchronized(mutex) { return DoubleSets.synchronize(n.headSet(toElement, inclusive), mutex); } }

		@Override
		public DoubleNavigableSet tailSet(double fromElement, boolean inclusive) { synchronized(mutex) { return DoubleSets.synchronize(n.tailSet(fromElement, inclusive), mutex); } }

		@Override
		public DoubleBidirectionalIterator descendingIterator() { synchronized(mutex) { return n.descendingIterator(); } }

		@Override
		public DoubleNavigableSet descendingSet() { synchronized(mutex) { return DoubleSets.synchronize(n.descendingSet(), mutex); } }
		
		@Override
		public DoubleNavigableSet subSet(double fromElement, double toElement) { synchronized(mutex) { return DoubleSets.synchronize(n.subSet(fromElement, toElement), mutex); } }

		@Override
		public DoubleNavigableSet headSet(double toElement) { synchronized(mutex) { return DoubleSets.synchronize(n.headSet(toElement), mutex); } }

		@Override
		public DoubleNavigableSet tailSet(double fromElement) { synchronized(mutex) { return DoubleSets.synchronize(n.tailSet(fromElement), mutex); } }
	}
	
	private static class SynchronizedSortedTrimSet extends SynchronizedSortedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedSortedTrimSet(DoubleSortedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedSortedTrimSet(DoubleSortedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSortedSet extends SynchronizedSet implements DoubleSortedSet
	{
		DoubleSortedSet s;
		
		SynchronizedSortedSet(DoubleSortedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSortedSet(DoubleSortedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public DoubleComparator comparator(){ synchronized(mutex) { return s.comparator(); } }
		@Override
		public DoubleBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public DoubleBidirectionalIterator iterator(double fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public DoubleSortedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public DoubleSortedSet subSet(double fromElement, double toElement) { synchronized(mutex) { return DoubleSets.synchronize(s.subSet(fromElement, toElement), mutex); } }
		@Override
		public DoubleSortedSet headSet(double toElement) { synchronized(mutex) { return DoubleSets.synchronize(s.headSet(toElement), mutex); } }
		@Override
		public DoubleSortedSet tailSet(double fromElement) { synchronized(mutex) { return DoubleSets.synchronize(s.tailSet(fromElement), mutex); } }
		@Override
		public double firstDouble() { synchronized(mutex) { return s.firstDouble(); } }
		@Override
		public double pollFirstDouble() { synchronized(mutex) { return s.pollFirstDouble(); } }
		@Override
		public double lastDouble() { synchronized(mutex) { return s.lastDouble(); } }
		@Override
		public double pollLastDouble() { synchronized(mutex) { return s.pollLastDouble(); } }
	}
	
	private static class SynchronizedOrderedTrimSet extends SynchronizedOrderedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedOrderedTrimSet(DoubleOrderedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedOrderedTrimSet(DoubleOrderedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedOrderedSet extends SynchronizedSet implements DoubleOrderedSet
	{
		DoubleOrderedSet s;
		
		SynchronizedOrderedSet(DoubleOrderedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedOrderedSet(DoubleOrderedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}

		@Override
		public boolean addAndMoveToFirst(double o) { synchronized(mutex) { return s.addAndMoveToFirst(o); } }
		@Override
		public boolean addAndMoveToLast(double o) { synchronized(mutex) { return s.addAndMoveToLast(o); } }
		@Override
		public boolean moveToFirst(double o) { synchronized(mutex) { return s.moveToFirst(o); } }
		@Override
		public boolean moveToLast(double o) { synchronized(mutex) { return s.moveToLast(o); } }
		@Override
		public DoubleBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public DoubleBidirectionalIterator iterator(double fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public DoubleOrderedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public double firstDouble() { synchronized(mutex) { return s.firstDouble(); } }
		@Override
		public double pollFirstDouble() { synchronized(mutex) { return s.pollFirstDouble(); } }
		@Override
		public double lastDouble() { synchronized(mutex) { return s.lastDouble(); } }
		@Override
		public double pollLastDouble() { synchronized(mutex) { return s.pollLastDouble(); } }
	}
	
	private static class SynchronizedTrimSet extends SynchronizedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedTrimSet(DoubleSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedTrimSet(DoubleSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSet extends SynchronizedCollection implements DoubleSet
	{
		DoubleSet s;
		
		SynchronizedSet(DoubleSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSet(DoubleSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public DoubleSet copy() { synchronized(mutex) { return s.copy(); } }
		
		@Override
		public boolean remove(double o) { synchronized(mutex) { return s.remove(o); } }
	}
}