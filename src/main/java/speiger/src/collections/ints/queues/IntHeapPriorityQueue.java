package speiger.src.collections.ints.queues;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Heap base Priority Queue implementation
 * It is a ArrayBased Alternative to TreeSets that has less object allocations
 */
public class IntHeapPriorityQueue extends AbstractIntPriorityQueue
{
	/** The Backing Array */
	protected transient int[] array = IntArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Sorter of the Array */
	protected IntComparator comparator;
	
	/**
	 * Default Constructor
	 */
	public IntHeapPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public IntHeapPriorityQueue(IntComparator comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public IntHeapPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public IntHeapPriorityQueue(int size, IntComparator comp) {
		if(size > 0) array = new int[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public IntHeapPriorityQueue(int[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public IntHeapPriorityQueue(int[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		IntArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public IntHeapPriorityQueue(int[] array, IntComparator comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public IntHeapPriorityQueue(int[] array, int size, IntComparator comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		comparator = comp;
		IntArrays.heapify(array, size, comp);
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public IntHeapPriorityQueue(IntCollection c) {
		array = c.toIntArray();
		size = c.size();
		IntArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public IntHeapPriorityQueue(IntCollection c, IntComparator comp) {
		array = c.toIntArray();
		size = c.size();
		comparator = comp;
		IntArrays.heapify(array, size, comp);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static IntHeapPriorityQueue wrap(int[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static IntHeapPriorityQueue wrap(int[] array, int size) {
		IntHeapPriorityQueue queue = new IntHeapPriorityQueue();
		queue.array = array;
		queue.size = size;
		IntArrays.heapify(array, size, null);
		return queue;
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static IntHeapPriorityQueue wrap(int[] array, IntComparator comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static IntHeapPriorityQueue wrap(int[] array, int size, IntComparator comp) {
		IntHeapPriorityQueue queue = new IntHeapPriorityQueue(comp);
		queue.array = array;
		queue.size = size;
		IntArrays.heapify(array, size, comp);
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
	public IntIterator iterator() {
		return new Iter();
	}
	
	@Override
	public void enqueue(int e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (array.length >> 1), SanityChecks.MAX_ARRAY_SIZE), size+1));
		array[size++] = e;
		IntArrays.shiftUp(array, size-1, comparator);
	}
	
	@Override
	public int dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		int value = array[0];
		array[0] = array[--size];
		if(size != 0) IntArrays.shiftDown(array, size, 0, comparator);
		return value;
	}
	
	@Override
	public int peek(int index) {
		if(index < 0 || index >= size) throw new NoSuchElementException();
		return array[index];
	}
	
	@Override
	public boolean removeFirst(int e) {
		for(int i = 0;i<size;i++)
			if(e == array[i]) return removeIndex(i);
		return false;
	}
	
	@Override
	public boolean removeLast(int e) {
		for(int i = size-1;i>=0;i--)
			if(e == array[i]) return removeIndex(i);
		return false;
	}
	
	@Override
	public void forEach(IntConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(dequeue());
	}
	
	@Override
	public <E> void forEach(E input, ObjectIntConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(input, dequeue());
	}
	
	@Override
	public boolean matchesAny(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public int reduce(int identity, IntIntUnaryOperator operator) {
		Objects.requireNonNull(operator);
		int state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.applyAsInt(state, array[i]);
		}
		return state;
	}
	
	@Override
	public int reduce(IntIntUnaryOperator operator) {
		Objects.requireNonNull(operator);
		int state = 0;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = array[i];
				continue;
			}
			state = operator.applyAsInt(state, array[i]);
		}
		return state;
	}
	
	@Override
	public int findFirst(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) {
				int data = array[i];
				removeIndex(i);
				return data;
			}
		}
		return 0;
	}
	
	@Override
	public int count(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) result++;
		}
		return result;
	}
	
	protected boolean removeIndex(int index) {
		array[index] = array[--size];
		if(size != index) IntArrays.shiftDown(array, size, index, comparator);
		return true;
	}
	
	@Override
	public void onChanged() {
		if(size <= 0) return;
		IntArrays.shiftDown(array, size, 0, comparator);
	}
	
	@Override
	public IntHeapPriorityQueue copy() {
		IntHeapPriorityQueue queue = new IntHeapPriorityQueue();
		queue.size = size;
		queue.comparator = comparator;
		queue.array = Arrays.copyOf(array, array.length);
		return queue;
	}
	
	@Override
	public IntComparator comparator() {
		return comparator;
	}
	
	@Override
	public int[] toIntArray(int[] input) {
		if(input == null || input.length < size()) input = new int[size()];
		System.arraycopy(array, 0, input, 0, size());
		return input;
	}
	
	private class Iter implements IntIterator {
		@Override
		public boolean hasNext() {
			return !isEmpty();
		}
		
		@Override
		public int nextInt() {
			return dequeue();
		}
	}
}