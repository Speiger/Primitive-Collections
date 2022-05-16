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
 * A Array Priority Queue, this is a very unoptimized implementation of the PriorityQueue for very specific usecases.
 * It allows for duplicated entries and works like {@link java.util.List#indexOf(Object)} search.
 * It is highly suggested to use HeapPriorityQueue otherwise, unless you know why you need this specific implementation
 */
public class DoubleArrayPriorityQueue extends AbstractDoublePriorityQueue
{
	/** The Backing Array */
	protected transient double[] array = DoubleArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Last known first index pointer */
	protected int firstIndex = -1;
	/** The Sorter of the Array */
	protected DoubleComparator comparator;
	
	/**
	 * Default Constructor
	 */
	public DoubleArrayPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public DoubleArrayPriorityQueue(DoubleComparator comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public DoubleArrayPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public DoubleArrayPriorityQueue(int size, DoubleComparator comp) {
		if(size < 0) throw new IllegalAccessError("Size has to be 0 or positive");
		if(size > 0) array = new double[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public DoubleArrayPriorityQueue(double[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public DoubleArrayPriorityQueue(double[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public DoubleArrayPriorityQueue(double[] array, DoubleComparator comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public DoubleArrayPriorityQueue(double[] array, int size, DoubleComparator comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		this.comparator = comp;
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public DoubleArrayPriorityQueue(DoubleCollection c) {
		array = c.toDoubleArray();
		size = c.size();
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public DoubleArrayPriorityQueue(DoubleCollection c, DoubleComparator comp) {
		array = c.toDoubleArray();
		size = c.size();
		comparator = comp;
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @return a ArrayPriorityQueue containing the original input array
	 */
	public static DoubleArrayPriorityQueue wrap(double[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @return a ArrayPriorityQueue containing the original input array
	 */
	public static DoubleArrayPriorityQueue wrap(double[] array, int size) {
		DoubleArrayPriorityQueue queue = new DoubleArrayPriorityQueue();
		queue.array = array;
		queue.size = size;
		return queue;
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a ArrayPriorityQueue containing the original input array
	 */
	public static DoubleArrayPriorityQueue wrap(double[] array, DoubleComparator comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a ArrayPriorityQueue containing the original input array
	 */
	public static DoubleArrayPriorityQueue wrap(double[] array, int size, DoubleComparator comp) {
		DoubleArrayPriorityQueue queue = new DoubleArrayPriorityQueue(comp);
		queue.array = array;
		queue.size = size;
		return queue;
	}
	
	@Override
	public void enqueue(double e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (array.length >> 1), SanityChecks.MAX_ARRAY_SIZE), size+1));
		if(firstIndex != -1){
			int compare = comparator == null ? Double.compare(e, array[firstIndex]) : comparator.compare(e, array[firstIndex]);
			if(compare < 0) firstIndex = size;
			else if(compare > 0) firstIndex = -1;
		}
		array[size++] = e;
	}
	
	@Override
	public double dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		int index = findFirstIndex();
		double value = array[index];
		if(index != --size) System.arraycopy(array, index+1, array, index, size - index);
		firstIndex = -1;
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
	
	protected boolean removeIndex(int index) {
		if(index != --size) System.arraycopy(array, index+1, array, index, size - index);
		if(index == firstIndex) firstIndex = -1;
		else if(firstIndex != -1 && index >= firstIndex) firstIndex--;
		return true;
	}
	
	@Override
	public void onChanged() {
		firstIndex = -1;
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
	
	@Override
	public DoubleIterator iterator() {
		return new Iter();
	}
	
	@Override
	public DoubleArrayPriorityQueue copy() {
		DoubleArrayPriorityQueue queue = new DoubleArrayPriorityQueue();
		queue.firstIndex = firstIndex;
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
	
	protected int findFirstIndex() {
		if(firstIndex == -1) {
			int index = size-1;
			double value = array[index];
			if(comparator == null) {
				for(int i = index;i>=0;i--) {
					if(Double.compare(array[i], value) < 0) 
						value = array[index = i];
				}
			}
			else {
				for(int i = index;i>=0;i--) {
					if(comparator.compare(array[i], value) < 0) 
						value = array[index = i];
				}
			}
			firstIndex = index;
		}
		return firstIndex;
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