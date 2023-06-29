package speiger.src.collections.doubles.utils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.nio.DoubleBuffer;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.lists.AbstractDoubleList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Lists
 */
public class DoubleLists
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
	public static DoubleList unmodifiable(DoubleList l) {
		return l instanceof UnmodifiableList ? l : l instanceof RandomAccess ? new UnmodifiableRandomList(l) : new UnmodifiableList(l);
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IDoubleArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static DoubleList synchronize(DoubleList l) {
		return l instanceof SynchronizedList ? l : (l instanceof IDoubleArray ? new SynchronizedArrayList(l) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l)));
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or IDoubleArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static DoubleList synchronize(DoubleList l, Object mutex) {
		return l instanceof SynchronizedList ? l : (l instanceof IDoubleArray ? new SynchronizedArrayList(l, mutex) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, mutex) : new SynchronizedList(l, mutex)));
	}
	
	/**
	 * Creates a Unmodifiable Singleton list
	 * @param element that should be used in the Singleton
	 * @return a singleton list that is unmodifiable
	 */
	public static DoubleList singleton(double element) {
		return new SingletonList(element);
	}
	
	/**
	 * Reverses the list
	 * @param list that should be reversed.
	 * @return the input list
	 */
	public static DoubleList reverse(DoubleList list) {
		int size = list.size();
		if(list instanceof IDoubleArray) {
			IDoubleArray array = (IDoubleArray)list;
			if(list instanceof SynchronizedArrayList) array.elements(T -> DoubleArrays.reverse(T, size));
			else DoubleArrays.reverse(array.elements(), size);
			return list;
		}
		if(list instanceof RandomAccess) {
			for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
				double t = list.getDouble(i);
				list.set(i, list.getDouble(j));
				list.set(j, t);
			}
			return list;
		}
		DoubleListIterator fwd = list.listIterator();
		DoubleListIterator rev = list.listIterator(size);
		for(int i = 0, mid = size >> 1; i < mid; i++) {
			double tmp = fwd.nextDouble();
			fwd.set(rev.previousDouble());
			rev.set(tmp);
		}
		return list;
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @return the input list
	 */
	public static DoubleList shuffle(DoubleList list) {
		return shuffle(list, SanityChecks.getRandom());
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @param random the random that should be used
	 * @return the input list
	 */
	public static DoubleList shuffle(DoubleList list, Random random) {
		int size = list.size();
		if(list instanceof IDoubleArray) {
			IDoubleArray array = (IDoubleArray)list;
			if(list instanceof SynchronizedArrayList)  array.elements(T -> DoubleArrays.shuffle(T, size, random));
			else DoubleArrays.shuffle(array.elements(), size, random);
			return list;
		}
		for(int i = size; i-- != 0;) {
			int p = random.nextInt(i + 1);
			double t = list.getDouble(i);
			list.set(i, list.getDouble(p));
			list.set(p, t);
		}
		return list;
	}
	
	private static class SingletonList extends AbstractDoubleList
	{
		double element;
		
		SingletonList(double element)
		{
			this.element = element;
		}
		@Override
		public void add(int index, double e) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, Collection<? extends Double> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, DoubleList c) { throw new UnsupportedOperationException(); }

		@Override
		public double getDouble(int index) {
			if(index == 0) return element;
			throw new IndexOutOfBoundsException();
		}
		
		@Override
		public double set(int index, double e) { throw new UnsupportedOperationException(); }
		@Override
		public double removeDouble(int index) { throw new UnsupportedOperationException(); }
		@Override
		public double swapRemove(int index) { throw new UnsupportedOperationException(); }
		@Override
		public void addElements(int from, double[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public double[] getElements(int from, double[] a, int offset, int length) {
			if(from != 0 || length != 1) throw new IndexOutOfBoundsException();
			a[offset] = element;
			return a;
		}
		@Override
		public void fillBuffer(DoubleBuffer buffer) { buffer.put(element); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public double[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonList copy() { return new SingletonList(element); }
	}
	
	private static class SynchronizedArrayList extends SynchronizedList implements IDoubleArray
	{
		IDoubleArray l;
		
		SynchronizedArrayList(DoubleList l) {
			super(l);
			this.l = (IDoubleArray)l;
		}
		
		SynchronizedArrayList(DoubleList l, Object mutex) {
			super(l, mutex);
			this.l = (IDoubleArray)l;
		}
		
		@Override
		public void ensureCapacity(int size) { synchronized(mutex) { l.ensureCapacity(size); } }
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return l.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { l.clearAndTrim(size); } }
		
		@Override
		public double[] elements() { synchronized(mutex) { return l.elements(); } }
		
		@Override
		public void elements(Consumer<double[]> action) { 
			Objects.requireNonNull(action);
			synchronized(mutex) {
				l.elements(action);
			}
		}
	}
	
	private static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess 
	{
		SynchronizedRandomAccessList(DoubleList l) {
			super(l);
		}
		
		SynchronizedRandomAccessList(DoubleList l, Object mutex) {
			super(l, mutex);
		}
	}
	
	private static class SynchronizedList extends DoubleCollections.SynchronizedCollection implements DoubleList
	{
		DoubleList l;
		
		SynchronizedList(DoubleList l) {
			super(l);
			this.l = l;
		}
		
		SynchronizedList(DoubleList l, Object mutex) {
			super(l, mutex);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Double> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public void add(int index, Double element) { synchronized(mutex) { l.add(index, element); } }
		
		@Override
		public void add(int index, double e) { synchronized(mutex) { l.add(index, e); } }
		
		@Override
		public boolean addAll(int index, DoubleCollection c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public boolean addAll(DoubleList c) { synchronized(mutex) { return l.addAll(c); } }
		
		@Override
		public boolean addAll(int index, DoubleList c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public double getDouble(int index) { synchronized(mutex) { return l.getDouble(index); } }
		
		@Override
		public void forEach(DoubleConsumer action) { synchronized(mutex) { l.forEach(action); } }
		
		@Override
		public double set(int index, double e) { synchronized(mutex) { return l.set(index, e); } }
		
		@Override
		public double removeDouble(int index) { synchronized(mutex) { return l.removeDouble(index); } }
		
		@Override
		public double swapRemove(int index) { synchronized(mutex) { return l.swapRemove(index); } }
		
		@Override
		public boolean swapRemoveDouble(double e) { synchronized(mutex) { return l.swapRemoveDouble(e); } }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { synchronized(mutex) { return l.lastIndexOf(e); } }
				
		@Override
		public int indexOf(double e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		public int lastIndexOf(double e) { synchronized(mutex) { return l.lastIndexOf(e); } }
		
		@Override
		public void addElements(int from, double[] a, int offset, int length) { synchronized(mutex) { l.addElements(from, a, offset, length); } }
		
		@Override
		public double[] getElements(int from, double[] a, int offset, int length) { synchronized(mutex) { return l.getElements(from, a, offset, length); } }
		
		@Override
		public void removeElements(int from, int to) { synchronized(mutex) { l.removeElements(from, to); } }
		
		@Override
		public double[] extractElements(int from, int to) { synchronized(mutex) { return l.extractElements(from, to); } }
		
		public void fillBuffer(DoubleBuffer buffer) { synchronized(mutex) { l.fillBuffer(buffer); } }
		@Override
		public DoubleListIterator listIterator() {
			return l.listIterator();
		}
		
		@Override
		public DoubleListIterator listIterator(int index) {
			return l.listIterator(index);
		}
		
		@Override
		public DoubleListIterator indexedIterator(int...indecies) {
			return l.indexedIterator(indecies);
		}
		
		@Override
		public DoubleListIterator indexedIterator(IntList indecies) {
			return l.indexedIterator(indecies);
		}
		
		@Override
		public DoubleList subList(int from, int to) {
			return DoubleLists.synchronize(l.subList(from, to));
		}
		
		@Override
		public DoubleList reversed() {
			return DoubleLists.synchronize(l.reversed());
		}
		
		@Override
		public void size(int size) { synchronized(mutex) { l.size(size); } }
		
		@Override
		public DoubleList copy() { synchronized(mutex) { return l.copy(); } }
	}
	
	private static class UnmodifiableRandomList extends UnmodifiableList implements RandomAccess 
	{
		UnmodifiableRandomList(DoubleList l) {
			super(l);
		}
	}
	
	private static class UnmodifiableList extends DoubleCollections.UnmodifiableCollection implements DoubleList
	{
		final DoubleList l;
		
		UnmodifiableList(DoubleList l) {
			super(l);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Double> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Double element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, double e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, DoubleCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(DoubleList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, DoubleList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public double getDouble(int index) { return l.getDouble(index); }
		
		@Override
		public void forEach(DoubleConsumer action) { l.forEach(action); }
		
		@Override
		public double set(int index, double e) { throw new UnsupportedOperationException(); }
		
		@Override
		public double removeDouble(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public double swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveDouble(double e) { throw new UnsupportedOperationException(); }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { return l.indexOf(e); }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { return l.lastIndexOf(e); }
				
		@Override
		public int indexOf(double e) { return l.indexOf(e); }
		
		@Override
		public int lastIndexOf(double e) { return l.lastIndexOf(e); }
		
		@Override
		public void addElements(int from, double[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public double[] getElements(int from, double[] a, int offset, int length) {
			return l.getElements(from, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public double[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		public void fillBuffer(DoubleBuffer buffer) { l.fillBuffer(buffer); }
		
		@Override
		public DoubleListIterator listIterator() {
			return DoubleIterators.unmodifiable(l.listIterator());
		}
		
		@Override
		public DoubleListIterator listIterator(int index) {
			return DoubleIterators.unmodifiable(l.listIterator(index));
		}
		
		@Override
		public DoubleListIterator indexedIterator(int...indecies) {
			return DoubleIterators.unmodifiable(l.indexedIterator(indecies));
		}
		
		@Override
		public DoubleListIterator indexedIterator(IntList indecies) {
			return DoubleIterators.unmodifiable(l.indexedIterator(indecies));
		}
		
		@Override
		public DoubleList subList(int from, int to) {
			return DoubleLists.unmodifiable(l.subList(from, to));
		}
		
		@Override
		public DoubleList reversed() {
			return DoubleLists.unmodifiable(l.reversed());
		}
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleList copy() { return l.copy(); }
	}
	
	private static class EmptyList extends DoubleCollections.EmptyCollection implements DoubleList
	{
		@Override
		public boolean addAll(int index, Collection<? extends Double> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Double element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, double e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, DoubleCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(DoubleList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, DoubleList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public double getDouble(int index) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public double set(int index, double e) { throw new UnsupportedOperationException(); }
		
		@Override
		public double removeDouble(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public double swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveDouble(double e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int indexOf(Object e) { return -1; }
		
		@Override
		public int lastIndexOf(Object e) { return -1; }
				
		@Override
		public int indexOf(double e) { return -1; }
		
		@Override
		public int lastIndexOf(double e) { return -1; }
		
		@Override
		public void addElements(int from, double[] a, int offset, int length){ throw new UnsupportedOperationException(); }
		
		@Override
		public double[] getElements(int from, double[] a, int offset, int length) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public double[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleListIterator listIterator() {
			return DoubleIterators.empty();
		}
		
		@Override
		public DoubleListIterator listIterator(int index) {
			if(index != 0)
				throw new IndexOutOfBoundsException();
			return DoubleIterators.empty();
		}
		
		@Override
		public DoubleListIterator indexedIterator(int...indecies) {
			return DoubleIterators.empty(); 
		}
		
		@Override
		public DoubleListIterator indexedIterator(IntList indecies) {
			return DoubleIterators.empty(); 
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
		public DoubleList subList(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleList reversed() { return this; }
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public EmptyList copy() { return this; }
	}
}