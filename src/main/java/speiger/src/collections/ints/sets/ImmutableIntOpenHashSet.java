package speiger.src.collections.ints.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntPredicate;

import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.ints.utils.IntIterators;

import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashSet
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys.
 * Extra to that there is a couple quality of life functions provided
 */
public class ImmutableIntOpenHashSet extends AbstractIntSet implements IntOrderedSet
{
	/** The Backing keys array */
	protected transient int[] keys;
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
	protected ImmutableIntOpenHashSet() {}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 */
	public ImmutableIntOpenHashSet(int[] array) {
		this(array, 0, array.length, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableIntOpenHashSet(int[] array, float loadFactor) {
		this(array, 0, array.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ImmutableIntOpenHashSet(int[] array, int offset, int length) {
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
	public ImmutableIntOpenHashSet(int[] array, int offset, int length, float loadFactor) {
		init(array, offset, length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	@Deprecated
	public ImmutableIntOpenHashSet(Collection<? extends Integer> collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	@Deprecated
	public ImmutableIntOpenHashSet(Collection<? extends Integer> collection, float loadFactor) {
		init(IntArrays.unwrap(collection.toArray(new Integer[collection.size()])), 0, collection.size(), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	public ImmutableIntOpenHashSet(IntCollection collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableIntOpenHashSet(IntCollection collection, float loadFactor) {
		init(collection.toIntArray(new int[collection.size()]), 0, collection.size(), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ImmutableIntOpenHashSet(Iterator<Integer> iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableIntOpenHashSet(Iterator<Integer> iterator, float loadFactor) {
		this(IntIterators.wrap(iterator), loadFactor);
	}
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ImmutableIntOpenHashSet(IntIterator iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableIntOpenHashSet(IntIterator iterator, float loadFactor) {
		int[] array = IntArrays.pour(iterator);
		init(array, 0, array.length, loadFactor);
	}
	
	protected void init(int[] a, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		int[] newKeys = new int[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			int o = a[i];
			if(o == 0) {
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
			int pos = HashUtil.mix(Integer.hashCode(o)) & newMask;
			int current = newKeys[pos];
			if(current != 0) {
				if(current == o) continue;
				while((current = newKeys[pos = (++pos & newMask)]) != 0) {
					if(current == o) {
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
	public boolean add(int o) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Integer> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(IntCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAndMoveToFirst(int o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAndMoveToLast(int o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(int o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(int o) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean contains(Object o) {
		if(o == null) return false;
		if(o instanceof Integer && ((Integer)o).intValue() == 0) return containsNull;
		int pos = HashUtil.mix(o.hashCode()) & mask;
		int current = keys[pos];
		if(current == 0) return false;
		if(Objects.equals(o, Integer.valueOf(current))) return true;
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == 0) return false;
			else if(Objects.equals(o, Integer.valueOf(current))) return true;
		}
	}
	
	@Override
	public int firstInt() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public int pollFirstInt() { throw new UnsupportedOperationException(); }
	
	@Override
	public int lastInt() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public int pollLastInt() { throw new UnsupportedOperationException(); }
	@Override
	public boolean remove(Object o) { throw new UnsupportedOperationException(); }

	@Override
	public boolean contains(int o) {
		if(o == 0) return containsNull;
		int pos = HashUtil.mix(Integer.hashCode(o)) & mask;
		int current = keys[pos];
		if(current == 0) return false;
		if(current == o) return true;
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == 0) return false;
			else if(current == o) return true;
		}
	}
	
	@Override
	public boolean remove(int o) { throw new UnsupportedOperationException(); }
	
	@Override
	public void forEach(IntConsumer action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public void forEachIndexed(IntIntConsumer action) {
		Objects.requireNonNull(action);
		int count = 0;
		int index = firstIndex;
		while(index != -1) {
			action.accept(count++, keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectIntConsumer<E> action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(input, keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean matchesAny(IntPredicate filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.test(keys[index])) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(IntPredicate filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.test(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(IntPredicate filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(!filter.test(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public int reduce(int identity, IntIntUnaryOperator operator) {
		Objects.requireNonNull(operator);
		int state = identity;
		int index = firstIndex;
		while(index != -1) {
			state = operator.applyAsInt(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public int reduce(IntIntUnaryOperator operator) {
		Objects.requireNonNull(operator);
		int state = 0;
		boolean empty = true;
		int index = firstIndex;
		while(index != -1) {
			if(empty) {
				state = keys[index];
				empty = false;
			}
			else state = operator.applyAsInt(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public int findFirst(IntPredicate filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.test(keys[index])) return keys[index];
			index = (int)links[index];
		}
		return 0;
	}
	
	@Override
	public int count(IntPredicate filter) {
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
	public IntListIterator iterator() {
		return new SetIterator();
	}
	
	@Override
	public IntBidirectionalIterator iterator(int fromElement) {
		return new SetIterator(fromElement);
	}
	
	@Override
	public int[] toIntArray(int[] a) {
		if(a == null || a.length < size()) a = new int[size()];
		for(int i = 0, index = firstIndex;index != -1;i++,index = (int)links[index]) {
			a[i] = keys[index];
		}
		if (a.length > size) a[size] = 0;
		return a;
	}
	
	@Override
	@Deprecated
	public Object[] toArray() {
		if(isEmpty()) return ObjectArrays.EMPTY_ARRAY;
		Object[] obj = new Object[size()];
		for(int i = 0, index = firstIndex;index != -1;i++,index = (int)links[index]) {
			obj[i] = Integer.valueOf(keys[index]);
		}
		return obj;
	}
	
	@Override
	@Deprecated
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size()];
		else if(a.length < size()) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size());
		for(int i = 0, index = firstIndex;index != -1;i++,index = (int)links[index]) {
			a[i] = (E)Integer.valueOf(keys[index]);
		}
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public ImmutableIntOpenHashSet copy() {
		ImmutableIntOpenHashSet set = new ImmutableIntOpenHashSet();
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

	private class SetIterator implements IntListIterator {
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		SetIterator() {
			next = firstIndex;
		}
		
		SetIterator(int from) {
			if(from == 0) {
				if(containsNull) {
					next = (int) links[nullIndex];
					previous = nullIndex;
				}
				else throw new NoSuchElementException("The null element is not in the set");
			}
			else if(keys[lastIndex] == from) {
				previous = lastIndex;
				index = size;
			}
			else {
				int pos = HashUtil.mix(Integer.hashCode(from)) & mask;
				while(keys[pos] != 0) {
					if(keys[pos] == from) {
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
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			current = next = previous;
			previous = (int)(links[current] >> 32);
			if(index >= 0) index--;
			return keys[current];
		}

		@Override
		public int nextInt() {
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
		public void set(int e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(int e) { throw new UnsupportedOperationException(); }
	}
}