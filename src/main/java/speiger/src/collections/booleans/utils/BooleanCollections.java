package speiger.src.collections.booleans.utils;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Consumer;

import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.utils.BooleanArrays;
import speiger.src.collections.booleans.functions.function.Boolean2BooleanFunction;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
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
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) { synchronized(mutex) { return c.equals(obj); } }
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectBooleanConsumer<E> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(Boolean2BooleanFunction filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Boolean2BooleanFunction filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Boolean2BooleanFunction filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		@Override
		public boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public boolean reduce(BooleanBooleanUnaryOperator operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public boolean findFirst(Boolean2BooleanFunction filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(Boolean2BooleanFunction filter) { synchronized(mutex) { return c.count(filter); } }
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
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectBooleanConsumer<E> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(Boolean2BooleanFunction filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(Boolean2BooleanFunction filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(Boolean2BooleanFunction filter) { return c.matchesAll(filter); }
		@Override
		public boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) { return c.reduce(identity, operator); }
		@Override
		public boolean reduce(BooleanBooleanUnaryOperator operator) { return c.reduce(operator); }
		@Override
		public boolean findFirst(Boolean2BooleanFunction filter) { return c.findFirst(filter); }
		@Override
		public int count(Boolean2BooleanFunction filter) { return c.count(filter); }
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
		public boolean containsAll(BooleanCollection c) { return false; }
		@Override
		public boolean containsAny(BooleanCollection c) { return false; }
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
		public <T> T[] toArray(T[] a) { return a; }
		@Override
		public boolean[] toBooleanArray() { return BooleanArrays.EMPTY_ARRAY; }
		@Override
		public boolean[] toBooleanArray(boolean[] a) { return a; }
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