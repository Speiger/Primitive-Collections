package speiger.src.collections.doubles.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.DoublePredicate;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.utils.DoubleIterators;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.ints.functions.consumer.IntDoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.utils.DoubleStrategy;
import speiger.src.collections.objects.utils.ObjectArrays;

import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific HashSet that allows for custom HashControl.
 * For cases where Objects/primitive do not allow hashcoding this can be really useful and provide a lot of control.
 */
public class DoubleOpenCustomHashSet extends AbstractDoubleSet implements ITrimmable
{
	/** The Backing keys array */
	protected transient double[] keys;
	/** If a null value is present */
	protected transient boolean containsNull;
	/** Minimum array size the HashSet will be */
	protected transient int minCapacity;
	/** Index of the Null Value */
	protected transient int nullIndex;
	/** Maximum amount of Values that can be stored before the array gets expanded usually 75% */
	protected transient int maxFill;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	
	/** Amount of Elements stored in the HashSet */
	protected int size;
	/** How full the Array is allowed to get before resize */
	protected final float loadFactor;
	/** Strategy that allows to control the Hash Generation and equals comparason */
	protected final DoubleStrategy strategy;
	
	/**
	 * Default Contstructor
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public DoubleOpenCustomHashSet(DoubleStrategy strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashSet is allowed to be.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public DoubleOpenCustomHashSet(int minCapacity, DoubleStrategy strategy) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashSet is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public DoubleOpenCustomHashSet(int minCapacity, float loadFactor, DoubleStrategy strategy) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new double[nullIndex + 1];
		this.strategy = strategy;
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public DoubleOpenCustomHashSet(double[] array, DoubleStrategy strategy) {
		this(array, 0, array.length, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public DoubleOpenCustomHashSet(double[] array, float loadFactor, DoubleStrategy strategy) {
		this(array, 0, array.length, loadFactor, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public DoubleOpenCustomHashSet(double[] array, int offset, int length, DoubleStrategy strategy) {
		this(array, offset, length, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a set from unboxed values
	 * @param array the elements that should be put into the set
	 * @param offset the starting index within the array that should be used
	 * @param length the amount of elements used from the array
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public DoubleOpenCustomHashSet(double[] array, int offset, int length, float loadFactor, DoubleStrategy strategy) {
		this(length < 0 ? 0 : length, strategy);
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	@Deprecated
	public DoubleOpenCustomHashSet(Collection<? extends Double> collection, DoubleStrategy strategy) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	@Deprecated
	public DoubleOpenCustomHashSet(Collection<? extends Double> collection, float loadFactor, DoubleStrategy strategy) {
		this(collection.size(), loadFactor, strategy);
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public DoubleOpenCustomHashSet(DoubleCollection collection, DoubleStrategy strategy) {
		this(collection, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the Set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public DoubleOpenCustomHashSet(DoubleCollection collection, float loadFactor, DoubleStrategy strategy) {
		this(collection.size(), strategy);
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public DoubleOpenCustomHashSet(Iterator<Double> iterator, DoubleStrategy strategy) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public DoubleOpenCustomHashSet(Iterator<Double> iterator, float loadFactor, DoubleStrategy strategy) {
		this(DoubleIterators.wrap(iterator), loadFactor, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public DoubleOpenCustomHashSet(DoubleIterator iterator, DoubleStrategy strategy) {
		this(iterator, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**	
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public DoubleOpenCustomHashSet(DoubleIterator iterator, float loadFactor, DoubleStrategy strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, loadFactor, strategy);
		while(iterator.hasNext()) add(iterator.nextDouble());
	}
	
	/**
	 * Helper getter function to get the current strategy
	 * @return the current strategy
	 */
	public DoubleStrategy getStrategy() {
		return strategy;
	}
	
	@Override
	public boolean add(double o) {
		if(strategy.equals(o, 0D)) {
			if(containsNull) return false;
			containsNull = true;
			onNodeAdded(nullIndex);
		}
		else {
			int pos = HashUtil.mix(strategy.hashCode(o)) & mask;
			double current = keys[pos];
			if(!strategy.equals(current, 0D)) {
				if(strategy.equals(current, o)) return false;
				while(!strategy.equals((current = keys[pos = (++pos & mask)]), 0D))
					if(strategy.equals(current, o)) return false;
			}
			keys[pos] = o;
			onNodeAdded(pos);
		}
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
		return true;
	}
	
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Double> c) {
		if(loadFactor <= 0.5F) ensureCapacity(c.size());
		else ensureCapacity(c.size() + size());
		return super.addAll(c);
	}
	
	@Override
	public boolean addAll(DoubleCollection c) {
		if(loadFactor <= 0.5F) ensureCapacity(c.size());
		else ensureCapacity(c.size() + size());		
		return super.addAll(c);
	}
	
	@Override
	public boolean contains(double o) {
		if(strategy.equals(o, 0D)) return containsNull;
		int pos = HashUtil.mix(strategy.hashCode(o)) & mask;
		double current = keys[pos];
		if(strategy.equals(current, 0D)) return false;
		if(strategy.equals(current, o)) return true;
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), 0D)) return false;
			else if(strategy.equals(current, o)) return true;
		}
	}
	
	@Override
	public boolean remove(double o) {
		if(strategy.equals(o, 0D)) return (containsNull ? removeNullIndex() : false);
		int pos = HashUtil.mix(strategy.hashCode(o)) & mask;
		double current = keys[pos];
		if(strategy.equals(current, 0D)) return false;
		if(strategy.equals(current, o)) return removeIndex(pos);
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), 0D)) return false;
			else if(strategy.equals(current, o)) return removeIndex(pos);
		}
	}
	
	@Override
	public boolean trim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= nullIndex || this.size >= Math.min((int)Math.ceil(request * loadFactor), request - 1)) return false;
		try {
			rehash(request);
		}
		catch(OutOfMemoryError e) { return false; }
		return true;
	}
	
	@Override
	public void clearAndTrim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= nullIndex) {
			clear();
			return;
		}
		nullIndex = request;
		mask = request-1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new double[request + 1];
		this.size = 0;
		containsNull = false;
	}
	
	private void ensureCapacity(int newCapacity) {
		int size = HashUtil.arraySize(newCapacity, loadFactor);
		if(size > nullIndex) rehash(size);
	}
	
	protected boolean removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : false;
		keys[pos] = 0D;
		size--;
		onNodeRemoved(pos);
		shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return true;
	}
	
	protected boolean removeNullIndex() {
		containsNull = false;
		keys[nullIndex] = 0D;
		size--;
		onNodeRemoved(nullIndex);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return true;
	}
	
	protected void onNodeAdded(int pos) {
		
	}
	
	protected void onNodeRemoved(int pos) {
		
	}
	
	protected void onNodeMoved(int from, int to) {
		
	}
	
	protected void shiftKeys(int startPos) {
		int slot, last;
		double current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if(strategy.equals((current = keys[startPos]), 0D)) {
					keys[last] = 0D;
					return;
				}
				slot = HashUtil.mix(strategy.hashCode(current)) & mask;
				if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
				startPos = ++startPos & mask;
			}
			keys[last] = current;
			onNodeMoved(startPos, last);
		}
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		double[] newKeys = new double[newSize + 1];
		for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Set was modified during rehash");
				if(!strategy.equals(keys[i], 0D)) break;
			}
			if(!strategy.equals(newKeys[pos = HashUtil.mix(Double.hashCode(keys[i])) & newMask], 0D))
				while(!strategy.equals(newKeys[pos = (++pos & newMask)], 0D));
			newKeys[pos] = keys[i];
		}
		nullIndex = newSize;
		mask = newMask;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = newKeys;
	}
	
	@Override
	public DoubleIterator iterator() {
		return new SetIterator();
	}
	
	@Override
	public double[] toDoubleArray(double[] a) {
		if(a == null || a.length < size()) a = new double[size()];
		if(containsNull) a[0] = 0D;
		for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D)) a[index++] = keys[i];
		}
		if (a.length > size) a[size] = 0D;
		return a;
	}
	
	@Override
	@Deprecated
	public Object[] toArray() {
		if(isEmpty()) return ObjectArrays.EMPTY_ARRAY;
		Object[] obj = new Object[size()];
		if(containsNull) obj[0] = Double.valueOf(0D);
		for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D)) obj[index++] = Double.valueOf(keys[i]);
		}
		return obj;
	}
	
	@Override
	@Deprecated
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size()];
		else if(a.length < size()) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size());
		if(containsNull) a[0] = (E)Double.valueOf(0D);
		for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D)) a[index++] = (E)Double.valueOf(keys[i]);
		}
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public DoubleOpenCustomHashSet copy() {
		DoubleOpenCustomHashSet set = new DoubleOpenCustomHashSet(0, loadFactor, strategy);
		set.minCapacity = minCapacity;
		set.mask = mask;
		set.maxFill = maxFill;
		set.nullIndex = nullIndex;
		set.containsNull = containsNull;
		set.size = size;
		set.keys = Arrays.copyOf(keys, keys.length);
		return set;
	}
	
	@Override
	public void clear() {
		if(size == 0) return;
		size = 0;
		containsNull = false;
		Arrays.fill(keys, 0D);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public void forEach(DoubleConsumer action) {
		Objects.requireNonNull(action);
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D)) action.accept(keys[i]);
		}
	}
	
	@Override
	public void forEachIndexed(IntDoubleConsumer action) {
		Objects.requireNonNull(action);
		if(size() <= 0) return;
		if(containsNull) action.accept(0, keys[nullIndex]);
		for(int i = nullIndex-1, index = containsNull ? 1 : 0;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D)) action.accept(index++, keys[i]);
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
		Objects.requireNonNull(action);
		if(size() <= 0) return;
		if(containsNull) action.accept(input, keys[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D)) action.accept(input, keys[i]);
		}
	}
	
	@Override
	public boolean matchesAny(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return false;
		if(containsNull && filter.test(keys[nullIndex])) return true;
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D) && filter.test(keys[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return true;
		if(containsNull && filter.test(keys[nullIndex])) return false;
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D) && filter.test(keys[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return true;
		if(containsNull && !filter.test(keys[nullIndex])) return false;
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D) && !filter.test(keys[i])) return false;
		}
		return true;
	}
	
	@Override
	public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = identity;
		if(containsNull) state = operator.applyAsDouble(state, keys[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(strategy.equals(keys[i], 0D)) continue;
			state = operator.applyAsDouble(state, keys[i]);
		}
		return state;
	}
	
	@Override
	public double reduce(DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = 0D;
		boolean empty = true;
		if(containsNull) {
			state = keys[nullIndex];
			empty = false;
		}
		for(int i = nullIndex-1;i>=0;i--) {
			if(strategy.equals(keys[i], 0D)) continue;
			if(empty) {
				empty = false;
				state = keys[i];
				continue;
			}
			state = operator.applyAsDouble(state, keys[i]);
		}
		return state;
	}
	
	@Override
	public double findFirst(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return 0D;
		if(containsNull && filter.test(keys[nullIndex])) return keys[nullIndex];
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D) && filter.test(keys[i])) return keys[i];
		}
		return 0D;
	}
	
	@Override
	public int count(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		if(size() <= 0) return 0;
		int result = 0;
		if(containsNull && filter.test(keys[nullIndex])) result++;
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], 0D) && filter.test(keys[i])) result++;
		}
		return result;
	}
	
	private class SetIterator implements DoubleIterator {
		int pos = nullIndex;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		boolean returnNull = containsNull;
		double[] wrapped = null;
		int wrappedIndex = 0;
		
		@Override
		public boolean hasNext() {
			if(nextIndex == Integer.MIN_VALUE) {
				if(returnNull) {
					returnNull = false;
					nextIndex = nullIndex;
				}
				else {
					while(true) {
						if(--pos < 0) {
							if(wrapped == null || wrappedIndex <= -pos - 1) break;
							nextIndex = -pos - 1;
							break;
						}
						if(Double.doubleToLongBits(keys[pos]) != 0){
							nextIndex = pos;
							break;
						}
					}
				}
			}
			return nextIndex != Integer.MIN_VALUE;
		}
		
		@Override
		public double nextDouble() {
			if(!hasNext()) throw new NoSuchElementException();
			returnedPos = pos;
			if(nextIndex < 0){
				lastReturned = Integer.MAX_VALUE;
				double value = wrapped[nextIndex];
				nextIndex = Integer.MIN_VALUE;
				return value;
			}
			double value = keys[(lastReturned = nextIndex)];
			nextIndex = Integer.MIN_VALUE;
			return value;
		}
		
		@Override
		public void remove() {
			if(lastReturned == -1) throw new IllegalStateException();
			if(lastReturned == nullIndex) {
				containsNull = false;
				keys[nullIndex] = 0D;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				DoubleOpenCustomHashSet.this.remove(wrapped[-returnedPos - 1]);
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			double current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if(strategy.equals((current = keys[startPos]), 0D)) {
						keys[last] = 0D;
						return;
					}
					slot = HashUtil.mix(strategy.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) addWrapper(keys[startPos]);
				keys[last] = current;
			}
		}
		
		private void addWrapper(double value) {
			if(wrapped == null) wrapped = new double[2];
			else if(wrappedIndex >= wrapped.length) {
				double[] newArray = new double[wrapped.length * 2];
				System.arraycopy(wrapped, 0, newArray, 0, wrapped.length);
				wrapped = newArray;
			}
			wrapped[wrappedIndex++] = value;
		}
	}
}