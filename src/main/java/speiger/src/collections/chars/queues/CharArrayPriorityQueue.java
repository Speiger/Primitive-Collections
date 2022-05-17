package speiger.src.collections.chars.queues;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.chars.functions.function.Char2BooleanFunction;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.utils.CharArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Array Priority Queue, this is a very unoptimized implementation of the PriorityQueue for very specific usecases.
 * It allows for duplicated entries and works like {@link java.util.List#indexOf(Object)} search.
 * It is highly suggested to use HeapPriorityQueue otherwise, unless you know why you need this specific implementation
 */
public class CharArrayPriorityQueue extends AbstractCharPriorityQueue
{
	/** The Backing Array */
	protected transient char[] array = CharArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Last known first index pointer */
	protected int firstIndex = -1;
	/** The Sorter of the Array */
	protected CharComparator comparator;
	
	/**
	 * Default Constructor
	 */
	public CharArrayPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public CharArrayPriorityQueue(CharComparator comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public CharArrayPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public CharArrayPriorityQueue(int size, CharComparator comp) {
		if(size < 0) throw new IllegalAccessError("Size has to be 0 or positive");
		if(size > 0) array = new char[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public CharArrayPriorityQueue(char[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public CharArrayPriorityQueue(char[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public CharArrayPriorityQueue(char[] array, CharComparator comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public CharArrayPriorityQueue(char[] array, int size, CharComparator comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		this.comparator = comp;
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public CharArrayPriorityQueue(CharCollection c) {
		array = c.toCharArray();
		size = c.size();
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public CharArrayPriorityQueue(CharCollection c, CharComparator comp) {
		array = c.toCharArray();
		size = c.size();
		comparator = comp;
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @return a ArrayPriorityQueue containing the original input array
	 */
	public static CharArrayPriorityQueue wrap(char[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @return a ArrayPriorityQueue containing the original input array
	 */
	public static CharArrayPriorityQueue wrap(char[] array, int size) {
		CharArrayPriorityQueue queue = new CharArrayPriorityQueue();
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
	public static CharArrayPriorityQueue wrap(char[] array, CharComparator comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @return a ArrayPriorityQueue containing the original input array
	 */
	public static CharArrayPriorityQueue wrap(char[] array, int size, CharComparator comp) {
		CharArrayPriorityQueue queue = new CharArrayPriorityQueue(comp);
		queue.array = array;
		queue.size = size;
		return queue;
	}
	
	@Override
	public void enqueue(char e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (long)(array.length >> 1), (long)SanityChecks.MAX_ARRAY_SIZE), size+1));
		if(firstIndex != -1){
			int compare = comparator == null ? Character.compare(e, array[firstIndex]) : comparator.compare(e, array[firstIndex]);
			if(compare < 0) firstIndex = size;
			else if(compare > 0) firstIndex = -1;
		}
		array[size++] = e;
	}
	
	@Override
	public char dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		int index = findFirstIndex();
		char value = array[index];
		if(index != --size) System.arraycopy(array, index+1, array, index, size - index);
		firstIndex = -1;
		return value;
	}
	
	@Override
	public char peek(int index) {
		if(index < 0 || index >= size) throw new NoSuchElementException();
		return array[index];
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
	public void forEach(CharConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(dequeue());
	}
	
	@Override
	public <E> void forEach(E input, ObjectCharConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(input, dequeue());
	}
	
	@Override
	public boolean matchesAny(Char2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Char2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Char2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.get(array[i])) return false;
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
	public char findFirst(Char2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) {
				char data = array[i];
				removeIndex(i);
				return data;
			}
		}
		return (char)0;
	}
	
	@Override
	public int count(Char2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.get(array[i])) result++;
		}
		return result;
	}
	
	@Override
	public CharIterator iterator() {
		return new Iter();
	}
	
	@Override
	public CharArrayPriorityQueue copy() {
		CharArrayPriorityQueue queue = new CharArrayPriorityQueue();
		queue.firstIndex = firstIndex;
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
	
	protected int findFirstIndex() {
		if(firstIndex == -1) {
			int index = size-1;
			char value = array[index];
			if(comparator == null) {
				for(int i = index;i>=0;i--) {
					if(Character.compare(array[i], value) < 0) 
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