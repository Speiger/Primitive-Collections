package speiger.src.collections.longs.utils;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.LongPredicate;
import java.util.function.Consumer;

import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.longs.utils.LongArrays;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
/**
 * A Helper class for Collections
 */
public class LongCollections
{
	/**
	 * Empty Collection Reference
	 */
	public static final LongCollection EMPTY = new EmptyCollection();
	
	/**
	 * Returns a Immutable EmptyCollection instance that is automatically casted.
	 * @return an empty collection
	 */
	public static LongCollection empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable Collection instance based on the instance given.
	 * @param c that should be made immutable/unmodifiable
	 * @return a unmodifiable collection wrapper. If the Collection already a unmodifiable wrapper then it just returns itself.
	 */
	public static LongCollection unmodifiable(LongCollection c) {
		return c instanceof UnmodifiableCollection ? c : new UnmodifiableCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static LongCollection synchronize(LongCollection c) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static LongCollection synchronize(LongCollection c, Object mutex) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c, mutex);
	}
	
	/**
	 * Synchronized Collection Wrapper for the synchronizedCollection function
	 */
	public static class SynchronizedCollection implements LongCollection {
		LongCollection c;
		protected Object mutex;
		
		SynchronizedCollection(LongCollection c) {
			this.c = c;
			mutex = this;
		}
		
		SynchronizedCollection(LongCollection c, Object mutex) {
			this.c = c;
			this.mutex = mutex;
		}
		
		@Override
		public boolean add(long o) { synchronized(mutex) { return c.add(o); } }
		@Override
		public boolean addAll(Collection<? extends Long> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(LongCollection c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(long[] e, int offset, int length) { synchronized(mutex) { return c.addAll(e, offset, length); } }
		@Override
		public boolean contains(long o) { synchronized(mutex) { return c.contains(o); } }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public boolean containsAll(LongCollection c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(LongCollection c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public int size() { synchronized(mutex) { return c.size(); } }
		
		@Override
		public boolean isEmpty() { synchronized(mutex) { return c.isEmpty(); } }
		
		@Override
		public LongIterator iterator() {
			return c.iterator();
		}
		
		@Override
		public LongCollection copy() { synchronized(mutex) { return c.copy(); } }
		
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
		public boolean remLong(long o) { synchronized(mutex) { return c.remLong(o); } }
		@Override
		public boolean removeAll(LongCollection c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean removeAll(LongCollection c, LongConsumer r) { synchronized(mutex) { return this.c.removeAll(c, r); } }
		@Override
		public boolean retainAll(LongCollection c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean retainAll(LongCollection c, LongConsumer r) { synchronized(mutex) { return this.c.retainAll(c, r); } }
		@Override
		public boolean remIf(LongPredicate filter){ synchronized(mutex) { return c.remIf(filter); } }
		@Override
		public void clear() { synchronized(mutex) { c.clear(); } }
		@Override
		public Object[] toArray() { synchronized(mutex) { return c.toArray(); } }
		@Override
		public <T> T[] toArray(T[] a) { synchronized(mutex) { return c.toArray(a); } }
		@Override
		public long[] toLongArray() { synchronized(mutex) { return c.toLongArray(); } }
		@Override
		public long[] toLongArray(long[] a) { synchronized(mutex) { return c.toLongArray(a); } }
		@Override
		public void forEach(LongConsumer action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Long> action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) { synchronized(mutex) { return c.equals(obj); } }
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(Long2BooleanFunction filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Long2BooleanFunction filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Long2BooleanFunction filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public long reduce(LongLongUnaryOperator operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public long findFirst(Long2BooleanFunction filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(Long2BooleanFunction filter) { synchronized(mutex) { return c.count(filter); } }
	}
	
	/**
	 * Unmodifyable Collection Wrapper for the unmodifyableCollection method
	 */
	public static class UnmodifiableCollection implements LongCollection {
		LongCollection c;
		
		UnmodifiableCollection(LongCollection c) {
			this.c = c;
		}
		
		@Override
		public boolean add(long o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(Collection<? extends Long> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(LongCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(long[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean contains(long o) { return c.contains(o); }
		@Override
		public boolean containsAll(LongCollection c) { return this.c.containsAll(c); }
		@Override
		public boolean containsAny(LongCollection c) { return this.c.containsAny(c); }
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
		public LongIterator iterator() { return LongIterators.unmodifiable(c.iterator()); }
		@Override
		public LongCollection copy() { return c.copy(); }
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
		public boolean removeIf(Predicate<? super Long> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remLong(long o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(LongCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(LongCollection c, LongConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(LongCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(LongCollection c, LongConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(LongPredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return c.toArray(); }
		@Override
		public <T> T[] toArray(T[] a) { return c.toArray(a); }
		@Override
		public long[] toLongArray() { return c.toLongArray(); }
		@Override
		public long[] toLongArray(long[] a) { return c.toLongArray(a); }
		@Override
		public void forEach(LongConsumer action) { c.forEach(action); }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Long> action) { c.forEach(action); }
		@Override
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(Long2BooleanFunction filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(Long2BooleanFunction filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(Long2BooleanFunction filter) { return c.matchesAll(filter); }
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) { return c.reduce(identity, operator); }
		@Override
		public long reduce(LongLongUnaryOperator operator) { return c.reduce(operator); }
		@Override
		public long findFirst(Long2BooleanFunction filter) { return c.findFirst(filter); }
		@Override
		public int count(Long2BooleanFunction filter) { return c.count(filter); }
	}
	
	/**
	 * Empty Collection implementation for the empty collection function
	 */
	public static class EmptyCollection extends AbstractLongCollection {
		@Override
		public boolean add(long o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(LongCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(long[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(long o) { return false; }
		@Override
		public boolean containsAll(LongCollection c) { return false; }
		@Override
		public boolean containsAny(LongCollection c) { return false; }
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
		public boolean removeIf(Predicate<? super Long> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remLong(long o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(LongCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(LongCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(LongPredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return ObjectArrays.EMPTY_ARRAY; }
		@Override
		public <T> T[] toArray(T[] a) { return a; }
		@Override
		public long[] toLongArray() { return LongArrays.EMPTY_ARRAY; }
		@Override
		public long[] toLongArray(long[] a) { return a; }
		@Override
		public LongIterator iterator() { return LongIterators.empty(); }
		@Override
		public void clear() {}
		@Override
		public int size() { return 0; }
		@Override
		public EmptyCollection copy() { return this; }
	}
}