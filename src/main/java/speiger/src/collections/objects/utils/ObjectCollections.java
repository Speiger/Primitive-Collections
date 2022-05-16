package speiger.src.collections.objects.utils;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Consumer;

import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
/**
 * A Helper class for Collections
 */
public class ObjectCollections
{
	/**
	 * Empty Collection Reference
	 */
	public static final ObjectCollection<?> EMPTY = new EmptyCollection<>();
	
	/**
	 * Returns a Immutable EmptyCollection instance that is automatically casted.
	 * @param <T> the type of elements maintained by this Collection
	 * @return an empty collection
	 */
	public static <T> ObjectCollection<T> empty() {
		return (ObjectCollection<T>)EMPTY;
	}
	
	/**
	 * Returns a Immutable Collection instance based on the instance given.
	 * @param c that should be made immutable/unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable collection wrapper. If the Collection already a unmodifiable wrapper then it just returns itself.
	 */
	public static <T> ObjectCollection<T> unmodifiable(ObjectCollection<T> c) {
		return c instanceof UnmodifiableCollection ? c : new UnmodifiableCollection<>(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static <T> ObjectCollection<T> synchronize(ObjectCollection<T> c) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection<>(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static <T> ObjectCollection<T> synchronize(ObjectCollection<T> c, Object mutex) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection<>(c, mutex);
	}
	
	/**
	 * Synchronized Collection Wrapper for the synchronizedCollection function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedCollection<T> implements ObjectCollection<T> {
		ObjectCollection<T> c;
		protected Object mutex;
		
		SynchronizedCollection(ObjectCollection<T> c) {
			this.c = c;
			mutex = this;
		}
		
		SynchronizedCollection(ObjectCollection<T> c, Object mutex) {
			this.c = c;
			this.mutex = mutex;
		}
		
		@Override
		public boolean add(T o) { synchronized(mutex) { return c.add(o); } }
		@Override
		public boolean addAll(Collection<? extends T> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(ObjectCollection<T> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(T[] e, int offset, int length) { synchronized(mutex) { return c.addAll(e, offset, length); } }
		@Override
		public boolean contains(Object o) { synchronized(mutex) { return c.contains(o); } }
		@Override
		public boolean containsAll(Collection<?> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(Collection<?> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public boolean containsAll(ObjectCollection<T> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(ObjectCollection<T> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public int size() { synchronized(mutex) { return c.size(); } }
		
		@Override
		public boolean isEmpty() { synchronized(mutex) { return c.isEmpty(); } }
		
		@Override
		public ObjectIterator<T> iterator() {
			return c.iterator();
		}
		
		@Override
		public ObjectCollection<T> copy() { synchronized(mutex) { return c.copy(); } }
		
		@Override
		public boolean remove(Object o) { synchronized(mutex) { return c.remove(o); } }
		@Override
		public boolean removeAll(Collection<?> c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean retainAll(Collection<?> c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean removeAll(ObjectCollection<T> c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean removeAll(ObjectCollection<T> c, Consumer<T> r) { synchronized(mutex) { return this.c.removeAll(c, r); } }
		@Override
		public boolean retainAll(ObjectCollection<T> c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean retainAll(ObjectCollection<T> c, Consumer<T> r) { synchronized(mutex) { return this.c.retainAll(c, r); } }
		@Override
		public void clear() { synchronized(mutex) { c.clear(); } }
		@Override
		public Object[] toArray() { synchronized(mutex) { return c.toArray(); } }
		@Override
		public <E> E[] toArray(E[] a) { synchronized(mutex) { return c.toArray(a); } }
		@Override
		public void forEach(Consumer<? super T> action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) { synchronized(mutex) { return c.equals(obj); } }
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(Object2BooleanFunction<T> filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Object2BooleanFunction<T> filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Object2BooleanFunction<T> filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public T findFirst(Object2BooleanFunction<T> filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(Object2BooleanFunction<T> filter) { synchronized(mutex) { return c.count(filter); } }
	}
	
	/**
	 * Unmodifyable Collection Wrapper for the unmodifyableCollection method
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifiableCollection<T> implements ObjectCollection<T> {
		ObjectCollection<T> c;
		
		UnmodifiableCollection(ObjectCollection<T> c) {
			this.c = c;
		}
		
		@Override
		public boolean add(T o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(Collection<? extends T> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(T[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean contains(Object o) { return c.contains(o); }
		@Override
		public boolean containsAll(ObjectCollection<T> c) { return this.c.containsAll(c); }
		@Override
		public boolean containsAny(ObjectCollection<T> c) { return this.c.containsAny(c); }
		@Override
		public boolean containsAny(Collection<?> c) { return this.c.containsAny(c); }
		@Override
		public boolean containsAll(Collection<?> c) { return this.c.containsAll(c); }
		@Override
		public int size() { return c.size(); }
		@Override
		public boolean isEmpty() { return c.isEmpty(); }
		@Override
		public ObjectIterator<T> iterator() { return ObjectIterators.unmodifiable(c.iterator()); }
		@Override
		public ObjectCollection<T> copy() { return c.copy(); }
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeIf(Predicate<? super T> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(ObjectCollection<T> c, Consumer<T> r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(ObjectCollection<T> c, Consumer<T> r) { throw new UnsupportedOperationException(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return c.toArray(); }
		@Override
		public <E> E[] toArray(E[] a) { return c.toArray(a); }
		@Override
		public void forEach(Consumer<? super T> action) { c.forEach(action); }
		@Override
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(Object2BooleanFunction<T> filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(Object2BooleanFunction<T> filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(Object2BooleanFunction<T> filter) { return c.matchesAll(filter); }
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) { return c.reduce(identity, operator); }
		@Override
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) { return c.reduce(operator); }
		@Override
		public T findFirst(Object2BooleanFunction<T> filter) { return c.findFirst(filter); }
		@Override
		public int count(Object2BooleanFunction<T> filter) { return c.count(filter); }
	}
	
	/**
	 * Empty Collection implementation for the empty collection function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class EmptyCollection<T> extends AbstractObjectCollection<T> {
		@Override
		public boolean add(T o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(T[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(Object o) { return false; }
		@Override
		public boolean containsAny(Collection<?> c) { return false; }
		@Override
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
		public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeIf(Predicate<? super T> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return ObjectArrays.EMPTY_ARRAY; }
		@Override
		public <E> E[] toArray(E[] a) { return a; }
		@Override
		public ObjectIterator<T> iterator() { return ObjectIterators.empty(); }
		@Override
		public void clear() {}
		@Override
		public int size() { return 0; }
		@Override
		public EmptyCollection<T> copy() { return this; }
	}
}