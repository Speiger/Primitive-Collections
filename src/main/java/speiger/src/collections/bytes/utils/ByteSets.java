package speiger.src.collections.bytes.utils;

import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.utils.ByteCollections.EmptyCollection;
import speiger.src.collections.bytes.utils.ByteCollections.SynchronizedCollection;
import speiger.src.collections.bytes.utils.ByteCollections.UnmodifiableCollection;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Helper class for sets
 */
public class ByteSets
{
	/**
	 * Empty Set Variable
	 */
	public static final ByteSet EMPTY = new EmptySet();
	
	/**
	 * EmptySet getter
	 * @return a EmptySet
	 */
	public static ByteSet empty() {
		return EMPTY;
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ByteSet synchronize(ByteSet s) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s) : new SynchronizedSet(s));
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ByteSet synchronize(ByteSet s, Object mutex) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s, mutex) : new SynchronizedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ByteSortedSet synchronize(ByteSortedSet s) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s) : new SynchronizedSortedSet(s));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ByteSortedSet synchronize(ByteSortedSet s, Object mutex) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s, mutex) : new SynchronizedSortedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ByteOrderedSet synchronize(ByteOrderedSet s) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s) : new SynchronizedOrderedSet(s));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ByteOrderedSet synchronize(ByteOrderedSet s, Object mutex) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s, mutex) : new SynchronizedOrderedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ByteNavigableSet synchronize(ByteNavigableSet s) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s) : new SynchronizedNavigableSet(s));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static ByteNavigableSet synchronize(ByteNavigableSet s, Object mutex) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s, mutex) : new SynchronizedNavigableSet(s, mutex));
	}
	
	/**
	 * Creates Unmodifyable Set wrapper
	 * @param s set that should be made unmodifiable
	 * @return a UnmodifyableSet, if the set is already unmodifiable then it returns itself
	 */
	public static ByteSet unmodifiable(ByteSet s) {
		return s instanceof UnmodifiableSet ? s : new UnmodifiableSet(s);
	}
	
	/**
	 * Creates Unmodifyable SortedSet wrapper
	 * @param s sortedSet that should be made unmodifiable
	 * @return a UnmodifyableSortedSet, if the set is already unmodifiable then it returns itself
	 */
	public static ByteSortedSet unmodifiable(ByteSortedSet s) {
		return s instanceof UnmodifiableSortedSet ? s : new UnmodifiableSortedSet(s);
	}
	
	/**
	 * Creates Unmodifyable OrderedSet wrapper
	 * @param s OrderedSet that should be made unmodifiable
	 * @return a UnmodifyableOrderedSet, if the set is already unmodifiable then it returns itself
	 */
	public static ByteOrderedSet unmodifiable(ByteOrderedSet s) {
		return s instanceof UnmodifiableOrderedSet ? s : new UnmodifiableOrderedSet(s);
	}
	
	/**
	 * Creates Unmodifyable NavigableSet wrapper
	 * @param s navigableSet that should be made unmodifiable
	 * @return a UnmodifyableNavigableSet, if the set is already unmodifiable then it returns itself
	 */
	public static ByteNavigableSet unmodifiable(ByteNavigableSet s) {
		return s instanceof UnmodifiableNavigableSet ? s : new UnmodifiableNavigableSet(s);
	}
	
	/**
	 * Creates a Singleton set of a given element
	 * @param element the element that should be converted into a singleton set
	 * @return a singletonset of the given element
	 */
	public static ByteSet singleton(byte element) {
		return new SingletonSet(element);
	}
	
	private static class SingletonSet extends AbstractByteSet
	{
		byte element;
		
		SingletonSet(byte element) {
			this.element = element;
		}
		
		@Override
		public boolean remove(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean add(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public ByteIterator iterator()
		{
			return new ByteIterator() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next = false; }
				@Override
				public byte nextByte() {
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
	
	private static class EmptySet extends EmptyCollection implements ByteSet
	{
		@Override
		public boolean remove(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public EmptySet copy() { return this; }
	}
	
	private static class UnmodifiableNavigableSet extends UnmodifiableSortedSet implements ByteNavigableSet
	{
		ByteNavigableSet n;
		
		UnmodifiableNavigableSet(ByteNavigableSet c) {
			super(c);
			n = c;
		}
		
		@Override
		public boolean contains(byte o) { return n.contains(o); }

		@Override
		@Deprecated
		public boolean contains(Object o) { return n.contains(o); }
		
		@Override
		public byte lower(byte e) { return n.lower(e); }
		
		@Override
		public byte floor(byte e) { return n.floor(e); }
		
		@Override
		public byte ceiling(byte e) { return n.ceiling(e); }
		
		@Override
		public byte higher(byte e) { return n.higher(e); }
		
		@Override
		public void setDefaultMaxValue(byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte getDefaultMaxValue() { return n.getDefaultMaxValue(); }
		
		@Override
		public void setDefaultMinValue(byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte getDefaultMinValue() { return n.getDefaultMinValue(); }
		
		@Override
		public ByteNavigableSet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteNavigableSet subSet(byte fromElement, boolean fromInclusive, byte toElement, boolean toInclusive) { return ByteSets.unmodifiable(n.subSet(fromElement, fromInclusive, toElement, toInclusive)); }
		
		@Override
		public ByteNavigableSet headSet(byte toElement, boolean inclusive) { return ByteSets.unmodifiable(n.headSet(toElement, inclusive)); }

		@Override
		public ByteNavigableSet tailSet(byte fromElement, boolean inclusive) { return ByteSets.unmodifiable(n.tailSet(fromElement, inclusive)); }

		@Override
		public ByteBidirectionalIterator descendingIterator() { return ByteIterators.unmodifiable(n.descendingIterator()); }

		@Override
		public ByteNavigableSet descendingSet() { return ByteSets.unmodifiable(n.descendingSet()); }
		
		@Override
		public ByteNavigableSet subSet(byte fromElement, byte toElement) { return ByteSets.unmodifiable(n.subSet(fromElement, toElement)); }
		
		@Override
		public ByteNavigableSet headSet(byte toElement) { return ByteSets.unmodifiable(n.headSet(toElement)); }
		
		@Override
		public ByteNavigableSet tailSet(byte fromElement) { return ByteSets.unmodifiable(n.tailSet(fromElement)); }
	}
	
	private static class UnmodifiableOrderedSet extends UnmodifiableSet implements ByteOrderedSet
	{
		ByteOrderedSet s;
		UnmodifiableOrderedSet(ByteOrderedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public boolean addAndMoveToFirst(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public ByteBidirectionalIterator iterator() { return ByteIterators.unmodifiable(s.iterator()); }
		@Override
		public ByteBidirectionalIterator iterator(byte fromElement) { return ByteIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public ByteOrderedSet copy() { return s.copy(); }
		@Override
		public byte firstByte() { return s.firstByte(); }
		@Override
		public byte pollFirstByte() { throw new UnsupportedOperationException(); }
		@Override
		public byte lastByte() { return s.lastByte(); }
		@Override
		public byte pollLastByte() { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableSortedSet extends UnmodifiableSet implements ByteSortedSet
	{
		ByteSortedSet s;
		UnmodifiableSortedSet(ByteSortedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public ByteComparator comparator() { return s.comparator(); }
		@Override
		public ByteBidirectionalIterator iterator() { return ByteIterators.unmodifiable(s.iterator()); }
		@Override
		public ByteBidirectionalIterator iterator(byte fromElement) { return ByteIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public ByteSortedSet copy() { return s.copy(); }
		@Override
		public ByteSortedSet subSet(byte fromElement, byte toElement) { return ByteSets.unmodifiable(s.subSet(fromElement, toElement)); }
		@Override
		public ByteSortedSet headSet(byte toElement) { return ByteSets.unmodifiable(s.headSet(toElement)); }
		@Override
		public ByteSortedSet tailSet(byte fromElement) { return ByteSets.unmodifiable(s.tailSet(fromElement)); }
		@Override
		public byte firstByte() { return s.firstByte(); }
		@Override
		public byte pollFirstByte() { throw new UnsupportedOperationException(); }
		@Override
		public byte lastByte() { return s.lastByte(); }
		@Override
		public byte pollLastByte() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * Unmodifyable Set wrapper that helps is used with unmodifyableSet function
	 */
	public static class UnmodifiableSet extends UnmodifiableCollection implements ByteSet
	{
		ByteSet s;
		
		protected UnmodifiableSet(ByteSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public ByteSet copy() { return s.copy(); }
		
		@Override
		public boolean remove(byte o) { throw new UnsupportedOperationException(); }
	}
	
	private static class SynchronizedNavigableTrimSet extends SynchronizedNavigableSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedNavigableTrimSet(ByteNavigableSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedNavigableTrimSet(ByteNavigableSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedNavigableSet extends SynchronizedSortedSet implements ByteNavigableSet
	{
		ByteNavigableSet n;
		
		SynchronizedNavigableSet(ByteNavigableSet c) {
			super(c);
			n = c;
		}
		
		SynchronizedNavigableSet(ByteNavigableSet c, Object mutex) {
			super(c, mutex);
			n = c;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) { synchronized(mutex) { return n.contains(o); } }

		@Override
		public boolean contains(byte o) { synchronized(mutex) { return n.contains(o); } }
		
		@Override
		public byte lower(byte e) { synchronized(mutex) { return n.lower(e); } }
		
		@Override
		public byte floor(byte e) { synchronized(mutex) { return n.floor(e); } }
		
		@Override
		public byte ceiling(byte e) { synchronized(mutex) { return n.ceiling(e); } }
		
		@Override
		public byte higher(byte e) { synchronized(mutex) { return n.higher(e); } }
		
		@Override
		public void setDefaultMaxValue(byte e) { synchronized(mutex) { n.setDefaultMaxValue(e); } }
		
		@Override
		public byte getDefaultMaxValue() { synchronized(mutex) { return n.getDefaultMaxValue(); } }
		
		@Override
		public void setDefaultMinValue(byte e) { synchronized(mutex) { n.setDefaultMinValue(e); } }
		
		@Override
		public byte getDefaultMinValue() { synchronized(mutex) { return n.getDefaultMinValue(); } }
		
		@Override
		public ByteNavigableSet copy() { synchronized(mutex) { return n.copy(); } }
		
		@Override
		public ByteNavigableSet subSet(byte fromElement, boolean fromInclusive, byte toElement, boolean toInclusive) { synchronized(mutex) { return ByteSets.synchronize(n.subSet(fromElement, fromInclusive, toElement, toInclusive), mutex); } }

		@Override
		public ByteNavigableSet headSet(byte toElement, boolean inclusive) { synchronized(mutex) { return ByteSets.synchronize(n.headSet(toElement, inclusive), mutex); } }

		@Override
		public ByteNavigableSet tailSet(byte fromElement, boolean inclusive) { synchronized(mutex) { return ByteSets.synchronize(n.tailSet(fromElement, inclusive), mutex); } }

		@Override
		public ByteBidirectionalIterator descendingIterator() { synchronized(mutex) { return n.descendingIterator(); } }

		@Override
		public ByteNavigableSet descendingSet() { synchronized(mutex) { return ByteSets.synchronize(n.descendingSet(), mutex); } }
		
		@Override
		public ByteNavigableSet subSet(byte fromElement, byte toElement) { synchronized(mutex) { return ByteSets.synchronize(n.subSet(fromElement, toElement), mutex); } }

		@Override
		public ByteNavigableSet headSet(byte toElement) { synchronized(mutex) { return ByteSets.synchronize(n.headSet(toElement), mutex); } }

		@Override
		public ByteNavigableSet tailSet(byte fromElement) { synchronized(mutex) { return ByteSets.synchronize(n.tailSet(fromElement), mutex); } }
	}
	
	private static class SynchronizedSortedTrimSet extends SynchronizedSortedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedSortedTrimSet(ByteSortedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedSortedTrimSet(ByteSortedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSortedSet extends SynchronizedSet implements ByteSortedSet
	{
		ByteSortedSet s;
		
		SynchronizedSortedSet(ByteSortedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSortedSet(ByteSortedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public ByteComparator comparator(){ synchronized(mutex) { return s.comparator(); } }
		@Override
		public ByteBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public ByteBidirectionalIterator iterator(byte fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public ByteSortedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public ByteSortedSet subSet(byte fromElement, byte toElement) { synchronized(mutex) { return ByteSets.synchronize(s.subSet(fromElement, toElement), mutex); } }
		@Override
		public ByteSortedSet headSet(byte toElement) { synchronized(mutex) { return ByteSets.synchronize(s.headSet(toElement), mutex); } }
		@Override
		public ByteSortedSet tailSet(byte fromElement) { synchronized(mutex) { return ByteSets.synchronize(s.tailSet(fromElement), mutex); } }
		@Override
		public byte firstByte() { synchronized(mutex) { return s.firstByte(); } }
		@Override
		public byte pollFirstByte() { synchronized(mutex) { return s.pollFirstByte(); } }
		@Override
		public byte lastByte() { synchronized(mutex) { return s.lastByte(); } }
		@Override
		public byte pollLastByte() { synchronized(mutex) { return s.pollLastByte(); } }
	}
	
	private static class SynchronizedOrderedTrimSet extends SynchronizedOrderedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedOrderedTrimSet(ByteOrderedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedOrderedTrimSet(ByteOrderedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedOrderedSet extends SynchronizedSet implements ByteOrderedSet
	{
		ByteOrderedSet s;
		
		SynchronizedOrderedSet(ByteOrderedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedOrderedSet(ByteOrderedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}

		@Override
		public boolean addAndMoveToFirst(byte o) { synchronized(mutex) { return s.addAndMoveToFirst(o); } }
		@Override
		public boolean addAndMoveToLast(byte o) { synchronized(mutex) { return s.addAndMoveToLast(o); } }
		@Override
		public boolean moveToFirst(byte o) { synchronized(mutex) { return s.moveToFirst(o); } }
		@Override
		public boolean moveToLast(byte o) { synchronized(mutex) { return s.moveToLast(o); } }
		@Override
		public ByteBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public ByteBidirectionalIterator iterator(byte fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public ByteOrderedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public byte firstByte() { synchronized(mutex) { return s.firstByte(); } }
		@Override
		public byte pollFirstByte() { synchronized(mutex) { return s.pollFirstByte(); } }
		@Override
		public byte lastByte() { synchronized(mutex) { return s.lastByte(); } }
		@Override
		public byte pollLastByte() { synchronized(mutex) { return s.pollLastByte(); } }
	}
	
	private static class SynchronizedTrimSet extends SynchronizedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedTrimSet(ByteSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedTrimSet(ByteSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSet extends SynchronizedCollection implements ByteSet
	{
		ByteSet s;
		
		SynchronizedSet(ByteSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSet(ByteSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public ByteSet copy() { synchronized(mutex) { return s.copy(); } }
		
		@Override
		public boolean remove(byte o) { synchronized(mutex) { return s.remove(o); } }
	}
}