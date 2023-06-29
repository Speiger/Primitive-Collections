package speiger.src.collections.shorts.queues;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.ints.functions.consumer.IntShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.ShortPredicate;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.utils.ShortArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Heap base Priority Queue implementation
 * It is a ArrayBased Alternative to TreeSets that has less object allocations
 */
public class ShortHeapPriorityQueue extends AbstractShortPriorityQueue
{
	/** The Backing Array */
	protected transient short[] array = ShortArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Sorter of the Array */
	protected ShortComparator comparator;
	
	/**
	 * Default Constructor
	 */
	public ShortHeapPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public ShortHeapPriorityQueue(ShortComparator comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public ShortHeapPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public ShortHeapPriorityQueue(int size, ShortComparator comp) {
		if(size > 0) array = new short[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public ShortHeapPriorityQueue(short[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public ShortHeapPriorityQueue(short[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		ShortArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public ShortHeapPriorityQueue(short[] array, ShortComparator comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public ShortHeapPriorityQueue(short[] array, int size, ShortComparator comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		comparator = comp;
		ShortArrays.heapify(array, size, comp);
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public ShortHeapPriorityQueue(ShortCollection c) {
		array = c.toShortArray();
		size = c.size();
		ShortArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public ShortHeapPriorityQueue(ShortCollection c, ShortComparator comp) {
		array = c.toShortArray();
		size = c.size();
		comparator = comp;
		ShortArrays.heapify(array, size, comp);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static ShortHeapPriorityQueue wrap(short[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static ShortHeapPriorityQueue wrap(short[] array, int size) {
		ShortHeapPriorityQueue queue = new ShortHeapPriorityQueue();
		queue.array = array;
		queue.size = size;
		ShortArrays.heapify(array, size, null);
		return queue;
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static ShortHeapPriorityQueue wrap(short[] array, ShortComparator comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static ShortHeapPriorityQueue wrap(short[] array, int size, ShortComparator comp) {
		ShortHeapPriorityQueue queue = new ShortHeapPriorityQueue(comp);
		queue.array = array;
		queue.size = size;
		ShortArrays.heapify(array, size, comp);
		return queue;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public void clear() {
		size = 0;
	}
	
	@Override
	public ShortIterator iterator() {
		return new Iter();
	}
	
	@Override
	public void enqueue(short e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (long)(array.length >> 1), (long)SanityChecks.MAX_ARRAY_SIZE), size+1));
		array[size++] = e;
		ShortArrays.shiftUp(array, size-1, comparator);
	}
	
	@Override
	public short dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		short value = array[0];
		array[0] = array[--size];
		if(size != 0) ShortArrays.shiftDown(array, size, 0, comparator);
		return value;
	}
	
	@Override
	public short peek(int index) {
		if(index < 0 || index >= size) throw new NoSuchElementException();
		return array[index];
	}
	
	@Override
	public boolean contains(short e) {
		for(int i = 0;i<size;i++)
			if(e == array[i]) return true;
		return false;
	}
	
	@Override
	public boolean removeFirst(short e) {
		for(int i = 0;i<size;i++)
			if(e == array[i]) return removeIndex(i);
		return false;
	}
	
	@Override
	public boolean removeLast(short e) {
		for(int i = size-1;i>=0;i--)
			if(e == array[i]) return removeIndex(i);
		return false;
	}
	
	@Override
	public void forEach(ShortConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(dequeue());
	}
	
	@Override
	public void forEachIndexed(IntShortConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(i, dequeue());
	}
	
	@Override
	public <E> void forEach(E input, ObjectShortConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(input, dequeue());
	}
	
	@Override
	public boolean matchesAny(ShortPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(ShortPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(ShortPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.test(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public short reduce(short identity, ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.applyAsShort(state, array[i]);
		}
		return state;
	}
	
	@Override
	public short reduce(ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = (short)0;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = array[i];
				continue;
			}
			state = operator.applyAsShort(state, array[i]);
		}
		return state;
	}
	
	@Override
	public short findFirst(ShortPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) {
				short data = array[i];
				removeIndex(i);
				return data;
			}
		}
		return (short)0;
	}
	
	@Override
	public int count(ShortPredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) result++;
		}
		return result;
	}
	
	protected boolean removeIndex(int index) {
		array[index] = array[--size];
		if(size != index) ShortArrays.shiftDown(array, size, index, comparator);
		return true;
	}
	
	@Override
	public void onChanged() {
		if(size <= 0) return;
		ShortArrays.shiftDown(array, size, 0, comparator);
	}
	
	@Override
	public ShortHeapPriorityQueue copy() {
		ShortHeapPriorityQueue queue = new ShortHeapPriorityQueue();
		queue.size = size;
		queue.comparator = comparator;
		queue.array = Arrays.copyOf(array, array.length);
		return queue;
	}
	
	@Override
	public ShortComparator comparator() {
		return comparator;
	}
	
	@Override
	public short[] toShortArray(short[] input) {
		if(input == null || input.length < size()) input = new short[size()];
		System.arraycopy(array, 0, input, 0, size());
		return input;
	}
	
	private class Iter implements ShortIterator {
		@Override
		public boolean hasNext() {
			return !isEmpty();
		}
		
		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			return dequeue();
		}
	}
}