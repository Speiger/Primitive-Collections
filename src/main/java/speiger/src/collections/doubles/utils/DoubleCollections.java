package speiger.src.collections.doubles.utils;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.DoublePredicate;
import java.util.function.Consumer;

import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.utils.DoubleArrays;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
/**
 * A Helper class for Collections
 */
public class DoubleCollections
{
	/**
	 * Empty Collection Reference
	 */
	public static final DoubleCollection EMPTY = new EmptyCollection();
	
	/**
	 * Returns a Immutable EmptyCollection instance that is automatically casted.
	 * @return an empty collection
	 */
	public static DoubleCollection empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable Collection instance based on the instance given.
	 * @param c that should be made immutable/unmodifiable
	 * @return a unmodifiable collection wrapper. If the Collection already a unmodifiable wrapper then it just returns itself.
	 */
	public static DoubleCollection unmodifiable(DoubleCollection c) {
		return c instanceof UnmodifiableCollection ? c : new UnmodifiableCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static DoubleCollection synchronize(DoubleCollection c) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static DoubleCollection synchronize(DoubleCollection c, Object mutex) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c, mutex);
	}
	
	/**
	 * Synchronized Collection Wrapper for the synchronizedCollection function
	 */
	public static class SynchronizedCollection implements DoubleCollection {
		DoubleCollection c;
		protected Object mutex;
		
		SynchronizedCollection(DoubleCollection c) {
			this.c = c;
			mutex = this;
		}
		
		SynchronizedCollection(DoubleCollection c, Object mutex) {
			this.c = c;
			this.mutex = mutex;
		}
		
		@Override
		public boolean add(double o) { synchronized(mutex) { return c.add(o); } }
		@Override
		public boolean addAll(Collection<? extends Double> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(DoubleCollection c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(double[] e, int offset, int length) { synchronized(mutex) { return c.addAll(e, offset, length); } }
		@Override
		public boolean contains(double o) { synchronized(mutex) { return c.contains(o); } }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public boolean containsAll(DoubleCollection c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(DoubleCollection c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public int size() { synchronized(mutex) { return c.size(); } }
		
		@Override
		public boolean isEmpty() { synchronized(mutex) { return c.isEmpty(); } }
		
		@Override
		public DoubleIterator iterator() {
			return c.iterator();
		}
		
		@Override
		public DoubleCollection copy() { synchronized(mutex) { return c.copy(); } }
		
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
		public boolean remDouble(double o) { synchronized(mutex) { return c.remDouble(o); } }
		@Override
		public boolean removeAll(DoubleCollection c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean removeAll(DoubleCollection c, DoubleConsumer r) { synchronized(mutex) { return this.c.removeAll(c, r); } }
		@Override
		public boolean retainAll(DoubleCollection c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean retainAll(DoubleCollection c, DoubleConsumer r) { synchronized(mutex) { return this.c.retainAll(c, r); } }
		@Override
		public boolean remIf(DoublePredicate filter){ synchronized(mutex) { return c.remIf(filter); } }
		@Override
		public void clear() { synchronized(mutex) { c.clear(); } }
		@Override
		public Object[] toArray() { synchronized(mutex) { return c.toArray(); } }
		@Override
		public <T> T[] toArray(T[] a) { synchronized(mutex) { return c.toArray(a); } }
		@Override
		public double[] toDoubleArray() { synchronized(mutex) { return c.toDoubleArray(); } }
		@Override
		public double[] toDoubleArray(double[] a) { synchronized(mutex) { return c.toDoubleArray(a); } }
		@Override
		public void forEach(DoubleConsumer action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Double> action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) { synchronized(mutex) { return c.equals(obj); } }
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(Double2BooleanFunction filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Double2BooleanFunction filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Double2BooleanFunction filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		@Override
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public double reduce(DoubleDoubleUnaryOperator operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public double findFirst(Double2BooleanFunction filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(Double2BooleanFunction filter) { synchronized(mutex) { return c.count(filter); } }
	}
	
	/**
	 * Unmodifyable Collection Wrapper for the unmodifyableCollection method
	 */
	public static class UnmodifiableCollection implements DoubleCollection {
		DoubleCollection c;
		
		UnmodifiableCollection(DoubleCollection c) {
			this.c = c;
		}
		
		@Override
		public boolean add(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(Collection<? extends Double> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(double[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean contains(double o) { return c.contains(o); }
		@Override
		public boolean containsAll(DoubleCollection c) { return this.c.containsAll(c); }
		@Override
		public boolean containsAny(DoubleCollection c) { return this.c.containsAny(c); }
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
		public DoubleIterator iterator() { return DoubleIterators.unmodifiable(c.iterator()); }
		@Override
		public DoubleCollection copy() { return c.copy(); }
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
		public boolean removeIf(Predicate<? super Double> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remDouble(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(DoubleCollection c, DoubleConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(DoubleCollection c, DoubleConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(DoublePredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return c.toArray(); }
		@Override
		public <T> T[] toArray(T[] a) { return c.toArray(a); }
		@Override
		public double[] toDoubleArray() { return c.toDoubleArray(); }
		@Override
		public double[] toDoubleArray(double[] a) { return c.toDoubleArray(a); }
		@Override
		public void forEach(DoubleConsumer action) { c.forEach(action); }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Double> action) { c.forEach(action); }
		@Override
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(Double2BooleanFunction filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(Double2BooleanFunction filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(Double2BooleanFunction filter) { return c.matchesAll(filter); }
		@Override
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) { return c.reduce(identity, operator); }
		@Override
		public double reduce(DoubleDoubleUnaryOperator operator) { return c.reduce(operator); }
		@Override
		public double findFirst(Double2BooleanFunction filter) { return c.findFirst(filter); }
		@Override
		public int count(Double2BooleanFunction filter) { return c.count(filter); }
	}
	
	/**
	 * Empty Collection implementation for the empty collection function
	 */
	public static class EmptyCollection extends AbstractDoubleCollection {
		@Override
		public boolean add(double o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(double[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(double o) { return false; }
		@Override
		public boolean containsAll(DoubleCollection c) { return false; }
		@Override
		public boolean containsAny(DoubleCollection c) { return false; }
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
		public boolean removeIf(Predicate<? super Double> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remDouble(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(DoublePredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return ObjectArrays.EMPTY_ARRAY; }
		@Override
		public <T> T[] toArray(T[] a) { return a; }
		@Override
		public double[] toDoubleArray() { return DoubleArrays.EMPTY_ARRAY; }
		@Override
		public double[] toDoubleArray(double[] a) { return a; }
		@Override
		public DoubleIterator iterator() { return DoubleIterators.empty(); }
		@Override
		public void clear() {}
		@Override
		public int size() { return 0; }
		@Override
		public EmptyCollection copy() { return this; }
	}
}