package speiger.src.collections.booleans.queues;

import java.util.Arrays;
import java.util.Objects;
import java.util.NoSuchElementException;

import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.ints.functions.consumer.IntBooleanConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.booleans.functions.function.BooleanPredicate;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Simple First In First Out Priority Queue that is a Good Replacement for a linked list (or ArrayDequeue)
 * Its specific implementation uses a backing array that grows and shrinks as it is needed.
 */
public class BooleanArrayFIFOQueue extends AbstractBooleanPriorityQueue implements BooleanPriorityDequeue, ITrimmable
{
	/** Max Possible ArraySize without the JVM Crashing */
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	/** The Minimum Capacity that is allowed */
	public static final int MIN_CAPACITY = 4;
	/** The Backing array */
	protected transient boolean[] array;
	/** The First Index pointer */
	protected int first;
	/** The Last Index pointer */
	protected int last;
	/** The Minimum Capacity of the Queue **/
	protected int minCapacity;
	
	/**
	 * Constructor using a initial array
	 * @param values the Array that should be used
	 */
	public BooleanArrayFIFOQueue(boolean[] values) {
		this(values, 0, values.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param values the Array that should be used
	 * @param size the amount of elements that are in the initial array
	 * @throws IllegalStateException if values is smaller then size
	 */
	public BooleanArrayFIFOQueue(boolean[] values, int size) {
		this(values, 0, size);
	}
	
	/**
	 * Constructor using a initial array
	 * @param values the Array that should be used
	 * @param offset where to begin in the initial array
	 * @param size the amount of elements that are in the initial array
	 * @throws IllegalStateException if values is smaller then size
	 */
	public BooleanArrayFIFOQueue(boolean[] values, int offset, int size) {
		if (values.length < size) throw new IllegalArgumentException("Initial array (" + values.length + ") is smaller then the expected size (" + size + ")");
		if(values.length <= 0) values = new boolean[MIN_CAPACITY];
		else if(values.length < MIN_CAPACITY) values = Arrays.copyOf(values, MIN_CAPACITY);
		minCapacity = MIN_CAPACITY;
		array = values;
		first = offset;
		last = (offset + size) % array.length;
		if(array.length == size) expand();
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param capacity the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public BooleanArrayFIFOQueue(int capacity) {
		if (capacity < 0) throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
		array = new boolean[Math.max(MIN_CAPACITY, capacity+1)];
		minCapacity = array.length;
	}
	
	/**
	 * Default Construtor
	 */
	public BooleanArrayFIFOQueue() {
		this(MIN_CAPACITY);
	}
	
	@Override
	public BooleanIterator iterator() {
		return new Iter();
	}
	
	@Override
	public int size() {
		final int apparentLength = last - first;
		return apparentLength >= 0 ? apparentLength : array.length + apparentLength;
	}
	
	@Override
	public void clear() {
		if(first != last) {
			first = last = 0;
		}
		else if(first != 0) {
			first = last = 0;
		}
	}
	
	@Override
	public void enqueue(boolean e) {
		array[last++] = e;
		if(last == array.length) last = 0;
		if(last == first) expand();
	}
	
	@Override
	public void enqueueFirst(boolean e) {
		if(first == 0) first = array.length;
		array[--first] = e;
		if(first == last) expand();
	}
	
	@Override
	public boolean dequeue() {
		if(first == last) throw new NoSuchElementException();
		boolean data = array[first];
		if(++first == array.length) first = 0;
		reduce();
		return data;
	}
	
	@Override
	public boolean dequeueLast() {
		if(first == last) throw new NoSuchElementException();
		if(last == 0) last = array.length;
		boolean data = array[--last];
		reduce();
		return data;
	}
	
	@Override
	public boolean peek(int index) {
		if(first == last || index < 0 || index >= size()) throw new NoSuchElementException();
		index += first;
		return index >= array.length ? array[index-array.length] : array[index];
	}
	
	@Override
	public boolean removeFirst(boolean e) {
		if(first == last) return false;
		for(int i = 0,m=size();i<m;i++) {
			int index = (first + i) % array.length;
			if(e == array[index])
				return removeIndex(index);
		}
		return false;
	}
	
	@Override
	public boolean removeLast(boolean e) {
		if(first == last) return false;
		for(int i = size()-1;i>=0;i--) {
			int index = (first + i) % array.length;
			if(e == array[index])
				return removeIndex(index);
		}
		return false;
	}
	
	protected boolean removeIndex(int index) {
		if(first >= last ? index < first && index > last : index < first || index > last) return false;
		if(index == first) {
			first++;
		}
		else if(index == last) {
			last--;
		}
		else if(index > last) {
			System.arraycopy(array, first, array, first+1, (index - first));
			first = ++first % array.length;
		}
		else if(index < first) {
			System.arraycopy(array, index+1, array, index, (last - index) - 1);
			if(--last < 0) last += array.length;
		}
		else {
			if(index - first < last - index) {
				System.arraycopy(array, first, array, first+1, (index - first));
				first = ++first % array.length;
			}
			else {
				System.arraycopy(array, index+1, array, index, (last - index) - 1);
				if(--last < 0) last += array.length;
			}
		}
		reduce();
		return true;
	}
	
	@Override
	public void onChanged() {}
	
	@Override
	public BooleanArrayFIFOQueue copy() {
		BooleanArrayFIFOQueue queue = new BooleanArrayFIFOQueue();
		queue.first = first;
		queue.last = last;
		queue.minCapacity = minCapacity;
		queue.array = Arrays.copyOf(array, array.length);
		return queue;
	}
	
	@Override
	public BooleanComparator comparator() { return null; }
	
	@Override
	public void forEach(BooleanConsumer action) {
		Objects.requireNonNull(action);
		if(first == last) return;
		for(int i = 0,m=size();i<m;i++)
			action.accept(array[(first + i) % array.length]);
		clearAndTrim(0);
	}
	
	@Override
	public void forEachIndexed(IntBooleanConsumer action) {
		Objects.requireNonNull(action);
		if(first == last) return;
		for(int i = 0,m=size();i<m;i++)
			action.accept(i, array[(first + i) % array.length]);
		clearAndTrim(0);
	}
	
	@Override
	public <E> void forEach(E input, ObjectBooleanConsumer<E> action) {
		Objects.requireNonNull(action);
		if(first == last) return;
		for(int i = 0,m=size();i<m;i++)
			action.accept(input, array[(first + i) % array.length]);
		clearAndTrim(0);	
	}
	
	@Override
	public boolean matchesAny(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=size();i<m;i++) {
			if(filter.test(array[(first + i) % array.length])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=size();i<m;i++) {
			if(filter.test(array[(first + i) % array.length])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=size();i<m;i++) {
			if(!filter.test(array[(first + i) % array.length])) return false;
		}
		return true;
	}
	
	@Override
	public boolean findFirst(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(int i = 0,m=size();i<m;i++) {
			int index = (first + i) % array.length;
			if(filter.test(array[index])) {
				boolean data = array[index];
				removeIndex(index);
				return data;
			}
		}
		return false;
	}
	
	@Override
	public boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) {
		Objects.requireNonNull(operator);
		boolean state = identity;
		for(int i = 0,m=size();i<m;i++) {
			state = operator.applyAsBoolean(state, array[(first + i) % array.length]);
		}
		return state;
	}
	
	@Override
	public boolean reduce(BooleanBooleanUnaryOperator operator) {
		Objects.requireNonNull(operator);
		boolean state = false;
		boolean empty = true;
		for(int i = 0,m=size();i<m;i++) {
			if(empty) {
				empty = false;
				state = array[(first + i) % array.length];
				continue;
			}
			state = operator.applyAsBoolean(state, array[(first + i) % array.length]);
		}
		return state;
	}
	
	@Override
	public int count(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0,m=size();i<m;i++) {
			if(filter.test(array[(first + i) % array.length])) result++;
		}
		return result;
	}
	
	@Override
	public boolean trim(int size) {
		int newSize = Math.max(Math.max(size, size()), minCapacity);
		if(newSize >= array.length) return false;
		boolean[] newArray = new boolean[newSize];
		if(first <= last) System.arraycopy(array, first, newArray, 0, last - first);
		else {
			System.arraycopy(array, first, newArray, 0, array.length - first);
			System.arraycopy(array, 0, newArray, array.length - first, last);
		}
		first = 0;
		last = size();
		array = newArray;
		return true;
	}
	
	/**
	 * Trims the collection down to the requested size and clears all elements while doing so
	 * @param size the amount of elements that should be allowed
	 * @note this will enforce minimum size of the collection itself
	 */
	@Override
	public void clearAndTrim(int size) {
		int newSize = Math.max(minCapacity, size);
		if(array.length <= newSize) {
			clear();
			return;
		}
		first = last = 0;
		array = new boolean[newSize];
	}
	
	@Override
	public boolean[] toBooleanArray(boolean[] input) {
		if(input == null || input.length < size()) input = new boolean[size()];
		if (first <= last) System.arraycopy(array, first, input, 0, last - first);
		else {
			System.arraycopy(array, first, input, 0, array.length - first);
			System.arraycopy(array, 0, input, array.length - first, last);
		}
		return input;
	}
	
	protected void reduce() {
		final int size = size();
		if (array.length > minCapacity && size <= array.length / 4) resize(size, Math.max(array.length / 2, minCapacity));
	}
	
	protected void expand() {
		resize(array.length, (int)Math.min(MAX_ARRAY_SIZE, 2L * array.length));
	}
	
	protected final void resize(int oldSize, int newSize) {
		boolean[] newArray = new boolean[newSize];
		if(first >= last) {
			if(oldSize != 0)
			{
				System.arraycopy(array, first, newArray, 0, array.length - first);
				System.arraycopy(array, 0, newArray, array.length - first, last);
			}
		}
		else System.arraycopy(array, first, newArray, 0, last-first);
		first = 0;
		last = oldSize;
		array = newArray;
	}
	
	private class Iter implements BooleanIterator
	{
		int index = first;
		@Override
		public boolean hasNext()
		{
			return index != last;
		}

		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new NoSuchElementException();
			boolean value = array[index];
			removeIndex(index);
			index = ++index % array.length;
			return value;
		}
	}
}