package speiger.src.collections.doubles.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.DoublePredicate;

import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.ints.functions.consumer.IntDoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.doubles.utils.DoubleArrays;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.doubles.utils.DoubleIterators;

import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashSet
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys.
 * Extra to that there is a couple quality of life functions provided
 */
public class ImmutableDoubleOpenHashSet extends AbstractDoubleSet implements DoubleOrderedSet
{
	/** The Backing keys array */
	protected transient double[] keys;
	/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
	protected transient long[] links;
	/** If a null value is present */
	protected transient boolean containsNull;
	/** Index of the Null Value */
	protected transient int nullIndex;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	/** Amount of Elements stored in the HashSet */
	protected int size;
	/** The First Index in the Map */
	protected int firstIndex = -1;
	/** The Last Index in the Map */
	protected int lastIndex = -1;
	
	/**
	 * Helper constructor to optimize the copying
	 * Only accessible through implementations
	 */
	protected ImmutableDoubleOpenHashSet() {}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 */
	public ImmutableDoubleOpenHashSet(double[] array) {
		this(array, 0, array.length, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableDoubleOpenHashSet(double[] array, float loadFactor) {
		this(array, 0, array.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ImmutableDoubleOpenHashSet(double[] array, int offset, int length) {
		this(array, offset, length, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ImmutableDoubleOpenHashSet(double[] array, int offset, int length, float loadFactor) {
		init(array, offset, length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	@Deprecated
	public ImmutableDoubleOpenHashSet(Collection<? extends Double> collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	@Deprecated
	public ImmutableDoubleOpenHashSet(Collection<? extends Double> collection, float loadFactor) {
		init(DoubleArrays.unwrap(collection.toArray(new Double[collection.size()])), 0, collection.size(), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	public ImmutableDoubleOpenHashSet(DoubleCollection collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableDoubleOpenHashSet(DoubleCollection collection, float loadFactor) {
		init(collection.toDoubleArray(new double[collection.size()]), 0, collection.size(), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ImmutableDoubleOpenHashSet(Iterator<Double> iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableDoubleOpenHashSet(Iterator<Double> iterator, float loadFactor) {
		this(DoubleIterators.wrap(iterator), loadFactor);
	}
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ImmutableDoubleOpenHashSet(DoubleIterator iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableDoubleOpenHashSet(DoubleIterator iterator, float loadFactor) {
		double[] array = DoubleArrays.pour(iterator);
		init(array, 0, array.length, loadFactor);
	}
	
	protected void init(double[] a, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		double[] newKeys = new double[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			double o = a[i];
			if(Double.doubleToLongBits(o) == 0) {
				if(!containsNull) {
					size++;
					if(prev != -1) {
						newLinks[prev] ^= ((newLinks[prev] ^ (newSize & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
						newLinks[newSize] ^= ((newLinks[newSize] ^ ((prev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
						prev = newSize;
					}
					else {
						prev = firstIndex = newSize;
						newLinks[newSize] = -1L;
					}
				}
				containsNull = true;
				continue;
			}
			boolean found = true;
			int pos = HashUtil.mix(Double.hashCode(o)) & newMask;
			double current = newKeys[pos];
			if(Double.doubleToLongBits(current) != 0) {
				if(Double.doubleToLongBits(current) == Double.doubleToLongBits(o)) continue;
				while(Double.doubleToLongBits((current = newKeys[pos = (++pos & newMask)])) != 0) {
					if(Double.doubleToLongBits(current) == Double.doubleToLongBits(o)) {
						found = false;
						break;
					}
				}
			}
			if(found) {
				size++;
				newKeys[pos] = o;
				if(prev != -1) {
					newLinks[prev] ^= ((newLinks[prev] ^ (pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
					newLinks[pos] ^= ((newLinks[pos] ^ ((prev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
					prev = pos;
				}
				else {
					prev = firstIndex = pos;
					newLinks[pos] = -1L;
				}
			}
		}
		nullIndex = newSize;
		mask = newMask;
		keys = newKeys;
		links = newLinks;
		lastIndex = prev;
		if(prev != -1) newLinks[prev] |= 0xFFFFFFFFL;
	}
	
	@Override
	public boolean add(double o) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Double> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(DoubleCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAndMoveToFirst(double o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAndMoveToLast(double o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(double o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(double o) { throw new UnsupportedOperationException(); }
	
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
	public double firstDouble() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public double pollFirstDouble() { throw new UnsupportedOperationException(); }
	
	@Override
	public double lastDouble() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public double pollLastDouble() { throw new UnsupportedOperationException(); }
	@Override
	public boolean remove(Object o) { throw new UnsupportedOperationException(); }

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
	public boolean remove(double o) { throw new UnsupportedOperationException(); }
	
	@Override
	public void forEach(DoubleConsumer action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public void forEachIndexed(IntDoubleConsumer action) {
		Objects.requireNonNull(action);
		int count = 0;
		int index = firstIndex;
		while(index != -1) {
			action.accept(count++, keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(input, keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean matchesAny(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.test(keys[index])) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.test(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(!filter.test(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = identity;
		int index = firstIndex;
		while(index != -1) {
			state = operator.applyAsDouble(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public double reduce(DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = 0D;
		boolean empty = true;
		int index = firstIndex;
		while(index != -1) {
			if(empty) {
				state = keys[index];
				empty = false;
			}
			else state = operator.applyAsDouble(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public double findFirst(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.test(keys[index])) return keys[index];
			index = (int)links[index];
		}
		return 0D;
	}
	
	@Override
	public int count(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		int index = firstIndex;
		while(index != -1) {
			if(filter.test(keys[index])) result++;
			index = (int)links[index];
		}
		return result;
	}
	
	@Override
	public DoubleListIterator iterator() {
		return new SetIterator();
	}
	
	@Override
	public DoubleBidirectionalIterator iterator(double fromElement) {
		return new SetIterator(fromElement);
	}
	
	@Override
	public double[] toDoubleArray(double[] a) {
		if(a == null || a.length < size()) a = new double[size()];
		for(int i = 0, index = firstIndex;index != -1;i++,index = (int)links[index]) {
			a[i] = keys[index];
		}
		if (a.length > size) a[size] = 0D;
		return a;
	}
	
	@Override
	@Deprecated
	public Object[] toArray() {
		if(isEmpty()) return ObjectArrays.EMPTY_ARRAY;
		Object[] obj = new Object[size()];
		for(int i = 0, index = firstIndex;index != -1;i++,index = (int)links[index]) {
			obj[i] = Double.valueOf(keys[index]);
		}
		return obj;
	}
	
	@Override
	@Deprecated
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size()];
		else if(a.length < size()) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size());
		for(int i = 0, index = firstIndex;index != -1;i++,index = (int)links[index]) {
			a[i] = (E)Double.valueOf(keys[index]);
		}
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public ImmutableDoubleOpenHashSet copy() {
		ImmutableDoubleOpenHashSet set = new ImmutableDoubleOpenHashSet();
		set.containsNull = containsNull;
		set.firstIndex = firstIndex;
		set.lastIndex = lastIndex;
		set.size = size;
		set.mask = mask;
		set.nullIndex = nullIndex;
		set.keys = Arrays.copyOf(keys, keys.length);
		set.links = Arrays.copyOf(links, links.length);
		return set;
	}
	
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	
	@Override
	public int size() {
		return size;
	}

	private class SetIterator implements DoubleListIterator {
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		SetIterator() {
			next = firstIndex;
		}
		
		SetIterator(double from) {
			if(Double.doubleToLongBits(from) == 0) {
				if(containsNull) {
					next = (int) links[nullIndex];
					previous = nullIndex;
				}
				else throw new NoSuchElementException("The null element is not in the set");
			}
			else if(Double.doubleToLongBits(keys[lastIndex]) == Double.doubleToLongBits(from)) {
				previous = lastIndex;
				index = size;
			}
			else {
				int pos = HashUtil.mix(Double.hashCode(from)) & mask;
				while(Double.doubleToLongBits(keys[pos]) != 0) {
					if(Double.doubleToLongBits(keys[pos]) == Double.doubleToLongBits(from)) {
						next = (int)links[pos];
						previous = pos;
						break;
					}
					pos = ++pos & mask;
				}
				if(previous == -1 && next == -1)
					throw new NoSuchElementException("The element was not found");
			}
		}
		
		@Override
		public int skip(int amount) {
			int result = 0;
			while(next != -1 && result != amount) {
				current = previous = next;
				next = (int)(links[current]);
				result++;
			}
			if(index >= 0) index+=result;
			return result;
		}
		
		@Override
		public int back(int amount) {
			int result = 0;
			while(previous != -1 && result != amount) {
				current = next = previous;
				previous = (int)(links[current] >> 32);
				result++;
			}
			if(index >= 0) index-=result;
			return result;
		}
		
		@Override
		public boolean hasNext() {
			return next != -1;
		}

		@Override
		public boolean hasPrevious() {
			return previous != -1;
		}
		
		@Override
		public int nextIndex() {
			ensureIndexKnown();
			return index;
		}
		
		@Override
		public int previousIndex() {
			ensureIndexKnown();
			return index - 1;
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }
		
		@Override
		public double previousDouble() {
			if(!hasPrevious()) throw new NoSuchElementException();
			current = next = previous;
			previous = (int)(links[current] >> 32);
			if(index >= 0) index--;
			return keys[current];
		}

		@Override
		public double nextDouble() {
			if(!hasNext()) throw new NoSuchElementException();
			current = previous = next;
			next = (int)(links[current]);
			if(index >= 0) index++;
			return keys[current];
		}
		
		private void ensureIndexKnown() {
			if(index == -1) {
				if(previous == -1) {
					index = 0;
				}
				else if(next == -1) {
					index = size;
				}
				else {
					index = 1;
					for(int pos = firstIndex;pos != previous;pos = (int)links[pos], index++);
				}
			}
		}
		
		@Override
		public void set(double e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(double e) { throw new UnsupportedOperationException(); }
	}
}