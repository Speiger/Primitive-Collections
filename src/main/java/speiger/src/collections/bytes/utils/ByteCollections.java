package speiger.src.collections.bytes.utils;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.IntPredicate;
import java.util.function.Consumer;

import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.utils.ByteArrays;
import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
/**
 * A Helper class for Collections
 */
public class ByteCollections
{
	/**
	 * Empty Collection Reference
	 */
	public static final ByteCollection EMPTY = new EmptyCollection();
	
	/**
	 * Returns a Immutable EmptyCollection instance that is automatically casted.
	 * @return an empty collection
	 */
	public static ByteCollection empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable Collection instance based on the instance given.
	 * @param c that should be made immutable/unmodifiable
	 * @return a unmodifiable collection wrapper. If the Collection already a unmodifiable wrapper then it just returns itself.
	 */
	public static ByteCollection unmodifiable(ByteCollection c) {
		return c instanceof UnmodifiableCollection ? c : new UnmodifiableCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static ByteCollection synchronize(ByteCollection c) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static ByteCollection synchronize(ByteCollection c, Object mutex) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c, mutex);
	}
	
	/**
	 * Synchronized Collection Wrapper for the synchronizedCollection function
	 */
	public static class SynchronizedCollection implements ByteCollection {
		ByteCollection c;
		protected Object mutex;
		
		SynchronizedCollection(ByteCollection c) {
			this.c = c;
			mutex = this;
		}
		
		SynchronizedCollection(ByteCollection c, Object mutex) {
			this.c = c;
			this.mutex = mutex;
		}
		
		@Override
		public boolean add(byte o) { synchronized(mutex) { return c.add(o); } }
		@Override
		public boolean addAll(Collection<? extends Byte> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(ByteCollection c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(byte[] e, int offset, int length) { synchronized(mutex) { return c.addAll(e, offset, length); } }
		@Override
		public boolean contains(byte o) { synchronized(mutex) { return c.contains(o); } }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public boolean containsAll(ByteCollection c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(ByteCollection c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public int size() { synchronized(mutex) { return c.size(); } }
		
		@Override
		public boolean isEmpty() { synchronized(mutex) { return c.isEmpty(); } }
		
		@Override
		public ByteIterator iterator() {
			return c.iterator();
		}
		
		@Override
		public ByteCollection copy() { synchronized(mutex) { return c.copy(); } }
		
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
		public boolean remByte(byte o) { synchronized(mutex) { return c.remByte(o); } }
		@Override
		public boolean removeAll(ByteCollection c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean removeAll(ByteCollection c, ByteConsumer r) { synchronized(mutex) { return this.c.removeAll(c, r); } }
		@Override
		public boolean retainAll(ByteCollection c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean retainAll(ByteCollection c, ByteConsumer r) { synchronized(mutex) { return this.c.retainAll(c, r); } }
		@Override
		public boolean remIf(IntPredicate filter){ synchronized(mutex) { return c.remIf(filter); } }
		@Override
		public void clear() { synchronized(mutex) { c.clear(); } }
		@Override
		public Object[] toArray() { synchronized(mutex) { return c.toArray(); } }
		@Override
		public <T> T[] toArray(T[] a) { synchronized(mutex) { return c.toArray(a); } }
		@Override
		public byte[] toByteArray() { synchronized(mutex) { return c.toByteArray(); } }
		@Override
		public byte[] toByteArray(byte[] a) { synchronized(mutex) { return c.toByteArray(a); } }
		@Override
		public void forEach(ByteConsumer action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Byte> action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) { synchronized(mutex) { return c.equals(obj); } }
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectByteConsumer<E> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(Byte2BooleanFunction filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Byte2BooleanFunction filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Byte2BooleanFunction filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		@Override
		public byte reduce(byte identity, ByteByteUnaryOperator operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public byte reduce(ByteByteUnaryOperator operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public byte findFirst(Byte2BooleanFunction filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(Byte2BooleanFunction filter) { synchronized(mutex) { return c.count(filter); } }
	}
	
	/**
	 * Unmodifyable Collection Wrapper for the unmodifyableCollection method
	 */
	public static class UnmodifiableCollection implements ByteCollection {
		ByteCollection c;
		
		UnmodifiableCollection(ByteCollection c) {
			this.c = c;
		}
		
		@Override
		public boolean add(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(Collection<? extends Byte> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(ByteCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(byte[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean contains(byte o) { return c.contains(o); }
		@Override
		public boolean containsAll(ByteCollection c) { return this.c.containsAll(c); }
		@Override
		public boolean containsAny(ByteCollection c) { return this.c.containsAny(c); }
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
		public ByteIterator iterator() { return ByteIterators.unmodifiable(c.iterator()); }
		@Override
		public ByteCollection copy() { return c.copy(); }
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
		public boolean removeIf(Predicate<? super Byte> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remByte(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(ByteCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(ByteCollection c, ByteConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(ByteCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(ByteCollection c, ByteConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(IntPredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return c.toArray(); }
		@Override
		public <T> T[] toArray(T[] a) { return c.toArray(a); }
		@Override
		public byte[] toByteArray() { return c.toByteArray(); }
		@Override
		public byte[] toByteArray(byte[] a) { return c.toByteArray(a); }
		@Override
		public void forEach(ByteConsumer action) { c.forEach(action); }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Byte> action) { c.forEach(action); }
		@Override
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectByteConsumer<E> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(Byte2BooleanFunction filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(Byte2BooleanFunction filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(Byte2BooleanFunction filter) { return c.matchesAll(filter); }
		@Override
		public byte reduce(byte identity, ByteByteUnaryOperator operator) { return c.reduce(identity, operator); }
		@Override
		public byte reduce(ByteByteUnaryOperator operator) { return c.reduce(operator); }
		@Override
		public byte findFirst(Byte2BooleanFunction filter) { return c.findFirst(filter); }
		@Override
		public int count(Byte2BooleanFunction filter) { return c.count(filter); }
	}
	
	/**
	 * Empty Collection implementation for the empty collection function
	 */
	public static class EmptyCollection extends AbstractByteCollection {
		@Override
		public boolean add(byte o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(ByteCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(byte[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(byte o) { return false; }
		@Override
		public boolean containsAll(ByteCollection c) { return false; }
		@Override
		public boolean containsAny(ByteCollection c) { return false; }
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
		public boolean removeIf(Predicate<? super Byte> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remByte(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(ByteCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(ByteCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(IntPredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return ObjectArrays.EMPTY_ARRAY; }
		@Override
		public <T> T[] toArray(T[] a) { return a; }
		@Override
		public byte[] toByteArray() { return ByteArrays.EMPTY_ARRAY; }
		@Override
		public byte[] toByteArray(byte[] a) { return a; }
		@Override
		public ByteIterator iterator() { return ByteIterators.empty(); }
		@Override
		public void clear() {}
		@Override
		public int size() { return 0; }
		@Override
		public EmptyCollection copy() { return this; }
	}
}