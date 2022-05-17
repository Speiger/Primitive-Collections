package speiger.src.collections.floats.queues;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.floats.functions.function.Float2BooleanFunction;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.utils.FloatArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Heap base Priority Queue implementation
 * It is a ArrayBased Alternative to TreeSets that has less object allocations
 */
public class FloatHeapPriorityQueue extends AbstractFloatPriorityQueue
{
	/** The Backing Array */
	protected transient float[] array = FloatArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Sorter of the Array */
	protected FloatComparator comparator;
	
	/**
	 * Default Constructor
	 */
	public FloatHeapPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public FloatHeapPriorityQueue(FloatComparator comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public FloatHeapPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public FloatHeapPriorityQueue(int size, FloatComparator comp) {
		if(size > 0) array = new float[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public FloatHeapPriorityQueue(float[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public FloatHeapPriorityQueue(float[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		FloatArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public FloatHeapPriorityQueue(float[] array, FloatComparator comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public FloatHeapPriorityQueue(float[] array, int size, FloatComparator comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		comparator = comp;
		FloatArrays.heapify(array, size, comp);
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public FloatHeapPriorityQueue(FloatCollection c) {
		array = c.toFloatArray();
		size = c.size();
		FloatArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public FloatHeapPriorityQueue(FloatCollection c, FloatComparator comp) {
		array = c.toFloatArray();
		size = c.size();
		comparator = comp;
		FloatArrays.heapify(array, size, comp);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static FloatHeapPriorityQueue wrap(float[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static FloatHeapPriorityQueue wrap(float[] array, int size) {
		FloatHeapPriorityQueue queue = new FloatHeapPriorityQueue();
		queue.array = array;
		queue.size = size;
		FloatArrays.heapify(array, size, null);
		return queue;
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static FloatHeapPriorityQueue wrap(float[] array, FloatComparator comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static FloatHeapPriorityQueue wrap(float[] array, int size, FloatComparator comp) {
		FloatHeapPriorityQueue queue = new FloatHeapPriorityQueue(comp);
		queue.array = array;
		queue.size = size;
		FloatArrays.heapify(array, size, comp);
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
	public FloatIterator iterator() {
		return new Iter();
	}
	
	@Override
	public void enqueue(float e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (long)(array.length >> 1), (long)SanityChecks.MAX_ARRAY_SIZE), size+1));
		array[size++] = e;
		FloatArrays.shiftUp(array, size-1, comparator);
	}
	
	@Override
	public float dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		float value = array[0];
		array[0] = array[--size];
		if(size != 0) FloatArrays.shiftDown(array, size, 0, comparator);
		return value;
	}
	
	@Override
	public float peek(int index) {
		if(index < 0 || index >= size) throw new NoSuchElementException();
		return array[index];
	}
	
	@Override
	public boolean removeFirst(float e) {
		for(int i = 0;i<size;i++)
			if(Float.floatToIntBits(e) == Float.floatToIntBits(array[i])) return removeIndex(i);
		return false;
	}
	
	@Override
	public boolean removeLast(float e) {
		for(int i = size-1;i>=0;i--)
			if(Float.floatToIntBits(e) == Float.floatToIntBits(array[i])) return removeIndex(i);
		return false;
	}
	
	@Override
	public void forEach(FloatConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(dequeue());
	}
	
	@Override
	public <E> void forEach(E input, ObjectFloatConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(input, dequeue());
	}
	
	@Override
	public boolean matchesAny(Float2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Float2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Float2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public float reduce(float identity, FloatFloatUnaryOperator operator) {
		Objects.requireNonNull(operator);
		float state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.applyAsFloat(state, array[i]);
		}
		return state;
	}
	
	@Override
	public float reduce(FloatFloatUnaryOperator operator) {
		Objects.requireNonNull(operator);
		float state = 0F;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = array[i];
				continue;
			}
			state = operator.applyAsFloat(state, array[i]);
		}
		return state;
	}
	
	@Override
	public float findFirst(Float2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) {
				float data = array[i];
				removeIndex(i);
				return data;
			}
		}
		return 0F;
	}
	
	@Override
	public int count(Float2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) result++;
		}
		return result;
	}
	
	protected boolean removeIndex(int index) {
		array[index] = array[--size];
		if(size != index) FloatArrays.shiftDown(array, size, index, comparator);
		return true;
	}
	
	@Override
	public void onChanged() {
		if(size <= 0) return;
		FloatArrays.shiftDown(array, size, 0, comparator);
	}
	
	@Override
	public FloatHeapPriorityQueue copy() {
		FloatHeapPriorityQueue queue = new FloatHeapPriorityQueue();
		queue.size = size;
		queue.comparator = comparator;
		queue.array = Arrays.copyOf(array, array.length);
		return queue;
	}
	
	@Override
	public FloatComparator comparator() {
		return comparator;
	}
	
	@Override
	public float[] toFloatArray(float[] input) {
		if(input == null || input.length < size()) input = new float[size()];
		System.arraycopy(array, 0, input, 0, size());
		return input;
	}
	
	private class Iter implements FloatIterator {
		@Override
		public boolean hasNext() {
			return !isEmpty();
		}
		
		@Override
		public float nextFloat() {
			if(!hasNext()) throw new NoSuchElementException();
			return dequeue();
		}
	}
}