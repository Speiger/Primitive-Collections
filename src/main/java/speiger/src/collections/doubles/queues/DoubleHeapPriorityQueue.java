package speiger.src.collections.doubles.queues;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.utils.DoubleArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Heap base Priority Queue implementation
 * It is a ArrayBased Alternative to TreeSets that has less object allocations
 */
public class DoubleHeapPriorityQueue extends AbstractDoublePriorityQueue
{
	/** The Backing Array */
	protected transient double[] array = DoubleArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Sorter of the Array */
	protected DoubleComparator comparator;
	
	/**
	 * Default Constructor
	 */
	public DoubleHeapPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public DoubleHeapPriorityQueue(DoubleComparator comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public DoubleHeapPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public DoubleHeapPriorityQueue(int size, DoubleComparator comp) {
		if(size > 0) array = new double[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public DoubleHeapPriorityQueue(double[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public DoubleHeapPriorityQueue(double[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		DoubleArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public DoubleHeapPriorityQueue(double[] array, DoubleComparator comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public DoubleHeapPriorityQueue(double[] array, int size, DoubleComparator comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		comparator = comp;
		DoubleArrays.heapify(array, size, comp);
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public DoubleHeapPriorityQueue(DoubleCollection c) {
		array = c.toDoubleArray();
		size = c.size();
		DoubleArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public DoubleHeapPriorityQueue(DoubleCollection c, DoubleComparator comp) {
		array = c.toDoubleArray();
		size = c.size();
		comparator = comp;
		DoubleArrays.heapify(array, size, comp);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static DoubleHeapPriorityQueue wrap(double[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static DoubleHeapPriorityQueue wrap(double[] array, int size) {
		DoubleHeapPriorityQueue queue = new DoubleHeapPriorityQueue();
		queue.array = array;
		queue.size = size;
		DoubleArrays.heapify(array, size, null);
		return queue;
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static DoubleHeapPriorityQueue wrap(double[] array, DoubleComparator comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static DoubleHeapPriorityQueue wrap(double[] array, int size, DoubleComparator comp) {
		DoubleHeapPriorityQueue queue = new DoubleHeapPriorityQueue(comp);
		queue.array = array;
		queue.size = size;
		DoubleArrays.heapify(array, size, comp);
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
	public DoubleIterator iterator() {
		return new Iter();
	}
	
	@Override
	public void enqueue(double e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (array.length >> 1), SanityChecks.MAX_ARRAY_SIZE), size+1));
		array[size++] = e;
		DoubleArrays.shiftUp(array, size-1, comparator);
	}
	
	@Override
	public double dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		double value = array[0];
		array[0] = array[--size];
		if(size != 0) DoubleArrays.shiftDown(array, size, 0, comparator);
		return value;
	}
	
	@Override
	public double peek(int index) {
		if(index < 0 || index >= size) throw new NoSuchElementException();
		return array[index];
	}
	
	@Override
	public boolean removeFirst(double e) {
		for(int i = 0;i<size;i++)
			if(Double.doubleToLongBits(e) == Double.doubleToLongBits(array[i])) return removeIndex(i);
		return false;
	}
	
	@Override
	public boolean removeLast(double e) {
		for(int i = size-1;i>=0;i--)
			if(Double.doubleToLongBits(e) == Double.doubleToLongBits(array[i])) return removeIndex(i);
		return false;
	}
	
	@Override
	public void forEach(DoubleConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(dequeue());
	}
	
	@Override
	public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(input, dequeue());
	}
	
	@Override
	public boolean matchesAny(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.applyAsDouble(state, array[i]);
		}
		return state;
	}
	
	@Override
	public double reduce(DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = 0D;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = array[i];
				continue;
			}
			state = operator.applyAsDouble(state, array[i]);
		}
		return state;
	}
	
	@Override
	public double findFirst(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) {
				double data = array[i];
				removeIndex(i);
				return data;
			}
		}
		return 0D;
	}
	
	@Override
	public int count(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) result++;
		}
		return result;
	}
	
	protected boolean removeIndex(int index) {
		array[index] = array[--size];
		if(size != index) DoubleArrays.shiftDown(array, size, index, comparator);
		return true;
	}
	
	@Override
	public void onChanged() {
		if(size <= 0) return;
		DoubleArrays.shiftDown(array, size, 0, comparator);
	}
	
	@Override
	public DoubleHeapPriorityQueue copy() {
		DoubleHeapPriorityQueue queue = new DoubleHeapPriorityQueue();
		queue.size = size;
		queue.comparator = comparator;
		queue.array = Arrays.copyOf(array, array.length);
		return queue;
	}
	
	@Override
	public DoubleComparator comparator() {
		return comparator;
	}
	
	@Override
	public double[] toDoubleArray(double[] input) {
		if(input == null || input.length < size()) input = new double[size()];
		System.arraycopy(array, 0, input, 0, size());
		return input;
	}
	
	private class Iter implements DoubleIterator {
		@Override
		public boolean hasNext() {
			return !isEmpty();
		}
		
		@Override
		public double nextDouble() {
			return dequeue();
		}
	}
}