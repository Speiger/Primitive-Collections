package speiger.src.collections.chars.utils;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.IntPredicate;
import java.util.function.Consumer;

import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.chars.utils.CharArrays;
import speiger.src.collections.chars.functions.function.Char2BooleanFunction;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
/**
 * A Helper class for Collections
 */
public class CharCollections
{
	/**
	 * Empty Collection Reference
	 */
	public static final CharCollection EMPTY = new EmptyCollection();
	
	/**
	 * Returns a Immutable EmptyCollection instance that is automatically casted.
	 * @return an empty collection
	 */
	public static CharCollection empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable Collection instance based on the instance given.
	 * @param c that should be made immutable/unmodifiable
	 * @return a unmodifiable collection wrapper. If the Collection already a unmodifiable wrapper then it just returns itself.
	 */
	public static CharCollection unmodifiable(CharCollection c) {
		return c instanceof UnmodifiableCollection ? c : new UnmodifiableCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static CharCollection synchronize(CharCollection c) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static CharCollection synchronize(CharCollection c, Object mutex) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c, mutex);
	}
	
	/**
	 * Synchronized Collection Wrapper for the synchronizedCollection function
	 */
	public static class SynchronizedCollection implements CharCollection {
		CharCollection c;
		protected Object mutex;
		
		SynchronizedCollection(CharCollection c) {
			this.c = c;
			mutex = this;
		}
		
		SynchronizedCollection(CharCollection c, Object mutex) {
			this.c = c;
			this.mutex = mutex;
		}
		
		@Override
		public boolean add(char o) { synchronized(mutex) { return c.add(o); } }
		@Override
		public boolean addAll(Collection<? extends Character> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(CharCollection c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(char[] e, int offset, int length) { synchronized(mutex) { return c.addAll(e, offset, length); } }
		@Override
		public boolean contains(char o) { synchronized(mutex) { return c.contains(o); } }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public boolean containsAll(CharCollection c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(CharCollection c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public int size() { synchronized(mutex) { return c.size(); } }
		
		@Override
		public boolean isEmpty() { synchronized(mutex) { return c.isEmpty(); } }
		
		@Override
		public CharIterator iterator() {
			return c.iterator();
		}
		
		@Override
		public CharCollection copy() { synchronized(mutex) { return c.copy(); } }
		
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
		public boolean remChar(char o) { synchronized(mutex) { return c.remChar(o); } }
		@Override
		public boolean removeAll(CharCollection c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean removeAll(CharCollection c, CharConsumer r) { synchronized(mutex) { return this.c.removeAll(c, r); } }
		@Override
		public boolean retainAll(CharCollection c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean retainAll(CharCollection c, CharConsumer r) { synchronized(mutex) { return this.c.retainAll(c, r); } }
		@Override
		public boolean remIf(IntPredicate filter){ synchronized(mutex) { return c.remIf(filter); } }
		@Override
		public void clear() { synchronized(mutex) { c.clear(); } }
		@Override
		public Object[] toArray() { synchronized(mutex) { return c.toArray(); } }
		@Override
		public <T> T[] toArray(T[] a) { synchronized(mutex) { return c.toArray(a); } }
		@Override
		public char[] toCharArray() { synchronized(mutex) { return c.toCharArray(); } }
		@Override
		public char[] toCharArray(char[] a) { synchronized(mutex) { return c.toCharArray(a); } }
		@Override
		public void forEach(CharConsumer action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Character> action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) { synchronized(mutex) { return c.equals(obj); } }
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectCharConsumer<E> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(Char2BooleanFunction filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(Char2BooleanFunction filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(Char2BooleanFunction filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		@Override
		public char reduce(char identity, CharCharUnaryOperator operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public char reduce(CharCharUnaryOperator operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public char findFirst(Char2BooleanFunction filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(Char2BooleanFunction filter) { synchronized(mutex) { return c.count(filter); } }
	}
	
	/**
	 * Unmodifyable Collection Wrapper for the unmodifyableCollection method
	 */
	public static class UnmodifiableCollection implements CharCollection {
		CharCollection c;
		
		UnmodifiableCollection(CharCollection c) {
			this.c = c;
		}
		
		@Override
		public boolean add(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(Collection<? extends Character> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(CharCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(char[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean contains(char o) { return c.contains(o); }
		@Override
		public boolean containsAll(CharCollection c) { return this.c.containsAll(c); }
		@Override
		public boolean containsAny(CharCollection c) { return this.c.containsAny(c); }
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
		public CharIterator iterator() { return CharIterators.unmodifiable(c.iterator()); }
		@Override
		public CharCollection copy() { return c.copy(); }
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
		public boolean removeIf(Predicate<? super Character> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remChar(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(CharCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(CharCollection c, CharConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(CharCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(CharCollection c, CharConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(IntPredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return c.toArray(); }
		@Override
		public <T> T[] toArray(T[] a) { return c.toArray(a); }
		@Override
		public char[] toCharArray() { return c.toCharArray(); }
		@Override
		public char[] toCharArray(char[] a) { return c.toCharArray(a); }
		@Override
		public void forEach(CharConsumer action) { c.forEach(action); }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Character> action) { c.forEach(action); }
		@Override
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectCharConsumer<E> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(Char2BooleanFunction filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(Char2BooleanFunction filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(Char2BooleanFunction filter) { return c.matchesAll(filter); }
		@Override
		public char reduce(char identity, CharCharUnaryOperator operator) { return c.reduce(identity, operator); }
		@Override
		public char reduce(CharCharUnaryOperator operator) { return c.reduce(operator); }
		@Override
		public char findFirst(Char2BooleanFunction filter) { return c.findFirst(filter); }
		@Override
		public int count(Char2BooleanFunction filter) { return c.count(filter); }
	}
	
	/**
	 * Empty Collection implementation for the empty collection function
	 */
	public static class EmptyCollection extends AbstractCharCollection {
		@Override
		public boolean add(char o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(CharCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(char[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(char o) { return false; }
		@Override
		public boolean containsAll(CharCollection c) { return false; }
		@Override
		public boolean containsAny(CharCollection c) { return false; }
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
		public boolean removeIf(Predicate<? super Character> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remChar(char o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(CharCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(CharCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(IntPredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return ObjectArrays.EMPTY_ARRAY; }
		@Override
		public <T> T[] toArray(T[] a) { return a; }
		@Override
		public char[] toCharArray() { return CharArrays.EMPTY_ARRAY; }
		@Override
		public char[] toCharArray(char[] a) { return a; }
		@Override
		public CharIterator iterator() { return CharIterators.empty(); }
		@Override
		public void clear() {}
		@Override
		public int size() { return 0; }
		@Override
		public EmptyCollection copy() { return this; }
	}
}