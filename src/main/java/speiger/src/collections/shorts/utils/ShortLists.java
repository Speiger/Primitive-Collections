package speiger.src.collections.shorts.utils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.nio.ShortBuffer;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.shorts.lists.AbstractShortList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Lists
 */
public class ShortLists
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
	public static ShortList unmodifiable(ShortList l) {
		return l instanceof UnmodifiableList ? l : l instanceof RandomAccess ? new UnmodifiableRandomList(l) : new UnmodifiableList(l);
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IShortArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static ShortList synchronize(ShortList l) {
		return l instanceof SynchronizedList ? l : (l instanceof IShortArray ? new SynchronizedArrayList(l) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l)));
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IShortArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static ShortList synchronize(ShortList l, Object mutex) {
		return l instanceof SynchronizedList ? l : (l instanceof IShortArray ? new SynchronizedArrayList(l, mutex) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, mutex) : new SynchronizedList(l, mutex)));
	}
	
	/**
	 * Creates a Unmodifiable Singleton list
	 * @param element that should be used in the Singleton
	 * @return a singleton list that is unmodifiable
	 */
	public static ShortList singleton(short element) {
		return new SingletonList(element);
	}
	
	/**
	 * Reverses the list
	 * @param list that should be reversed.
	 * @return the input list
	 */
	public static ShortList reverse(ShortList list) {
		int size = list.size();
		if(list instanceof IShortArray) {
			IShortArray array = (IShortArray)list;
			if(list instanceof SynchronizedArrayList) array.elements(T -> ShortArrays.reverse(T, size));
			else ShortArrays.reverse(array.elements(), size);
			return list;
		}
		if(list instanceof RandomAccess) {
			for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
				short t = list.getShort(i);
				list.set(i, list.getShort(j));
				list.set(j, t);
			}
			return list;
		}
		ShortListIterator fwd = list.listIterator();
		ShortListIterator rev = list.listIterator(size);
		for(int i = 0, mid = size >> 1; i < mid; i++) {
			short tmp = fwd.nextShort();
			fwd.set(rev.previousShort());
			rev.set(tmp);
		}
		return list;
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @return the input list
	 */
	public static ShortList shuffle(ShortList list) {
		return shuffle(list, SanityChecks.getRandom());
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @param random the random that should be used
	 * @return the input list
	 */
	public static ShortList shuffle(ShortList list, Random random) {
		int size = list.size();
		if(list instanceof IShortArray) {
			IShortArray array = (IShortArray)list;
			if(list instanceof SynchronizedArrayList)  array.elements(T -> ShortArrays.shuffle(T, size, random));
			else ShortArrays.shuffle(array.elements(), size, random);
			return list;
		}
		for(int i = size; i-- != 0;) {
			int p = random.nextInt(i + 1);
			short t = list.getShort(i);
			list.set(i, list.getShort(p));
			list.set(p, t);
		}
		return list;
	}
	
	private static class SingletonList extends AbstractShortList
	{
		short element;
		
		SingletonList(short element)
		{
			this.element = element;
		}
		@Override
		public void add(int index, short e) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, Collection<? extends Short> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, ShortCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, ShortList c) { throw new UnsupportedOperationException(); }

		@Override
		public short getShort(int index) {
			if(index == 0) return element;
			throw new IndexOutOfBoundsException();
		}
		
		@Override
		public short set(int index, short e) { throw new UnsupportedOperationException(); }
		@Override
		public short removeShort(int index) { throw new UnsupportedOperationException(); }
		@Override
		public short swapRemove(int index) { throw new UnsupportedOperationException(); }
		@Override
		public void addElements(int from, short[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public short[] getElements(int from, short[] a, int offset, int length) {
			if(from != 0 || length != 1) throw new IndexOutOfBoundsException();
			a[offset] = element;
			return a;
		}
		@Override
		public void fillBuffer(ShortBuffer buffer) { buffer.put(element); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public short[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonList copy() { return new SingletonList(element); }
	}
	
	private static class SynchronizedArrayList extends SynchronizedList implements IShortArray
	{
		IShortArray l;
		
		SynchronizedArrayList(ShortList l) {
			super(l);
			this.l = (IShortArray)l;
		}
		
		SynchronizedArrayList(ShortList l, Object mutex) {
			super(l, mutex);
			this.l = (IShortArray)l;
		}
		
		@Override
		public void ensureCapacity(int size) { synchronized(mutex) { l.ensureCapacity(size); } }
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return l.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { l.clearAndTrim(size); } }
		
		@Override
		public short[] elements() { synchronized(mutex) { return l.elements(); } }
		
		@Override
		public void elements(Consumer<short[]> action) { 
			Objects.requireNonNull(action);
			synchronized(mutex) {
				l.elements(action);
			}
		}
	}
	
	private static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess 
	{
		SynchronizedRandomAccessList(ShortList l) {
			super(l);
		}
		
		SynchronizedRandomAccessList(ShortList l, Object mutex) {
			super(l, mutex);
		}
	}
	
	private static class SynchronizedList extends ShortCollections.SynchronizedCollection implements ShortList
	{
		ShortList l;
		
		SynchronizedList(ShortList l) {
			super(l);
			this.l = l;
		}
		
		SynchronizedList(ShortList l, Object mutex) {
			super(l, mutex);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Short> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public void add(int index, Short element) { synchronized(mutex) { l.add(index, element); } }
		
		@Override
		public void add(int index, short e) { synchronized(mutex) { l.add(index, e); } }
		
		@Override
		public boolean addAll(int index, ShortCollection c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public boolean addAll(ShortList c) { synchronized(mutex) { return l.addAll(c); } }
		
		@Override
		public boolean addAll(int index, ShortList c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public short getShort(int index) { synchronized(mutex) { return l.getShort(index); } }
		
		@Override
		public void forEach(ShortConsumer action) { synchronized(mutex) { l.forEach(action); } }
		
		@Override
		public short set(int index, short e) { synchronized(mutex) { return l.set(index, e); } }
		
		@Override
		public short removeShort(int index) { synchronized(mutex) { return l.removeShort(index); } }
		
		@Override
		public short swapRemove(int index) { synchronized(mutex) { return l.swapRemove(index); } }
		
		@Override
		public boolean swapRemoveShort(short e) { synchronized(mutex) { return l.swapRemoveShort(e); } }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { synchronized(mutex) { return l.lastIndexOf(e); } }
				
		@Override
		public int indexOf(short e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		public int lastIndexOf(short e) { synchronized(mutex) { return l.lastIndexOf(e); } }
		
		@Override
		public void addElements(int from, short[] a, int offset, int length) { synchronized(mutex) { l.addElements(from, a, offset, length); } }
		
		@Override
		public short[] getElements(int from, short[] a, int offset, int length) { synchronized(mutex) { return l.getElements(from, a, offset, length); } }
		
		@Override
		public void removeElements(int from, int to) { synchronized(mutex) { l.removeElements(from, to); } }
		
		@Override
		public short[] extractElements(int from, int to) { synchronized(mutex) { return l.extractElements(from, to); } }
		
		public void fillBuffer(ShortBuffer buffer) { synchronized(mutex) { l.fillBuffer(buffer); } }
		@Override
		public ShortListIterator listIterator() {
			return l.listIterator();
		}
		
		@Override
		public ShortListIterator listIterator(int index) {
			return l.listIterator(index);
		}
		
		@Override
		public ShortList subList(int from, int to) {
			return ShortLists.synchronize(l.subList(from, to));
		}
		
		@Override
		public ShortList reversed() {
			return ShortLists.synchronize(l.reversed());
		}
		
		@Override
		public void size(int size) { synchronized(mutex) { l.size(size); } }
		
		@Override
		public ShortList copy() { synchronized(mutex) { return l.copy(); } }
	}
	
	private static class UnmodifiableRandomList extends UnmodifiableList implements RandomAccess 
	{
		UnmodifiableRandomList(ShortList l) {
			super(l);
		}
	}
	
	private static class UnmodifiableList extends ShortCollections.UnmodifiableCollection implements ShortList
	{
		final ShortList l;
		
		UnmodifiableList(ShortList l) {
			super(l);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Short> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Short element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ShortCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(ShortList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ShortList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public short getShort(int index) { return l.getShort(index); }
		
		@Override
		public void forEach(ShortConsumer action) { l.forEach(action); }
		
		@Override
		public short set(int index, short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public short removeShort(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public short swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveShort(short e) { throw new UnsupportedOperationException(); }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { return l.indexOf(e); }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { return l.lastIndexOf(e); }
				
		@Override
		public int indexOf(short e) { return l.indexOf(e); }
		
		@Override
		public int lastIndexOf(short e) { return l.lastIndexOf(e); }
		
		@Override
		public void addElements(int from, short[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public short[] getElements(int from, short[] a, int offset, int length) {
			return l.getElements(from, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public short[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		public void fillBuffer(ShortBuffer buffer) { l.fillBuffer(buffer); }
		
		@Override
		public ShortListIterator listIterator() {
			return ShortIterators.unmodifiable(l.listIterator());
		}
		
		@Override
		public ShortListIterator listIterator(int index) {
			return ShortIterators.unmodifiable(l.listIterator(index));
		}
		
		@Override
		public ShortList subList(int from, int to) {
			return ShortLists.unmodifiable(l.subList(from, to));
		}
		
		@Override
		public ShortList reversed() {
			return ShortLists.unmodifiable(l.reversed());
		}
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortList copy() { return l.copy(); }
	}
	
	private static class EmptyList extends ShortCollections.EmptyCollection implements ShortList
	{
		@Override
		public boolean addAll(int index, Collection<? extends Short> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Short element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ShortCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(ShortList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ShortList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public short getShort(int index) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public short set(int index, short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public short removeShort(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public short swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveShort(short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int indexOf(Object e) { return -1; }
		
		@Override
		public int lastIndexOf(Object e) { return -1; }
				
		@Override
		public int indexOf(short e) { return -1; }
		
		@Override
		public int lastIndexOf(short e) { return -1; }
		
		@Override
		public void addElements(int from, short[] a, int offset, int length){ throw new UnsupportedOperationException(); }
		
		@Override
		public short[] getElements(int from, short[] a, int offset, int length) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public short[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortListIterator listIterator() {
			return ShortIterators.empty();
		}
		
		@Override
		public ShortListIterator listIterator(int index) {
			if(index != 0)
				throw new IndexOutOfBoundsException();
			return ShortIterators.empty();
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
		public ShortList subList(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortList reversed() { return this; }
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public EmptyList copy() { return this; }
	}
}