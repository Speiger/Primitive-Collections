package speiger.src.collections.chars.utils;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.utils.CharCollections.EmptyCollection;
import speiger.src.collections.chars.utils.CharCollections.SynchronizedCollection;
import speiger.src.collections.chars.utils.CharCollections.UnmodifiableCollection;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Helper class for sets
 */
public class CharSets
{
	/**
	 * Empty Set Variable
	 */
	public static final CharSet EMPTY = new EmptySet();
	
	/**
	 * EmptySet getter
	 * @return a EmptySet
	 */
	public static CharSet empty() {
		return EMPTY;
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static CharSet synchronize(CharSet s) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s) : new SynchronizedSet(s));
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static CharSet synchronize(CharSet s, Object mutex) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet(s, mutex) : new SynchronizedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static CharSortedSet synchronize(CharSortedSet s) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s) : new SynchronizedSortedSet(s));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static CharSortedSet synchronize(CharSortedSet s, Object mutex) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet(s, mutex) : new SynchronizedSortedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static CharOrderedSet synchronize(CharOrderedSet s) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s) : new SynchronizedOrderedSet(s));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static CharOrderedSet synchronize(CharOrderedSet s, Object mutex) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet(s, mutex) : new SynchronizedOrderedSet(s, mutex));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static CharNavigableSet synchronize(CharNavigableSet s) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s) : new SynchronizedNavigableSet(s));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static CharNavigableSet synchronize(CharNavigableSet s, Object mutex) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet(s, mutex) : new SynchronizedNavigableSet(s, mutex));
	}
	
	/**
	 * Creates Unmodifyable Set wrapper
	 * @param s set that should be made unmodifiable
	 * @return a UnmodifyableSet, if the set is already unmodifiable then it returns itself
	 */
	public static CharSet unmodifiable(CharSet s) {
		return s instanceof UnmodifiableSet ? s : new UnmodifiableSet(s);
	}
	
	/**
	 * Creates Unmodifyable SortedSet wrapper
	 * @param s sortedSet that should be made unmodifiable
	 * @return a UnmodifyableSortedSet, if the set is already unmodifiable then it returns itself
	 */
	public static CharSortedSet unmodifiable(CharSortedSet s) {
		return s instanceof UnmodifiableSortedSet ? s : new UnmodifiableSortedSet(s);
	}
	
	/**
	 * Creates Unmodifyable OrderedSet wrapper
	 * @param s OrderedSet that should be made unmodifiable
	 * @return a UnmodifyableOrderedSet, if the set is already unmodifiable then it returns itself
	 */
	public static CharOrderedSet unmodifiable(CharOrderedSet s) {
		return s instanceof UnmodifiableOrderedSet ? s : new UnmodifiableOrderedSet(s);
	}
	
	/**
	 * Creates Unmodifyable NavigableSet wrapper
	 * @param s navigableSet that should be made unmodifiable
	 * @return a UnmodifyableNavigableSet, if the set is already unmodifiable then it returns itself
	 */
	public static CharNavigableSet unmodifiable(CharNavigableSet s) {
		return s instanceof UnmodifiableNavigableSet ? s : new UnmodifiableNavigableSet(s);
	}
	
	/**
	 * Creates a Singleton set of a given element
	 * @param element the element that should be converted into a singleton set
	 * @return a singletonset of the given element
	 */
	public static CharSet singleton(char element) {
		return new SingletonSet(element);
	}
	
	private static class SingletonSet extends AbstractCharSet
	{
		char element;
		
		SingletonSet(char element) {
			this.element = element;
		}
		
		@Override
		public boolean remove(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean add(char o) { throw new UnsupportedOperationException(); }
		@Override
		public CharIterator iterator()
		{
			return new CharIterator() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next = false; }
				@Override
				public char nextChar() {
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
	
	private static class EmptySet extends EmptyCollection implements CharSet
	{
		@Override
		public boolean remove(char o) { throw new UnsupportedOperationException(); }
		@Override
		public EmptySet copy() { return this; }
	}
	
	private static class UnmodifiableNavigableSet extends UnmodifiableSortedSet implements CharNavigableSet
	{
		CharNavigableSet n;
		
		UnmodifiableNavigableSet(CharNavigableSet c) {
			super(c);
			n = c;
		}
		
		@Override
		public boolean contains(char o) { return n.contains(o); }

		@Override
		@Deprecated
		public boolean contains(Object o) { return n.contains(o); }
		
		@Override
		public char lower(char e) { return n.lower(e); }
		
		@Override
		public char floor(char e) { return n.floor(e); }
		
		@Override
		public char ceiling(char e) { return n.ceiling(e); }
		
		@Override
		public char higher(char e) { return n.higher(e); }
		
		@Override
		public void setDefaultMaxValue(char e) { throw new UnsupportedOperationException(); }
		
		@Override
		public char getDefaultMaxValue() { return n.getDefaultMaxValue(); }
		
		@Override
		public void setDefaultMinValue(char e) { throw new UnsupportedOperationException(); }
		
		@Override
		public char getDefaultMinValue() { return n.getDefaultMinValue(); }
		
		@Override
		public CharNavigableSet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public CharNavigableSet subSet(char fromElement, boolean fromInclusive, char toElement, boolean toInclusive) { return CharSets.unmodifiable(n.subSet(fromElement, fromInclusive, toElement, toInclusive)); }
		
		@Override
		public CharNavigableSet headSet(char toElement, boolean inclusive) { return CharSets.unmodifiable(n.headSet(toElement, inclusive)); }

		@Override
		public CharNavigableSet tailSet(char fromElement, boolean inclusive) { return CharSets.unmodifiable(n.tailSet(fromElement, inclusive)); }

		@Override
		public CharBidirectionalIterator descendingIterator() { return CharIterators.unmodifiable(n.descendingIterator()); }

		@Override
		public CharNavigableSet descendingSet() { return CharSets.unmodifiable(n.descendingSet()); }
		
		@Override
		public CharNavigableSet subSet(char fromElement, char toElement) { return CharSets.unmodifiable(n.subSet(fromElement, toElement)); }
		
		@Override
		public CharNavigableSet headSet(char toElement) { return CharSets.unmodifiable(n.headSet(toElement)); }
		
		@Override
		public CharNavigableSet tailSet(char fromElement) { return CharSets.unmodifiable(n.tailSet(fromElement)); }
	}
	
	private static class UnmodifiableOrderedSet extends UnmodifiableSet implements CharOrderedSet
	{
		CharOrderedSet s;
		UnmodifiableOrderedSet(CharOrderedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public boolean addAndMoveToFirst(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(char o) { throw new UnsupportedOperationException(); }
		@Override
		public CharBidirectionalIterator iterator() { return CharIterators.unmodifiable(s.iterator()); }
		@Override
		public CharBidirectionalIterator iterator(char fromElement) { return CharIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public CharOrderedSet copy() { return s.copy(); }
		@Override
		public char firstChar() { return s.firstChar(); }
		@Override
		public char pollFirstChar() { throw new UnsupportedOperationException(); }
		@Override
		public char lastChar() { return s.lastChar(); }
		@Override
		public char pollLastChar() { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableSortedSet extends UnmodifiableSet implements CharSortedSet
	{
		CharSortedSet s;
		UnmodifiableSortedSet(CharSortedSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public CharComparator comparator() { return s.comparator(); }
		@Override
		public CharBidirectionalIterator iterator() { return CharIterators.unmodifiable(s.iterator()); }
		@Override
		public CharBidirectionalIterator iterator(char fromElement) { return CharIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public CharSortedSet copy() { return s.copy(); }
		@Override
		public CharSortedSet subSet(char fromElement, char toElement) { return CharSets.unmodifiable(s.subSet(fromElement, toElement)); }
		@Override
		public CharSortedSet headSet(char toElement) { return CharSets.unmodifiable(s.headSet(toElement)); }
		@Override
		public CharSortedSet tailSet(char fromElement) { return CharSets.unmodifiable(s.tailSet(fromElement)); }
		@Override
		public char firstChar() { return s.firstChar(); }
		@Override
		public char pollFirstChar() { throw new UnsupportedOperationException(); }
		@Override
		public char lastChar() { return s.lastChar(); }
		@Override
		public char pollLastChar() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * Unmodifyable Set wrapper that helps is used with unmodifyableSet function
	 */
	public static class UnmodifiableSet extends UnmodifiableCollection implements CharSet
	{
		CharSet s;
		
		protected UnmodifiableSet(CharSet c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public CharSet copy() { return s.copy(); }
		
		@Override
		public boolean remove(char o) { throw new UnsupportedOperationException(); }
	}
	
	private static class SynchronizedNavigableTrimSet extends SynchronizedNavigableSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedNavigableTrimSet(CharNavigableSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedNavigableTrimSet(CharNavigableSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedNavigableSet extends SynchronizedSortedSet implements CharNavigableSet
	{
		CharNavigableSet n;
		
		SynchronizedNavigableSet(CharNavigableSet c) {
			super(c);
			n = c;
		}
		
		SynchronizedNavigableSet(CharNavigableSet c, Object mutex) {
			super(c, mutex);
			n = c;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) { synchronized(mutex) { return n.contains(o); } }

		@Override
		public boolean contains(char o) { synchronized(mutex) { return n.contains(o); } }
		
		@Override
		public char lower(char e) { synchronized(mutex) { return n.lower(e); } }
		
		@Override
		public char floor(char e) { synchronized(mutex) { return n.floor(e); } }
		
		@Override
		public char ceiling(char e) { synchronized(mutex) { return n.ceiling(e); } }
		
		@Override
		public char higher(char e) { synchronized(mutex) { return n.higher(e); } }
		
		@Override
		public void setDefaultMaxValue(char e) { synchronized(mutex) { n.setDefaultMaxValue(e); } }
		
		@Override
		public char getDefaultMaxValue() { synchronized(mutex) { return n.getDefaultMaxValue(); } }
		
		@Override
		public void setDefaultMinValue(char e) { synchronized(mutex) { n.setDefaultMinValue(e); } }
		
		@Override
		public char getDefaultMinValue() { synchronized(mutex) { return n.getDefaultMinValue(); } }
		
		@Override
		public CharNavigableSet copy() { synchronized(mutex) { return n.copy(); } }
		
		@Override
		public CharNavigableSet subSet(char fromElement, boolean fromInclusive, char toElement, boolean toInclusive) { synchronized(mutex) { return CharSets.synchronize(n.subSet(fromElement, fromInclusive, toElement, toInclusive), mutex); } }

		@Override
		public CharNavigableSet headSet(char toElement, boolean inclusive) { synchronized(mutex) { return CharSets.synchronize(n.headSet(toElement, inclusive), mutex); } }

		@Override
		public CharNavigableSet tailSet(char fromElement, boolean inclusive) { synchronized(mutex) { return CharSets.synchronize(n.tailSet(fromElement, inclusive), mutex); } }

		@Override
		public CharBidirectionalIterator descendingIterator() { synchronized(mutex) { return n.descendingIterator(); } }

		@Override
		public CharNavigableSet descendingSet() { synchronized(mutex) { return CharSets.synchronize(n.descendingSet(), mutex); } }
		
		@Override
		public CharNavigableSet subSet(char fromElement, char toElement) { synchronized(mutex) { return CharSets.synchronize(n.subSet(fromElement, toElement), mutex); } }

		@Override
		public CharNavigableSet headSet(char toElement) { synchronized(mutex) { return CharSets.synchronize(n.headSet(toElement), mutex); } }

		@Override
		public CharNavigableSet tailSet(char fromElement) { synchronized(mutex) { return CharSets.synchronize(n.tailSet(fromElement), mutex); } }
	}
	
	private static class SynchronizedSortedTrimSet extends SynchronizedSortedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedSortedTrimSet(CharSortedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedSortedTrimSet(CharSortedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSortedSet extends SynchronizedSet implements CharSortedSet
	{
		CharSortedSet s;
		
		SynchronizedSortedSet(CharSortedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSortedSet(CharSortedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public CharComparator comparator(){ synchronized(mutex) { return s.comparator(); } }
		@Override
		public CharBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public CharBidirectionalIterator iterator(char fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public CharSortedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public CharSortedSet subSet(char fromElement, char toElement) { synchronized(mutex) { return CharSets.synchronize(s.subSet(fromElement, toElement), mutex); } }
		@Override
		public CharSortedSet headSet(char toElement) { synchronized(mutex) { return CharSets.synchronize(s.headSet(toElement), mutex); } }
		@Override
		public CharSortedSet tailSet(char fromElement) { synchronized(mutex) { return CharSets.synchronize(s.tailSet(fromElement), mutex); } }
		@Override
		public char firstChar() { synchronized(mutex) { return s.firstChar(); } }
		@Override
		public char pollFirstChar() { synchronized(mutex) { return s.pollFirstChar(); } }
		@Override
		public char lastChar() { synchronized(mutex) { return s.lastChar(); } }
		@Override
		public char pollLastChar() { synchronized(mutex) { return s.pollLastChar(); } }
	}
	
	private static class SynchronizedOrderedTrimSet extends SynchronizedOrderedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedOrderedTrimSet(CharOrderedSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedOrderedTrimSet(CharOrderedSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedOrderedSet extends SynchronizedSet implements CharOrderedSet
	{
		CharOrderedSet s;
		
		SynchronizedOrderedSet(CharOrderedSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedOrderedSet(CharOrderedSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}

		@Override
		public boolean addAndMoveToFirst(char o) { synchronized(mutex) { return s.addAndMoveToFirst(o); } }
		@Override
		public boolean addAndMoveToLast(char o) { synchronized(mutex) { return s.addAndMoveToLast(o); } }
		@Override
		public boolean moveToFirst(char o) { synchronized(mutex) { return s.moveToFirst(o); } }
		@Override
		public boolean moveToLast(char o) { synchronized(mutex) { return s.moveToLast(o); } }
		@Override
		public CharBidirectionalIterator iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public CharBidirectionalIterator iterator(char fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public CharOrderedSet copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public char firstChar() { synchronized(mutex) { return s.firstChar(); } }
		@Override
		public char pollFirstChar() { synchronized(mutex) { return s.pollFirstChar(); } }
		@Override
		public char lastChar() { synchronized(mutex) { return s.lastChar(); } }
		@Override
		public char pollLastChar() { synchronized(mutex) { return s.pollLastChar(); } }
	}
	
	private static class SynchronizedTrimSet extends SynchronizedSet implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedTrimSet(CharSet c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedTrimSet(CharSet c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSet extends SynchronizedCollection implements CharSet
	{
		CharSet s;
		
		SynchronizedSet(CharSet c) {
			super(c);
			s = c;
		}
		
		SynchronizedSet(CharSet c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public CharSet copy() { synchronized(mutex) { return s.copy(); } }
		
		@Override
		public boolean remove(char o) { synchronized(mutex) { return s.remove(o); } }
	}
}