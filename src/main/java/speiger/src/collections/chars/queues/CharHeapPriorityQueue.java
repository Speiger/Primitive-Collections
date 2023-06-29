package speiger.src.collections.chars.queues;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.ints.functions.consumer.IntCharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.chars.functions.function.CharPredicate;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.utils.CharArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Heap base Priority Queue implementation
 * It is a ArrayBased Alternative to TreeSets that has less object allocations
 */
public class CharHeapPriorityQueue extends AbstractCharPriorityQueue
{
	/** The Backing Array */
	protected transient char[] array = CharArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Sorter of the Array */
	protected CharComparator comparator;
	
	/**
	 * Default Constructor
	 */
	public CharHeapPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public CharHeapPriorityQueue(CharComparator comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public CharHeapPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public CharHeapPriorityQueue(int size, CharComparator comp) {
		if(size > 0) array = new char[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public CharHeapPriorityQueue(char[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public CharHeapPriorityQueue(char[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		CharArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public CharHeapPriorityQueue(char[] array, CharComparator comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public CharHeapPriorityQueue(char[] array, int size, CharComparator comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		comparator = comp;
		CharArrays.heapify(array, size, comp);
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public CharHeapPriorityQueue(CharCollection c) {
		array = c.toCharArray();
		size = c.size();
		CharArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public CharHeapPriorityQueue(CharCollection c, CharComparator comp) {
		array = c.toCharArray();
		size = c.size();
		comparator = comp;
		CharArrays.heapify(array, size, comp);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static CharHeapPriorityQueue wrap(char[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static CharHeapPriorityQueue wrap(char[] array, int size) {
		CharHeapPriorityQueue queue = new CharHeapPriorityQueue();
		queue.array = array;
		queue.size = size;
		CharArrays.heapify(array, size, null);
		return queue;
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static CharHeapPriorityQueue wrap(char[] array, CharComparator comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static CharHeapPriorityQueue wrap(char[] array, int size, CharComparator comp) {
		CharHeapPriorityQueue queue = new CharHeapPriorityQueue(comp);
		queue.array = array;
		queue.size = size;
		CharArrays.heapify(array, size, comp);
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
	public CharIterator iterator() {
		return new Iter();
	}
	
	@Override
	public void enqueue(char e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (long)(array.length >> 1), (long)SanityChecks.MAX_ARRAY_SIZE), size+1));
		array[size++] = e;
		CharArrays.shiftUp(array, size-1, comparator);
	}
	
	@Override
	public char dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		char value = array[0];
		array[0] = array[--size];
		if(size != 0) CharArrays.shiftDown(array, size, 0, comparator);
		return value;
	}
	
	@Override
	public char peek(int index) {
		if(index < 0 || index >= size) throw new NoSuchElementException();
		return array[index];
	}
	
	@Override
	public boolean contains(char e) {
		for(int i = 0;i<size;i++)
			if(e == array[i]) return true;
		return false;
	}
	
	@Override
	public boolean removeFirst(char e) {
		for(int i = 0;i<size;i++)
			if(e == array[i]) return removeIndex(i);
		return false;
	}
	
	@Override
	public boolean removeLast(char e) {
		for(int i = size-1;i>=0;i--)
			if(e == array[i]) return removeIndex(i);
		return false;
	}
	
	@Override
	public void forEach(CharConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(dequeue());
	}
	
	@Override
	public void forEachIndexed(IntCharConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(i, dequeue());
	}
	
	@Override
	public <E> void forEach(E input, ObjectCharConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(input, dequeue());
	}
	
	@Override
	public boolean matchesAny(CharPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(CharPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(CharPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.test(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public char reduce(char identity, CharCharUnaryOperator operator) {
		Objects.requireNonNull(operator);
		char state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.applyAsChar(state, array[i]);
		}
		return state;
	}
	
	@Override
	public char reduce(CharCharUnaryOperator operator) {
		Objects.requireNonNull(operator);
		char state = (char)0;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = array[i];
				continue;
			}
			state = operator.applyAsChar(state, array[i]);
		}
		return state;
	}
	
	@Override
	public char findFirst(CharPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) {
				char data = array[i];
				removeIndex(i);
				return data;
			}
		}
		return (char)0;
	}
	
	@Override
	public int count(CharPredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.test(array[i])) result++;
		}
		return result;
	}
	
	protected boolean removeIndex(int index) {
		array[index] = array[--size];
		if(size != index) CharArrays.shiftDown(array, size, index, comparator);
		return true;
	}
	
	@Override
	public void onChanged() {
		if(size <= 0) return;
		CharArrays.shiftDown(array, size, 0, comparator);
	}
	
	@Override
	public CharHeapPriorityQueue copy() {
		CharHeapPriorityQueue queue = new CharHeapPriorityQueue();
		queue.size = size;
		queue.comparator = comparator;
		queue.array = Arrays.copyOf(array, array.length);
		return queue;
	}
	
	@Override
	public CharComparator comparator() {
		return comparator;
	}
	
	@Override
	public char[] toCharArray(char[] input) {
		if(input == null || input.length < size()) input = new char[size()];
		System.arraycopy(array, 0, input, 0, size());
		return input;
	}
	
	private class Iter implements CharIterator {
		@Override
		public boolean hasNext() {
			return !isEmpty();
		}
		
		@Override
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			return dequeue();
		}
	}
}