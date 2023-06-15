package speiger.src.collections.objects.utils;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Comparator;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.utils.ObjectCollections.EmptyCollection;
import speiger.src.collections.objects.utils.ObjectCollections.SynchronizedCollection;
import speiger.src.collections.objects.utils.ObjectCollections.UnmodifiableCollection;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Helper class for sets
 */
public class ObjectSets
{
	/**
	 * Empty Set Variable
	 */
	private static final ObjectSet<?> EMPTY = new EmptySet<>();
	
	/**
	 * EmptySet getter
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a EmptySet
	 */
	public static <T> ObjectSet<T> empty() {
		return (ObjectSet<T>)EMPTY;
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static <T> ObjectSet<T> synchronize(ObjectSet<T> s) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet<>(s) : new SynchronizedSet<>(s));
	}
	
	/**
	 * Creates a Synchronized set while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a set that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static <T> ObjectSet<T> synchronize(ObjectSet<T> s, Object mutex) {
		return s instanceof SynchronizedSet ? s : (s instanceof ITrimmable ? new SynchronizedTrimSet<>(s, mutex) : new SynchronizedSet<>(s, mutex));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static <T> ObjectSortedSet<T> synchronize(ObjectSortedSet<T> s) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet<>(s) : new SynchronizedSortedSet<>(s));
	}
	
	/**
	 * Creates a Synchronized SortedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a SortedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static <T> ObjectSortedSet<T> synchronize(ObjectSortedSet<T> s, Object mutex) {
		return s instanceof SynchronizedSortedSet ? s : (s instanceof ITrimmable ? new SynchronizedSortedTrimSet<>(s, mutex) : new SynchronizedSortedSet<>(s, mutex));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static <T> ObjectOrderedSet<T> synchronize(ObjectOrderedSet<T> s) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet<>(s) : new SynchronizedOrderedSet<>(s));
	}
	
	/**
	 * Creates a Synchronized OrderedSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a OrderedSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static <T> ObjectOrderedSet<T> synchronize(ObjectOrderedSet<T> s, Object mutex) {
		return s instanceof SynchronizedOrderedSet ? s : (s instanceof ITrimmable ? new SynchronizedOrderedTrimSet<>(s, mutex) : new SynchronizedOrderedSet<>(s, mutex));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static <T> ObjectNavigableSet<T> synchronize(ObjectNavigableSet<T> s) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet<>(s) : new SynchronizedNavigableSet<>(s));
	}
	
	/**
	 * Creates a Synchronized NavigableSet while preserving the ITrimmable interface
	 * @param s the set that should be synchronized
	 * @param mutex controller for access
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a NavigableSet that is synchronized
	 * @note if the set is already synchronized then it will just self return it
	 */
	public static <T> ObjectNavigableSet<T> synchronize(ObjectNavigableSet<T> s, Object mutex) {
		return s instanceof SynchronizedNavigableSet ? s : (s instanceof ITrimmable ? new SynchronizedNavigableTrimSet<>(s, mutex) : new SynchronizedNavigableSet<>(s, mutex));
	}
	
	/**
	 * Creates Unmodifyable Set wrapper
	 * @param s set that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a UnmodifyableSet, if the set is already unmodifiable then it returns itself
	 */
	public static <T> ObjectSet<T> unmodifiable(ObjectSet<T> s) {
		return s instanceof UnmodifiableSet ? s : new UnmodifiableSet<>(s);
	}
	
	/**
	 * Creates Unmodifyable SortedSet wrapper
	 * @param s sortedSet that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a UnmodifyableSortedSet, if the set is already unmodifiable then it returns itself
	 */
	public static <T> ObjectSortedSet<T> unmodifiable(ObjectSortedSet<T> s) {
		return s instanceof UnmodifiableSortedSet ? s : new UnmodifiableSortedSet<>(s);
	}
	
	/**
	 * Creates Unmodifyable OrderedSet wrapper
	 * @param s OrderedSet that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a UnmodifyableOrderedSet, if the set is already unmodifiable then it returns itself
	 */
	public static <T> ObjectOrderedSet<T> unmodifiable(ObjectOrderedSet<T> s) {
		return s instanceof UnmodifiableOrderedSet ? s : new UnmodifiableOrderedSet<>(s);
	}
	
	/**
	 * Creates Unmodifyable NavigableSet wrapper
	 * @param s navigableSet that should be made unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a UnmodifyableNavigableSet, if the set is already unmodifiable then it returns itself
	 */
	public static <T> ObjectNavigableSet<T> unmodifiable(ObjectNavigableSet<T> s) {
		return s instanceof UnmodifiableNavigableSet ? s : new UnmodifiableNavigableSet<>(s);
	}
	
	/**
	 * Creates a Singleton set of a given element
	 * @param element the element that should be converted into a singleton set
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a singletonset of the given element
	 */
	public static <T> ObjectSet<T> singleton(T element) {
		return new SingletonSet<>(element);
	}
	
	private static class SingletonSet<T> extends AbstractObjectSet<T>
	{
		T element;
		
		SingletonSet(T element) {
			this.element = element;
		}
		
		@Override
		public boolean add(T o) { throw new UnsupportedOperationException(); }
		public T addOrGet(T o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectIterator<T> iterator()
		{
			return new ObjectIterator<T>() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next; }
				@Override
				public T next() {
					if(!hasNext()) throw new NoSuchElementException();
					next = false;
					return element;
				}
			};
		}
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonSet<T> copy() { return new SingletonSet<>(element); }
	}
	
	private static class EmptySet<T> extends EmptyCollection<T> implements ObjectSet<T>
	{
		@Override
		public T addOrGet(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean equals(Object o) {
			if(o == this) return true;
			if(!(o instanceof Set)) return false;
			return ((Set<?>)o).isEmpty();
		}
		
		@Override
		public EmptySet<T> copy() { return this; }
	}
	
	private static class UnmodifiableNavigableSet<T> extends UnmodifiableSortedSet<T> implements ObjectNavigableSet<T>
	{
		ObjectNavigableSet<T> n;
		
		UnmodifiableNavigableSet(ObjectNavigableSet<T> c) {
			super(c);
			n = c;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) { return n.contains(o); }
		@Override
		public T lower(T e) { return n.lower(e); }
		@Override
		public T floor(T e) { return n.floor(e); }
		@Override
		public T ceiling(T e) { return n.ceiling(e); }
		@Override
		public T higher(T e) { return n.higher(e); }
		
		@Override
		public ObjectNavigableSet<T> copy() { return n.copy(); }
		
		@Override
		public ObjectNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) { return ObjectSets.unmodifiable(n.subSet(fromElement, fromInclusive, toElement, toInclusive)); }
		
		@Override
		public ObjectNavigableSet<T> headSet(T toElement, boolean inclusive) { return ObjectSets.unmodifiable(n.headSet(toElement, inclusive)); }

		@Override
		public ObjectNavigableSet<T> tailSet(T fromElement, boolean inclusive) { return ObjectSets.unmodifiable(n.tailSet(fromElement, inclusive)); }

		@Override
		public ObjectBidirectionalIterator<T> descendingIterator() { return ObjectIterators.unmodifiable(n.descendingIterator()); }

		@Override
		public ObjectNavigableSet<T> descendingSet() { return ObjectSets.unmodifiable(n.descendingSet()); }
		
		@Override
		public ObjectNavigableSet<T> subSet(T fromElement, T toElement) { return ObjectSets.unmodifiable(n.subSet(fromElement, toElement)); }
		
		@Override
		public ObjectNavigableSet<T> headSet(T toElement) { return ObjectSets.unmodifiable(n.headSet(toElement)); }
		
		@Override
		public ObjectNavigableSet<T> tailSet(T fromElement) { return ObjectSets.unmodifiable(n.tailSet(fromElement)); }
	}
	
	private static class UnmodifiableOrderedSet<T> extends UnmodifiableSet<T> implements ObjectOrderedSet<T>
	{
		ObjectOrderedSet<T> s;
		UnmodifiableOrderedSet(ObjectOrderedSet<T> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public boolean addAndMoveToFirst(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(T o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectBidirectionalIterator<T> iterator() { return ObjectIterators.unmodifiable(s.iterator()); }
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) { return ObjectIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public ObjectOrderedSet<T> copy() { return s.copy(); }
		@Override
		public T first() { return s.first(); }
		@Override
		public T pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public T last() { return s.last(); }
		@Override
		public T pollLast() { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableSortedSet<T> extends UnmodifiableSet<T> implements ObjectSortedSet<T>
	{
		ObjectSortedSet<T> s;
		UnmodifiableSortedSet(ObjectSortedSet<T> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public Comparator<T> comparator() { return s.comparator(); }
		@Override
		public ObjectBidirectionalIterator<T> iterator() { return ObjectIterators.unmodifiable(s.iterator()); }
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) { return ObjectIterators.unmodifiable(s.iterator(fromElement)); }
		@Override
		public ObjectSortedSet<T> copy() { return s.copy(); }
		@Override
		public ObjectSortedSet<T> subSet(T fromElement, T toElement) { return ObjectSets.unmodifiable(s.subSet(fromElement, toElement)); }
		@Override
		public ObjectSortedSet<T> headSet(T toElement) { return ObjectSets.unmodifiable(s.headSet(toElement)); }
		@Override
		public ObjectSortedSet<T> tailSet(T fromElement) { return ObjectSets.unmodifiable(s.tailSet(fromElement)); }
		@Override
		public T first() { return s.first(); }
		@Override
		public T pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public T last() { return s.last(); }
		@Override
		public T pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * Unmodifyable Set wrapper that helps is used with unmodifyableSet function
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class UnmodifiableSet<T> extends UnmodifiableCollection<T> implements ObjectSet<T>
	{
		ObjectSet<T> s;
		
		protected UnmodifiableSet(ObjectSet<T> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public T addOrGet(T o) { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectSet<T> copy() { return s.copy(); }
		
	}
	
	private static class SynchronizedNavigableTrimSet<T> extends SynchronizedNavigableSet<T> implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedNavigableTrimSet(ObjectNavigableSet<T> c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedNavigableTrimSet(ObjectNavigableSet<T> c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedNavigableSet<T> extends SynchronizedSortedSet<T> implements ObjectNavigableSet<T>
	{
		ObjectNavigableSet<T> n;
		
		SynchronizedNavigableSet(ObjectNavigableSet<T> c) {
			super(c);
			n = c;
		}
		
		SynchronizedNavigableSet(ObjectNavigableSet<T> c, Object mutex) {
			super(c, mutex);
			n = c;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) { synchronized(mutex) { return n.contains(o); } }

		@Override
		public T lower(T e) { synchronized(mutex) { return n.lower(e); } }
		@Override
		public T floor(T e) { synchronized(mutex) { return n.floor(e); } }
		@Override
		public T ceiling(T e) { synchronized(mutex) { return n.ceiling(e); } }
		@Override
		public T higher(T e) { synchronized(mutex) { return n.higher(e); } }
		
		@Override
		public ObjectNavigableSet<T> copy() { synchronized(mutex) { return n.copy(); } }
		
		@Override
		public ObjectNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) { synchronized(mutex) { return ObjectSets.synchronize(n.subSet(fromElement, fromInclusive, toElement, toInclusive), mutex); } }

		@Override
		public ObjectNavigableSet<T> headSet(T toElement, boolean inclusive) { synchronized(mutex) { return ObjectSets.synchronize(n.headSet(toElement, inclusive), mutex); } }

		@Override
		public ObjectNavigableSet<T> tailSet(T fromElement, boolean inclusive) { synchronized(mutex) { return ObjectSets.synchronize(n.tailSet(fromElement, inclusive), mutex); } }

		@Override
		public ObjectBidirectionalIterator<T> descendingIterator() { synchronized(mutex) { return n.descendingIterator(); } }

		@Override
		public ObjectNavigableSet<T> descendingSet() { synchronized(mutex) { return ObjectSets.synchronize(n.descendingSet(), mutex); } }
		
		@Override
		public ObjectNavigableSet<T> subSet(T fromElement, T toElement) { synchronized(mutex) { return ObjectSets.synchronize(n.subSet(fromElement, toElement), mutex); } }

		@Override
		public ObjectNavigableSet<T> headSet(T toElement) { synchronized(mutex) { return ObjectSets.synchronize(n.headSet(toElement), mutex); } }

		@Override
		public ObjectNavigableSet<T> tailSet(T fromElement) { synchronized(mutex) { return ObjectSets.synchronize(n.tailSet(fromElement), mutex); } }
	}
	
	private static class SynchronizedSortedTrimSet<T> extends SynchronizedSortedSet<T> implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedSortedTrimSet(ObjectSortedSet<T> c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedSortedTrimSet(ObjectSortedSet<T> c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSortedSet<T> extends SynchronizedSet<T> implements ObjectSortedSet<T>
	{
		ObjectSortedSet<T> s;
		
		SynchronizedSortedSet(ObjectSortedSet<T> c) {
			super(c);
			s = c;
		}
		
		SynchronizedSortedSet(ObjectSortedSet<T> c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public Comparator<T> comparator(){ synchronized(mutex) { return s.comparator(); } }
		@Override
		public ObjectBidirectionalIterator<T> iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public ObjectSortedSet<T> copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public ObjectSortedSet<T> subSet(T fromElement, T toElement) { synchronized(mutex) { return ObjectSets.synchronize(s.subSet(fromElement, toElement), mutex); } }
		@Override
		public ObjectSortedSet<T> headSet(T toElement) { synchronized(mutex) { return ObjectSets.synchronize(s.headSet(toElement), mutex); } }
		@Override
		public ObjectSortedSet<T> tailSet(T fromElement) { synchronized(mutex) { return ObjectSets.synchronize(s.tailSet(fromElement), mutex); } }
		@Override
		public T first() { synchronized(mutex) { return s.first(); } }
		@Override
		public T pollFirst() { synchronized(mutex) { return s.pollFirst(); } }
		@Override
		public T last() { synchronized(mutex) { return s.last(); } }
		@Override
		public T pollLast() { synchronized(mutex) { return s.pollLast(); } }
	}
	
	private static class SynchronizedOrderedTrimSet<T> extends SynchronizedOrderedSet<T> implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedOrderedTrimSet(ObjectOrderedSet<T> c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedOrderedTrimSet(ObjectOrderedSet<T> c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedOrderedSet<T> extends SynchronizedSet<T> implements ObjectOrderedSet<T>
	{
		ObjectOrderedSet<T> s;
		
		SynchronizedOrderedSet(ObjectOrderedSet<T> c) {
			super(c);
			s = c;
		}
		
		SynchronizedOrderedSet(ObjectOrderedSet<T> c, Object mutex) {
			super(c, mutex);
			s = c;
		}

		@Override
		public boolean addAndMoveToFirst(T o) { synchronized(mutex) { return s.addAndMoveToFirst(o); } }
		@Override
		public boolean addAndMoveToLast(T o) { synchronized(mutex) { return s.addAndMoveToLast(o); } }
		@Override
		public boolean moveToFirst(T o) { synchronized(mutex) { return s.moveToFirst(o); } }
		@Override
		public boolean moveToLast(T o) { synchronized(mutex) { return s.moveToLast(o); } }
		@Override
		public ObjectBidirectionalIterator<T> iterator() { synchronized(mutex) { return s.iterator(); } }
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) { synchronized(mutex) { return s.iterator(fromElement); } }
		@Override
		public ObjectOrderedSet<T> copy() { synchronized(mutex) { return s.copy(); } }
		@Override
		public T first() { synchronized(mutex) { return s.first(); } }
		@Override
		public T pollFirst() { synchronized(mutex) { return s.pollFirst(); } }
		@Override
		public T last() { synchronized(mutex) { return s.last(); } }
		@Override
		public T pollLast() { synchronized(mutex) { return s.pollLast(); } }
	}
	
	private static class SynchronizedTrimSet<T> extends SynchronizedSet<T> implements ITrimmable
	{
		ITrimmable trim;
		
		SynchronizedTrimSet(ObjectSet<T> c) {
			super(c);
			trim = (ITrimmable)c;
		}
		
		SynchronizedTrimSet(ObjectSet<T> c, Object mutex) {
			super(c, mutex);
			trim = (ITrimmable)c;
		}
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return trim.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { trim.clearAndTrim(size); } }
	}
	
	private static class SynchronizedSet<T> extends SynchronizedCollection<T> implements ObjectSet<T>
	{
		ObjectSet<T> s;
		
		SynchronizedSet(ObjectSet<T> c) {
			super(c);
			s = c;
		}
		
		SynchronizedSet(ObjectSet<T> c, Object mutex) {
			super(c, mutex);
			s = c;
		}
		
		@Override
		public T addOrGet(T o) { synchronized(mutex) { return s.addOrGet(o); } }
		
		@Override
		public ObjectSet<T> copy() { synchronized(mutex) { return s.copy(); } }
		
	}
}