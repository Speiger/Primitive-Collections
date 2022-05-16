package speiger.src.collections.floats.utils;

import speiger.src.collections.floats.collections.FloatBidirectionalIterator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.utils.FloatCollections.EmptyCollection;
import speiger.src.collections.floats.utils.FloatCollections.SynchronizedCollection;
import speiger.src.collections.floats.utils.FloatCollections.UnmodifiableCollection;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Helper class for sets
 */
public class FloatSets
{
	/**
	 * Empty Set Variable
	 */
	public static final FloatSet EMPTY = new EmptySet();
	
	/**
	 * EmptySet getter
	 * @return a EmptySet
	 */
	public static FloatSet empty() {
		return EMPTY;
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static FloatSet synchronize(FloatSet s) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s) : new SynchronizedSet(s));
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static FloatSet synchronize(FloatSet s, Object mutex) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s, mutex) : new SynchronizedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static FloatSortedSet synchronize(FloatSortedSet s) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s) : new SynchronizedSortedSet(s));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static FloatSortedSet synchronize(FloatSortedSet s, Object mutex) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s, mutex) : new SynchronizedSortedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static FloatOrderedSet synchronize(FloatOrderedSet s) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s) : new SynchronizedOrderedSet(s));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static FloatOrderedSet synchronize(FloatOrderedSet s, Object mutex) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s, mutex) : new SynchronizedOrderedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static FloatNavigableSet synchronize(FloatNavigableSet s) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s) : new SynchronizedNavigableSet(s));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static FloatNavigableSet synchronize(FloatNavigableSet s, Object mutex) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s, mutex) : new SynchronizedNavigableSet(s, mutex));
	}
	
	/**
	 * Creates Unmodifyable Set wrapper
	 * @param s set that should be made unmodifiable
	 * @return a UnmodifyableSet, if the set is already unmodifiable then it returns itself
	 */
	public static FloatSet unmodifiable(FloatSet s) {
		return s instanceof UnmodifiableSet ? s : new UnmodifiableSet(s);
	}
	
	/**
	 * Creates Unmodifyable SortedSet wrapper
	 * @param s sortedSet that should be made unmodifiable
	 * @return a UnmodifyableSortedSet, if the set is already unmodifiable then it returns itself
	 */
	public static FloatSortedSet unmodifiable(FloatSortedSet s) {
		return s instanceof UnmodifiableSortedSet ? s : new UnmodifiableSortedSet(s);
	}
	
	/**
	 * Creates Unmodifyable OrderedSet wrapper
	 * @param s OrderedSet that should be made unmodifiable
	 * @return a UnmodifyableOrderedSet, if the set is already unmodifiable then it returns itself
	 */
	public static FloatOrderedSet unmodifiable(FloatOrderedSet s) {
		return s instanceof UnmodifiableOrderedSet ? s : new UnmodifiableOrderedSet(s);
	}
	
	/**
	 * Creates Unmodifyable NavigableSet wrapper
	 * @param s navigableSet that should be made unmodifiable
	 * @return a UnmodifyableNavigableSet, if the set is already unmodifiable then it returns itself
	 */
	public static FloatNavigableSet unmodifiable(FloatNavigableSet s) {
		return s instanceof UnmodifiableNavigableSet ? s : new UnmodifiableNavigableSet(s);
	}
	
	/**
	 * Creates a Singleton set of a given element
	 * @param element the element that should be converted into a singleton set
	 * @return a singletonset of the given element
	 */
	public static FloatSet singleton(float element) {
		return new SingletonSet(element);
	}
	
	private static class SingletonSet extends AbstractFloatSet
	{
		float element;
		
		SingletonSet(float element) {
			this.element = element;
		}
		
		@Override
		public boolean remove(float o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean add(float o) { throw new UnsupportedOperationException(); }
		@Override
		public FloatIterator iterator()
		{
			return new FloatIterator() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next = false; }
				@Override
				public float nextFloat() {
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
	
	private static class EmptySet extends EmptyCollection implements FloatSet
	{
		@Override
		public boolean remove(float o) { throw new UnsupportedOperationException(); }
		@Override
		public EmptySet copy() { return this; }
	}
	
	private static class UnmodifiableNavigableSet extends UnmodifiableSortedSet implements FloatNavigableSet
	{
		FloatNavigableSet n;
		
		UnmodifiableNavigableSet(FloatNavigableSet c) {
			super(c);
			n = c;
		}
		
		@Override
		public boolean contains(float o) { return n.contains(o); }

		@Override
		@Deprecated
		public boolean contains(Object o) { return n.contains(o); }
		
		@Override
		public float lower(float e) { return n.lower(e); }
		
		@Override
		public float floor(float e) { return n.floor(e); }
		
		@Override
		public float ceiling(float e) { return n.ceiling(e); }
		
		@Override
		public float higher(float e) { return n.higher(e); }
		
		@Override
		public void setDefaultMaxValue(float e) { throw new UnsupportedOperationException(); }
		
		@Override
		public float getDefaultMaxValue() { return n.getDefaultMaxValue(); }
		
		@Override
		public void setDefaultMinValue(float e) { throw new UnsupportedOperationException(); }
		
		@Override
		public float getDefaultMinValue() { return n.getDefaultMinValue(); }
		
		@Override
		public FloatNavigableSet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatNavigableSet subSet(float fromElement, boolean fromInclusive, float toElement, boolean toInclusive) { return FloatSets.unmodifiable(n.subSet(fromElement, fromInclusive, toElement, toInclusive)); }
		
		@Override
		public FloatNavigableSet headSet(float toElement, boolean inclusive) { return FloatSets.unmodifiable(n.headSet(toElement, inclusive)); }

		@Override
		public FloatNavigableSet tailSet(float fromElement, boolean inclusive) { return FloatSets.unmodifiable(n.tailSet(fromElement, inclusive)); }

		@Override
		public FloatBidirectionalIterator descendingIterator() { return FloatIterators.unmodifiable(n.descendingIterator()); }

		@Override
		public FloatNavigableSet descendingSet() { return FloatSets.unmodifiable(n.descendingSet()); }
		
		@Override
		public FloatNavigableSet subSet(float fromElement, float toElement) { return FloatSets.unmodifiable(n.subSet(fromElement, toElement)); }
		
		@Override
		public FloatNavigableSet headSet(float toElement) { return FloatSets.unmodifiable(n.headSet(toElement)); }
		
		@Override
		public FloatNavigableSet tailSet(float fromElement) { return FloatSets.unmodifiable(n.tailSet(fromElement)); }
	}
	
	private static class UnmodifiableOrderedSet extends UnmodifiableSet implements FloatOrderedSet
	{
		FloatOrderedSet s;
		UnmodifiableOrderedSet(FloatOrderedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public boolean addAndMoveToFirst(float o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(float o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(float o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(float o) { throw new UnsupportedOperationException(); }
		@Override
		public FloatBidirectionalIterator iterator() { return FloatIterators.unmodifiable(s.iterator()); }
		@Override
		public FloatBidirectionalIterator iterator(float fromElement) { return FloatIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public FloatOrderedSet copy() { return s.copy(); }
		@Override
		public float firstFloat() { return s.firstFloat(); }
		@Override
		public float pollFirstFloat() { throw new UnsupportedOperationException(); }
		@Override
		public float lastFloat() { return s.lastFloat(); }
		@Override
		public float pollLastFloat() { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableSortedSet extends UnmodifiableSet implements FloatSortedSet
	{
		FloatSortedSet s;
		UnmodifiableSortedSet(FloatSortedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public FloatComparator comparator() { return s.comparator(); }
		@Override
		public FloatBidirectionalIterator iterator() { return FloatIterators.unmodifiable(s.iterator()); }
		@Override
		public FloatBidirectionalIterator iterator(float fromElement) { return FloatIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public FloatSortedSet copy() { return s.copy(); }
		@Override
		public FloatSortedSet subSet(float fromElement, float toElement) { return FloatSets.unmodifiable(s.subSet(fromElement, toElement)); }
		@Override
		public FloatSortedSet headSet(float toElement) { return FloatSets.unmodifiable(s.headSet(toElement)); }
		@Override
		public FloatSortedSet tailSet(float fromElement) { return FloatSets.unmodifiable(s.tailSet(fromElement)); }
		@Override
		public float firstFloat() { return s.firstFloat(); }
		@Override
		public float pollFirstFloat() { throw new UnsupportedOperationException(); }
		@Override
		public float lastFloat() { return s.lastFloat(); }
		@Override
		public float pollLastFloat() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * Unmodifyable Set wrapper that helps is used with unmodifyableSet function
	 */
	public static class UnmodifiableSet extends UnmodifiableCollection implements FloatSet
	{
		FloatSet s;
		
		protected UnmodifiableSet(FloatSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public FloatSet copy() { return s.copy(); }
		
		@Override
		public boolean remove(float o) { throw new UnsupportedOperationException(); }
	}
	
	private static class SynchronizedNavigableTrimSet extends SynchronizedNavigableSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedNavigableTrimSet(FloatNavigableSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedNavigableTrimSet(FloatNavigableSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedNavigableSet extends SynchronizedSortedSet implements FloatNavigableSet
	{
		FloatNavigableSet n;
		
		SynchronizedNavigableSet(FloatNavigableSet c) {
			super(c);
			n = c;
		}
		
		SynchronizedNavigableSet(FloatNavigableSet c, Object mutex) {
			super(c, mutex);
			n = c;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) { synchronized(mutex) { return n.contains(o); } }

		@Override
		public boolean contains(float o) { synchronized(mutex) { return n.contains(o); } }
		
		@Override
		public float lower(float e) { synchronized(mutex) { return n.lower(e); } }
		
		@Override
		public float floor(float e) { synchronized(mutex) { return n.floor(e); } }
		
		@Override
		public float ceiling(float e) { synchronized(mutex) { return n.ceiling(e); } }
		
		@Override
		public float higher(float e) { synchronized(mutex) { return n.higher(e); } }
		
		@Override
		public void setDefaultMaxValue(float e) { synchronized(mutex) { n.setDefaultMaxValue(e); } }
		
		@Override
		public float getDefaultMaxValue() { synchronized(mutex) { return n.getDefaultMaxValue(); } }
		
		@Override
		public void setDefaultMinValue(float e) { synchronized(mutex) { n.setDefaultMinValue(e); } }
		
		@Override
		public float getDefaultMinValue() { synchronized(mutex) { return n.getDefaultMinValue(); } }
		
		@Override
		public FloatNavigableSet copy() { synchronized(mutex) { return n.copy(); } }
		
		@Override
		public FloatNavigableSet subSet(float fromElement, boolean fromInclusive, float toElement, boolean toInclusive) { synchronized(mutex) { return FloatSets.synchronize(n.subSet(fromElement, fromInclusive, toElement, toInclusive), mutex); } }

		@Override
		public FloatNavigableSet headSet(float toElement, boolean inclusive) { synchronized(mutex) { return FloatSets.synchronize(n.headSet(toElement, inclusive), mutex); } }

		@Override
		public FloatNavigableSet tailSet(float fromElement, boolean inclusive) { synchronized(mutex) { return FloatSets.synchronize(n.tailSet(fromElement, inclusive), mutex); } }

		@Override
		public FloatBidirectionalIterator descendingIterator() { synchronized(mutex) { return n.descendingIterator(); } }

		@Override
		public FloatNavigableSet descendingSet() { synchronized(mutex) { return FloatSets.synchronize(n.descendingSet(), mutex); } }
		
		@Override
		public FloatNavigableSet subSet(float fromElement, float toElement) { synchronized(mutex) { return FloatSets.synchronize(n.subSet(fromElement, toElement), mutex); } }

		@Override
		public FloatNavigableSet headSet(float toElement) { synchronized(mutex) { return FloatSets.synchronize(n.headSet(toElement), mutex); } }

		@Override
		public FloatNavigableSet tailSet(float fromElement) { synchronized(mutex) { return FloatSets.synchronize(n.tailSet(fromElement), mutex); } }
	}
	
	private static class SynchronizedSortedTrimSet extends SynchronizedSortedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedSortedTrimSet(FloatSortedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedSortedTrimSet(FloatSortedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSortedSet extends SynchronizedSet implements FloatSortedSet
	{
		FloatSortedSet s;
		
		SynchronizedSortedSet(FloatSortedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSortedSet(FloatSortedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public FloatComparator comparator(){ synchronized(mutex) { return s.comparator(); } }
		@Override
		public FloatBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public FloatBidirectionalIterator iterator(float fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public FloatSortedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public FloatSortedSet subSet(float fromElement, float toElement) { synchronized(mutex) { return FloatSets.synchronize(s.subSet(fromElement, toElement), mutex); } }
		@Override
		public FloatSortedSet headSet(float toElement) { synchronized(mutex) { return FloatSets.synchronize(s.headSet(toElement), mutex); } }
		@Override
		public FloatSortedSet tailSet(float fromElement) { synchronized(mutex) { return FloatSets.synchronize(s.tailSet(fromElement), mutex); } }
		@Override
		public float firstFloat() { synchronized(mutex) { return s.firstFloat(); } }
		@Override
		public float pollFirstFloat() { synchronized(mutex) { return s.pollFirstFloat(); } }
		@Override
		public float lastFloat() { synchronized(mutex) { return s.lastFloat(); } }
		@Override
		public float pollLastFloat() { synchronized(mutex) { return s.pollLastFloat(); } }
	}
	
	private static class SynchronizedOrderedTrimSet extends SynchronizedOrderedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedOrderedTrimSet(FloatOrderedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedOrderedTrimSet(FloatOrderedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedOrderedSet extends SynchronizedSet implements FloatOrderedSet
	{
		FloatOrderedSet s;
		
		SynchronizedOrderedSet(FloatOrderedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedOrderedSet(FloatOrderedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}

		@Override
		public boolean addAndMoveToFirst(float o) { synchronized(mutex) { return s.addAndMoveToFirst(o); } }
		@Override
		public boolean addAndMoveToLast(float o) { synchronized(mutex) { return s.addAndMoveToLast(o); } }
		@Override
		public boolean moveToFirst(float o) { synchronized(mutex) { return s.moveToFirst(o); } }
		@Override
		public boolean moveToLast(float o) { synchronized(mutex) { return s.moveToLast(o); } }
		@Override
		public FloatBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public FloatBidirectionalIterator iterator(float fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public FloatOrderedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public float firstFloat() { synchronized(mutex) { return s.firstFloat(); } }
		@Override
		public float pollFirstFloat() { synchronized(mutex) { return s.pollFirstFloat(); } }
		@Override
		public float lastFloat() { synchronized(mutex) { return s.lastFloat(); } }
		@Override
		public float pollLastFloat() { synchronized(mutex) { return s.pollLastFloat(); } }
	}
	
	private static class SynchronizedTrimSet extends SynchronizedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedTrimSet(FloatSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedTrimSet(FloatSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSet extends SynchronizedCollection implements FloatSet
	{
		FloatSet s;
		
		SynchronizedSet(FloatSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSet(FloatSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public FloatSet copy() { synchronized(mutex) { return s.copy(); } }
		
		@Override
		public boolean remove(float o) { synchronized(mutex) { return s.remove(o); } }
	}
}