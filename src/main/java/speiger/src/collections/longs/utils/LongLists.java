package speiger.src.collections.longs.utils;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.nio.LongBuffer;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.longs.lists.AbstractLongList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.lists.LongListIterator;
import speiger.src.collections.longs.utils.LongArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Lists
 */
public class LongLists
{
	/**
	 * Empty List reference
	 */
	public static final EmptyList EMPTY = new EmptyList();
	
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
	public static LongList unmodifiable(LongList l) {
		return l instanceof UnmodifiableList ? l : l instanceof RandomAccess ? new UnmodifiableRandomList(l) : new UnmodifiableList(l);
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or ILongArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static LongList synchronize(LongList l) {
		return l instanceof SynchronizedList ? l : (l instanceof ILongArray ? new SynchronizedArrayList(l) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l)));
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or ILongArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static LongList synchronize(LongList l, Object mutex) {
		return l instanceof SynchronizedList ? l : (l instanceof ILongArray ? new SynchronizedArrayList(l, mutex) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, mutex) : new SynchronizedList(l, mutex)));
	}
	
	/**
	 * Creates a Unmodifiable Singleton list
	 * @param element that should be used in the Singleton
	 * @return a singleton list that is unmodifiable
	 */
	public static LongList singleton(long element) {
		return new SingletonList(element);
	}
	
	/**
	 * Reverses the list
	 * @param list that should be reversed.
	 * @return the input list
	 */
	public static LongList reverse(LongList list) {
		int size = list.size();
		if(list instanceof ILongArray) {
			ILongArray array = (ILongArray)list;
			if(list instanceof SynchronizedArrayList) array.elements(T -> LongArrays.reverse(T, size));
			else LongArrays.reverse(array.elements(), size);
			return list;
		}
		if(list instanceof RandomAccess) {
			for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
				long t = list.getLong(i);
				list.set(i, list.getLong(j));
				list.set(j, t);
			}
			return list;
		}
		LongListIterator fwd = list.listIterator();
		LongListIterator rev = list.listIterator(size);
		for(int i = 0, mid = size >> 1; i < mid; i++) {
			long tmp = fwd.nextLong();
			fwd.set(rev.previousLong());
			rev.set(tmp);
		}
		return list;
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @return the input list
	 */
	public static LongList shuffle(LongList list) {
		return shuffle(list, SanityChecks.getRandom());
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @param random the random that should be used
	 * @return the input list
	 */
	public static LongList shuffle(LongList list, Random random) {
		int size = list.size();
		if(list instanceof ILongArray) {
			ILongArray array = (ILongArray)list;
			if(list instanceof SynchronizedArrayList)  array.elements(T -> LongArrays.shuffle(T, size, random));
			else LongArrays.shuffle(array.elements(), size, random);
			return list;
		}
		for(int i = list.size(); i-- != 0;) {
			int p = random.nextInt(i + 1);
			long t = list.getLong(i);
			list.set(i, list.getLong(p));
			list.set(p, t);
		}
		return list;
	}
	
	private static class SingletonList extends AbstractLongList
	{
		long element;
		
		SingletonList(long element)
		{
			this.element = element;
		}
		@Override
		public void add(int index, long e) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, Collection<? extends Long> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, LongCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, LongList c) { throw new UnsupportedOperationException(); }

		@Override
		public long getLong(int index) {
			if(index == 0) return element;
			throw new IndexOutOfBoundsException();
		}
		
		@Override
		public long set(int index, long e) { throw new UnsupportedOperationException(); }
		@Override
		public long removeLong(int index) { throw new UnsupportedOperationException(); }
		@Override
		public long swapRemove(int index) { throw new UnsupportedOperationException(); }
		@Override
		public void addElements(int from, long[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public long[] getElements(int from, long[] a, int offset, int length) {
			if(from != 0 || length != 1) throw new IndexOutOfBoundsException();
			a[offset] = element;
			return a;
		}
		@Override
		public void fillBuffer(LongBuffer buffer) { buffer.put(element); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public long[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonList copy() { return new SingletonList(element); }
	}
	
	private static class SynchronizedArrayList extends SynchronizedList implements ILongArray
	{
		ILongArray l;
		
		SynchronizedArrayList(LongList l) {
			super(l);
			this.l = (ILongArray)l;
		}
		
		SynchronizedArrayList(LongList l, Object mutex) {
			super(l, mutex);
			this.l = (ILongArray)l;
		}
		
		@Override
		public void ensureCapacity(int size) { synchronized(mutex) { l.ensureCapacity(size); } }
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return l.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { l.clearAndTrim(size); } }
		
		@Override
		public long[] elements() { synchronized(mutex) { return l.elements(); } }
		
		@Override
		public void elements(Consumer<long[]> action) { 
			Objects.requireNonNull(action);
			synchronized(mutex) {
				l.elements(action);
			}
		}
	}
	
	private static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess 
	{
		SynchronizedRandomAccessList(LongList l) {
			super(l);
		}
		
		SynchronizedRandomAccessList(LongList l, Object mutex) {
			super(l, mutex);
		}
	}
	
	private static class SynchronizedList extends LongCollections.SynchronizedCollection implements LongList
	{
		LongList l;
		
		SynchronizedList(LongList l) {
			super(l);
			this.l = l;
		}
		
		SynchronizedList(LongList l, Object mutex) {
			super(l, mutex);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Long> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public void add(int index, Long element) { synchronized(mutex) { l.add(index, element); } }
		
		@Override
		public void add(int index, long e) { synchronized(mutex) { l.add(index, e); } }
		
		@Override
		public boolean addAll(int index, LongCollection c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public boolean addAll(LongList c) { synchronized(mutex) { return l.addAll(c); } }
		
		@Override
		public boolean addAll(int index, LongList c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public long getLong(int index) { synchronized(mutex) { return l.getLong(index); } }
		
		@Override
		public void forEach(LongConsumer action) { synchronized(mutex) { l.forEach(action); } }
		
		@Override
		public long set(int index, long e) { synchronized(mutex) { return l.set(index, e); } }
		
		@Override
		public long removeLong(int index) { synchronized(mutex) { return l.removeLong(index); } }
		
		@Override
		public long swapRemove(int index) { synchronized(mutex) { return l.swapRemove(index); } }
		
		@Override
		public boolean swapRemoveLong(long e) { synchronized(mutex) { return l.swapRemoveLong(e); } }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { synchronized(mutex) { return l.lastIndexOf(e); } }
				
		@Override
		public int indexOf(long e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		public int lastIndexOf(long e) { synchronized(mutex) { return l.lastIndexOf(e); } }
		
		@Override
		public void addElements(int from, long[] a, int offset, int length) { synchronized(mutex) { addElements(from, a, offset, length); } }
		
		@Override
		public long[] getElements(int from, long[] a, int offset, int length) { synchronized(mutex) { return l.getElements(from, a, offset, length); } }
		
		@Override
		public void removeElements(int from, int to) { synchronized(mutex) { l.removeElements(from, to); } }
		
		@Override
		public long[] extractElements(int from, int to) { synchronized(mutex) { return l.extractElements(from, to); } }
		
		public void fillBuffer(LongBuffer buffer) { synchronized(mutex) { l.fillBuffer(buffer); } }
		@Override
		public LongListIterator listIterator() {
			return l.listIterator();
		}
		
		@Override
		public LongListIterator listIterator(int index) {
			return l.listIterator(index);
		}
		
		@Override
		public LongList subList(int from, int to) {
			return LongLists.synchronize(l.subList(from, to));
		}
		
		@Override
		public void size(int size) { synchronized(mutex) { l.size(size); } }
		
		@Override
		public LongList copy() { synchronized(mutex) { return l.copy(); } }
	}
	
	private static class UnmodifiableRandomList extends UnmodifiableList implements RandomAccess 
	{
		UnmodifiableRandomList(LongList l) {
			super(l);
		}
	}
	
	private static class UnmodifiableList extends LongCollections.UnmodifiableCollection implements LongList
	{
		final LongList l;
		
		UnmodifiableList(LongList l) {
			super(l);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Long> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Long element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, long e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, LongCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(LongList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, LongList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public long getLong(int index) { return l.getLong(index); }
		
		@Override
		public void forEach(LongConsumer action) { l.forEach(action); }
		
		@Override
		public long set(int index, long e) { throw new UnsupportedOperationException(); }
		
		@Override
		public long removeLong(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public long swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveLong(long e) { throw new UnsupportedOperationException(); }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { return l.indexOf(e); }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { return l.lastIndexOf(e); }
				
		@Override
		public int indexOf(long e) { return l.indexOf(e); }
		
		@Override
		public int lastIndexOf(long e) { return l.lastIndexOf(e); }
		
		@Override
		public void addElements(int from, long[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public long[] getElements(int from, long[] a, int offset, int length) {
			return l.getElements(from, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public long[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		public void fillBuffer(LongBuffer buffer) { l.fillBuffer(buffer); }
		
		@Override
		public LongListIterator listIterator() {
			return LongIterators.unmodifiable(l.listIterator());
		}
		
		@Override
		public LongListIterator listIterator(int index) {
			return LongIterators.unmodifiable(l.listIterator(index));
		}
		
		@Override
		public LongList subList(int from, int to) {
			return LongLists.unmodifiable(l.subList(from, to));
		}
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public LongList copy() { return l.copy(); }
	}
	
	private static class EmptyList extends LongCollections.EmptyCollection implements LongList
	{
		@Override
		public boolean addAll(int index, Collection<? extends Long> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Long element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, long e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, LongCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(LongList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, LongList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public long getLong(int index) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public long set(int index, long e) { throw new UnsupportedOperationException(); }
		
		@Override
		public long removeLong(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public long swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveLong(long e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int indexOf(Object e) { return -1; }
		
		@Override
		public int lastIndexOf(Object e) { return -1; }
				
		@Override
		public int indexOf(long e) { return -1; }
		
		@Override
		public int lastIndexOf(long e) { return -1; }
		
		@Override
		public void addElements(int from, long[] a, int offset, int length){ throw new UnsupportedOperationException(); }
		
		@Override
		public long[] getElements(int from, long[] a, int offset, int length) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public long[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public LongListIterator listIterator() {
			return LongIterators.empty();
		}
		
		@Override
		public LongListIterator listIterator(int index) {
			if(index != 0)
				throw new IndexOutOfBoundsException();
			return LongIterators.empty();
		}
		
		@Override
		public LongList subList(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public EmptyList copy() { return this; }
	}
}