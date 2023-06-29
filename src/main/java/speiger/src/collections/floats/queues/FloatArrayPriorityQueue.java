package speiger.src.collections.floats.queues;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.utils.FloatArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Array Priority Queue, this is a very unoptimized implementation of the PriorityQueue for very specific usecases.
 * It allows for duplicated entries and works like {@link java.util.List#indexOf(Object)} search.
 * It is highly suggested to use HeapPriorityQueue otherwise, unless you know why you need this specific implementation
 */
public class FloatArrayPriorityQueue extends AbstractFloatPriorityQueue
{
	/** The Backing Array */
	protected transient float[] array = FloatArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Last known first index pointer */
	protected int firstIndex = -1;
	/** The Sorter of the Array */
	protected FloatComparator comparator;
	
	/**
	 * Default Constructor
	 */
	public FloatArrayPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public FloatArrayPriorityQueue(FloatComparator comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public FloatArrayPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public FloatArrayPriorityQueue(int size, FloatComparator comp) {
		if(size < 0) throw new IllegalAccessError("Size has to be 0 or positive");
		if(size > 0) array = new float[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public FloatArrayPriorityQueue(float[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public FloatArrayPriorityQueue(float[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public FloatArrayPriorityQueue(float[] array, FloatComparator comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public FloatArrayPriorityQueue(float[] array, int size, FloatComparator comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		this.comparator = comp;
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public FloatArrayPriorityQueue(FloatCollection c) {
		array = c.toFloatArray();
		size = c.size();
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public FloatArrayPriorityQueue(FloatCollection c, FloatComparator comp) {
		array = c.toFloatArray();
		size = c.size();
		comparator = comp;
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @return a ArrayPriorityQueue containing the original input array
	 */
	public static FloatArrayPriorityQueue wrap(float[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @return a ArrayPriorityQueue containing the original input array
	 */
	public static FloatArrayPriorityQueue wrap(float[] array, int size) {
		FloatArrayPriorityQueue queue = new FloatArrayPriorityQueue();
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
	public static FloatArrayPriorityQueue wrap(float[] array, FloatComparator comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a ArrayPriorityQueue containing the original input array
	 */
	public static FloatArrayPriorityQueue wrap(float[] array, int size, FloatComparator comp) {
		FloatArrayPriorityQueue queue = new FloatArrayPriorityQueue(comp);
		queue.array = array;
		queue.size = size;
		return queue;
	}
	
	@Override
	public void enqueue(float e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (long)(array.length >> 1), (long)SanityChecks.MAX_ARRAY_SIZE), size+1));
		if(firstIndex != -1){
			int compare = comparator == null ? Float.compare(e, array[firstIndex]) : comparator.compare(e, array[firstIndex]);
			if(compare < 0) firstIndex = size;
			else if(compare > 0) firstIndex = -1;
		}
		array[size++] = e;
	}
	
	@Override
	public float dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		int index = findFirstIndex();
		float value = array[index];
		if(index != --size) System.arraycopy(array, index+1, array, index, size - index);
		firstIndex = -1;
		return value;
	}
	
	@Override
	public float first() {
		if(isEmpty()) throw new NoSuchElementException();
		if(firstIndex == -1) findFirstIndex();
		return array[firstIndex];
	}
	
	@Override
	public float peek(int index) {
		if(index < 0 || index >= size) throw new NoSuchElementException();
		return array[index];
	}
	
	@Override
	public boolean contains(float e) {
		for(int i = 0;i<size;i++)
			if(Float.floatToIntBits(e) == Float.floatToIntBits(array[i])) return true;
		return false;
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
	public void forEach(FloatConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(dequeue());
	}
	
	@Override
	public void forEachIndexed(IntFloatConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(i, dequeue());
	}
	
	@Override
	public <E> void forEach(E input, ObjectFloatConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(input, dequeue());
	}
	
	@Override
	public boolean matchesAny(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.test(array[i])) return false;
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
	public float findFirst(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) {
				float data = array[i];
				removeIndex(i);
				return data;
			}
		}
		return 0F;
	}
	
	@Override
	public int count(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) result++;
		}
		return result;
	}
	
	@Override
	public FloatIterator iterator() {
		return new Iter();
	}
	
	@Override
	public FloatArrayPriorityQueue copy() {
		FloatArrayPriorityQueue queue = new FloatArrayPriorityQueue();
		queue.firstIndex = firstIndex;
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
	
	protected int findFirstIndex() {
		if(firstIndex == -1) {
			int index = size-1;
			float value = array[index];
			if(comparator == null) {
				for(int i = index;i>=0;i--) {
					if(Float.compare(array[i], value) < 0) 
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