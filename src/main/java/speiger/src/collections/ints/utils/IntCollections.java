package speiger.src.collections.ints.utils;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.IntPredicate;
import java.util.function.Consumer;

import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
/**
 * A Helper class for Collections
 */
public class IntCollections
{
	/**
	 * Empty Collection Reference
	 */
	public static final IntCollection EMPTY = new EmptyCollection();
	
	/**
	 * Returns a Immutable EmptyCollection instance that is automatically casted.
	 * @return an empty collection
	 */
	public static IntCollection empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable Collection instance based on the instance given.
	 * @param c that should be made immutable/unmodifiable
	 * @return a unmodifiable collection wrapper. If the Collection already a unmodifiable wrapper then it just returns itself.
	 */
	public static IntCollection unmodifiable(IntCollection c) {
		return c instanceof UnmodifiableCollection ? c : new UnmodifiableCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static IntCollection synchronize(IntCollection c) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static IntCollection synchronize(IntCollection c, Object mutex) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c, mutex);
	}
	
	/**
	 * Synchronized Collection Wrapper for the synchronizedCollection function
	 */
	public static class SynchronizedCollection implements IntCollection {
		IntCollection c;
		protected Object mutex;
		
		SynchronizedCollection(IntCollection c) {
			this.c = c;
			mutex = this;
		}
		
		SynchronizedCollection(IntCollection c, Object mutex) {
			this.c = c;
			this.mutex = mutex;
		}
		
		@Override
		public boolean add(int o) { synchronized(mutex) { return c.add(o); } }
		@Override
		public boolean addAll(Collection<? extends Integer> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(IntCollection c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(int[] e, int offset, int length) { synchronized(mutex) { return c.addAll(e, offset, length); } }
		@Override
		public boolean contains(int o) { synchronized(mutex) { return c.contains(o); } }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public boolean containsAll(IntCollection c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(IntCollection c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public int size() { synchronized(mutex) { return c.size(); } }
		
		@Override
		public boolean isEmpty() { synchronized(mutex) { return c.isEmpty(); } }
		
		@Override
		public IntIterator iterator() {
			return c.iterator();
		}
		
		@Override
		public IntCollection copy() { synchronized(mutex) { return c.copy(); } }
		
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
		public boolean remInt(int o) { synchronized(mutex) { return c.remInt(o); } }
		@Override
		public boolean removeAll(IntCollection c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean removeAll(IntCollection c, IntConsumer r) { synchronized(mutex) { return this.c.removeAll(c, r); } }
		@Override
		public boolean retainAll(IntCollection c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean retainAll(IntCollection c, IntConsumer r) { synchronized(mutex) { return this.c.retainAll(c, r); } }
		@Override
		public boolean remIf(IntPredicate filter){ synchronized(mutex) { return c.remIf(filter); } }
		@Override
		public void clear() { synchronized(mutex) { c.clear(); } }
		@Override
		public Object[] toArray() { synchronized(mutex) { return c.toArray(); } }
		@Override
		public <T> T[] toArray(T[] a) { synchronized(mutex) { return c.toArray(a); } }
		@Override
		public int[] toIntArray() { synchronized(mutex) { return c.toIntArray(); } }
		@Override
		public int[] toIntArray(int[] a) { synchronized(mutex) { return c.toIntArray(a); } }
		@Override
		public void forEach(IntConsumer action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Integer> action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) { synchronized(mutex) { return c.equals(obj); } }
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(Int2BooleanFunction filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Int2BooleanFunction filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Int2BooleanFunction filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public int reduce(IntIntUnaryOperator operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public int findFirst(Int2BooleanFunction filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(Int2BooleanFunction filter) { synchronized(mutex) { return c.count(filter); } }
	}
	
	/**
	 * Unmodifyable Collection Wrapper for the unmodifyableCollection method
	 */
	public static class UnmodifiableCollection implements IntCollection {
		IntCollection c;
		
		UnmodifiableCollection(IntCollection c) {
			this.c = c;
		}
		
		@Override
		public boolean add(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(Collection<? extends Integer> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(IntCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean contains(int o) { return c.contains(o); }
		@Override
		public boolean containsAll(IntCollection c) { return this.c.containsAll(c); }
		@Override
		public boolean containsAny(IntCollection c) { return this.c.containsAny(c); }
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
		public IntIterator iterator() { return IntIterators.unmodifiable(c.iterator()); }
		@Override
		public IntCollection copy() { return c.copy(); }
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
		public boolean removeIf(Predicate<? super Integer> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remInt(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(IntCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(IntCollection c, IntConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(IntCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(IntCollection c, IntConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(IntPredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return c.toArray(); }
		@Override
		public <T> T[] toArray(T[] a) { return c.toArray(a); }
		@Override
		public int[] toIntArray() { return c.toIntArray(); }
		@Override
		public int[] toIntArray(int[] a) { return c.toIntArray(a); }
		@Override
		public void forEach(IntConsumer action) { c.forEach(action); }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Integer> action) { c.forEach(action); }
		@Override
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(Int2BooleanFunction filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(Int2BooleanFunction filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(Int2BooleanFunction filter) { return c.matchesAll(filter); }
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) { return c.reduce(identity, operator); }
		@Override
		public int reduce(IntIntUnaryOperator operator) { return c.reduce(operator); }
		@Override
		public int findFirst(Int2BooleanFunction filter) { return c.findFirst(filter); }
		@Override
		public int count(Int2BooleanFunction filter) { return c.count(filter); }
	}
	
	/**
	 * Empty Collection implementation for the empty collection function
	 */
	public static class EmptyCollection extends AbstractIntCollection {
		@Override
		public boolean add(int o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(IntCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(int o) { return false; }
		@Override
		public boolean containsAll(IntCollection c) { return false; }
		@Override
		public boolean containsAny(IntCollection c) { return false; }
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { return false; }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { return false; }
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
		public boolean removeIf(Predicate<? super Integer> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remInt(int o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(IntCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(IntCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(IntPredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return ObjectArrays.EMPTY_ARRAY; }
		@Override
		public <T> T[] toArray(T[] a) { return a; }
		@Override
		public int[] toIntArray() { return IntArrays.EMPTY_ARRAY; }
		@Override
		public int[] toIntArray(int[] a) { return a; }
		@Override
		public IntIterator iterator() { return IntIterators.empty(); }
		@Override
		public void clear() {}
		@Override
		public int size() { return 0; }
		@Override
		public EmptyCollection copy() { return this; }
	}
}