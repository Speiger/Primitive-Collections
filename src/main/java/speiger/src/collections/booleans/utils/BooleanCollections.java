package speiger.src.collections.booleans.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Consumer;

import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.functions.function.BooleanPredicate;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.ints.functions.consumer.IntBooleanConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.utils.ITrimmable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Collections
 */
public class BooleanCollections
{
	/**
	 * Empty Collection Reference
	 */
	public static final BooleanCollection EMPTY = new EmptyCollection();
	
	/**
	 * Returns a Immutable EmptyCollection instance that is automatically casted.
	 * @return an empty collection
	 */
	public static BooleanCollection empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable Collection instance based on the instance given.
	 * @param c that should be made immutable/unmodifiable
	 * @return a unmodifiable collection wrapper. If the Collection already a unmodifiable wrapper then it just returns itself.
	 */
	public static BooleanCollection unmodifiable(BooleanCollection c) {
		return c instanceof UnmodifiableCollection ? c : new UnmodifiableCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static BooleanCollection synchronize(BooleanCollection c) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static BooleanCollection synchronize(BooleanCollection c, Object mutex) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c, mutex);
	}
	
	/**
	 * Creates a Singleton Collection of a given element
	 * @param element the element that should be converted into a singleton collection
	 * @return a singletoncollection of the given element
	 */
	public static BooleanCollection singleton(boolean element) {
		return new SingletonCollection(element);
	}
	
	protected static CollectionWrapper wrapper() {
		return new CollectionWrapper();
	}
	
	protected static CollectionWrapper wrapper(int size) {
		return new CollectionWrapper(size);
	}
	
	protected static class CollectionWrapper extends AbstractBooleanCollection implements ITrimmable {
		boolean[] elements;
		int size = 0;
		
		public CollectionWrapper() {
			this(10);
		}
		
		public CollectionWrapper(int size) {
			if(size < 0) throw new IllegalStateException("Size has to be 0 or greater");
			elements = new boolean[size];
		}
		
		@Override
		public boolean add(boolean o) {
			if(size >= elements.length) elements = Arrays.copyOf(elements, (int)Math.min((long)elements.length + (elements.length >> 1), SanityChecks.MAX_ARRAY_SIZE));
			elements[size++] = o;
			return true;
		}
		
		public boolean getBoolean(int index) {
			if(index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			return elements[index];
		}
		
		@Override
		public boolean remBoolean(boolean e) {
			for(int i = 0;i<size;i++) {
				if(elements[i] == e) {
					removeIndex(i);
					return true;
				}
			}
			return false;
		}
		
		private void removeIndex(int index) {
			if(index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			size--;
			if(index != size) System.arraycopy(elements, index+1, elements, index, size - index);
		}
		
		@Override
		public BooleanIterator iterator() {
			return new BooleanIterator() {
				int index = 0;
				int lastReturned = -1;
				
				@Override
				public boolean hasNext() {
					return index < size;
				}
				
				@Override
				public boolean nextBoolean() {
					int i = index++;
					return elements[(lastReturned = i)];
				}
				
				@Override
				public void remove() {
					if(lastReturned == -1) throw new IllegalStateException();
					removeIndex(lastReturned);
					index = lastReturned;
					lastReturned = -1;
				}
			};
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public void clear() {
			size = 0;
		}
		
		public void sort(BooleanComparator c) {
			if(c != null) BooleanArrays.stableSort(elements, size, c);
			else BooleanArrays.stableSort(elements, size);
		}
		
		public void unstableSort(BooleanComparator c) {
			if(c != null) BooleanArrays.unstableSort(elements, size, c);
			else BooleanArrays.unstableSort(elements, size);		
		}
		
		@Override
		public void forEach(BooleanConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(elements[i]);
		}
		
		@Override
		public <E> void forEach(E input, ObjectBooleanConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(input, elements[i]);		
		}
		
		@Override
		public boolean trim(int size) {
			if(size > size() || size() == elements.length) return false;
			int value = Math.max(size, size());
			elements = value == 0 ? BooleanArrays.EMPTY_ARRAY : Arrays.copyOf(elements, value);
			return true;
		}
		
		@Override
		public void clearAndTrim(int size) {
			if(elements.length <= size) {
				clear();
				return;
			}
			elements = size == 0 ? BooleanArrays.EMPTY_ARRAY : new boolean[size];
			this.size = size;
		}
		
		@Override
		@Deprecated
		public Object[] toArray() {
			Object[] obj = new Object[size];
			for(int i = 0;i<size;i++)
				obj[i] = Boolean.valueOf(elements[i]);
			return obj;
		}
		
		@Override
		@Deprecated
		public <E> E[] toArray(E[] a) {
			if(a == null) a = (E[])new Object[size];
			else if(a.length < size) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size);
			for(int i = 0;i<size;i++)
				a[i] = (E)Boolean.valueOf(elements[i]);
			if (a.length > size) a[size] = null;
			return a;
		}
		
		@Override
		public boolean[] toBooleanArray(boolean[] a) {
			if(a.length < size) a = new boolean[size];
			System.arraycopy(elements, 0, a, 0, size);
			if (a.length > size) a[size] = false;
			return a;
		}		
	}
	
	private static class SingletonCollection extends AbstractBooleanCollection
	{
		boolean element;
		
		SingletonCollection(boolean element) {
			this.element = element;
		}
		
		@Override
		public boolean remBoolean(boolean o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean add(boolean o) { throw new UnsupportedOperationException(); }
		@Override
		public BooleanIterator iterator()
		{
			return new BooleanIterator() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next; }
				@Override
				public boolean nextBoolean() {
					if(!hasNext()) throw new NoSuchElementException();
					next = false;
					return element;
				}
			};
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (!(o instanceof Collection))
				return false;
			Collection<?> l = (Collection<?>)o;
			if(l.size() != size()) return false;
			Iterator<?> iter = l.iterator();
			if (iter.hasNext() && !Objects.equals(element, iter.next())) {
				return false;
			}
			return !iter.hasNext();
		}
		
		@Override
		public int hashCode() {
			return Boolean.hashCode(element);
		}
		
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonCollection copy() { return new SingletonCollection(element); }
	}
	
	/**
	 * Synchronized Collection Wrapper for the synchronizedCollection function
	 */
	public static class SynchronizedCollection implements BooleanCollection {
		BooleanCollection c;
		protected Object mutex;
		
		SynchronizedCollection(BooleanCollection c) {
			this.c = c;
			mutex = this;
		}
		
		SynchronizedCollection(BooleanCollection c, Object mutex) {
			this.c = c;
			this.mutex = mutex;
		}
		
		@Override
		public boolean add(boolean o) { synchronized(mutex) { return c.add(o); } }
		@Override
		public boolean addAll(Collection<? extends Boolean> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(BooleanCollection c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(boolean[] e, int offset, int length) { synchronized(mutex) { return c.addAll(e, offset, length); } }
		@Override
		public boolean contains(boolean o) { synchronized(mutex) { return c.contains(o); } }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public boolean containsAll(BooleanCollection c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(BooleanCollection c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public int size() { synchronized(mutex) { return c.size(); } }
		
		@Override
		public boolean isEmpty() { synchronized(mutex) { return c.isEmpty(); } }
		
		@Override
		public BooleanIterator iterator() {
			return c.iterator();
		}
		
		@Override
		public BooleanCollection copy() { synchronized(mutex) { return c.copy(); } }
		
		@Override
		@Deprecated
		public boolean remove(Object o) { synchronized(mutex) { return c.remove(o); } }
		@Override
		@Deprecated
		public boolean removeAll(Collection<?> c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		@Deprecated
		public boolean retainAll(Collection<?> c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean remBoolean(boolean o) { synchronized(mutex) { return c.remBoolean(o); } }
		@Override
		public boolean removeAll(BooleanCollection c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean removeAll(BooleanCollection c, BooleanConsumer r) { synchronized(mutex) { return this.c.removeAll(c, r); } }
		@Override
		public boolean retainAll(BooleanCollection c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean retainAll(BooleanCollection c, BooleanConsumer r) { synchronized(mutex) { return this.c.retainAll(c, r); } }
		@Override
		public void clear() { synchronized(mutex) { c.clear(); } }
		@Override
		public Object[] toArray() { synchronized(mutex) { return c.toArray(); } }
		@Override
		public <T> T[] toArray(T[] a) { synchronized(mutex) { return c.toArray(a); } }
		@Override
		public boolean[] toBooleanArray() { synchronized(mutex) { return c.toBooleanArray(); } }
		@Override
		public boolean[] toBooleanArray(boolean[] a) { synchronized(mutex) { return c.toBooleanArray(a); } }
		@Override
		public void forEach(BooleanConsumer action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Boolean> action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		public void forEachIndexed(IntBooleanConsumer action) { synchronized(mutex) { c.forEachIndexed(action); } }
		@Override
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) {
			if(obj == this) return true;
			synchronized(mutex) { return c.equals(obj); } 
		}
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectBooleanConsumer<E> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(BooleanPredicate filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(BooleanPredicate filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(BooleanPredicate filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		@Override
		public boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public boolean reduce(BooleanBooleanUnaryOperator operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public boolean findFirst(BooleanPredicate filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(BooleanPredicate filter) { synchronized(mutex) { return c.count(filter); } }
	}
	
	/**
	 * Unmodifyable Collection Wrapper for the unmodifyableCollection method
	 */
	public static class UnmodifiableCollection implements BooleanCollection {
		BooleanCollection c;
		
		UnmodifiableCollection(BooleanCollection c) {
			this.c = c;
		}
		
		@Override
		public boolean add(boolean o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(Collection<? extends Boolean> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(BooleanCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(boolean[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean contains(boolean o) { return c.contains(o); }
		@Override
		public boolean containsAll(BooleanCollection c) { return this.c.containsAll(c); }
		@Override
		public boolean containsAny(BooleanCollection c) { return this.c.containsAny(c); }
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { return this.c.containsAny(c); }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { return this.c.containsAll(c); }
		@Override
		public int size() { return c.size(); }
		@Override
		public boolean isEmpty() { return c.isEmpty(); }
		@Override
		public BooleanIterator iterator() { return BooleanIterators.unmodifiable(c.iterator()); }
		@Override
		public BooleanCollection copy() { return c.copy(); }
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public boolean removeIf(Predicate<? super Boolean> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remBoolean(boolean o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(BooleanCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(BooleanCollection c, BooleanConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(BooleanCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(BooleanCollection c, BooleanConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return c.toArray(); }
		@Override
		public <T> T[] toArray(T[] a) { return c.toArray(a); }
		@Override
		public boolean[] toBooleanArray() { return c.toBooleanArray(); }
		@Override
		public boolean[] toBooleanArray(boolean[] a) { return c.toBooleanArray(a); }
		@Override
		public void forEach(BooleanConsumer action) { c.forEach(action); }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Boolean> action) { c.forEach(action); }
		@Override
		public void forEachIndexed(IntBooleanConsumer action) { c.forEachIndexed(action); }
		@Override
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return obj == this || c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectBooleanConsumer<E> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(BooleanPredicate filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(BooleanPredicate filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(BooleanPredicate filter) { return c.matchesAll(filter); }
		@Override
		public boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) { return c.reduce(identity, operator); }
		@Override
		public boolean reduce(BooleanBooleanUnaryOperator operator) { return c.reduce(operator); }
		@Override
		public boolean findFirst(BooleanPredicate filter) { return c.findFirst(filter); }
		@Override
		public int count(BooleanPredicate filter) { return c.count(filter); }
	}
	
	/**
	 * Empty Collection implementation for the empty collection function
	 */
	public static class EmptyCollection extends AbstractBooleanCollection {
		@Override
		public boolean add(boolean o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(BooleanCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(boolean[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(boolean o) { return false; }
		@Override
		public boolean containsAll(BooleanCollection c) { return c.isEmpty(); }
		@Override
		public boolean containsAny(BooleanCollection c) { return false; }
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { return false; }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { return c.isEmpty(); }
		@Override
		public int hashCode() { return 0; }
		
		@Override
		public boolean equals(Object o) {
			if(o == this) return true;
		  	if(!(o instanceof Collection)) return false;
		  	return ((Collection<?>)o).isEmpty();
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		@Override
		@Deprecated
		public boolean removeIf(Predicate<? super Boolean> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remBoolean(boolean o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(BooleanCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(BooleanCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return ObjectArrays.EMPTY_ARRAY; }
		@Override
		public <T> T[] toArray(T[] a) {
			if(a != null && a.length > 0)
				a[0] = null;
			return a;
		}
		
		@Override
		public boolean[] toBooleanArray() { return BooleanArrays.EMPTY_ARRAY; }
		@Override
		public boolean[] toBooleanArray(boolean[] a) {
			if(a != null && a.length > 0)
				a[0] = false;
			return a;
		}
		
		@Override
		public BooleanIterator iterator() { return BooleanIterators.empty(); }
		@Override
		public void clear() {}
		@Override
		public int size() { return 0; }
		@Override
		public EmptyCollection copy() { return this; }
	}
}