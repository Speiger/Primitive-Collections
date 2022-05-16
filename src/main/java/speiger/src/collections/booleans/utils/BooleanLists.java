package speiger.src.collections.booleans.utils;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.lists.AbstractBooleanList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.lists.BooleanListIterator;
import speiger.src.collections.booleans.utils.BooleanArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Lists
 */
public class BooleanLists
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
	public static BooleanList unmodifiable(BooleanList l) {
		return l instanceof UnmodifiableList ? l : l instanceof RandomAccess ? new UnmodifiableRandomList(l) : new UnmodifiableList(l);
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IBooleanArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static BooleanList synchronize(BooleanList l) {
		return l instanceof SynchronizedList ? l : (l instanceof IBooleanArray ? new SynchronizedArrayList(l) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l)));
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IBooleanArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static BooleanList synchronize(BooleanList l, Object mutex) {
		return l instanceof SynchronizedList ? l : (l instanceof IBooleanArray ? new SynchronizedArrayList(l, mutex) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, mutex) : new SynchronizedList(l, mutex)));
	}
	
	/**
	 * Creates a Unmodifiable Singleton list
	 * @param element that should be used in the Singleton
	 * @return a singleton list that is unmodifiable
	 */
	public static BooleanList singleton(boolean element) {
		return new SingletonList(element);
	}
	
	/**
	 * Reverses the list
	 * @param list that should be reversed.
	 * @return the input list
	 */
	public static BooleanList reverse(BooleanList list) {
		int size = list.size();
		if(list instanceof IBooleanArray) {
			IBooleanArray array = (IBooleanArray)list;
			if(list instanceof SynchronizedArrayList) array.elements(T -> BooleanArrays.reverse(T, size));
			else BooleanArrays.reverse(array.elements(), size);
			return list;
		}
		if(list instanceof RandomAccess) {
			for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
				boolean t = list.getBoolean(i);
				list.set(i, list.getBoolean(j));
				list.set(j, t);
			}
			return list;
		}
		BooleanListIterator fwd = list.listIterator();
		BooleanListIterator rev = list.listIterator(size);
		for(int i = 0, mid = size >> 1; i < mid; i++) {
			boolean tmp = fwd.nextBoolean();
			fwd.set(rev.previousBoolean());
			rev.set(tmp);
		}
		return list;
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @return the input list
	 */
	public static BooleanList shuffle(BooleanList list) {
		return shuffle(list, SanityChecks.getRandom());
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @param random the random that should be used
	 * @return the input list
	 */
	public static BooleanList shuffle(BooleanList list, Random random) {
		int size = list.size();
		if(list instanceof IBooleanArray) {
			IBooleanArray array = (IBooleanArray)list;
			if(list instanceof SynchronizedArrayList)  array.elements(T -> BooleanArrays.shuffle(T, size, random));
			else BooleanArrays.shuffle(array.elements(), size, random);
			return list;
		}
		for(int i = list.size(); i-- != 0;) {
			int p = random.nextInt(i + 1);
			boolean t = list.getBoolean(i);
			list.set(i, list.getBoolean(p));
			list.set(p, t);
		}
		return list;
	}
	
	private static class SingletonList extends AbstractBooleanList
	{
		boolean element;
		
		SingletonList(boolean element)
		{
			this.element = element;
		}
		@Override
		public void add(int index, boolean e) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, Collection<? extends Boolean> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, BooleanCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, BooleanList c) { throw new UnsupportedOperationException(); }

		@Override
		public boolean getBoolean(int index) {
			if(index == 0) return element;
			throw new IndexOutOfBoundsException();
		}
		
		@Override
		public boolean set(int index, boolean e) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeBoolean(int index) { throw new UnsupportedOperationException(); }
		@Override
		public boolean swapRemove(int index) { throw new UnsupportedOperationException(); }
		@Override
		public void addElements(int from, boolean[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean[] getElements(int from, boolean[] a, int offset, int length) {
			if(from != 0 || length != 1) throw new IndexOutOfBoundsException();
			a[offset] = element;
			return a;
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public boolean[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonList copy() { return new SingletonList(element); }
	}
	
	private static class SynchronizedArrayList extends SynchronizedList implements IBooleanArray
	{
		IBooleanArray l;
		
		SynchronizedArrayList(BooleanList l) {
			super(l);
			this.l = (IBooleanArray)l;
		}
		
		SynchronizedArrayList(BooleanList l, Object mutex) {
			super(l, mutex);
			this.l = (IBooleanArray)l;
		}
		
		@Override
		public void ensureCapacity(int size) { synchronized(mutex) { l.ensureCapacity(size); } }
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return l.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { l.clearAndTrim(size); } }
		
		@Override
		public boolean[] elements() { synchronized(mutex) { return l.elements(); } }
		
		@Override
		public void elements(Consumer<boolean[]> action) { 
			Objects.requireNonNull(action);
			synchronized(mutex) {
				l.elements(action);
			}
		}
	}
	
	private static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess 
	{
		SynchronizedRandomAccessList(BooleanList l) {
			super(l);
		}
		
		SynchronizedRandomAccessList(BooleanList l, Object mutex) {
			super(l, mutex);
		}
	}
	
	private static class SynchronizedList extends BooleanCollections.SynchronizedCollection implements BooleanList
	{
		BooleanList l;
		
		SynchronizedList(BooleanList l) {
			super(l);
			this.l = l;
		}
		
		SynchronizedList(BooleanList l, Object mutex) {
			super(l, mutex);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Boolean> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public void add(int index, Boolean element) { synchronized(mutex) { l.add(index, element); } }
		
		@Override
		public void add(int index, boolean e) { synchronized(mutex) { l.add(index, e); } }
		
		@Override
		public boolean addAll(int index, BooleanCollection c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public boolean addAll(BooleanList c) { synchronized(mutex) { return l.addAll(c); } }
		
		@Override
		public boolean addAll(int index, BooleanList c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public boolean getBoolean(int index) { synchronized(mutex) { return l.getBoolean(index); } }
		
		@Override
		public void forEach(BooleanConsumer action) { synchronized(mutex) { l.forEach(action); } }
		
		@Override
		public boolean set(int index, boolean e) { synchronized(mutex) { return l.set(index, e); } }
		
		@Override
		public boolean removeBoolean(int index) { synchronized(mutex) { return l.removeBoolean(index); } }
		
		@Override
		public boolean swapRemove(int index) { synchronized(mutex) { return l.swapRemove(index); } }
		
		@Override
		public boolean swapRemoveBoolean(boolean e) { synchronized(mutex) { return l.swapRemoveBoolean(e); } }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { synchronized(mutex) { return l.lastIndexOf(e); } }
				
		@Override
		public int indexOf(boolean e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		public int lastIndexOf(boolean e) { synchronized(mutex) { return l.lastIndexOf(e); } }
		
		@Override
		public void addElements(int from, boolean[] a, int offset, int length) { synchronized(mutex) { addElements(from, a, offset, length); } }
		
		@Override
		public boolean[] getElements(int from, boolean[] a, int offset, int length) { synchronized(mutex) { return l.getElements(from, a, offset, length); } }
		
		@Override
		public void removeElements(int from, int to) { synchronized(mutex) { l.removeElements(from, to); } }
		
		@Override
		public boolean[] extractElements(int from, int to) { synchronized(mutex) { return l.extractElements(from, to); } }
		
		@Override
		public BooleanListIterator listIterator() {
			return l.listIterator();
		}
		
		@Override
		public BooleanListIterator listIterator(int index) {
			return l.listIterator(index);
		}
		
		@Override
		public BooleanList subList(int from, int to) {
			return BooleanLists.synchronize(l.subList(from, to));
		}
		
		@Override
		public void size(int size) { synchronized(mutex) { l.size(size); } }
		
		@Override
		public BooleanList copy() { synchronized(mutex) { return l.copy(); } }
	}
	
	private static class UnmodifiableRandomList extends UnmodifiableList implements RandomAccess 
	{
		UnmodifiableRandomList(BooleanList l) {
			super(l);
		}
	}
	
	private static class UnmodifiableList extends BooleanCollections.UnmodifiableCollection implements BooleanList
	{
		final BooleanList l;
		
		UnmodifiableList(BooleanList l) {
			super(l);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Boolean> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Boolean element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, BooleanCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(BooleanList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, BooleanList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean getBoolean(int index) { return l.getBoolean(index); }
		
		@Override
		public void forEach(BooleanConsumer action) { l.forEach(action); }
		
		@Override
		public boolean set(int index, boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean removeBoolean(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveBoolean(boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { return l.indexOf(e); }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { return l.lastIndexOf(e); }
				
		@Override
		public int indexOf(boolean e) { return l.indexOf(e); }
		
		@Override
		public int lastIndexOf(boolean e) { return l.lastIndexOf(e); }
		
		@Override
		public void addElements(int from, boolean[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean[] getElements(int from, boolean[] a, int offset, int length) {
			return l.getElements(from, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public BooleanListIterator listIterator() {
			return BooleanIterators.unmodifiable(l.listIterator());
		}
		
		@Override
		public BooleanListIterator listIterator(int index) {
			return BooleanIterators.unmodifiable(l.listIterator(index));
		}
		
		@Override
		public BooleanList subList(int from, int to) {
			return BooleanLists.unmodifiable(l.subList(from, to));
		}
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public BooleanList copy() { return l.copy(); }
	}
	
	private static class EmptyList extends BooleanCollections.EmptyCollection implements BooleanList
	{
		@Override
		public boolean addAll(int index, Collection<? extends Boolean> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Boolean element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, BooleanCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(BooleanList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, BooleanList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean getBoolean(int index) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public boolean set(int index, boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean removeBoolean(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveBoolean(boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int indexOf(Object e) { return -1; }
		
		@Override
		public int lastIndexOf(Object e) { return -1; }
				
		@Override
		public int indexOf(boolean e) { return -1; }
		
		@Override
		public int lastIndexOf(boolean e) { return -1; }
		
		@Override
		public void addElements(int from, boolean[] a, int offset, int length){ throw new UnsupportedOperationException(); }
		
		@Override
		public boolean[] getElements(int from, boolean[] a, int offset, int length) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public BooleanListIterator listIterator() {
			return BooleanIterators.empty();
		}
		
		@Override
		public BooleanListIterator listIterator(int index) {
			if(index != 0)
				throw new IndexOutOfBoundsException();
			return BooleanIterators.empty();
		}
		
		@Override
		public BooleanList subList(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public EmptyList copy() { return this; }
	}
}