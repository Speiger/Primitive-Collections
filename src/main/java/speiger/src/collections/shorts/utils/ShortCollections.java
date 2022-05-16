package speiger.src.collections.shorts.utils;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.IntPredicate;
import java.util.function.Consumer;

import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.shorts.utils.ShortArrays;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
/**
 * A Helper class for Collections
 */
public class ShortCollections
{
	/**
	 * Empty Collection Reference
	 */
	public static final ShortCollection EMPTY = new EmptyCollection();
	
	/**
	 * Returns a Immutable EmptyCollection instance that is automatically casted.
	 * @return an empty collection
	 */
	public static ShortCollection empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable Collection instance based on the instance given.
	 * @param c that should be made immutable/unmodifiable
	 * @return a unmodifiable collection wrapper. If the Collection already a unmodifiable wrapper then it just returns itself.
	 */
	public static ShortCollection unmodifiable(ShortCollection c) {
		return c instanceof UnmodifiableCollection ? c : new UnmodifiableCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static ShortCollection synchronize(ShortCollection c) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static ShortCollection synchronize(ShortCollection c, Object mutex) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c, mutex);
	}
	
	/**
	 * Synchronized Collection Wrapper for the synchronizedCollection function
	 */
	public static class SynchronizedCollection implements ShortCollection {
		ShortCollection c;
		protected Object mutex;
		
		SynchronizedCollection(ShortCollection c) {
			this.c = c;
			mutex = this;
		}
		
		SynchronizedCollection(ShortCollection c, Object mutex) {
			this.c = c;
			this.mutex = mutex;
		}
		
		@Override
		public boolean add(short o) { synchronized(mutex) { return c.add(o); } }
		@Override
		public boolean addAll(Collection<? extends Short> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(ShortCollection c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(short[] e, int offset, int length) { synchronized(mutex) { return c.addAll(e, offset, length); } }
		@Override
		public boolean contains(short o) { synchronized(mutex) { return c.contains(o); } }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public boolean containsAll(ShortCollection c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(ShortCollection c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public int size() { synchronized(mutex) { return c.size(); } }
		
		@Override
		public boolean isEmpty() { synchronized(mutex) { return c.isEmpty(); } }
		
		@Override
		public ShortIterator iterator() {
			return c.iterator();
		}
		
		@Override
		public ShortCollection copy() { synchronized(mutex) { return c.copy(); } }
		
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
		public boolean remShort(short o) { synchronized(mutex) { return c.remShort(o); } }
		@Override
		public boolean removeAll(ShortCollection c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean removeAll(ShortCollection c, ShortConsumer r) { synchronized(mutex) { return this.c.removeAll(c, r); } }
		@Override
		public boolean retainAll(ShortCollection c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean retainAll(ShortCollection c, ShortConsumer r) { synchronized(mutex) { return this.c.retainAll(c, r); } }
		@Override
		public boolean remIf(IntPredicate filter){ synchronized(mutex) { return c.remIf(filter); } }
		@Override
		public void clear() { synchronized(mutex) { c.clear(); } }
		@Override
		public Object[] toArray() { synchronized(mutex) { return c.toArray(); } }
		@Override
		public <T> T[] toArray(T[] a) { synchronized(mutex) { return c.toArray(a); } }
		@Override
		public short[] toShortArray() { synchronized(mutex) { return c.toShortArray(); } }
		@Override
		public short[] toShortArray(short[] a) { synchronized(mutex) { return c.toShortArray(a); } }
		@Override
		public void forEach(ShortConsumer action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Short> action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) { synchronized(mutex) { return c.equals(obj); } }
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(Short2BooleanFunction filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Short2BooleanFunction filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Short2BooleanFunction filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		@Override
		public short reduce(short identity, ShortShortUnaryOperator operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public short reduce(ShortShortUnaryOperator operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public short findFirst(Short2BooleanFunction filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(Short2BooleanFunction filter) { synchronized(mutex) { return c.count(filter); } }
	}
	
	/**
	 * Unmodifyable Collection Wrapper for the unmodifyableCollection method
	 */
	public static class UnmodifiableCollection implements ShortCollection {
		ShortCollection c;
		
		UnmodifiableCollection(ShortCollection c) {
			this.c = c;
		}
		
		@Override
		public boolean add(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(Collection<? extends Short> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(ShortCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(short[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean contains(short o) { return c.contains(o); }
		@Override
		public boolean containsAll(ShortCollection c) { return this.c.containsAll(c); }
		@Override
		public boolean containsAny(ShortCollection c) { return this.c.containsAny(c); }
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
		public ShortIterator iterator() { return ShortIterators.unmodifiable(c.iterator()); }
		@Override
		public ShortCollection copy() { return c.copy(); }
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
		public boolean removeIf(Predicate<? super Short> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remShort(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(ShortCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(ShortCollection c, ShortConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(ShortCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(ShortCollection c, ShortConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(IntPredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return c.toArray(); }
		@Override
		public <T> T[] toArray(T[] a) { return c.toArray(a); }
		@Override
		public short[] toShortArray() { return c.toShortArray(); }
		@Override
		public short[] toShortArray(short[] a) { return c.toShortArray(a); }
		@Override
		public void forEach(ShortConsumer action) { c.forEach(action); }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Short> action) { c.forEach(action); }
		@Override
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(Short2BooleanFunction filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(Short2BooleanFunction filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(Short2BooleanFunction filter) { return c.matchesAll(filter); }
		@Override
		public short reduce(short identity, ShortShortUnaryOperator operator) { return c.reduce(identity, operator); }
		@Override
		public short reduce(ShortShortUnaryOperator operator) { return c.reduce(operator); }
		@Override
		public short findFirst(Short2BooleanFunction filter) { return c.findFirst(filter); }
		@Override
		public int count(Short2BooleanFunction filter) { return c.count(filter); }
	}
	
	/**
	 * Empty Collection implementation for the empty collection function
	 */
	public static class EmptyCollection extends AbstractShortCollection {
		@Override
		public boolean add(short o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(ShortCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(short[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(short o) { return false; }
		@Override
		public boolean containsAll(ShortCollection c) { return false; }
		@Override
		public boolean containsAny(ShortCollection c) { return false; }
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
		public boolean removeIf(Predicate<? super Short> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remShort(short o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(ShortCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(ShortCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(IntPredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return ObjectArrays.EMPTY_ARRAY; }
		@Override
		public <T> T[] toArray(T[] a) { return a; }
		@Override
		public short[] toShortArray() { return ShortArrays.EMPTY_ARRAY; }
		@Override
		public short[] toShortArray(short[] a) { return a; }
		@Override
		public ShortIterator iterator() { return ShortIterators.empty(); }
		@Override
		public void clear() {}
		@Override
		public int size() { return 0; }
		@Override
		public EmptyCollection copy() { return this; }
	}
}