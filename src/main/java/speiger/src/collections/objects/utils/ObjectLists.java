package speiger.src.collections.objects.utils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.lists.AbstractObjectList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Lists
 */
public class ObjectLists
{
	/**
	 * Empty List reference
	 */
	private static final EmptyList<?> EMPTY = new EmptyList<>();
	
	/**
	 * Returns a Immutable EmptyList instance that is automatically casted.
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return an empty list
	 */
	public static <T> EmptyList<T> empty() {
		return (EmptyList<T>)EMPTY;
	}
	
	/**
	 * Returns a Immutable List instance based on the instance given.
	 * @param l that should be made immutable/unmodifiable
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a unmodifiable list wrapper. If the list is implementing RandomAccess that is also transferred. If the List already a unmodifiable wrapper then it just returns itself.
	 */
	public static <T> ObjectList<T> unmodifiable(ObjectList<T> l) {
		return l instanceof UnmodifiableList ? l : l instanceof RandomAccess ? new UnmodifiableRandomList<>(l) : new UnmodifiableList<>(l);
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IObjectArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static <T> ObjectList<T> synchronize(ObjectList<T> l) {
		return l instanceof SynchronizedList ? l : (l instanceof IObjectArray ? new SynchronizedArrayList<>(l) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList<>(l) : new SynchronizedList<>(l)));
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IObjectArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static <T> ObjectList<T> synchronize(ObjectList<T> l, Object mutex) {
		return l instanceof SynchronizedList ? l : (l instanceof IObjectArray ? new SynchronizedArrayList<>(l, mutex) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList<>(l, mutex) : new SynchronizedList<>(l, mutex)));
	}
	
	/**
	 * Creates a Unmodifiable Singleton list
	 * @param element that should be used in the Singleton
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a singleton list that is unmodifiable
	 */
	public static <T> ObjectList<T> singleton(T element) {
		return new SingletonList<>(element);
	}
	
	/**
	 * Reverses the list
	 * @param list that should be reversed.
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return the input list
	 */
	public static <T> ObjectList<T> reverse(ObjectList<T> list) {
		int size = list.size();
		if(list instanceof IObjectArray) {
			IObjectArray<T> array = (IObjectArray<T>)list;
			if(array.isCastable()) {
				if(list instanceof SynchronizedArrayList) array.elements(T -> ObjectArrays.reverse(T, size));
				else ObjectArrays.reverse(array.elements(), size);
				return list;
			}
		}
		if(list instanceof RandomAccess) {
			for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
				T t = list.get(i);
				list.set(i, list.get(j));
				list.set(j, t);
			}
			return list;
		}
		ObjectListIterator<T> fwd = list.listIterator();
		ObjectListIterator<T> rev = list.listIterator(size);
		for(int i = 0, mid = size >> 1; i < mid; i++) {
			T tmp = fwd.next();
			fwd.set(rev.previous());
			rev.set(tmp);
		}
		return list;
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return the input list
	 */
	public static <T> ObjectList<T> shuffle(ObjectList<T> list) {
		return shuffle(list, SanityChecks.getRandom());
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @param random the random that should be used
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return the input list
	 */
	public static <T> ObjectList<T> shuffle(ObjectList<T> list, Random random) {
		int size = list.size();
		if(list instanceof IObjectArray) {
			IObjectArray<T> array = (IObjectArray<T>)list;
			if(array.isCastable()) {
				if(list instanceof SynchronizedArrayList) array.elements(T -> ObjectArrays.shuffle(T, size, random));
				else ObjectArrays.shuffle(array.elements(), size, random);
			}
			else {
				for(int i = list.size(); i-- != 0;) {
					int p = random.nextInt(i + 1);
					T t = list.get(i);
					list.set(i, list.get(p));
					list.set(p, t);
				}
			}
			return list;
		}
		for(int i = size; i-- != 0;) {
			int p = random.nextInt(i + 1);
			T t = list.get(i);
			list.set(i, list.get(p));
			list.set(p, t);
		}
		return list;
	}
	
	private static class SingletonList<T> extends AbstractObjectList<T>
	{
		T element;
		
		SingletonList(T element)
		{
			this.element = element;
		}
		@Override
		public void add(int index, T e) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, Collection<? extends T> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, ObjectList<T> c) { throw new UnsupportedOperationException(); }

		@Override
		public T get(int index) {
			if(index == 0) return element;
			throw new IndexOutOfBoundsException();
		}
		
		@Override
		public T set(int index, T e) { throw new UnsupportedOperationException(); }
		@Override
		public T remove(int index) { throw new UnsupportedOperationException(); }
		@Override
		public T swapRemove(int index) { throw new UnsupportedOperationException(); }
		@Override
		public void addElements(int from, T[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public T[] getElements(int from, T[] a, int offset, int length) {
			if(from != 0 || length != 1) throw new IndexOutOfBoundsException();
			a[offset] = element;
			return a;
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public <K> K[] extractElements(int from, int to, Class<K> type) { throw new UnsupportedOperationException(); }
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonList<T> copy() { return new SingletonList<>(element); }
	}
	
	private static class SynchronizedArrayList<T> extends SynchronizedList<T> implements IObjectArray<T>
	{
		IObjectArray<T> l;
		
		SynchronizedArrayList(ObjectList<T> l) {
			super(l);
			this.l = (IObjectArray<T>)l;
		}
		
		SynchronizedArrayList(ObjectList<T> l, Object mutex) {
			super(l, mutex);
			this.l = (IObjectArray<T>)l;
		}
		
		@Override
		public void ensureCapacity(int size) { synchronized(mutex) { l.ensureCapacity(size); } }
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return l.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { l.clearAndTrim(size); } }
		
		@Override
		public T[] elements() { synchronized(mutex) { return l.elements(); } }
		
		@Override
		public boolean isCastable() { synchronized(mutex) { return l.isCastable(); } }
		
		@Override
		public void elements(Consumer<T[]> action) { 
			Objects.requireNonNull(action);
			synchronized(mutex) {
				l.elements(action);
			}
		}
	}
	
	private static class SynchronizedRandomAccessList<T> extends SynchronizedList<T> implements RandomAccess 
	{
		SynchronizedRandomAccessList(ObjectList<T> l) {
			super(l);
		}
		
		SynchronizedRandomAccessList(ObjectList<T> l, Object mutex) {
			super(l, mutex);
		}
	}
	
	private static class SynchronizedList<T> extends ObjectCollections.SynchronizedCollection<T> implements ObjectList<T>
	{
		ObjectList<T> l;
		
		SynchronizedList(ObjectList<T> l) {
			super(l);
			this.l = l;
		}
		
		SynchronizedList(ObjectList<T> l, Object mutex) {
			super(l, mutex);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends T> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public void add(int index, T element) { synchronized(mutex) { l.add(index, element); } }
		
		@Override
		public boolean addAll(int index, ObjectCollection<T> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public boolean addAll(ObjectList<T> c) { synchronized(mutex) { return l.addAll(c); } }
		
		@Override
		public boolean addAll(int index, ObjectList<T> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public T get(int index) { synchronized(mutex) { return l.get(index); } }
		
		@Override
		public void forEach(Consumer<? super T> action) { synchronized(mutex) { l.forEach(action); } }
		
		@Override
		public T set(int index, T e) { synchronized(mutex) { return l.set(index, e); } }
		
		@Override
		public T remove(int index) { synchronized(mutex) { return l.remove(index); } }
		
		@Override
		public T swapRemove(int index) { synchronized(mutex) { return l.swapRemove(index); } }
		
		@Override
		public boolean swapRemove(T e) { synchronized(mutex) { return l.swapRemove(e); } }
		
		@Override
		public int indexOf(Object e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		public int lastIndexOf(Object e) { synchronized(mutex) { return l.lastIndexOf(e); } }
				
		@Override
		public void addElements(int from, T[] a, int offset, int length) { synchronized(mutex) { l.addElements(from, a, offset, length); } }
		
		@Override
		public T[] getElements(int from, T[] a, int offset, int length) { synchronized(mutex) { return l.getElements(from, a, offset, length); } }
		
		@Override
		public void removeElements(int from, int to) { synchronized(mutex) { l.removeElements(from, to); } }
		
		@Override
		public <K> K[] extractElements(int from, int to, Class<K> clz) { synchronized(mutex) { return l.extractElements(from, to, clz); } }
		
		@Override
		public ObjectListIterator<T> listIterator() {
			return l.listIterator();
		}
		
		@Override
		public ObjectListIterator<T> listIterator(int index) {
			return l.listIterator(index);
		}
		
		@Override
		public ObjectListIterator<T> indexedIterator(int...indecies) {
			return l.indexedIterator(indecies);
		}
		
		@Override
		public ObjectListIterator<T> indexedIterator(IntList indecies) {
			return l.indexedIterator(indecies);
		}
		
		@Override
		public ObjectList<T> subList(int from, int to) {
			return ObjectLists.synchronize(l.subList(from, to));
		}
		
		@Override
		public ObjectList<T> reversed() {
			return ObjectLists.synchronize(l.reversed());
		}
		
		@Override
		public void size(int size) { synchronized(mutex) { l.size(size); } }
		
		@Override
		public ObjectList<T> copy() { synchronized(mutex) { return l.copy(); } }
	}
	
	private static class UnmodifiableRandomList<T> extends UnmodifiableList<T> implements RandomAccess 
	{
		UnmodifiableRandomList(ObjectList<T> l) {
			super(l);
		}
	}
	
	private static class UnmodifiableList<T> extends ObjectCollections.UnmodifiableCollection<T> implements ObjectList<T>
	{
		final ObjectList<T> l;
		
		UnmodifiableList(ObjectList<T> l) {
			super(l);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends T> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, T element) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(ObjectList<T> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ObjectList<T> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public T get(int index) { return l.get(index); }
		
		@Override
		public void forEach(Consumer<? super T> action) { l.forEach(action); }
		
		@Override
		public T set(int index, T e) { throw new UnsupportedOperationException(); }
		
		@Override
		public T remove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public T swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemove(T e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int indexOf(Object e) { return l.indexOf(e); }
		
		@Override
		public int lastIndexOf(Object e) { return l.lastIndexOf(e); }
				
		@Override
		public void addElements(int from, T[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public T[] getElements(int from, T[] a, int offset, int length) {
			return l.getElements(from, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public <K> K[] extractElements(int from, int to, Class<K> clz) { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectListIterator<T> listIterator() {
			return ObjectIterators.unmodifiable(l.listIterator());
		}
		
		@Override
		public ObjectListIterator<T> listIterator(int index) {
			return ObjectIterators.unmodifiable(l.listIterator(index));
		}
		
		@Override
		public ObjectListIterator<T> indexedIterator(int...indecies) {
			return ObjectIterators.unmodifiable(l.indexedIterator(indecies));
		}
		
		@Override
		public ObjectListIterator<T> indexedIterator(IntList indecies) {
			return ObjectIterators.unmodifiable(l.indexedIterator(indecies));
		}
		
		@Override
		public ObjectList<T> subList(int from, int to) {
			return ObjectLists.unmodifiable(l.subList(from, to));
		}
		
		@Override
		public ObjectList<T> reversed() {
			return ObjectLists.unmodifiable(l.reversed());
		}
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectList<T> copy() { return l.copy(); }
	}
	
	private static class EmptyList<T> extends ObjectCollections.EmptyCollection<T> implements ObjectList<T>
	{
		@Override
		public boolean addAll(int index, Collection<? extends T> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, T element) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ObjectCollection<T> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(ObjectList<T> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ObjectList<T> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public T get(int index) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public T set(int index, T e) { throw new UnsupportedOperationException(); }
		
		@Override
		public T remove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public T swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemove(T e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int indexOf(Object e) { return -1; }
		
		@Override
		public int lastIndexOf(Object e) { return -1; }
				
		@Override
		public void addElements(int from, T[] a, int offset, int length){ throw new UnsupportedOperationException(); }
		
		@Override
		public T[] getElements(int from, T[] a, int offset, int length) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public <K> K[] extractElements(int from, int to, Class<K> clz) { throw new UnsupportedOperationException(); }

		@Override
		public ObjectListIterator<T> listIterator() {
			return ObjectIterators.empty();
		}
		
		@Override
		public ObjectListIterator<T> listIterator(int index) {
			if(index != 0)
				throw new IndexOutOfBoundsException();
			return ObjectIterators.empty();
		}
		
		@Override
		public ObjectListIterator<T> indexedIterator(int...indecies) {
			return ObjectIterators.empty(); 
		}
		
		@Override
		public ObjectListIterator<T> indexedIterator(IntList indecies) {
			return ObjectIterators.empty(); 
		}
		
		@Override
		public int hashCode() { return 1; }
		
		@Override
		public boolean equals(Object o) {
			if(o == this) return true;
			if(!(o instanceof List)) return false;
			return ((List<?>)o).isEmpty();
		}
		
		@Override
		public ObjectList<T> subList(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectList<T> reversed() { return this; }
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public EmptyList<T> copy() { return this; }
	}
}