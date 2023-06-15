package speiger.src.collections.longs.utils;

import java.util.NoSuchElementException;
import java.util.Set;
import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.collections.longs.utils.LongCollections.EmptyCollection;
import speiger.src.collections.longs.utils.LongCollections.SynchronizedCollection;
import speiger.src.collections.longs.utils.LongCollections.UnmodifiableCollection;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Helper class for sets
 */
public class LongSets
{
	/**
	 * Empty Set Variable
	 */
	private static final LongSet EMPTY = new EmptySet();
	
	/**
	 * EmptySet getter
	 * @return a EmptySet
	 */
	public static LongSet empty() {
		return EMPTY;
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static LongSet synchronize(LongSet s) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s) : new SynchronizedSet(s));
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static LongSet synchronize(LongSet s, Object mutex) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s, mutex) : new SynchronizedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static LongSortedSet synchronize(LongSortedSet s) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s) : new SynchronizedSortedSet(s));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static LongSortedSet synchronize(LongSortedSet s, Object mutex) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s, mutex) : new SynchronizedSortedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static LongOrderedSet synchronize(LongOrderedSet s) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s) : new SynchronizedOrderedSet(s));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static LongOrderedSet synchronize(LongOrderedSet s, Object mutex) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s, mutex) : new SynchronizedOrderedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static LongNavigableSet synchronize(LongNavigableSet s) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s) : new SynchronizedNavigableSet(s));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static LongNavigableSet synchronize(LongNavigableSet s, Object mutex) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s, mutex) : new SynchronizedNavigableSet(s, mutex));
	}
	
	/**
	 * Creates Unmodifyable Set wrapper
	 * @param s set that should be made unmodifiable
	 * @return a UnmodifyableSet, if the set is already unmodifiable then it returns itself
	 */
	public static LongSet unmodifiable(LongSet s) {
		return s instanceof UnmodifiableSet ? s : new UnmodifiableSet(s);
	}
	
	/**
	 * Creates Unmodifyable SortedSet wrapper
	 * @param s sortedSet that should be made unmodifiable
	 * @return a UnmodifyableSortedSet, if the set is already unmodifiable then it returns itself
	 */
	public static LongSortedSet unmodifiable(LongSortedSet s) {
		return s instanceof UnmodifiableSortedSet ? s : new UnmodifiableSortedSet(s);
	}
	
	/**
	 * Creates Unmodifyable OrderedSet wrapper
	 * @param s OrderedSet that should be made unmodifiable
	 * @return a UnmodifyableOrderedSet, if the set is already unmodifiable then it returns itself
	 */
	public static LongOrderedSet unmodifiable(LongOrderedSet s) {
		return s instanceof UnmodifiableOrderedSet ? s : new UnmodifiableOrderedSet(s);
	}
	
	/**
	 * Creates Unmodifyable NavigableSet wrapper
	 * @param s navigableSet that should be made unmodifiable
	 * @return a UnmodifyableNavigableSet, if the set is already unmodifiable then it returns itself
	 */
	public static LongNavigableSet unmodifiable(LongNavigableSet s) {
		return s instanceof UnmodifiableNavigableSet ? s : new UnmodifiableNavigableSet(s);
	}
	
	/**
	 * Creates a Singleton set of a given element
	 * @param element the element that should be converted into a singleton set
	 * @return a singletonset of the given element
	 */
	public static LongSet singleton(long element) {
		return new SingletonSet(element);
	}
	
	private static class SingletonSet extends AbstractLongSet
	{
		long element;
		
		SingletonSet(long element) {
			this.element = element;
		}
		
		@Override
		public boolean remove(long o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean add(long o) { throw new UnsupportedOperationException(); }
		@Override
		public LongIterator iterator()
		{
			return new LongIterator() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next; }
				@Override
				public long nextLong() {
					if(!hasNext()) throw new NoSuchElementException();
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
	
	private static class EmptySet extends EmptyCollection implements LongSet
	{
		@Override
		public boolean remove(long o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean equals(Object o) {
			if(o == this) return true;
			if(!(o instanceof Set)) return false;
			return ((Set<?>)o).isEmpty();
		}
		
		@Override
		public EmptySet copy() { return this; }
	}
	
	private static class UnmodifiableNavigableSet extends UnmodifiableSortedSet implements LongNavigableSet
	{
		LongNavigableSet n;
		
		UnmodifiableNavigableSet(LongNavigableSet c) {
			super(c);
			n = c;
		}
		
		@Override
		public boolean contains(long o) { return n.contains(o); }
		@Override
		@SuppressWarnings("deprecation")
		public Long lower(Long e) { return n.lower(e); }
		@Override
		@SuppressWarnings("deprecation")
		public Long floor(Long e) { return n.floor(e); }
		@Override
		@SuppressWarnings("deprecation")
		public Long ceiling(Long e) { return n.ceiling(e); }
		@Override
		@SuppressWarnings("deprecation")
		public Long higher(Long e) { return n.higher(e); }
		
		@Override
		@Deprecated
		public boolean contains(Object o) { return n.contains(o); }
		@Override
		public long lower(long e) { return n.lower(e); }
		@Override
		public long floor(long e) { return n.floor(e); }
		@Override
		public long ceiling(long e) { return n.ceiling(e); }
		@Override
		public long higher(long e) { return n.higher(e); }
		
		@Override
		public Long pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Long pollLast() { throw new UnsupportedOperationException(); }

		@Override
		public void setDefaultMaxValue(long e) { throw new UnsupportedOperationException(); }
		
		@Override
		public long getDefaultMaxValue() { return n.getDefaultMaxValue(); }
		
		@Override
		public void setDefaultMinValue(long e) { throw new UnsupportedOperationException(); }
		
		@Override
		public long getDefaultMinValue() { return n.getDefaultMinValue(); }
		
		@Override
		public LongNavigableSet copy() { return n.copy(); }
		
		@Override
		public LongNavigableSet subSet(long fromElement, boolean fromInclusive, long toElement, boolean toInclusive) { return LongSets.unmodifiable(n.subSet(fromElement, fromInclusive, toElement, toInclusive)); }
		
		@Override
		public LongNavigableSet headSet(long toElement, boolean inclusive) { return LongSets.unmodifiable(n.headSet(toElement, inclusive)); }

		@Override
		public LongNavigableSet tailSet(long fromElement, boolean inclusive) { return LongSets.unmodifiable(n.tailSet(fromElement, inclusive)); }

		@Override
		public LongBidirectionalIterator descendingIterator() { return LongIterators.unmodifiable(n.descendingIterator()); }

		@Override
		public LongNavigableSet descendingSet() { return LongSets.unmodifiable(n.descendingSet()); }
		
		@Override
		public LongNavigableSet subSet(long fromElement, long toElement) { return LongSets.unmodifiable(n.subSet(fromElement, toElement)); }
		
		@Override
		public LongNavigableSet headSet(long toElement) { return LongSets.unmodifiable(n.headSet(toElement)); }
		
		@Override
		public LongNavigableSet tailSet(long fromElement) { return LongSets.unmodifiable(n.tailSet(fromElement)); }
	}
	
	private static class UnmodifiableOrderedSet extends UnmodifiableSet implements LongOrderedSet
	{
		LongOrderedSet s;
		UnmodifiableOrderedSet(LongOrderedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public boolean addAndMoveToFirst(long o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(long o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(long o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(long o) { throw new UnsupportedOperationException(); }
		@Override
		public LongBidirectionalIterator iterator() { return LongIterators.unmodifiable(s.iterator()); }
		@Override
		public LongBidirectionalIterator iterator(long fromElement) { return LongIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public LongOrderedSet copy() { return s.copy(); }
		@Override
		public long firstLong() { return s.firstLong(); }
		@Override
		public long pollFirstLong() { throw new UnsupportedOperationException(); }
		@Override
		public long lastLong() { return s.lastLong(); }
		@Override
		public long pollLastLong() { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableSortedSet extends UnmodifiableSet implements LongSortedSet
	{
		LongSortedSet s;
		UnmodifiableSortedSet(LongSortedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public LongComparator comparator() { return s.comparator(); }
		@Override
		public LongBidirectionalIterator iterator() { return LongIterators.unmodifiable(s.iterator()); }
		@Override
		public LongBidirectionalIterator iterator(long fromElement) { return LongIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public LongSortedSet copy() { return s.copy(); }
		@Override
		public LongSortedSet subSet(long fromElement, long toElement) { return LongSets.unmodifiable(s.subSet(fromElement, toElement)); }
		@Override
		public LongSortedSet headSet(long toElement) { return LongSets.unmodifiable(s.headSet(toElement)); }
		@Override
		public LongSortedSet tailSet(long fromElement) { return LongSets.unmodifiable(s.tailSet(fromElement)); }
		@Override
		public long firstLong() { return s.firstLong(); }
		@Override
		public long pollFirstLong() { throw new UnsupportedOperationException(); }
		@Override
		public long lastLong() { return s.lastLong(); }
		@Override
		public long pollLastLong() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * Unmodifyable Set wrapper that helps is used with unmodifyableSet function
	 */
	public static class UnmodifiableSet extends UnmodifiableCollection implements LongSet
	{
		LongSet s;
		
		protected UnmodifiableSet(LongSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public LongSet copy() { return s.copy(); }
		
		@Override
		public boolean remove(long o) { throw new UnsupportedOperationException(); }
	}
	
	private static class SynchronizedNavigableTrimSet extends SynchronizedNavigableSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedNavigableTrimSet(LongNavigableSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedNavigableTrimSet(LongNavigableSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedNavigableSet extends SynchronizedSortedSet implements LongNavigableSet
	{
		LongNavigableSet n;
		
		SynchronizedNavigableSet(LongNavigableSet c) {
			super(c);
			n = c;
		}
		
		SynchronizedNavigableSet(LongNavigableSet c, Object mutex) {
			super(c, mutex);
			n = c;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) { synchronized(mutex) { return n.contains(o); } }

		@Override
		public boolean contains(long o) { synchronized(mutex) { return n.contains(o); } }
		@Override
		@SuppressWarnings("deprecation")
		public Long lower(Long e) { synchronized(mutex) { return n.lower(e); } }
		@Override
		@SuppressWarnings("deprecation")
		public Long floor(Long e) { synchronized(mutex) { return n.floor(e); } }
		@Override
		@SuppressWarnings("deprecation")
		public Long ceiling(Long e) { synchronized(mutex) { return n.ceiling(e); } }
		@Override
		@SuppressWarnings("deprecation")
		public Long higher(Long e) { synchronized(mutex) { return n.higher(e); } }
		@Override
		public long lower(long e) { synchronized(mutex) { return n.lower(e); } }
		@Override
		public long floor(long e) { synchronized(mutex) { return n.floor(e); } }
		@Override
		public long ceiling(long e) { synchronized(mutex) { return n.ceiling(e); } }
		@Override
		public long higher(long e) { synchronized(mutex) { return n.higher(e); } }
		
		@Override
		public void setDefaultMaxValue(long e) { synchronized(mutex) { n.setDefaultMaxValue(e); } }
		@Override
		public long getDefaultMaxValue() { synchronized(mutex) { return n.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(long e) { synchronized(mutex) { n.setDefaultMinValue(e); } }
		
		@Override
		public long getDefaultMinValue() { synchronized(mutex) { return n.getDefaultMinValue(); } }
		
		@Override
		public LongNavigableSet copy() { synchronized(mutex) { return n.copy(); } }
		
		@Override
		public LongNavigableSet subSet(long fromElement, boolean fromInclusive, long toElement, boolean toInclusive) { synchronized(mutex) { return LongSets.synchronize(n.subSet(fromElement, fromInclusive, toElement, toInclusive), mutex); } }

		@Override
		public LongNavigableSet headSet(long toElement, boolean inclusive) { synchronized(mutex) { return LongSets.synchronize(n.headSet(toElement, inclusive), mutex); } }

		@Override
		public LongNavigableSet tailSet(long fromElement, boolean inclusive) { synchronized(mutex) { return LongSets.synchronize(n.tailSet(fromElement, inclusive), mutex); } }

		@Override
		public LongBidirectionalIterator descendingIterator() { synchronized(mutex) { return n.descendingIterator(); } }

		@Override
		public LongNavigableSet descendingSet() { synchronized(mutex) { return LongSets.synchronize(n.descendingSet(), mutex); } }
		
		@Override
		public LongNavigableSet subSet(long fromElement, long toElement) { synchronized(mutex) { return LongSets.synchronize(n.subSet(fromElement, toElement), mutex); } }

		@Override
		public LongNavigableSet headSet(long toElement) { synchronized(mutex) { return LongSets.synchronize(n.headSet(toElement), mutex); } }

		@Override
		public LongNavigableSet tailSet(long fromElement) { synchronized(mutex) { return LongSets.synchronize(n.tailSet(fromElement), mutex); } }
	}
	
	private static class SynchronizedSortedTrimSet extends SynchronizedSortedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedSortedTrimSet(LongSortedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedSortedTrimSet(LongSortedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSortedSet extends SynchronizedSet implements LongSortedSet
	{
		LongSortedSet s;
		
		SynchronizedSortedSet(LongSortedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSortedSet(LongSortedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public LongComparator comparator(){ synchronized(mutex) { return s.comparator(); } }
		@Override
		public LongBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public LongBidirectionalIterator iterator(long fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public LongSortedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public LongSortedSet subSet(long fromElement, long toElement) { synchronized(mutex) { return LongSets.synchronize(s.subSet(fromElement, toElement), mutex); } }
		@Override
		public LongSortedSet headSet(long toElement) { synchronized(mutex) { return LongSets.synchronize(s.headSet(toElement), mutex); } }
		@Override
		public LongSortedSet tailSet(long fromElement) { synchronized(mutex) { return LongSets.synchronize(s.tailSet(fromElement), mutex); } }
		@Override
		public long firstLong() { synchronized(mutex) { return s.firstLong(); } }
		@Override
		public long pollFirstLong() { synchronized(mutex) { return s.pollFirstLong(); } }
		@Override
		public long lastLong() { synchronized(mutex) { return s.lastLong(); } }
		@Override
		public long pollLastLong() { synchronized(mutex) { return s.pollLastLong(); } }
	}
	
	private static class SynchronizedOrderedTrimSet extends SynchronizedOrderedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedOrderedTrimSet(LongOrderedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedOrderedTrimSet(LongOrderedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedOrderedSet extends SynchronizedSet implements LongOrderedSet
	{
		LongOrderedSet s;
		
		SynchronizedOrderedSet(LongOrderedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedOrderedSet(LongOrderedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}

		@Override
		public boolean addAndMoveToFirst(long o) { synchronized(mutex) { return s.addAndMoveToFirst(o); } }
		@Override
		public boolean addAndMoveToLast(long o) { synchronized(mutex) { return s.addAndMoveToLast(o); } }
		@Override
		public boolean moveToFirst(long o) { synchronized(mutex) { return s.moveToFirst(o); } }
		@Override
		public boolean moveToLast(long o) { synchronized(mutex) { return s.moveToLast(o); } }
		@Override
		public LongBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public LongBidirectionalIterator iterator(long fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public LongOrderedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public long firstLong() { synchronized(mutex) { return s.firstLong(); } }
		@Override
		public long pollFirstLong() { synchronized(mutex) { return s.pollFirstLong(); } }
		@Override
		public long lastLong() { synchronized(mutex) { return s.lastLong(); } }
		@Override
		public long pollLastLong() { synchronized(mutex) { return s.pollLastLong(); } }
	}
	
	private static class SynchronizedTrimSet extends SynchronizedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedTrimSet(LongSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedTrimSet(LongSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSet extends SynchronizedCollection implements LongSet
	{
		LongSet s;
		
		SynchronizedSet(LongSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSet(LongSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public LongSet copy() { synchronized(mutex) { return s.copy(); } }
		
		@Override
		public boolean remove(long o) { synchronized(mutex) { return s.remove(o); } }
	}
}