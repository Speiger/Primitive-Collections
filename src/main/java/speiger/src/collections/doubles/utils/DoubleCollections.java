package speiger.src.collections.doubles.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.DoublePredicate;
import java.util.function.Consumer;

import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.ints.functions.consumer.IntDoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Collections
 */
public class DoubleCollections
{
	/**
	 * Empty Collection Reference
	 */
	public static final DoubleCollection EMPTY = new EmptyCollection();
	
	/**
	 * Returns a Immutable EmptyCollection instance that is automatically casted.
	 * @return an empty collection
	 */
	public static DoubleCollection empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable Collection instance based on the instance given.
	 * @param c that should be made immutable/unmodifiable
	 * @return a unmodifiable collection wrapper. If the Collection already a unmodifiable wrapper then it just returns itself.
	 */
	public static DoubleCollection unmodifiable(DoubleCollection c) {
		return c instanceof UnmodifiableCollection ? c : new UnmodifiableCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static DoubleCollection synchronize(DoubleCollection c) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c);
	}
	
	/**
	 * Returns a synchronized Collection instance based on the instance given.
	 * @param c that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized collection wrapper. If the Collection already a synchronized wrapper then it just returns itself.
	 */
	public static DoubleCollection synchronize(DoubleCollection c, Object mutex) {
		return c instanceof SynchronizedCollection ? c : new SynchronizedCollection(c, mutex);
	}
	
	/**
	 * Creates a Singleton Collection of a given element
	 * @param element the element that should be converted into a singleton collection
	 * @return a singletoncollection of the given element
	 */
	public static DoubleCollection singleton(double element) {
		return new SingletonCollection(element);
	}
	
	protected static CollectionWrapper wrapper() {
		return new CollectionWrapper();
	}
	
	protected static CollectionWrapper wrapper(int size) {
		return new CollectionWrapper(size);
	}
	
	protected static DistinctCollectionWrapper distinctWrapper() {
		return new DistinctCollectionWrapper();
	}
	
	protected static DistinctCollectionWrapper distinctWrapper(int size) {
		return new DistinctCollectionWrapper(size);
	}
	
	protected static class CollectionWrapper extends AbstractDoubleCollection implements ITrimmable {
		double[] elements;
		int size = 0;
		
		public CollectionWrapper() {
			this(10);
		}
		
		public CollectionWrapper(int size) {
			if(size < 0) throw new IllegalStateException("Size has to be 0 or greater");
			elements = new double[size];
		}
		
		@Override
		public boolean add(double o) {
			if(size >= elements.length) elements = Arrays.copyOf(elements, (int)Math.min((long)elements.length + (elements.length >> 1), SanityChecks.MAX_ARRAY_SIZE));
			elements[size++] = o;
			return true;
		}
		
		public double getDouble(int index) {
			if(index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			return elements[index];
		}
		
		@Override
		public boolean remDouble(double e) {
			for(int i = 0;i<size;i++) {
				if(Double.doubleToLongBits(elements[i]) == Double.doubleToLongBits(e)) {
					removeIndex(i);
					return true;
				}
			}
			return false;
		}
		
		private void removeIndex(int index) {
			if(index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			size--;
			if(index != size) System.arraycopy(elements, index+1, elements, index, size - index);
		}
		
		@Override
		public DoubleIterator iterator() {
			return new DoubleIterator() {
				int index = 0;
				int lastReturned = -1;
				
				@Override
				public boolean hasNext() {
					return index < size;
				}
				
				@Override
				public double nextDouble() {
					int i = index++;
					return elements[(lastReturned = i)];
				}
				
				@Override
				public void remove() {
					if(lastReturned == -1) throw new IllegalStateException();
					removeIndex(lastReturned);
					index = lastReturned;
					lastReturned = -1;
				}
			};
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public void clear() {
			size = 0;
		}
		
		public void sort(DoubleComparator c) {
			if(c != null) DoubleArrays.stableSort(elements, size, c);
			else DoubleArrays.stableSort(elements, size);
		}
		
		public void unstableSort(DoubleComparator c) {
			if(c != null) DoubleArrays.unstableSort(elements, size, c);
			else DoubleArrays.unstableSort(elements, size);		
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(elements[i]);
		}
		
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i<size;i++)
				action.accept(input, elements[i]);		
		}
		
		@Override
		public boolean trim(int size) {
			if(size > size() || size() == elements.length) return false;
			int value = Math.max(size, size());
			elements = value == 0 ? DoubleArrays.EMPTY_ARRAY : Arrays.copyOf(elements, value);
			return true;
		}
		
		@Override
		public void clearAndTrim(int size) {
			if(elements.length <= size) {
				clear();
				return;
			}
			elements = size == 0 ? DoubleArrays.EMPTY_ARRAY : new double[size];
			this.size = size;
		}
		
		@Override
		@Deprecated
		public Object[] toArray() {
			Object[] obj = new Object[size];
			for(int i = 0;i<size;i++)
				obj[i] = Double.valueOf(elements[i]);
			return obj;
		}
		
		@Override
		@Deprecated
		public <E> E[] toArray(E[] a) {
			if(a == null) a = (E[])new Object[size];
			else if(a.length < size) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size);
			for(int i = 0;i<size;i++)
				a[i] = (E)Double.valueOf(elements[i]);
			if (a.length > size) a[size] = null;
			return a;
		}
		
		@Override
		public double[] toDoubleArray(double[] a) {
			if(a.length < size) a = new double[size];
			System.arraycopy(elements, 0, a, 0, size);
			if (a.length > size) a[size] = 0D;
			return a;
		}		
	}
	
	protected static class DistinctCollectionWrapper extends AbstractDoubleCollection {
		double[] keys;
		boolean containsNull;
		int minCapacity;
		int nullIndex;
		int maxFill;
		int mask;
		int size;
		
		public DistinctCollectionWrapper() {
			this(HashUtil.DEFAULT_MIN_CAPACITY);
		}
		
		public DistinctCollectionWrapper(int size) {
			if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
			minCapacity = nullIndex = HashUtil.arraySize(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
			mask = nullIndex - 1;
			maxFill = Math.min((int)Math.ceil(nullIndex * HashUtil.DEFAULT_LOAD_FACTOR), nullIndex - 1);
			keys = new double[nullIndex + 1];
		}

		@Override
		public boolean add(double o) {
			if(Double.doubleToLongBits(o) == 0) {
				if(containsNull) return false;
				containsNull = true;
			}
			else {
				int pos = HashUtil.mix(Double.hashCode(o)) & mask;
				double current = keys[pos];
				if(Double.doubleToLongBits(current) != 0) {
					if(Double.doubleToLongBits(current) == Double.doubleToLongBits(o)) return false;
					while(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) != 0)
						if(Double.doubleToLongBits(current) == Double.doubleToLongBits(o)) return false;
				}
				keys[pos] = o;
			}
			if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, HashUtil.DEFAULT_LOAD_FACTOR));
			return true;
		}
		
		@Override
		public boolean contains(Object o) {
			if(o == null) return false;
			if(o instanceof Double && Double.doubleToLongBits(((Double)o).doubleValue()) == Double.doubleToLongBits(0D)) return containsNull;
			int pos = HashUtil.mix(o.hashCode()) & mask;
			double current = keys[pos];
			if(Double.doubleToLongBits(current) == 0) return false;
			if(Objects.equals(o, Double.valueOf(current))) return true;
			while(true) {
				if(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) == 0) return false;
				else if(Objects.equals(o, Double.valueOf(current))) return true;
			}
		}

		@Override
		public boolean remove(Object o) {
			if(o == null) return false;
			if(o instanceof Double && Double.doubleToLongBits(((Double)o).doubleValue()) == Double.doubleToLongBits(0D)) return (containsNull ? removeNullIndex() : false);
			int pos = HashUtil.mix(o.hashCode()) & mask;
			double current = keys[pos];
			if(Double.doubleToLongBits(current) == 0) return false;
			if(Objects.equals(o, Double.valueOf(current))) return removeIndex(pos);
			while(true) {
				if(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) == 0) return false;
				else if(Objects.equals(o, Double.valueOf(current))) return removeIndex(pos);
			}
		}

		@Override
		public boolean contains(double o) {
			if(Double.doubleToLongBits(o) == 0) return containsNull;
			int pos = HashUtil.mix(Double.hashCode(o)) & mask;
			double current = keys[pos];
			if(Double.doubleToLongBits(current) == 0) return false;
			if(Double.doubleToLongBits(current) == Double.doubleToLongBits(o)) return true;
			while(true) {
				if(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) == 0) return false;
				else if(Double.doubleToLongBits(current) == Double.doubleToLongBits(o)) return true;
			}
		}
		
		@Override
		public boolean remDouble(double o) {
			if(Double.doubleToLongBits(o) == 0) return containsNull ? removeNullIndex() : false;
			int pos = HashUtil.mix(Double.hashCode(o)) & mask;
			double current = keys[pos];
			if(Double.doubleToLongBits(current) == 0) return false;
			if(Double.doubleToLongBits(current) == Double.doubleToLongBits(o)) return removeIndex(pos);
			while(true) {
				if(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) == 0) return false;
				else if(Double.doubleToLongBits(current) == Double.doubleToLongBits(o)) return removeIndex(pos);
			}
		}
		
		protected boolean removeIndex(int pos) {
			if(pos == nullIndex) return containsNull ? removeNullIndex() : false;
			keys[pos] = 0D;
			size--;
			shiftKeys(pos);
			if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
			return true;
		}
		
		protected boolean removeNullIndex() {
			containsNull = false;
			keys[nullIndex] = 0D;
			size--;
			if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
			return true;
		}
		
		@Override
		public DoubleIterator iterator() {
			return new SetIterator();
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			if(size() <= 0) return;
			if(containsNull) action.accept(keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0) action.accept(keys[i]);
			}
		}
		
		@Override
		public DistinctCollectionWrapper copy() {
			DistinctCollectionWrapper set = new DistinctCollectionWrapper(0);
			set.minCapacity = minCapacity;
			set.mask = mask;
			set.maxFill = maxFill;
			set.nullIndex = nullIndex;
			set.containsNull = containsNull;
			set.size = size;
			set.keys = Arrays.copyOf(keys, keys.length);
			return set;
		}
		
		protected void shiftKeys(int startPos) {
			int slot, last;
			double current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if(Double.doubleToLongBits((current = keys[startPos])) == 0) {
						keys[last] = 0D;
						return;
					}
					slot = HashUtil.mix(Double.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				keys[last] = current;
			}
		}
		
		protected void rehash(int newSize) {
			int newMask = newSize - 1;
			double[] newKeys = new double[newSize + 1];
			for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
				while(true) {
					if(--i < 0) throw new ConcurrentModificationException("Set was modified during rehash");
					if(Double.doubleToLongBits(keys[i]) != 0) break;
				}
				if(Double.doubleToLongBits(newKeys[pos = HashUtil.mix(Double.hashCode(keys[i])) & newMask]) != 0)
					while(Double.doubleToLongBits(newKeys[pos = (++pos & newMask)]) != 0);
				newKeys[pos] = keys[i];
			}
			nullIndex = newSize;
			mask = newMask;
			maxFill = Math.min((int)Math.ceil(nullIndex * HashUtil.DEFAULT_LOAD_FACTOR), nullIndex - 1);
			keys = newKeys;
		}
		
		@Override
		public void clear() {
			if(size == 0) return;
			size = 0;
			containsNull = false;
			Arrays.fill(keys, 0D);
		}
		
		@Override
		public int size() {
			return size;
		}
		
		private class SetIterator implements DoubleIterator {
			int pos = nullIndex;
			int returnedPos = -1;
			int lastReturned = -1;
			int nextIndex = Integer.MIN_VALUE;
			boolean returnNull = containsNull;
			double[] wrapped = null;
			int wrappedIndex = 0;
			
			@Override
			public boolean hasNext() {
				if(nextIndex == Integer.MIN_VALUE) {
					if(returnNull) {
						returnNull = false;
						nextIndex = nullIndex;
					}
					else
					{
						while(true) {
							if(--pos < 0) {
								if(wrapped == null || wrappedIndex <= -pos - 1) break;
								nextIndex = -pos - 1;
								break;
							}
							if(Double.doubleToLongBits(keys[pos]) != 0){
								nextIndex = pos;
								break;
							}
						}
					}
				}
				return nextIndex != Integer.MIN_VALUE;
			}
			
			@Override
			public double nextDouble() {
				if(!hasNext()) throw new NoSuchElementException();
				returnedPos = pos;
				if(nextIndex < 0){
					lastReturned = Integer.MAX_VALUE;
					double value = wrapped[nextIndex];
					nextIndex = Integer.MIN_VALUE;
					return value;
				}
				double value = keys[(lastReturned = nextIndex)];
				nextIndex = Integer.MIN_VALUE;
				return value;
			}
			
			@Override
			public void remove() {
				if(lastReturned == -1) throw new IllegalStateException();
				if(lastReturned == nullIndex) {
					containsNull = false;
					keys[nullIndex] = 0D;
				}
				else if(returnedPos >= 0) shiftKeys(returnedPos);
				else {
					DistinctCollectionWrapper.this.remDouble(wrapped[-returnedPos - 1]);
					lastReturned = -1;
					return;
				}
				size--;
				lastReturned = -1;
			}
			
			private void shiftKeys(int startPos) {
				int slot, last;
				double current;
				while(true) {
					startPos = ((last = startPos) + 1) & mask;
					while(true){
						if(Double.doubleToLongBits((current = keys[startPos])) == 0) {
							keys[last] = 0D;
							return;
						}
						slot = HashUtil.mix(Double.hashCode(current)) & mask;
						if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
						startPos = ++startPos & mask;
					}
					if(startPos < last) addWrapper(keys[startPos]);
					keys[last] = current;
				}
			}
			
			private void addWrapper(double value) {
				if(wrapped == null) wrapped = new double[2];
				else if(wrappedIndex >= wrapped.length) {
					double[] newArray = new double[wrapped.length * 2];
					System.arraycopy(wrapped, 0, newArray, 0, wrapped.length);
					wrapped = newArray;
				}
				wrapped[wrappedIndex++] = value;
			}
		}
	}
	
	private static class SingletonCollection extends AbstractDoubleCollection
	{
		double element;
		
		SingletonCollection(double element) {
			this.element = element;
		}
		
		@Override
		public boolean remDouble(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean add(double o) { throw new UnsupportedOperationException(); }
		@Override
		public DoubleIterator iterator()
		{
			return new DoubleIterator() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next; }
				@Override
				public double nextDouble() {
					if(!hasNext()) throw new NoSuchElementException();
					next = false;
					return element;
				}
			};
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (!(o instanceof Collection))
				return false;
			Collection<?> l = (Collection<?>)o;
			if(l.size() != size()) return false;
			Iterator<?> iter = l.iterator();
			if (iter.hasNext() && !Objects.equals(element, iter.next())) {
				return false;
			}
			return !iter.hasNext();
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(element);
		}
		
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonCollection copy() { return new SingletonCollection(element); }
	}
	
	/**
	 * Synchronized Collection Wrapper for the synchronizedCollection function
	 */
	public static class SynchronizedCollection implements DoubleCollection {
		DoubleCollection c;
		protected Object mutex;
		
		SynchronizedCollection(DoubleCollection c) {
			this.c = c;
			mutex = this;
		}
		
		SynchronizedCollection(DoubleCollection c, Object mutex) {
			this.c = c;
			this.mutex = mutex;
		}
		
		@Override
		public boolean add(double o) { synchronized(mutex) { return c.add(o); } }
		@Override
		public boolean addAll(Collection<? extends Double> c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(DoubleCollection c) { synchronized(mutex) { return this.c.addAll(c); } }
		@Override
		public boolean addAll(double[] e, int offset, int length) { synchronized(mutex) { return c.addAll(e, offset, length); } }
		@Override
		public boolean contains(double o) { synchronized(mutex) { return c.contains(o); } }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public boolean containsAll(DoubleCollection c) { synchronized(mutex) { return this.c.containsAll(c); } }
		
		@Override
		public boolean containsAny(DoubleCollection c) { synchronized(mutex) { return this.c.containsAny(c); } }
		
		@Override
		public int size() { synchronized(mutex) { return c.size(); } }
		
		@Override
		public boolean isEmpty() { synchronized(mutex) { return c.isEmpty(); } }
		
		@Override
		public DoubleIterator iterator() {
			return c.iterator();
		}
		
		@Override
		public DoubleCollection copy() { synchronized(mutex) { return c.copy(); } }
		
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
		public boolean remDouble(double o) { synchronized(mutex) { return c.remDouble(o); } }
		@Override
		public boolean removeAll(DoubleCollection c) { synchronized(mutex) { return this.c.removeAll(c); } }
		@Override
		public boolean removeAll(DoubleCollection c, DoubleConsumer r) { synchronized(mutex) { return this.c.removeAll(c, r); } }
		@Override
		public boolean retainAll(DoubleCollection c) { synchronized(mutex) { return this.c.retainAll(c); } }
		@Override
		public boolean retainAll(DoubleCollection c, DoubleConsumer r) { synchronized(mutex) { return this.c.retainAll(c, r); } }
		@Override
		public boolean remIf(DoublePredicate filter){ synchronized(mutex) { return c.remIf(filter); } }
		@Override
		public void clear() { synchronized(mutex) { c.clear(); } }
		@Override
		public Object[] toArray() { synchronized(mutex) { return c.toArray(); } }
		@Override
		public <T> T[] toArray(T[] a) { synchronized(mutex) { return c.toArray(a); } }
		@Override
		public double[] toDoubleArray() { synchronized(mutex) { return c.toDoubleArray(); } }
		@Override
		public double[] toDoubleArray(double[] a) { synchronized(mutex) { return c.toDoubleArray(a); } }
		@Override
		public void forEach(DoubleConsumer action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Double> action) { synchronized(mutex) { c.forEach(action); } }
		@Override
		public void forEachIndexed(IntDoubleConsumer action) { synchronized(mutex) { c.forEachIndexed(action); } }
		@Override
		public int hashCode() { synchronized(mutex) { return c.hashCode(); } }
		@Override
		public boolean equals(Object obj) {
			if(obj == this) return true;
			synchronized(mutex) { return c.equals(obj); } 
		}
		@Override
		public String toString() { synchronized(mutex) { return c.toString(); } }
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) { synchronized(mutex) { c.forEach(input, action); } }
		@Override
		public boolean matchesAny(DoublePredicate filter) { synchronized(mutex) { return c.matchesAny(filter); } }
		@Override
		public boolean matchesNone(DoublePredicate filter) { synchronized(mutex) { return c.matchesNone(filter); } }
		@Override
		public boolean matchesAll(DoublePredicate filter) { synchronized(mutex) { return c.matchesAll(filter); } }
		@Override
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) { synchronized(mutex) { return c.reduce(identity, operator); } }
		@Override
		public double reduce(DoubleDoubleUnaryOperator operator) { synchronized(mutex) { return c.reduce(operator); } }
		@Override
		public double findFirst(DoublePredicate filter) { synchronized(mutex) { return c.findFirst(filter); } }
		@Override
		public int count(DoublePredicate filter) { synchronized(mutex) { return c.count(filter); } }
	}
	
	/**
	 * Unmodifyable Collection Wrapper for the unmodifyableCollection method
	 */
	public static class UnmodifiableCollection implements DoubleCollection {
		DoubleCollection c;
		
		UnmodifiableCollection(DoubleCollection c) {
			this.c = c;
		}
		
		@Override
		public boolean add(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(Collection<? extends Double> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(double[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public boolean contains(double o) { return c.contains(o); }
		@Override
		public boolean containsAll(DoubleCollection c) { return this.c.containsAll(c); }
		@Override
		public boolean containsAny(DoubleCollection c) { return this.c.containsAny(c); }
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
		public DoubleIterator iterator() { return DoubleIterators.unmodifiable(c.iterator()); }
		@Override
		public DoubleCollection copy() { return c.copy(); }
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
		public boolean removeIf(Predicate<? super Double> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remDouble(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(DoubleCollection c, DoubleConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(DoubleCollection c, DoubleConsumer r) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(DoublePredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return c.toArray(); }
		@Override
		public <T> T[] toArray(T[] a) { return c.toArray(a); }
		@Override
		public double[] toDoubleArray() { return c.toDoubleArray(); }
		@Override
		public double[] toDoubleArray(double[] a) { return c.toDoubleArray(a); }
		@Override
		public void forEach(DoubleConsumer action) { c.forEach(action); }
		@Override
		@Deprecated
		public void forEach(Consumer<? super Double> action) { c.forEach(action); }
		@Override
		public void forEachIndexed(IntDoubleConsumer action) { c.forEachIndexed(action); }
		@Override
		public int hashCode() { return c.hashCode(); }
		@Override
		public boolean equals(Object obj) { return obj == this || c.equals(obj); }
		@Override
		public String toString() { return c.toString(); }
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) { c.forEach(input, action); }
		@Override
		public boolean matchesAny(DoublePredicate filter) { return c.matchesAny(filter); }
		@Override
		public boolean matchesNone(DoublePredicate filter) { return c.matchesNone(filter); }
		@Override
		public boolean matchesAll(DoublePredicate filter) { return c.matchesAll(filter); }
		@Override
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) { return c.reduce(identity, operator); }
		@Override
		public double reduce(DoubleDoubleUnaryOperator operator) { return c.reduce(operator); }
		@Override
		public double findFirst(DoublePredicate filter) { return c.findFirst(filter); }
		@Override
		public int count(DoublePredicate filter) { return c.count(filter); }
	}
	
	/**
	 * Empty Collection implementation for the empty collection function
	 */
	public static class EmptyCollection extends AbstractDoubleCollection {
		@Override
		public boolean add(double o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(double[] e, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(double o) { return false; }
		@Override
		public boolean containsAll(DoubleCollection c) { return c.isEmpty(); }
		@Override
		public boolean containsAny(DoubleCollection c) { return false; }
		@Override
		@Deprecated
		public boolean containsAny(Collection<?> c) { return false; }
		@Override
		@Deprecated
		public boolean containsAll(Collection<?> c) { return c.isEmpty(); }
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
		public boolean removeIf(Predicate<? super Double> filter) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remDouble(double o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean removeAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean retainAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remIf(DoublePredicate filter){ throw new UnsupportedOperationException(); }
		@Override
		public Object[] toArray() { return ObjectArrays.EMPTY_ARRAY; }
		@Override
		public <T> T[] toArray(T[] a) {
			if(a != null && a.length > 0)
				a[0] = null;
			return a;
		}
		
		@Override
		public double[] toDoubleArray() { return DoubleArrays.EMPTY_ARRAY; }
		@Override
		public double[] toDoubleArray(double[] a) {
			if(a != null && a.length > 0)
				a[0] = 0D;
			return a;
		}
		
		@Override
		public DoubleIterator iterator() { return DoubleIterators.empty(); }
		@Override
		public void clear() {}
		@Override
		public int size() { return 0; }
		@Override
		public EmptyCollection copy() { return this; }
	}
}