package speiger.src.collections.floats.utils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.nio.FloatBuffer;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.floats.lists.AbstractFloatList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.lists.FloatListIterator;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Lists
 */
public class FloatLists
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
	public static FloatList unmodifiable(FloatList l) {
		return l instanceof UnmodifiableList ? l : l instanceof RandomAccess ? new UnmodifiableRandomList(l) : new UnmodifiableList(l);
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IFloatArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static FloatList synchronize(FloatList l) {
		return l instanceof SynchronizedList ? l : (l instanceof IFloatArray ? new SynchronizedArrayList(l) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l)));
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IFloatArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static FloatList synchronize(FloatList l, Object mutex) {
		return l instanceof SynchronizedList ? l : (l instanceof IFloatArray ? new SynchronizedArrayList(l, mutex) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, mutex) : new SynchronizedList(l, mutex)));
	}
	
	/**
	 * Creates a Unmodifiable Singleton list
	 * @param element that should be used in the Singleton
	 * @return a singleton list that is unmodifiable
	 */
	public static FloatList singleton(float element) {
		return new SingletonList(element);
	}
	
	/**
	 * Reverses the list
	 * @param list that should be reversed.
	 * @return the input list
	 */
	public static FloatList reverse(FloatList list) {
		int size = list.size();
		if(list instanceof IFloatArray) {
			IFloatArray array = (IFloatArray)list;
			if(list instanceof SynchronizedArrayList) array.elements(T -> FloatArrays.reverse(T, size));
			else FloatArrays.reverse(array.elements(), size);
			return list;
		}
		if(list instanceof RandomAccess) {
			for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
				float t = list.getFloat(i);
				list.set(i, list.getFloat(j));
				list.set(j, t);
			}
			return list;
		}
		FloatListIterator fwd = list.listIterator();
		FloatListIterator rev = list.listIterator(size);
		for(int i = 0, mid = size >> 1; i < mid; i++) {
			float tmp = fwd.nextFloat();
			fwd.set(rev.previousFloat());
			rev.set(tmp);
		}
		return list;
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @return the input list
	 */
	public static FloatList shuffle(FloatList list) {
		return shuffle(list, SanityChecks.getRandom());
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @param random the random that should be used
	 * @return the input list
	 */
	public static FloatList shuffle(FloatList list, Random random) {
		int size = list.size();
		if(list instanceof IFloatArray) {
			IFloatArray array = (IFloatArray)list;
			if(list instanceof SynchronizedArrayList)  array.elements(T -> FloatArrays.shuffle(T, size, random));
			else FloatArrays.shuffle(array.elements(), size, random);
			return list;
		}
		for(int i = size; i-- != 0;) {
			int p = random.nextInt(i + 1);
			float t = list.getFloat(i);
			list.set(i, list.getFloat(p));
			list.set(p, t);
		}
		return list;
	}
	
	private static class SingletonList extends AbstractFloatList
	{
		float element;
		
		SingletonList(float element)
		{
			this.element = element;
		}
		@Override
		public void add(int index, float e) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, Collection<? extends Float> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, FloatCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, FloatList c) { throw new UnsupportedOperationException(); }

		@Override
		public float getFloat(int index) {
			if(index == 0) return element;
			throw new IndexOutOfBoundsException();
		}
		
		@Override
		public float set(int index, float e) { throw new UnsupportedOperationException(); }
		@Override
		public float removeFloat(int index) { throw new UnsupportedOperationException(); }
		@Override
		public float swapRemove(int index) { throw new UnsupportedOperationException(); }
		@Override
		public void addElements(int from, float[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public float[] getElements(int from, float[] a, int offset, int length) {
			if(from != 0 || length != 1) throw new IndexOutOfBoundsException();
			a[offset] = element;
			return a;
		}
		@Override
		public void fillBuffer(FloatBuffer buffer) { buffer.put(element); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public float[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonList copy() { return new SingletonList(element); }
	}
	
	private static class SynchronizedArrayList extends SynchronizedList implements IFloatArray
	{
		IFloatArray l;
		
		SynchronizedArrayList(FloatList l) {
			super(l);
			this.l = (IFloatArray)l;
		}
		
		SynchronizedArrayList(FloatList l, Object mutex) {
			super(l, mutex);
			this.l = (IFloatArray)l;
		}
		
		@Override
		public void ensureCapacity(int size) { synchronized(mutex) { l.ensureCapacity(size); } }
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return l.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { l.clearAndTrim(size); } }
		
		@Override
		public float[] elements() { synchronized(mutex) { return l.elements(); } }
		
		@Override
		public void elements(Consumer<float[]> action) { 
			Objects.requireNonNull(action);
			synchronized(mutex) {
				l.elements(action);
			}
		}
	}
	
	private static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess 
	{
		SynchronizedRandomAccessList(FloatList l) {
			super(l);
		}
		
		SynchronizedRandomAccessList(FloatList l, Object mutex) {
			super(l, mutex);
		}
	}
	
	private static class SynchronizedList extends FloatCollections.SynchronizedCollection implements FloatList
	{
		FloatList l;
		
		SynchronizedList(FloatList l) {
			super(l);
			this.l = l;
		}
		
		SynchronizedList(FloatList l, Object mutex) {
			super(l, mutex);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Float> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public void add(int index, Float element) { synchronized(mutex) { l.add(index, element); } }
		
		@Override
		public void add(int index, float e) { synchronized(mutex) { l.add(index, e); } }
		
		@Override
		public boolean addAll(int index, FloatCollection c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public boolean addAll(FloatList c) { synchronized(mutex) { return l.addAll(c); } }
		
		@Override
		public boolean addAll(int index, FloatList c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public float getFloat(int index) { synchronized(mutex) { return l.getFloat(index); } }
		
		@Override
		public void forEach(FloatConsumer action) { synchronized(mutex) { l.forEach(action); } }
		
		@Override
		public float set(int index, float e) { synchronized(mutex) { return l.set(index, e); } }
		
		@Override
		public float removeFloat(int index) { synchronized(mutex) { return l.removeFloat(index); } }
		
		@Override
		public float swapRemove(int index) { synchronized(mutex) { return l.swapRemove(index); } }
		
		@Override
		public boolean swapRemoveFloat(float e) { synchronized(mutex) { return l.swapRemoveFloat(e); } }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { synchronized(mutex) { return l.lastIndexOf(e); } }
				
		@Override
		public int indexOf(float e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		public int lastIndexOf(float e) { synchronized(mutex) { return l.lastIndexOf(e); } }
		
		@Override
		public void addElements(int from, float[] a, int offset, int length) { synchronized(mutex) { l.addElements(from, a, offset, length); } }
		
		@Override
		public float[] getElements(int from, float[] a, int offset, int length) { synchronized(mutex) { return l.getElements(from, a, offset, length); } }
		
		@Override
		public void removeElements(int from, int to) { synchronized(mutex) { l.removeElements(from, to); } }
		
		@Override
		public float[] extractElements(int from, int to) { synchronized(mutex) { return l.extractElements(from, to); } }
		
		public void fillBuffer(FloatBuffer buffer) { synchronized(mutex) { l.fillBuffer(buffer); } }
		@Override
		public FloatListIterator listIterator() {
			return l.listIterator();
		}
		
		@Override
		public FloatListIterator listIterator(int index) {
			return l.listIterator(index);
		}
		
		@Override
		public FloatList subList(int from, int to) {
			return FloatLists.synchronize(l.subList(from, to));
		}
		
		@Override
		public FloatList reversed() {
			return FloatLists.synchronize(l.reversed());
		}
		
		@Override
		public void size(int size) { synchronized(mutex) { l.size(size); } }
		
		@Override
		public FloatList copy() { synchronized(mutex) { return l.copy(); } }
	}
	
	private static class UnmodifiableRandomList extends UnmodifiableList implements RandomAccess 
	{
		UnmodifiableRandomList(FloatList l) {
			super(l);
		}
	}
	
	private static class UnmodifiableList extends FloatCollections.UnmodifiableCollection implements FloatList
	{
		final FloatList l;
		
		UnmodifiableList(FloatList l) {
			super(l);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Float> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Float element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, float e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, FloatCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(FloatList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, FloatList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public float getFloat(int index) { return l.getFloat(index); }
		
		@Override
		public void forEach(FloatConsumer action) { l.forEach(action); }
		
		@Override
		public float set(int index, float e) { throw new UnsupportedOperationException(); }
		
		@Override
		public float removeFloat(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public float swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveFloat(float e) { throw new UnsupportedOperationException(); }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { return l.indexOf(e); }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { return l.lastIndexOf(e); }
				
		@Override
		public int indexOf(float e) { return l.indexOf(e); }
		
		@Override
		public int lastIndexOf(float e) { return l.lastIndexOf(e); }
		
		@Override
		public void addElements(int from, float[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public float[] getElements(int from, float[] a, int offset, int length) {
			return l.getElements(from, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public float[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		public void fillBuffer(FloatBuffer buffer) { l.fillBuffer(buffer); }
		
		@Override
		public FloatListIterator listIterator() {
			return FloatIterators.unmodifiable(l.listIterator());
		}
		
		@Override
		public FloatListIterator listIterator(int index) {
			return FloatIterators.unmodifiable(l.listIterator(index));
		}
		
		@Override
		public FloatList subList(int from, int to) {
			return FloatLists.unmodifiable(l.subList(from, to));
		}
		
		@Override
		public FloatList reversed() {
			return FloatLists.unmodifiable(l.reversed());
		}
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatList copy() { return l.copy(); }
	}
	
	private static class EmptyList extends FloatCollections.EmptyCollection implements FloatList
	{
		@Override
		public boolean addAll(int index, Collection<? extends Float> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Float element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, float e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, FloatCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(FloatList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, FloatList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public float getFloat(int index) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public float set(int index, float e) { throw new UnsupportedOperationException(); }
		
		@Override
		public float removeFloat(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public float swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveFloat(float e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int indexOf(Object e) { return -1; }
		
		@Override
		public int lastIndexOf(Object e) { return -1; }
				
		@Override
		public int indexOf(float e) { return -1; }
		
		@Override
		public int lastIndexOf(float e) { return -1; }
		
		@Override
		public void addElements(int from, float[] a, int offset, int length){ throw new UnsupportedOperationException(); }
		
		@Override
		public float[] getElements(int from, float[] a, int offset, int length) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public float[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatListIterator listIterator() {
			return FloatIterators.empty();
		}
		
		@Override
		public FloatListIterator listIterator(int index) {
			if(index != 0)
				throw new IndexOutOfBoundsException();
			return FloatIterators.empty();
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
		public FloatList subList(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public FloatList reversed() { return this; }
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public EmptyList copy() { return this; }
	}
}