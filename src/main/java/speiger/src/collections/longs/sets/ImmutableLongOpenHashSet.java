package speiger.src.collections.longs.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.lists.LongListIterator;
import speiger.src.collections.longs.utils.LongArrays;
import speiger.src.collections.longs.utils.LongIterators;

import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashSet
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys.
 * Extra to that there is a couple quality of life functions provided
 */
public class ImmutableLongOpenHashSet extends AbstractLongSet implements LongOrderedSet
{
	/** The Backing keys array */
	protected transient long[] keys;
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
	protected ImmutableLongOpenHashSet() {}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 */
	public ImmutableLongOpenHashSet(long[] array) {
		this(array, 0, array.length, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableLongOpenHashSet(long[] array, float loadFactor) {
		this(array, 0, array.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ImmutableLongOpenHashSet(long[] array, int offset, int length) {
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
	public ImmutableLongOpenHashSet(long[] array, int offset, int length, float loadFactor) {
		init(array, offset, length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	@Deprecated
	public ImmutableLongOpenHashSet(Collection<? extends Long> collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	@Deprecated
	public ImmutableLongOpenHashSet(Collection<? extends Long> collection, float loadFactor) {
		init(LongArrays.unwrap(collection.toArray(new Long[collection.size()])), 0, collection.size(), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 */
	public ImmutableLongOpenHashSet(LongCollection collection) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableLongOpenHashSet(LongCollection collection, float loadFactor) {
		init(collection.toLongArray(new long[collection.size()]), 0, collection.size(), loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ImmutableLongOpenHashSet(Iterator<Long> iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableLongOpenHashSet(Iterator<Long> iterator, float loadFactor) {
		this(LongIterators.wrap(iterator), loadFactor);
	}
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ImmutableLongOpenHashSet(LongIterator iterator) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableLongOpenHashSet(LongIterator iterator, float loadFactor) {
		long[] array = LongArrays.pour(iterator);
		init(array, 0, array.length, loadFactor);
	}
	
	protected void init(long[] a, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		long[] newKeys = new long[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			long o = a[i];
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
			int pos = HashUtil.mix(Long.hashCode(o)) & newMask;
			long current = newKeys[pos];
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
	public boolean add(long o) { throw new UnsupportedOperationException(); }
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Long> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(LongCollection c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAndMoveToFirst(long o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAndMoveToLast(long o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(long o) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(long o) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean contains(Object o) {
		if(o == null) return false;
		if(o instanceof Long && ((Long)o).longValue() == 0L) return containsNull;
		int pos = HashUtil.mix(o.hashCode()) & mask;
		long current = keys[pos];
		if(current == 0) return false;
		if(Objects.equals(o, Long.valueOf(current))) return true;
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == 0) return false;
			else if(Objects.equals(o, Long.valueOf(current))) return true;
		}
	}
	
	@Override
	public long firstLong() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public long pollFirstLong() { throw new UnsupportedOperationException(); }
	
	@Override
	public long lastLong() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public long pollLastLong() { throw new UnsupportedOperationException(); }
	@Override
	public boolean remove(Object o) { throw new UnsupportedOperationException(); }

	@Override
	public boolean contains(long o) {
		if(o == 0) return containsNull;
		int pos = HashUtil.mix(Long.hashCode(o)) & mask;
		long current = keys[pos];
		if(current == 0) return false;
		if(current == o) return true;
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == 0) return false;
			else if(current == o) return true;
		}
	}
	
	@Override
	public boolean remove(long o) { throw new UnsupportedOperationException(); }
	
	@Override
	public void forEach(LongConsumer action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectLongConsumer<E> action) {
		Objects.requireNonNull(action);
		int index = firstIndex;
		while(index != -1) {
			action.accept(input, keys[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean matchesAny(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(!filter.get(keys[index])) return false;
			index = (int)links[index];
		}
		return true;
	}
	
	@Override
	public long reduce(long identity, LongLongUnaryOperator operator) {
		Objects.requireNonNull(operator);
		long state = identity;
		int index = firstIndex;
		while(index != -1) {
			state = operator.applyAsLong(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public long reduce(LongLongUnaryOperator operator) {
		Objects.requireNonNull(operator);
		long state = 0L;
		boolean empty = true;
		int index = firstIndex;
		while(index != -1) {
			if(empty) {
				state = keys[index];
				empty = false;
			}
			else state = operator.applyAsLong(state, keys[index]);
			index = (int)links[index];
		}
		return state;
	}
	
	@Override
	public long findFirst(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) return keys[index];
			index = (int)links[index];
		}
		return 0L;
	}
	
	@Override
	public int count(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		int index = firstIndex;
		while(index != -1) {
			if(filter.get(keys[index])) result++;
			index = (int)links[index];
		}
		return result;
	}
	
	@Override
	public LongListIterator iterator() {
		return new SetIterator();
	}
	
	@Override
	public LongBidirectionalIterator iterator(long fromElement) {
		return new SetIterator(fromElement);
	}
	
	@Override
	public ImmutableLongOpenHashSet copy() {
		ImmutableLongOpenHashSet set = new ImmutableLongOpenHashSet();
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

	private class SetIterator implements LongListIterator {
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		SetIterator() {
			next = firstIndex;
		}
		
		SetIterator(long from) {
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
				int pos = HashUtil.mix(Long.hashCode(from)) & mask;
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
		public long previousLong() {
			if(!hasPrevious()) throw new NoSuchElementException();
			current = previous;
			previous = (int)(links[current] >> 32);
			next = current;
			if(index >= 0) index--;
			return keys[current];
		}

		@Override
		public long nextLong() {
			if(!hasNext()) throw new NoSuchElementException();
			current = next;
			next = (int)(links[current]);
			previous = current;
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
		public void set(long e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(long e) { throw new UnsupportedOperationException(); }
	}
}