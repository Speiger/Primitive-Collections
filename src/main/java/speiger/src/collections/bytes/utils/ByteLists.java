package speiger.src.collections.bytes.utils;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.nio.ByteBuffer;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.lists.AbstractByteList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.lists.ByteListIterator;
import speiger.src.collections.bytes.utils.ByteArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Lists
 */
public class ByteLists
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
	public static ByteList unmodifiable(ByteList l) {
		return l instanceof UnmodifiableList ? l : l instanceof RandomAccess ? new UnmodifiableRandomList(l) : new UnmodifiableList(l);
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IByteArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static ByteList synchronize(ByteList l) {
		return l instanceof SynchronizedList ? l : (l instanceof IByteArray ? new SynchronizedArrayList(l) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l)));
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IByteArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static ByteList synchronize(ByteList l, Object mutex) {
		return l instanceof SynchronizedList ? l : (l instanceof IByteArray ? new SynchronizedArrayList(l, mutex) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, mutex) : new SynchronizedList(l, mutex)));
	}
	
	/**
	 * Creates a Unmodifiable Singleton list
	 * @param element that should be used in the Singleton
	 * @return a singleton list that is unmodifiable
	 */
	public static ByteList singleton(byte element) {
		return new SingletonList(element);
	}
	
	/**
	 * Reverses the list
	 * @param list that should be reversed.
	 * @return the input list
	 */
	public static ByteList reverse(ByteList list) {
		int size = list.size();
		if(list instanceof IByteArray) {
			IByteArray array = (IByteArray)list;
			if(list instanceof SynchronizedArrayList) array.elements(T -> ByteArrays.reverse(T, size));
			else ByteArrays.reverse(array.elements(), size);
			return list;
		}
		if(list instanceof RandomAccess) {
			for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
				byte t = list.getByte(i);
				list.set(i, list.getByte(j));
				list.set(j, t);
			}
			return list;
		}
		ByteListIterator fwd = list.listIterator();
		ByteListIterator rev = list.listIterator(size);
		for(int i = 0, mid = size >> 1; i < mid; i++) {
			byte tmp = fwd.nextByte();
			fwd.set(rev.previousByte());
			rev.set(tmp);
		}
		return list;
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @return the input list
	 */
	public static ByteList shuffle(ByteList list) {
		return shuffle(list, SanityChecks.getRandom());
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @param random the random that should be used
	 * @return the input list
	 */
	public static ByteList shuffle(ByteList list, Random random) {
		int size = list.size();
		if(list instanceof IByteArray) {
			IByteArray array = (IByteArray)list;
			if(list instanceof SynchronizedArrayList)  array.elements(T -> ByteArrays.shuffle(T, size, random));
			else ByteArrays.shuffle(array.elements(), size, random);
			return list;
		}
		for(int i = list.size(); i-- != 0;) {
			int p = random.nextInt(i + 1);
			byte t = list.getByte(i);
			list.set(i, list.getByte(p));
			list.set(p, t);
		}
		return list;
	}
	
	private static class SingletonList extends AbstractByteList
	{
		byte element;
		
		SingletonList(byte element)
		{
			this.element = element;
		}
		@Override
		public void add(int index, byte e) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, Collection<? extends Byte> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, ByteCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, ByteList c) { throw new UnsupportedOperationException(); }

		@Override
		public byte getByte(int index) {
			if(index == 0) return element;
			throw new IndexOutOfBoundsException();
		}
		
		@Override
		public byte set(int index, byte e) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeByte(int index) { throw new UnsupportedOperationException(); }
		@Override
		public byte swapRemove(int index) { throw new UnsupportedOperationException(); }
		@Override
		public void addElements(int from, byte[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public byte[] getElements(int from, byte[] a, int offset, int length) {
			if(from != 0 || length != 1) throw new IndexOutOfBoundsException();
			a[offset] = element;
			return a;
		}
		@Override
		public void fillBuffer(ByteBuffer buffer) { buffer.put(element); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public byte[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonList copy() { return new SingletonList(element); }
	}
	
	private static class SynchronizedArrayList extends SynchronizedList implements IByteArray
	{
		IByteArray l;
		
		SynchronizedArrayList(ByteList l) {
			super(l);
			this.l = (IByteArray)l;
		}
		
		SynchronizedArrayList(ByteList l, Object mutex) {
			super(l, mutex);
			this.l = (IByteArray)l;
		}
		
		@Override
		public void ensureCapacity(int size) { synchronized(mutex) { l.ensureCapacity(size); } }
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return l.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { l.clearAndTrim(size); } }
		
		@Override
		public byte[] elements() { synchronized(mutex) { return l.elements(); } }
		
		@Override
		public void elements(Consumer<byte[]> action) { 
			Objects.requireNonNull(action);
			synchronized(mutex) {
				l.elements(action);
			}
		}
	}
	
	private static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess 
	{
		SynchronizedRandomAccessList(ByteList l) {
			super(l);
		}
		
		SynchronizedRandomAccessList(ByteList l, Object mutex) {
			super(l, mutex);
		}
	}
	
	private static class SynchronizedList extends ByteCollections.SynchronizedCollection implements ByteList
	{
		ByteList l;
		
		SynchronizedList(ByteList l) {
			super(l);
			this.l = l;
		}
		
		SynchronizedList(ByteList l, Object mutex) {
			super(l, mutex);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Byte> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public void add(int index, Byte element) { synchronized(mutex) { l.add(index, element); } }
		
		@Override
		public void add(int index, byte e) { synchronized(mutex) { l.add(index, e); } }
		
		@Override
		public boolean addAll(int index, ByteCollection c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public boolean addAll(ByteList c) { synchronized(mutex) { return l.addAll(c); } }
		
		@Override
		public boolean addAll(int index, ByteList c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public byte getByte(int index) { synchronized(mutex) { return l.getByte(index); } }
		
		@Override
		public void forEach(ByteConsumer action) { synchronized(mutex) { l.forEach(action); } }
		
		@Override
		public byte set(int index, byte e) { synchronized(mutex) { return l.set(index, e); } }
		
		@Override
		public byte removeByte(int index) { synchronized(mutex) { return l.removeByte(index); } }
		
		@Override
		public byte swapRemove(int index) { synchronized(mutex) { return l.swapRemove(index); } }
		
		@Override
		public boolean swapRemoveByte(byte e) { synchronized(mutex) { return l.swapRemoveByte(e); } }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { synchronized(mutex) { return l.lastIndexOf(e); } }
				
		@Override
		public int indexOf(byte e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		public int lastIndexOf(byte e) { synchronized(mutex) { return l.lastIndexOf(e); } }
		
		@Override
		public void addElements(int from, byte[] a, int offset, int length) { synchronized(mutex) { addElements(from, a, offset, length); } }
		
		@Override
		public byte[] getElements(int from, byte[] a, int offset, int length) { synchronized(mutex) { return l.getElements(from, a, offset, length); } }
		
		@Override
		public void removeElements(int from, int to) { synchronized(mutex) { l.removeElements(from, to); } }
		
		@Override
		public byte[] extractElements(int from, int to) { synchronized(mutex) { return l.extractElements(from, to); } }
		
		public void fillBuffer(ByteBuffer buffer) { synchronized(mutex) { l.fillBuffer(buffer); } }
		@Override
		public ByteListIterator listIterator() {
			return l.listIterator();
		}
		
		@Override
		public ByteListIterator listIterator(int index) {
			return l.listIterator(index);
		}
		
		@Override
		public ByteList subList(int from, int to) {
			return ByteLists.synchronize(l.subList(from, to));
		}
		
		@Override
		public void size(int size) { synchronized(mutex) { l.size(size); } }
		
		@Override
		public ByteList copy() { synchronized(mutex) { return l.copy(); } }
	}
	
	private static class UnmodifiableRandomList extends UnmodifiableList implements RandomAccess 
	{
		UnmodifiableRandomList(ByteList l) {
			super(l);
		}
	}
	
	private static class UnmodifiableList extends ByteCollections.UnmodifiableCollection implements ByteList
	{
		final ByteList l;
		
		UnmodifiableList(ByteList l) {
			super(l);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Byte> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Byte element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ByteCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(ByteList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ByteList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte getByte(int index) { return l.getByte(index); }
		
		@Override
		public void forEach(ByteConsumer action) { l.forEach(action); }
		
		@Override
		public byte set(int index, byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte removeByte(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveByte(byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { return l.indexOf(e); }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { return l.lastIndexOf(e); }
				
		@Override
		public int indexOf(byte e) { return l.indexOf(e); }
		
		@Override
		public int lastIndexOf(byte e) { return l.lastIndexOf(e); }
		
		@Override
		public void addElements(int from, byte[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte[] getElements(int from, byte[] a, int offset, int length) {
			return l.getElements(from, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		public void fillBuffer(ByteBuffer buffer) { l.fillBuffer(buffer); }
		
		@Override
		public ByteListIterator listIterator() {
			return ByteIterators.unmodifiable(l.listIterator());
		}
		
		@Override
		public ByteListIterator listIterator(int index) {
			return ByteIterators.unmodifiable(l.listIterator(index));
		}
		
		@Override
		public ByteList subList(int from, int to) {
			return ByteLists.unmodifiable(l.subList(from, to));
		}
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteList copy() { return l.copy(); }
	}
	
	private static class EmptyList extends ByteCollections.EmptyCollection implements ByteList
	{
		@Override
		public boolean addAll(int index, Collection<? extends Byte> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Byte element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ByteCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(ByteList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, ByteList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte getByte(int index) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public byte set(int index, byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte removeByte(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveByte(byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int indexOf(Object e) { return -1; }
		
		@Override
		public int lastIndexOf(Object e) { return -1; }
				
		@Override
		public int indexOf(byte e) { return -1; }
		
		@Override
		public int lastIndexOf(byte e) { return -1; }
		
		@Override
		public void addElements(int from, byte[] a, int offset, int length){ throw new UnsupportedOperationException(); }
		
		@Override
		public byte[] getElements(int from, byte[] a, int offset, int length) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public byte[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteListIterator listIterator() {
			return ByteIterators.empty();
		}
		
		@Override
		public ByteListIterator listIterator(int index) {
			if(index != 0)
				throw new IndexOutOfBoundsException();
			return ByteIterators.empty();
		}
		
		@Override
		public ByteList subList(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public EmptyList copy() { return this; }
	}
}