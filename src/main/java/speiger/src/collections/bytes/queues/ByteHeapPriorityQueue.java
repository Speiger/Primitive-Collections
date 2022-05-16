package speiger.src.collections.bytes.queues;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.utils.ByteArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Heap base Priority Queue implementation
 * It is a ArrayBased Alternative to TreeSets that has less object allocations
 */
public class ByteHeapPriorityQueue extends AbstractBytePriorityQueue
{
	/** The Backing Array */
	protected transient byte[] array = ByteArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Sorter of the Array */
	protected ByteComparator comparator;
	
	/**
	 * Default Constructor
	 */
	public ByteHeapPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public ByteHeapPriorityQueue(ByteComparator comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public ByteHeapPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public ByteHeapPriorityQueue(int size, ByteComparator comp) {
		if(size > 0) array = new byte[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public ByteHeapPriorityQueue(byte[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public ByteHeapPriorityQueue(byte[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		ByteArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public ByteHeapPriorityQueue(byte[] array, ByteComparator comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public ByteHeapPriorityQueue(byte[] array, int size, ByteComparator comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		comparator = comp;
		ByteArrays.heapify(array, size, comp);
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public ByteHeapPriorityQueue(ByteCollection c) {
		array = c.toByteArray();
		size = c.size();
		ByteArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public ByteHeapPriorityQueue(ByteCollection c, ByteComparator comp) {
		array = c.toByteArray();
		size = c.size();
		comparator = comp;
		ByteArrays.heapify(array, size, comp);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static ByteHeapPriorityQueue wrap(byte[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static ByteHeapPriorityQueue wrap(byte[] array, int size) {
		ByteHeapPriorityQueue queue = new ByteHeapPriorityQueue();
		queue.array = array;
		queue.size = size;
		ByteArrays.heapify(array, size, null);
		return queue;
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static ByteHeapPriorityQueue wrap(byte[] array, ByteComparator comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static ByteHeapPriorityQueue wrap(byte[] array, int size, ByteComparator comp) {
		ByteHeapPriorityQueue queue = new ByteHeapPriorityQueue(comp);
		queue.array = array;
		queue.size = size;
		ByteArrays.heapify(array, size, comp);
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
	public ByteIterator iterator() {
		return new Iter();
	}
	
	@Override
	public void enqueue(byte e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (array.length >> 1), SanityChecks.MAX_ARRAY_SIZE), size+1));
		array[size++] = e;
		ByteArrays.shiftUp(array, size-1, comparator);
	}
	
	@Override
	public byte dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		byte value = array[0];
		array[0] = array[--size];
		if(size != 0) ByteArrays.shiftDown(array, size, 0, comparator);
		return value;
	}
	
	@Override
	public byte peek(int index) {
		if(index < 0 || index >= size) throw new NoSuchElementException();
		return array[index];
	}
	
	@Override
	public boolean removeFirst(byte e) {
		for(int i = 0;i<size;i++)
			if(e == array[i]) return removeIndex(i);
		return false;
	}
	
	@Override
	public boolean removeLast(byte e) {
		for(int i = size-1;i>=0;i--)
			if(e == array[i]) return removeIndex(i);
		return false;
	}
	
	@Override
	public void forEach(ByteConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(dequeue());
	}
	
	@Override
	public <E> void forEach(E input, ObjectByteConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(input, dequeue());
	}
	
	@Override
	public boolean matchesAny(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public byte reduce(byte identity, ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.applyAsByte(state, array[i]);
		}
		return state;
	}
	
	@Override
	public byte reduce(ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte state = (byte)0;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = array[i];
				continue;
			}
			state = operator.applyAsByte(state, array[i]);
		}
		return state;
	}
	
	@Override
	public byte findFirst(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) {
				byte data = array[i];
				removeIndex(i);
				return data;
			}
		}
		return (byte)0;
	}
	
	@Override
	public int count(Byte2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) result++;
		}
		return result;
	}
	
	protected boolean removeIndex(int index) {
		array[index] = array[--size];
		if(size != index) ByteArrays.shiftDown(array, size, index, comparator);
		return true;
	}
	
	@Override
	public void onChanged() {
		if(size <= 0) return;
		ByteArrays.shiftDown(array, size, 0, comparator);
	}
	
	@Override
	public ByteHeapPriorityQueue copy() {
		ByteHeapPriorityQueue queue = new ByteHeapPriorityQueue();
		queue.size = size;
		queue.comparator = comparator;
		queue.array = Arrays.copyOf(array, array.length);
		return queue;
	}
	
	@Override
	public ByteComparator comparator() {
		return comparator;
	}
	
	@Override
	public byte[] toByteArray(byte[] input) {
		if(input == null || input.length < size()) input = new byte[size()];
		System.arraycopy(array, 0, input, 0, size());
		return input;
	}
	
	private class Iter implements ByteIterator {
		@Override
		public boolean hasNext() {
			return !isEmpty();
		}
		
		@Override
		public byte nextByte() {
			return dequeue();
		}
	}
}