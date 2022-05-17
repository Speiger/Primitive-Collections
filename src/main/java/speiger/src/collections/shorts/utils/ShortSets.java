package speiger.src.collections.shorts.utils;

import java.util.NoSuchElementException;
import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.sets.ShortSortedSet;
import speiger.src.collections.shorts.utils.ShortCollections.EmptyCollection;
import speiger.src.collections.shorts.utils.ShortCollections.SynchronizedCollection;
import speiger.src.collections.shorts.utils.ShortCollections.UnmodifiableCollection;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Helper class for sets
 */
public class ShortSets
{
	/**
	 * Empty Set Variable
	 */
	private static final ShortSet EMPTY = new EmptySet();
	
	/**
	 * EmptySet getter
	 * @return a EmptySet
	 */
	public static ShortSet empty() {
		return EMPTY;
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ShortSet synchronize(ShortSet s) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s) : new SynchronizedSet(s));
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ShortSet synchronize(ShortSet s, Object mutex) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s, mutex) : new SynchronizedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ShortSortedSet synchronize(ShortSortedSet s) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s) : new SynchronizedSortedSet(s));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ShortSortedSet synchronize(ShortSortedSet s, Object mutex) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s, mutex) : new SynchronizedSortedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ShortOrderedSet synchronize(ShortOrderedSet s) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s) : new SynchronizedOrderedSet(s));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ShortOrderedSet synchronize(ShortOrderedSet s, Object mutex) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s, mutex) : new SynchronizedOrderedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ShortNavigableSet synchronize(ShortNavigableSet s) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s) : new SynchronizedNavigableSet(s));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ShortNavigableSet synchronize(ShortNavigableSet s, Object mutex) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s, mutex) : new SynchronizedNavigableSet(s, mutex));
	}
	
	/**
	 * Creates Unmodifyable Set wrapper
	 * @param s set that should be made unmodifiable
	 * @return a UnmodifyableSet, if the set is already unmodifiable then it returns itself
	 */
	public static ShortSet unmodifiable(ShortSet s) {
		return s instanceof UnmodifiableSet ? s : new UnmodifiableSet(s);
	}
	
	/**
	 * Creates Unmodifyable SortedSet wrapper
	 * @param s sortedSet that should be made unmodifiable
	 * @return a UnmodifyableSortedSet, if the set is already unmodifiable then it returns itself
	 */
	public static ShortSortedSet unmodifiable(ShortSortedSet s) {
		return s instanceof UnmodifiableSortedSet ? s : new UnmodifiableSortedSet(s);
	}
	
	/**
	 * Creates Unmodifyable OrderedSet wrapper
	 * @param s OrderedSet that should be made unmodifiable
	 * @return a UnmodifyableOrderedSet, if the set is already unmodifiable then it returns itself
	 */
	public static ShortOrderedSet unmodifiable(ShortOrderedSet s) {
		return s instanceof UnmodifiableOrderedSet ? s : new UnmodifiableOrderedSet(s);
	}
	
	/**
	 * Creates Unmodifyable NavigableSet wrapper
	 * @param s navigableSet that should be made unmodifiable
	 * @return a UnmodifyableNavigableSet, if the set is already unmodifiable then it returns itself
	 */
	public static ShortNavigableSet unmodifiable(ShortNavigableSet s) {
		return s instanceof UnmodifiableNavigableSet ? s : new UnmodifiableNavigableSet(s);
	}
	
	/**
	 * Creates a Singleton set of a given element
	 * @param element the element that should be converted into a singleton set
	 * @return a singletonset of the given element
	 */
	public static ShortSet singleton(short element) {
		return new SingletonSet(element);
	}
	
	private static class SingletonSet extends AbstractShortSet
	{
		short element;
		
		SingletonSet(short element) {
			this.element = element;
		}
		
		@Override
		public boolean remove(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean add(short o) { throw new UnsupportedOperationException(); }
		@Override
		public ShortIterator iterator()
		{
			return new ShortIterator() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next; }
				@Override
				public short nextShort() {
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
	
	private static class EmptySet extends EmptyCollection implements ShortSet
	{
		@Override
		public boolean remove(short o) { throw new UnsupportedOperationException(); }
		@Override
		public EmptySet copy() { return this; }
	}
	
	private static class UnmodifiableNavigableSet extends UnmodifiableSortedSet implements ShortNavigableSet
	{
		ShortNavigableSet n;
		
		UnmodifiableNavigableSet(ShortNavigableSet c) {
			super(c);
			n = c;
		}
		
		@Override
		public boolean contains(short o) { return n.contains(o); }

		@Override
		@Deprecated
		public boolean contains(Object o) { return n.contains(o); }
		
		@Override
		public short lower(short e) { return n.lower(e); }
		
		@Override
		public short floor(short e) { return n.floor(e); }
		
		@Override
		public short ceiling(short e) { return n.ceiling(e); }
		
		@Override
		public short higher(short e) { return n.higher(e); }
		
		@Override
		public void setDefaultMaxValue(short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public short getDefaultMaxValue() { return n.getDefaultMaxValue(); }
		
		@Override
		public void setDefaultMinValue(short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public short getDefaultMinValue() { return n.getDefaultMinValue(); }
		
		@Override
		public ShortNavigableSet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortNavigableSet subSet(short fromElement, boolean fromInclusive, short toElement, boolean toInclusive) { return ShortSets.unmodifiable(n.subSet(fromElement, fromInclusive, toElement, toInclusive)); }
		
		@Override
		public ShortNavigableSet headSet(short toElement, boolean inclusive) { return ShortSets.unmodifiable(n.headSet(toElement, inclusive)); }

		@Override
		public ShortNavigableSet tailSet(short fromElement, boolean inclusive) { return ShortSets.unmodifiable(n.tailSet(fromElement, inclusive)); }

		@Override
		public ShortBidirectionalIterator descendingIterator() { return ShortIterators.unmodifiable(n.descendingIterator()); }

		@Override
		public ShortNavigableSet descendingSet() { return ShortSets.unmodifiable(n.descendingSet()); }
		
		@Override
		public ShortNavigableSet subSet(short fromElement, short toElement) { return ShortSets.unmodifiable(n.subSet(fromElement, toElement)); }
		
		@Override
		public ShortNavigableSet headSet(short toElement) { return ShortSets.unmodifiable(n.headSet(toElement)); }
		
		@Override
		public ShortNavigableSet tailSet(short fromElement) { return ShortSets.unmodifiable(n.tailSet(fromElement)); }
	}
	
	private static class UnmodifiableOrderedSet extends UnmodifiableSet implements ShortOrderedSet
	{
		ShortOrderedSet s;
		UnmodifiableOrderedSet(ShortOrderedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public boolean addAndMoveToFirst(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(short o) { throw new UnsupportedOperationException(); }
		@Override
		public ShortBidirectionalIterator iterator() { return ShortIterators.unmodifiable(s.iterator()); }
		@Override
		public ShortBidirectionalIterator iterator(short fromElement) { return ShortIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public ShortOrderedSet copy() { return s.copy(); }
		@Override
		public short firstShort() { return s.firstShort(); }
		@Override
		public short pollFirstShort() { throw new UnsupportedOperationException(); }
		@Override
		public short lastShort() { return s.lastShort(); }
		@Override
		public short pollLastShort() { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableSortedSet extends UnmodifiableSet implements ShortSortedSet
	{
		ShortSortedSet s;
		UnmodifiableSortedSet(ShortSortedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public ShortComparator comparator() { return s.comparator(); }
		@Override
		public ShortBidirectionalIterator iterator() { return ShortIterators.unmodifiable(s.iterator()); }
		@Override
		public ShortBidirectionalIterator iterator(short fromElement) { return ShortIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public ShortSortedSet copy() { return s.copy(); }
		@Override
		public ShortSortedSet subSet(short fromElement, short toElement) { return ShortSets.unmodifiable(s.subSet(fromElement, toElement)); }
		@Override
		public ShortSortedSet headSet(short toElement) { return ShortSets.unmodifiable(s.headSet(toElement)); }
		@Override
		public ShortSortedSet tailSet(short fromElement) { return ShortSets.unmodifiable(s.tailSet(fromElement)); }
		@Override
		public short firstShort() { return s.firstShort(); }
		@Override
		public short pollFirstShort() { throw new UnsupportedOperationException(); }
		@Override
		public short lastShort() { return s.lastShort(); }
		@Override
		public short pollLastShort() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * Unmodifyable Set wrapper that helps is used with unmodifyableSet function
	 */
	public static class UnmodifiableSet extends UnmodifiableCollection implements ShortSet
	{
		ShortSet s;
		
		protected UnmodifiableSet(ShortSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public ShortSet copy() { return s.copy(); }
		
		@Override
		public boolean remove(short o) { throw new UnsupportedOperationException(); }
	}
	
	private static class SynchronizedNavigableTrimSet extends SynchronizedNavigableSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedNavigableTrimSet(ShortNavigableSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedNavigableTrimSet(ShortNavigableSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedNavigableSet extends SynchronizedSortedSet implements ShortNavigableSet
	{
		ShortNavigableSet n;
		
		SynchronizedNavigableSet(ShortNavigableSet c) {
			super(c);
			n = c;
		}
		
		SynchronizedNavigableSet(ShortNavigableSet c, Object mutex) {
			super(c, mutex);
			n = c;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) { synchronized(mutex) { return n.contains(o); } }

		@Override
		public boolean contains(short o) { synchronized(mutex) { return n.contains(o); } }
		
		@Override
		public short lower(short e) { synchronized(mutex) { return n.lower(e); } }
		
		@Override
		public short floor(short e) { synchronized(mutex) { return n.floor(e); } }
		
		@Override
		public short ceiling(short e) { synchronized(mutex) { return n.ceiling(e); } }
		
		@Override
		public short higher(short e) { synchronized(mutex) { return n.higher(e); } }
		
		@Override
		public void setDefaultMaxValue(short e) { synchronized(mutex) { n.setDefaultMaxValue(e); } }
		
		@Override
		public short getDefaultMaxValue() { synchronized(mutex) { return n.getDefaultMaxValue(); } }
		
		@Override
		public void setDefaultMinValue(short e) { synchronized(mutex) { n.setDefaultMinValue(e); } }
		
		@Override
		public short getDefaultMinValue() { synchronized(mutex) { return n.getDefaultMinValue(); } }
		
		@Override
		public ShortNavigableSet copy() { synchronized(mutex) { return n.copy(); } }
		
		@Override
		public ShortNavigableSet subSet(short fromElement, boolean fromInclusive, short toElement, boolean toInclusive) { synchronized(mutex) { return ShortSets.synchronize(n.subSet(fromElement, fromInclusive, toElement, toInclusive), mutex); } }

		@Override
		public ShortNavigableSet headSet(short toElement, boolean inclusive) { synchronized(mutex) { return ShortSets.synchronize(n.headSet(toElement, inclusive), mutex); } }

		@Override
		public ShortNavigableSet tailSet(short fromElement, boolean inclusive) { synchronized(mutex) { return ShortSets.synchronize(n.tailSet(fromElement, inclusive), mutex); } }

		@Override
		public ShortBidirectionalIterator descendingIterator() { synchronized(mutex) { return n.descendingIterator(); } }

		@Override
		public ShortNavigableSet descendingSet() { synchronized(mutex) { return ShortSets.synchronize(n.descendingSet(), mutex); } }
		
		@Override
		public ShortNavigableSet subSet(short fromElement, short toElement) { synchronized(mutex) { return ShortSets.synchronize(n.subSet(fromElement, toElement), mutex); } }

		@Override
		public ShortNavigableSet headSet(short toElement) { synchronized(mutex) { return ShortSets.synchronize(n.headSet(toElement), mutex); } }

		@Override
		public ShortNavigableSet tailSet(short fromElement) { synchronized(mutex) { return ShortSets.synchronize(n.tailSet(fromElement), mutex); } }
	}
	
	private static class SynchronizedSortedTrimSet extends SynchronizedSortedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedSortedTrimSet(ShortSortedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedSortedTrimSet(ShortSortedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSortedSet extends SynchronizedSet implements ShortSortedSet
	{
		ShortSortedSet s;
		
		SynchronizedSortedSet(ShortSortedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSortedSet(ShortSortedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public ShortComparator comparator(){ synchronized(mutex) { return s.comparator(); } }
		@Override
		public ShortBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public ShortBidirectionalIterator iterator(short fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public ShortSortedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public ShortSortedSet subSet(short fromElement, short toElement) { synchronized(mutex) { return ShortSets.synchronize(s.subSet(fromElement, toElement), mutex); } }
		@Override
		public ShortSortedSet headSet(short toElement) { synchronized(mutex) { return ShortSets.synchronize(s.headSet(toElement), mutex); } }
		@Override
		public ShortSortedSet tailSet(short fromElement) { synchronized(mutex) { return ShortSets.synchronize(s.tailSet(fromElement), mutex); } }
		@Override
		public short firstShort() { synchronized(mutex) { return s.firstShort(); } }
		@Override
		public short pollFirstShort() { synchronized(mutex) { return s.pollFirstShort(); } }
		@Override
		public short lastShort() { synchronized(mutex) { return s.lastShort(); } }
		@Override
		public short pollLastShort() { synchronized(mutex) { return s.pollLastShort(); } }
	}
	
	private static class SynchronizedOrderedTrimSet extends SynchronizedOrderedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedOrderedTrimSet(ShortOrderedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedOrderedTrimSet(ShortOrderedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedOrderedSet extends SynchronizedSet implements ShortOrderedSet
	{
		ShortOrderedSet s;
		
		SynchronizedOrderedSet(ShortOrderedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedOrderedSet(ShortOrderedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}

		@Override
		public boolean addAndMoveToFirst(short o) { synchronized(mutex) { return s.addAndMoveToFirst(o); } }
		@Override
		public boolean addAndMoveToLast(short o) { synchronized(mutex) { return s.addAndMoveToLast(o); } }
		@Override
		public boolean moveToFirst(short o) { synchronized(mutex) { return s.moveToFirst(o); } }
		@Override
		public boolean moveToLast(short o) { synchronized(mutex) { return s.moveToLast(o); } }
		@Override
		public ShortBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public ShortBidirectionalIterator iterator(short fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public ShortOrderedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public short firstShort() { synchronized(mutex) { return s.firstShort(); } }
		@Override
		public short pollFirstShort() { synchronized(mutex) { return s.pollFirstShort(); } }
		@Override
		public short lastShort() { synchronized(mutex) { return s.lastShort(); } }
		@Override
		public short pollLastShort() { synchronized(mutex) { return s.pollLastShort(); } }
	}
	
	private static class SynchronizedTrimSet extends SynchronizedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedTrimSet(ShortSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedTrimSet(ShortSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSet extends SynchronizedCollection implements ShortSet
	{
		ShortSet s;
		
		SynchronizedSet(ShortSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSet(ShortSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public ShortSet copy() { synchronized(mutex) { return s.copy(); } }
		
		@Override
		public boolean remove(short o) { synchronized(mutex) { return s.remove(o); } }
	}
}