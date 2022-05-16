package speiger.src.collections.ints.utils;

import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.utils.IntCollections.EmptyCollection;
import speiger.src.collections.ints.utils.IntCollections.SynchronizedCollection;
import speiger.src.collections.ints.utils.IntCollections.UnmodifiableCollection;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Helper class for sets
 */
public class IntSets
{
	/**
	 * Empty Set Variable
	 */
	public static final IntSet EMPTY = new EmptySet();
	
	/**
	 * EmptySet getter
	 * @return a EmptySet
	 */
	public static IntSet empty() {
		return EMPTY;
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static IntSet synchronize(IntSet s) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s) : new SynchronizedSet(s));
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static IntSet synchronize(IntSet s, Object mutex) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s, mutex) : new SynchronizedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static IntSortedSet synchronize(IntSortedSet s) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s) : new SynchronizedSortedSet(s));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static IntSortedSet synchronize(IntSortedSet s, Object mutex) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s, mutex) : new SynchronizedSortedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static IntOrderedSet synchronize(IntOrderedSet s) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s) : new SynchronizedOrderedSet(s));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static IntOrderedSet synchronize(IntOrderedSet s, Object mutex) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s, mutex) : new SynchronizedOrderedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static IntNavigableSet synchronize(IntNavigableSet s) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s) : new SynchronizedNavigableSet(s));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static IntNavigableSet synchronize(IntNavigableSet s, Object mutex) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s, mutex) : new SynchronizedNavigableSet(s, mutex));
	}
	
	/**
	 * Creates Unmodifyable Set wrapper
	 * @param s set that should be made unmodifiable
	 * @return a UnmodifyableSet, if the set is already unmodifiable then it returns itself
	 */
	public static IntSet unmodifiable(IntSet s) {
		return s instanceof UnmodifiableSet ? s : new UnmodifiableSet(s);
	}
	
	/**
	 * Creates Unmodifyable SortedSet wrapper
	 * @param s sortedSet that should be made unmodifiable
	 * @return a UnmodifyableSortedSet, if the set is already unmodifiable then it returns itself
	 */
	public static IntSortedSet unmodifiable(IntSortedSet s) {
		return s instanceof UnmodifiableSortedSet ? s : new UnmodifiableSortedSet(s);
	}
	
	/**
	 * Creates Unmodifyable OrderedSet wrapper
	 * @param s OrderedSet that should be made unmodifiable
	 * @return a UnmodifyableOrderedSet, if the set is already unmodifiable then it returns itself
	 */
	public static IntOrderedSet unmodifiable(IntOrderedSet s) {
		return s instanceof UnmodifiableOrderedSet ? s : new UnmodifiableOrderedSet(s);
	}
	
	/**
	 * Creates Unmodifyable NavigableSet wrapper
	 * @param s navigableSet that should be made unmodifiable
	 * @return a UnmodifyableNavigableSet, if the set is already unmodifiable then it returns itself
	 */
	public static IntNavigableSet unmodifiable(IntNavigableSet s) {
		return s instanceof UnmodifiableNavigableSet ? s : new UnmodifiableNavigableSet(s);
	}
	
	/**
	 * Creates a Singleton set of a given element
	 * @param element the element that should be converted into a singleton set
	 * @return a singletonset of the given element
	 */
	public static IntSet singleton(int element) {
		return new SingletonSet(element);
	}
	
	private static class SingletonSet extends AbstractIntSet
	{
		int element;
		
		SingletonSet(int element) {
			this.element = element;
		}
		
		@Override
		public boolean remove(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean add(int o) { throw new UnsupportedOperationException(); }
		@Override
		public IntIterator iterator()
		{
			return new IntIterator() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next = false; }
				@Override
				public int nextInt() {
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
	
	private static class EmptySet extends EmptyCollection implements IntSet
	{
		@Override
		public boolean remove(int o) { throw new UnsupportedOperationException(); }
		@Override
		public EmptySet copy() { return this; }
	}
	
	private static class UnmodifiableNavigableSet extends UnmodifiableSortedSet implements IntNavigableSet
	{
		IntNavigableSet n;
		
		UnmodifiableNavigableSet(IntNavigableSet c) {
			super(c);
			n = c;
		}
		
		@Override
		public boolean contains(int o) { return n.contains(o); }

		@Override
		@Deprecated
		public boolean contains(Object o) { return n.contains(o); }
		
		@Override
		public int lower(int e) { return n.lower(e); }
		
		@Override
		public int floor(int e) { return n.floor(e); }
		
		@Override
		public int ceiling(int e) { return n.ceiling(e); }
		
		@Override
		public int higher(int e) { return n.higher(e); }
		
		@Override
		public void setDefaultMaxValue(int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int getDefaultMaxValue() { return n.getDefaultMaxValue(); }
		
		@Override
		public void setDefaultMinValue(int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int getDefaultMinValue() { return n.getDefaultMinValue(); }
		
		@Override
		public IntNavigableSet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public IntNavigableSet subSet(int fromElement, boolean fromInclusive, int toElement, boolean toInclusive) { return IntSets.unmodifiable(n.subSet(fromElement, fromInclusive, toElement, toInclusive)); }
		
		@Override
		public IntNavigableSet headSet(int toElement, boolean inclusive) { return IntSets.unmodifiable(n.headSet(toElement, inclusive)); }

		@Override
		public IntNavigableSet tailSet(int fromElement, boolean inclusive) { return IntSets.unmodifiable(n.tailSet(fromElement, inclusive)); }

		@Override
		public IntBidirectionalIterator descendingIterator() { return IntIterators.unmodifiable(n.descendingIterator()); }

		@Override
		public IntNavigableSet descendingSet() { return IntSets.unmodifiable(n.descendingSet()); }
		
		@Override
		public IntNavigableSet subSet(int fromElement, int toElement) { return IntSets.unmodifiable(n.subSet(fromElement, toElement)); }
		
		@Override
		public IntNavigableSet headSet(int toElement) { return IntSets.unmodifiable(n.headSet(toElement)); }
		
		@Override
		public IntNavigableSet tailSet(int fromElement) { return IntSets.unmodifiable(n.tailSet(fromElement)); }
	}
	
	private static class UnmodifiableOrderedSet extends UnmodifiableSet implements IntOrderedSet
	{
		IntOrderedSet s;
		UnmodifiableOrderedSet(IntOrderedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public boolean addAndMoveToFirst(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(int o) { throw new UnsupportedOperationException(); }
		@Override
		public IntBidirectionalIterator iterator() { return IntIterators.unmodifiable(s.iterator()); }
		@Override
		public IntBidirectionalIterator iterator(int fromElement) { return IntIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public IntOrderedSet copy() { return s.copy(); }
		@Override
		public int firstInt() { return s.firstInt(); }
		@Override
		public int pollFirstInt() { throw new UnsupportedOperationException(); }
		@Override
		public int lastInt() { return s.lastInt(); }
		@Override
		public int pollLastInt() { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableSortedSet extends UnmodifiableSet implements IntSortedSet
	{
		IntSortedSet s;
		UnmodifiableSortedSet(IntSortedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public IntComparator comparator() { return s.comparator(); }
		@Override
		public IntBidirectionalIterator iterator() { return IntIterators.unmodifiable(s.iterator()); }
		@Override
		public IntBidirectionalIterator iterator(int fromElement) { return IntIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public IntSortedSet copy() { return s.copy(); }
		@Override
		public IntSortedSet subSet(int fromElement, int toElement) { return IntSets.unmodifiable(s.subSet(fromElement, toElement)); }
		@Override
		public IntSortedSet headSet(int toElement) { return IntSets.unmodifiable(s.headSet(toElement)); }
		@Override
		public IntSortedSet tailSet(int fromElement) { return IntSets.unmodifiable(s.tailSet(fromElement)); }
		@Override
		public int firstInt() { return s.firstInt(); }
		@Override
		public int pollFirstInt() { throw new UnsupportedOperationException(); }
		@Override
		public int lastInt() { return s.lastInt(); }
		@Override
		public int pollLastInt() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * Unmodifyable Set wrapper that helps is used with unmodifyableSet function
	 */
	public static class UnmodifiableSet extends UnmodifiableCollection implements IntSet
	{
		IntSet s;
		
		protected UnmodifiableSet(IntSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public IntSet copy() { return s.copy(); }
		
		@Override
		public boolean remove(int o) { throw new UnsupportedOperationException(); }
	}
	
	private static class SynchronizedNavigableTrimSet extends SynchronizedNavigableSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedNavigableTrimSet(IntNavigableSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedNavigableTrimSet(IntNavigableSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedNavigableSet extends SynchronizedSortedSet implements IntNavigableSet
	{
		IntNavigableSet n;
		
		SynchronizedNavigableSet(IntNavigableSet c) {
			super(c);
			n = c;
		}
		
		SynchronizedNavigableSet(IntNavigableSet c, Object mutex) {
			super(c, mutex);
			n = c;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) { synchronized(mutex) { return n.contains(o); } }

		@Override
		public boolean contains(int o) { synchronized(mutex) { return n.contains(o); } }
		
		@Override
		public int lower(int e) { synchronized(mutex) { return n.lower(e); } }
		
		@Override
		public int floor(int e) { synchronized(mutex) { return n.floor(e); } }
		
		@Override
		public int ceiling(int e) { synchronized(mutex) { return n.ceiling(e); } }
		
		@Override
		public int higher(int e) { synchronized(mutex) { return n.higher(e); } }
		
		@Override
		public void setDefaultMaxValue(int e) { synchronized(mutex) { n.setDefaultMaxValue(e); } }
		
		@Override
		public int getDefaultMaxValue() { synchronized(mutex) { return n.getDefaultMaxValue(); } }
		
		@Override
		public void setDefaultMinValue(int e) { synchronized(mutex) { n.setDefaultMinValue(e); } }
		
		@Override
		public int getDefaultMinValue() { synchronized(mutex) { return n.getDefaultMinValue(); } }
		
		@Override
		public IntNavigableSet copy() { synchronized(mutex) { return n.copy(); } }
		
		@Override
		public IntNavigableSet subSet(int fromElement, boolean fromInclusive, int toElement, boolean toInclusive) { synchronized(mutex) { return IntSets.synchronize(n.subSet(fromElement, fromInclusive, toElement, toInclusive), mutex); } }

		@Override
		public IntNavigableSet headSet(int toElement, boolean inclusive) { synchronized(mutex) { return IntSets.synchronize(n.headSet(toElement, inclusive), mutex); } }

		@Override
		public IntNavigableSet tailSet(int fromElement, boolean inclusive) { synchronized(mutex) { return IntSets.synchronize(n.tailSet(fromElement, inclusive), mutex); } }

		@Override
		public IntBidirectionalIterator descendingIterator() { synchronized(mutex) { return n.descendingIterator(); } }

		@Override
		public IntNavigableSet descendingSet() { synchronized(mutex) { return IntSets.synchronize(n.descendingSet(), mutex); } }
		
		@Override
		public IntNavigableSet subSet(int fromElement, int toElement) { synchronized(mutex) { return IntSets.synchronize(n.subSet(fromElement, toElement), mutex); } }

		@Override
		public IntNavigableSet headSet(int toElement) { synchronized(mutex) { return IntSets.synchronize(n.headSet(toElement), mutex); } }

		@Override
		public IntNavigableSet tailSet(int fromElement) { synchronized(mutex) { return IntSets.synchronize(n.tailSet(fromElement), mutex); } }
	}
	
	private static class SynchronizedSortedTrimSet extends SynchronizedSortedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedSortedTrimSet(IntSortedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedSortedTrimSet(IntSortedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSortedSet extends SynchronizedSet implements IntSortedSet
	{
		IntSortedSet s;
		
		SynchronizedSortedSet(IntSortedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSortedSet(IntSortedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public IntComparator comparator(){ synchronized(mutex) { return s.comparator(); } }
		@Override
		public IntBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public IntBidirectionalIterator iterator(int fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public IntSortedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public IntSortedSet subSet(int fromElement, int toElement) { synchronized(mutex) { return IntSets.synchronize(s.subSet(fromElement, toElement), mutex); } }
		@Override
		public IntSortedSet headSet(int toElement) { synchronized(mutex) { return IntSets.synchronize(s.headSet(toElement), mutex); } }
		@Override
		public IntSortedSet tailSet(int fromElement) { synchronized(mutex) { return IntSets.synchronize(s.tailSet(fromElement), mutex); } }
		@Override
		public int firstInt() { synchronized(mutex) { return s.firstInt(); } }
		@Override
		public int pollFirstInt() { synchronized(mutex) { return s.pollFirstInt(); } }
		@Override
		public int lastInt() { synchronized(mutex) { return s.lastInt(); } }
		@Override
		public int pollLastInt() { synchronized(mutex) { return s.pollLastInt(); } }
	}
	
	private static class SynchronizedOrderedTrimSet extends SynchronizedOrderedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedOrderedTrimSet(IntOrderedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedOrderedTrimSet(IntOrderedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedOrderedSet extends SynchronizedSet implements IntOrderedSet
	{
		IntOrderedSet s;
		
		SynchronizedOrderedSet(IntOrderedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedOrderedSet(IntOrderedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}

		@Override
		public boolean addAndMoveToFirst(int o) { synchronized(mutex) { return s.addAndMoveToFirst(o); } }
		@Override
		public boolean addAndMoveToLast(int o) { synchronized(mutex) { return s.addAndMoveToLast(o); } }
		@Override
		public boolean moveToFirst(int o) { synchronized(mutex) { return s.moveToFirst(o); } }
		@Override
		public boolean moveToLast(int o) { synchronized(mutex) { return s.moveToLast(o); } }
		@Override
		public IntBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public IntBidirectionalIterator iterator(int fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public IntOrderedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public int firstInt() { synchronized(mutex) { return s.firstInt(); } }
		@Override
		public int pollFirstInt() { synchronized(mutex) { return s.pollFirstInt(); } }
		@Override
		public int lastInt() { synchronized(mutex) { return s.lastInt(); } }
		@Override
		public int pollLastInt() { synchronized(mutex) { return s.pollLastInt(); } }
	}
	
	private static class SynchronizedTrimSet extends SynchronizedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedTrimSet(IntSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedTrimSet(IntSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSet extends SynchronizedCollection implements IntSet
	{
		IntSet s;
		
		SynchronizedSet(IntSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSet(IntSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public IntSet copy() { synchronized(mutex) { return s.copy(); } }
		
		@Override
		public boolean remove(int o) { synchronized(mutex) { return s.remove(o); } }
	}
}