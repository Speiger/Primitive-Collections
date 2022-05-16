package speiger.src.collections.booleans.queues;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2BooleanFunction;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.utils.BooleanArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Heap base Priority Queue implementation
 * It is a ArrayBased Alternative to TreeSets that has less object allocations
 */
public class BooleanHeapPriorityQueue extends AbstractBooleanPriorityQueue
{
	/** The Backing Array */
	protected transient boolean[] array = BooleanArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Sorter of the Array */
	protected BooleanComparator comparator;
	
	/**
	 * Default Constructor
	 */
	public BooleanHeapPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public BooleanHeapPriorityQueue(BooleanComparator comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public BooleanHeapPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public BooleanHeapPriorityQueue(int size, BooleanComparator comp) {
		if(size > 0) array = new boolean[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public BooleanHeapPriorityQueue(boolean[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public BooleanHeapPriorityQueue(boolean[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		BooleanArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public BooleanHeapPriorityQueue(boolean[] array, BooleanComparator comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public BooleanHeapPriorityQueue(boolean[] array, int size, BooleanComparator comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		comparator = comp;
		BooleanArrays.heapify(array, size, comp);
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public BooleanHeapPriorityQueue(BooleanCollection c) {
		array = c.toBooleanArray();
		size = c.size();
		BooleanArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public BooleanHeapPriorityQueue(BooleanCollection c, BooleanComparator comp) {
		array = c.toBooleanArray();
		size = c.size();
		comparator = comp;
		BooleanArrays.heapify(array, size, comp);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static BooleanHeapPriorityQueue wrap(boolean[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static BooleanHeapPriorityQueue wrap(boolean[] array, int size) {
		BooleanHeapPriorityQueue queue = new BooleanHeapPriorityQueue();
		queue.array = array;
		queue.size = size;
		BooleanArrays.heapify(array, size, null);
		return queue;
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static BooleanHeapPriorityQueue wrap(boolean[] array, BooleanComparator comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static BooleanHeapPriorityQueue wrap(boolean[] array, int size, BooleanComparator comp) {
		BooleanHeapPriorityQueue queue = new BooleanHeapPriorityQueue(comp);
		queue.array = array;
		queue.size = size;
		BooleanArrays.heapify(array, size, comp);
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
	public BooleanIterator iterator() {
		return new Iter();
	}
	
	@Override
	public void enqueue(boolean e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (array.length >> 1), SanityChecks.MAX_ARRAY_SIZE), size+1));
		array[size++] = e;
		BooleanArrays.shiftUp(array, size-1, comparator);
	}
	
	@Override
	public boolean dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		boolean value = array[0];
		array[0] = array[--size];
		if(size != 0) BooleanArrays.shiftDown(array, size, 0, comparator);
		return value;
	}
	
	@Override
	public boolean peek(int index) {
		if(index < 0 || index >= size) throw new NoSuchElementException();
		return array[index];
	}
	
	@Override
	public boolean removeFirst(boolean e) {
		for(int i = 0;i<size;i++)
			if(e == array[i]) return removeIndex(i);
		return false;
	}
	
	@Override
	public boolean removeLast(boolean e) {
		for(int i = size-1;i>=0;i--)
			if(e == array[i]) return removeIndex(i);
		return false;
	}
	
	@Override
	public void forEach(BooleanConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(dequeue());
	}
	
	@Override
	public <E> void forEach(E input, ObjectBooleanConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(input, dequeue());
	}
	
	@Override
	public boolean matchesAny(Boolean2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Boolean2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Boolean2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) {
		Objects.requireNonNull(operator);
		boolean state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.applyAsBoolean(state, array[i]);
		}
		return state;
	}
	
	@Override
	public boolean reduce(BooleanBooleanUnaryOperator operator) {
		Objects.requireNonNull(operator);
		boolean state = false;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = array[i];
				continue;
			}
			state = operator.applyAsBoolean(state, array[i]);
		}
		return state;
	}
	
	@Override
	public boolean findFirst(Boolean2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) {
				boolean data = array[i];
				removeIndex(i);
				return data;
			}
		}
		return false;
	}
	
	@Override
	public int count(Boolean2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) result++;
		}
		return result;
	}
	
	protected boolean removeIndex(int index) {
		array[index] = array[--size];
		if(size != index) BooleanArrays.shiftDown(array, size, index, comparator);
		return true;
	}
	
	@Override
	public void onChanged() {
		if(size <= 0) return;
		BooleanArrays.shiftDown(array, size, 0, comparator);
	}
	
	@Override
	public BooleanHeapPriorityQueue copy() {
		BooleanHeapPriorityQueue queue = new BooleanHeapPriorityQueue();
		queue.size = size;
		queue.comparator = comparator;
		queue.array = Arrays.copyOf(array, array.length);
		return queue;
	}
	
	@Override
	public BooleanComparator comparator() {
		return comparator;
	}
	
	@Override
	public boolean[] toBooleanArray(boolean[] input) {
		if(input == null || input.length < size()) input = new boolean[size()];
		System.arraycopy(array, 0, input, 0, size());
		return input;
	}
	
	private class Iter implements BooleanIterator {
		@Override
		public boolean hasNext() {
			return !isEmpty();
		}
		
		@Override
		public boolean nextBoolean() {
			return dequeue();
		}
	}
}