package speiger.src.collections.ints.utils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.nio.IntBuffer;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.lists.AbstractIntList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Lists
 */
public class IntLists
{
	/**
	 * Empty List reference
	 */
	private static final EmptyList EMPTY = new EmptyList();
	
	/**
	 * Returns a Immutable EmptyList instance that is automatically casted.
	 * @return an empty list
	 */
	public static EmptyList empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable List instance based on the instance given.
	 * @param l that should be made immutable/unmodifiable
	 * @return a unmodifiable list wrapper. If the list is implementing RandomAccess that is also transferred. If the List already a unmodifiable wrapper then it just returns itself.
	 */
	public static IntList unmodifiable(IntList l) {
		return l instanceof UnmodifiableList ? l : l instanceof RandomAccess ? new UnmodifiableRandomList(l) : new UnmodifiableList(l);
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IIntArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static IntList synchronize(IntList l) {
		return l instanceof SynchronizedList ? l : (l instanceof IIntArray ? new SynchronizedArrayList(l) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l)));
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IIntArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static IntList synchronize(IntList l, Object mutex) {
		return l instanceof SynchronizedList ? l : (l instanceof IIntArray ? new SynchronizedArrayList(l, mutex) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, mutex) : new SynchronizedList(l, mutex)));
	}
	
	/**
	 * Creates a Unmodifiable Singleton list
	 * @param element that should be used in the Singleton
	 * @return a singleton list that is unmodifiable
	 */
	public static IntList singleton(int element) {
		return new SingletonList(element);
	}
	
	/**
	 * Reverses the list
	 * @param list that should be reversed.
	 * @return the input list
	 */
	public static IntList reverse(IntList list) {
		int size = list.size();
		if(list instanceof IIntArray) {
			IIntArray array = (IIntArray)list;
			if(list instanceof SynchronizedArrayList) array.elements(T -> IntArrays.reverse(T, size));
			else IntArrays.reverse(array.elements(), size);
			return list;
		}
		if(list instanceof RandomAccess) {
			for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
				int t = list.getInt(i);
				list.set(i, list.getInt(j));
				list.set(j, t);
			}
			return list;
		}
		IntListIterator fwd = list.listIterator();
		IntListIterator rev = list.listIterator(size);
		for(int i = 0, mid = size >> 1; i < mid; i++) {
			int tmp = fwd.nextInt();
			fwd.set(rev.previousInt());
			rev.set(tmp);
		}
		return list;
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @return the input list
	 */
	public static IntList shuffle(IntList list) {
		return shuffle(list, SanityChecks.getRandom());
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @param random the random that should be used
	 * @return the input list
	 */
	public static IntList shuffle(IntList list, Random random) {
		int size = list.size();
		if(list instanceof IIntArray) {
			IIntArray array = (IIntArray)list;
			if(list instanceof SynchronizedArrayList)  array.elements(T -> IntArrays.shuffle(T, size, random));
			else IntArrays.shuffle(array.elements(), size, random);
			return list;
		}
		for(int i = size; i-- != 0;) {
			int p = random.nextInt(i + 1);
			int t = list.getInt(i);
			list.set(i, list.getInt(p));
			list.set(p, t);
		}
		return list;
	}
	
	private static class SingletonList extends AbstractIntList
	{
		int element;
		
		SingletonList(int element)
		{
			this.element = element;
		}
		@Override
		public void add(int index, int e) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, Collection<? extends Integer> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, IntCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, IntList c) { throw new UnsupportedOperationException(); }

		@Override
		public int getInt(int index) {
			if(index == 0) return element;
			throw new IndexOutOfBoundsException();
		}
		
		@Override
		public int set(int index, int e) { throw new UnsupportedOperationException(); }
		@Override
		public int removeInt(int index) { throw new UnsupportedOperationException(); }
		@Override
		public int swapRemove(int index) { throw new UnsupportedOperationException(); }
		@Override
		public void addElements(int from, int[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public int[] getElements(int from, int[] a, int offset, int length) {
			if(from != 0 || length != 1) throw new IndexOutOfBoundsException();
			a[offset] = element;
			return a;
		}
		@Override
		public void fillBuffer(IntBuffer buffer) { buffer.put(element); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public int[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonList copy() { return new SingletonList(element); }
	}
	
	private static class SynchronizedArrayList extends SynchronizedList implements IIntArray
	{
		IIntArray l;
		
		SynchronizedArrayList(IntList l) {
			super(l);
			this.l = (IIntArray)l;
		}
		
		SynchronizedArrayList(IntList l, Object mutex) {
			super(l, mutex);
			this.l = (IIntArray)l;
		}
		
		@Override
		public void ensureCapacity(int size) { synchronized(mutex) { l.ensureCapacity(size); } }
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return l.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { l.clearAndTrim(size); } }
		
		@Override
		public int[] elements() { synchronized(mutex) { return l.elements(); } }
		
		@Override
		public void elements(Consumer<int[]> action) { 
			Objects.requireNonNull(action);
			synchronized(mutex) {
				l.elements(action);
			}
		}
	}
	
	private static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess 
	{
		SynchronizedRandomAccessList(IntList l) {
			super(l);
		}
		
		SynchronizedRandomAccessList(IntList l, Object mutex) {
			super(l, mutex);
		}
	}
	
	private static class SynchronizedList extends IntCollections.SynchronizedCollection implements IntList
	{
		IntList l;
		
		SynchronizedList(IntList l) {
			super(l);
			this.l = l;
		}
		
		SynchronizedList(IntList l, Object mutex) {
			super(l, mutex);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Integer> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public void add(int index, Integer element) { synchronized(mutex) { l.add(index, element); } }
		
		@Override
		public void add(int index, int e) { synchronized(mutex) { l.add(index, e); } }
		
		@Override
		public boolean addAll(int index, IntCollection c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public boolean addAll(IntList c) { synchronized(mutex) { return l.addAll(c); } }
		
		@Override
		public boolean addAll(int index, IntList c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public int getInt(int index) { synchronized(mutex) { return l.getInt(index); } }
		
		@Override
		public void forEach(IntConsumer action) { synchronized(mutex) { l.forEach(action); } }
		
		@Override
		public int set(int index, int e) { synchronized(mutex) { return l.set(index, e); } }
		
		@Override
		public int removeInt(int index) { synchronized(mutex) { return l.removeInt(index); } }
		
		@Override
		public int swapRemove(int index) { synchronized(mutex) { return l.swapRemove(index); } }
		
		@Override
		public boolean swapRemoveInt(int e) { synchronized(mutex) { return l.swapRemoveInt(e); } }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { synchronized(mutex) { return l.lastIndexOf(e); } }
				
		@Override
		public int indexOf(int e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		public int lastIndexOf(int e) { synchronized(mutex) { return l.lastIndexOf(e); } }
		
		@Override
		public void addElements(int from, int[] a, int offset, int length) { synchronized(mutex) { l.addElements(from, a, offset, length); } }
		
		@Override
		public int[] getElements(int from, int[] a, int offset, int length) { synchronized(mutex) { return l.getElements(from, a, offset, length); } }
		
		@Override
		public void removeElements(int from, int to) { synchronized(mutex) { l.removeElements(from, to); } }
		
		@Override
		public int[] extractElements(int from, int to) { synchronized(mutex) { return l.extractElements(from, to); } }
		
		public void fillBuffer(IntBuffer buffer) { synchronized(mutex) { l.fillBuffer(buffer); } }
		@Override
		public IntListIterator listIterator() {
			return l.listIterator();
		}
		
		@Override
		public IntListIterator listIterator(int index) {
			return l.listIterator(index);
		}
		
		@Override
		public IntList subList(int from, int to) {
			return IntLists.synchronize(l.subList(from, to));
		}
		
		@Override
		public IntList reversed() {
			return IntLists.synchronize(l.reversed());
		}
		
		@Override
		public void size(int size) { synchronized(mutex) { l.size(size); } }
		
		@Override
		public IntList copy() { synchronized(mutex) { return l.copy(); } }
	}
	
	private static class UnmodifiableRandomList extends UnmodifiableList implements RandomAccess 
	{
		UnmodifiableRandomList(IntList l) {
			super(l);
		}
	}
	
	private static class UnmodifiableList extends IntCollections.UnmodifiableCollection implements IntList
	{
		final IntList l;
		
		UnmodifiableList(IntList l) {
			super(l);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Integer> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Integer element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, IntCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(IntList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, IntList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public int getInt(int index) { return l.getInt(index); }
		
		@Override
		public void forEach(IntConsumer action) { l.forEach(action); }
		
		@Override
		public int set(int index, int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int removeInt(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public int swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveInt(int e) { throw new UnsupportedOperationException(); }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { return l.indexOf(e); }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { return l.lastIndexOf(e); }
				
		@Override
		public int indexOf(int e) { return l.indexOf(e); }
		
		@Override
		public int lastIndexOf(int e) { return l.lastIndexOf(e); }
		
		@Override
		public void addElements(int from, int[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public int[] getElements(int from, int[] a, int offset, int length) {
			return l.getElements(from, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public int[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		public void fillBuffer(IntBuffer buffer) { l.fillBuffer(buffer); }
		
		@Override
		public IntListIterator listIterator() {
			return IntIterators.unmodifiable(l.listIterator());
		}
		
		@Override
		public IntListIterator listIterator(int index) {
			return IntIterators.unmodifiable(l.listIterator(index));
		}
		
		@Override
		public IntList subList(int from, int to) {
			return IntLists.unmodifiable(l.subList(from, to));
		}
		
		@Override
		public IntList reversed() {
			return IntLists.unmodifiable(l.reversed());
		}
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public IntList copy() { return l.copy(); }
	}
	
	private static class EmptyList extends IntCollections.EmptyCollection implements IntList
	{
		@Override
		public boolean addAll(int index, Collection<? extends Integer> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Integer element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, IntCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(IntList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, IntList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public int getInt(int index) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public int set(int index, int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int removeInt(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public int swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveInt(int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int indexOf(Object e) { return -1; }
		
		@Override
		public int lastIndexOf(Object e) { return -1; }
				
		@Override
		public int indexOf(int e) { return -1; }
		
		@Override
		public int lastIndexOf(int e) { return -1; }
		
		@Override
		public void addElements(int from, int[] a, int offset, int length){ throw new UnsupportedOperationException(); }
		
		@Override
		public int[] getElements(int from, int[] a, int offset, int length) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public int[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public IntListIterator listIterator() {
			return IntIterators.empty();
		}
		
		@Override
		public IntListIterator listIterator(int index) {
			if(index != 0)
				throw new IndexOutOfBoundsException();
			return IntIterators.empty();
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
		public IntList subList(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public IntList reversed() { return this; }
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public EmptyList copy() { return this; }
	}
}