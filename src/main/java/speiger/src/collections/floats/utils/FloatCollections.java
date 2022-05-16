package speiger.src.collections.floats.utils;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.DoublePredicate;
import java.util.function.Consumer;

import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.floats.utils.FloatArrays;
import speiger.src.collections.floats.functions.function.Float2BooleanFunction;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
/**
 * A Helper class for Collections
 */
public class FloatCollections
{
	/**
	 * Empty Collection Reference
	 */
	public static final FloatCollection EMPTY = new EmptyCollection();
	
	/**
	 * Returns a Immutable EmptyCollection instance that is automatically casted.
	 * @return an empty collection
	 */
	public static FloatCollection empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable Collection instance based on the instance given.
	 * @param c that should be made immutable/unmodifiable
	 * @return a unmodifiable collection wrapper. If the Collection already a unmodifiable wrapper then it just returns itself.
	 */
	public static FloatCollection unmodifiable(FloatCollection c) {
		return c instanceof UnmodifiableCollection ? c : new UnmodifiableCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static FloatCollection synchronize(FloatCollection c) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static FloatCollection synchronize(FloatCollection c, Object mutex) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c, mutex);
	}
	
	/**
	 * Synchronized Collection Wrapper for the synchronizedCollection function
	 */
	public static class SynchronizedCollection implements FloatCollection {
		FloatCollection c;
		protected Object mutex;
		
		SynchronizedCollection(FloatCollection c) {
			this.c = c;
			mutex = this;
		}
		
		SynchronizedCollection(FloatCollection c, Object mutex) {
			this.c = c;
			this.mutex = mutex;
		}
		
		@Override
		public boolean add(float o) { synchronized(mutex) { return c.add(o); } }
		@Override
		public boolean addAll(Collection<? extends Float> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(FloatCollection c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(float[] e, int offset, int length) { synchronized(mutex) { return c.addAll(e, offset, length); } }
		@Override
		public boolean contains(float o) { synchronized(mutex) { return c.contains(o); } }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public boolean containsAll(FloatCollection c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(FloatCollection c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public int size() { synchronized(mutex) { return c.size(); } }
		
		@Override
		public boolean isEmpty() { synchronized(mutex) { return c.isEmpty(); } }
		
		@Override
		public FloatIterator iterator() {
			return c.iterator();
		}
		
		@Override
		public FloatCollection copy() { synchronized(mutex) { return c.copy(); } }
		
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
		public boolean remFloat(float o) { synchronized(mutex) { return c.remFloat(o); } }
		@Override
		public boolean removeAll(FloatCollection c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean removeAll(FloatCollection c, FloatConsumer r) { synchronized(mutex) { return this.c.removeAll(c, r); } }
		@Override
		public boolean retainAll(FloatCollection c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean retainAll(FloatCollection c, FloatConsumer r) { synchronized(mutex) { return this.c.retainAll(c, r); } }
		@Override
		public boolean remIf(DoublePredicate filter){ synchronized(mutex) { return c.remIf(filter); } }
		@Override
		public void clear() { synchronized(mutex) { c.clear(); } }
		@Override
		public Object[] toArray() { synchronized(mutex) { return c.toArray(); } }
		@Override
		public <T> T[] toArray(T[] a) { synchronized(mutex) { return c.toArray(a); } }
		@Override
		public float[] toFloatArray() { synchronized(mutex) { return c.toFloatArray(); } }
		@Override
		public float[] toFloatArray(float[] a) { synchronized(mutex) { return c.toFloatArray(a); } }
		@Override
		public void forEach(FloatConsumer action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Float> action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) { synchronized(mutex) { return c.equals(obj); } }
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectFloatConsumer<E> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(Float2BooleanFunction filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Float2BooleanFunction filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Float2BooleanFunction filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		@Override
		public float reduce(float identity, FloatFloatUnaryOperator operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public float reduce(FloatFloatUnaryOperator operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public float findFirst(Float2BooleanFunction filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(Float2BooleanFunction filter) { synchronized(mutex) { return c.count(filter); } }
	}
	
	/**
	 * Unmodifyable Collection Wrapper for the unmodifyableCollection method
	 */
	public static class UnmodifiableCollection implements FloatCollection {
		FloatCollection c;
		
		UnmodifiableCollection(FloatCollection c) {
			this.c = c;
		}
		
		@Override
		public boolean add(float o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(Collection<? extends Float> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(FloatCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(float[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean contains(float o) { return c.contains(o); }
		@Override
		public boolean containsAll(FloatCollection c) { return this.c.containsAll(c); }
		@Override
		public boolean containsAny(FloatCollection c) { return this.c.containsAny(c); }
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
		public FloatIterator iterator() { return FloatIterators.unmodifiable(c.iterator()); }
		@Override
		public FloatCollection copy() { return c.copy(); }
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
		public boolean removeIf(Predicate<? super Float> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remFloat(float o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(FloatCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(FloatCollection c, FloatConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(FloatCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(FloatCollection c, FloatConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(DoublePredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return c.toArray(); }
		@Override
		public <T> T[] toArray(T[] a) { return c.toArray(a); }
		@Override
		public float[] toFloatArray() { return c.toFloatArray(); }
		@Override
		public float[] toFloatArray(float[] a) { return c.toFloatArray(a); }
		@Override
		public void forEach(FloatConsumer action) { c.forEach(action); }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Float> action) { c.forEach(action); }
		@Override
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectFloatConsumer<E> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(Float2BooleanFunction filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(Float2BooleanFunction filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(Float2BooleanFunction filter) { return c.matchesAll(filter); }
		@Override
		public float reduce(float identity, FloatFloatUnaryOperator operator) { return c.reduce(identity, operator); }
		@Override
		public float reduce(FloatFloatUnaryOperator operator) { return c.reduce(operator); }
		@Override
		public float findFirst(Float2BooleanFunction filter) { return c.findFirst(filter); }
		@Override
		public int count(Float2BooleanFunction filter) { return c.count(filter); }
	}
	
	/**
	 * Empty Collection implementation for the empty collection function
	 */
	public static class EmptyCollection extends AbstractFloatCollection {
		@Override
		public boolean add(float o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(FloatCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(float[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(float o) { return false; }
		@Override
		public boolean containsAll(FloatCollection c) { return false; }
		@Override
		public boolean containsAny(FloatCollection c) { return false; }
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
		public boolean removeIf(Predicate<? super Float> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remFloat(float o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(FloatCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(FloatCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(DoublePredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return ObjectArrays.EMPTY_ARRAY; }
		@Override
		public <T> T[] toArray(T[] a) { return a; }
		@Override
		public float[] toFloatArray() { return FloatArrays.EMPTY_ARRAY; }
		@Override
		public float[] toFloatArray(float[] a) { return a; }
		@Override
		public FloatIterator iterator() { return FloatIterators.empty(); }
		@Override
		public void clear() {}
		@Override
		public int size() { return 0; }
		@Override
		public EmptyCollection copy() { return this; }
	}
}