package speiger.src.collections.booleans.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.collections.booleans.functions.function.Boolean2ObjectFunction;
import speiger.src.collections.booleans.functions.function.Boolean2BooleanFunction;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.lists.BooleanListIterator;
import speiger.src.collections.booleans.collections.BooleanBidirectionalIterator;
import speiger.src.collections.booleans.collections.BooleanCollection;

/**
 * A Helper class for Iterators
 */
public class BooleanIterators
{
	/**
	 * Empty Iterator Reference
	 */
	public static final EmptyIterator EMPTY = new EmptyIterator();
	
	/**
	 * Returns a Immutable EmptyIterator instance that is automatically casted.
	 * @return an empty iterator
	 */
	public static EmptyIterator empty() {
		return EMPTY;
	}
	
	/**
	 * Inverter function for Bidirectional Iterators
	 * @param it the iterator that should be inverted
	 * @return a Inverted Bidirectional Iterator. If it was inverted then it just gives back the original reference
	 */
	public static BooleanBidirectionalIterator invert(BooleanBidirectionalIterator it) {
		return it instanceof ReverseBiIterator ? ((ReverseBiIterator)it).it : new ReverseBiIterator(it);
	}
	
	/**
	 * Inverter function for List Iterators
	 * @param it the iterator that should be inverted
	 * @return a Inverted List Iterator. If it was inverted then it just gives back the original reference
	 */
	public static BooleanListIterator invert(BooleanListIterator it) {
		return it instanceof ReverseListIterator ? ((ReverseListIterator)it).it : new ReverseListIterator(it);
	}
	
	/**
	 * Returns a Immutable Iterator instance based on the instance given.
	 * @param iterator that should be made immutable/unmodifiable
	 * @return a unmodifiable iterator wrapper. If the Iterator already a unmodifiable wrapper then it just returns itself.
	 */
	public static BooleanIterator unmodifiable(BooleanIterator iterator) {
		return iterator instanceof UnmodifiableIterator ? iterator : new UnmodifiableIterator(iterator);
	}
	
	/**
	 * Returns a Immutable Iterator instance based on the instance given.
	 * @param iterator that should be made immutable/unmodifiable
	 * @return a unmodifiable iterator wrapper. If the Iterator already a unmodifiable wrapper then it just returns itself.
	 */
	public static BooleanBidirectionalIterator unmodifiable(BooleanBidirectionalIterator iterator) {
		return iterator instanceof UnmodifiableBiIterator ? iterator : new UnmodifiableBiIterator(iterator);
	}
	
	/**
	 * Returns a Immutable ListIterator instance based on the instance given.
	 * @param iterator that should be made immutable/unmodifiable
	 * @return a unmodifiable listiterator wrapper. If the ListIterator already a unmodifiable wrapper then it just returns itself.
	 */
	public static BooleanListIterator unmodifiable(BooleanListIterator iterator) {
		return iterator instanceof UnmodifiableListIterator ? iterator : new UnmodifiableListIterator(iterator);
	}
	
	/**
	 * A Helper function that maps a Java-Iterator into a new Type.
	 * @param iterator that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is mapped to a new result
	 */
	public static <E> ObjectIterator<E> map(Iterator<? extends Boolean> iterator, Boolean2ObjectFunction<E> mapper) {
		return new MappedIterator<>(wrap(iterator), mapper);
	}
	
	/**
	 * A Helper function that maps a Iterator into a new Type.
	 * @param iterator that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is mapped to a new result
	 */
	public static <E> ObjectIterator<E> map(BooleanIterator iterator, Boolean2ObjectFunction<E> mapper) {
		return new MappedIterator<>(iterator, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterator into a new Type.
	 * @param iterator that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a iterator that is flatMapped to a new result
	 */
	public static <E, V extends Iterable<E>> ObjectIterator<E> flatMap(Iterator<? extends Boolean> iterator, Boolean2ObjectFunction<V> mapper) {
		return new FlatMappedIterator<>(wrap(iterator), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterator into a new Type.
	 * @param iterator that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a iterator that is flatMapped to a new result
	 */
	public static <E, V extends Iterable<E>> ObjectIterator<E> flatMap(BooleanIterator iterator, Boolean2ObjectFunction<V> mapper) {
		return new FlatMappedIterator<>(iterator, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterator into a new Type.
	 * @param iterator that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is flatMapped to a new result
	 */
	public static <E> ObjectIterator<E> arrayFlatMap(Iterator<? extends Boolean> iterator, Boolean2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterator<>(wrap(iterator), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterator into a new Type.
	 * @param iterator that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is flatMapped to a new result
	 */
	public static <E> ObjectIterator<E> arrayFlatMap(BooleanIterator iterator, Boolean2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterator<>(iterator, mapper);
	}
	
	/**
	 * A Helper function that filters out all desired elements from a Java-Iterator
	 * @param iterator that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterator
	 */
	public static BooleanIterator filter(Iterator<? extends Boolean> iterator, Boolean2BooleanFunction filter) {
		return new FilteredIterator(wrap(iterator), filter);
	}
	
	/**
	 * A Helper function that filters out all desired elements
	 * @param iterator that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterator
	 */
	public static BooleanIterator filter(BooleanIterator iterator, Boolean2BooleanFunction filter) {
		return new FilteredIterator(iterator, filter);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements.
	 * @param iterator that should be distinct
	 * @return a distinct iterator
	 */
	public static BooleanIterator distinct(BooleanIterator iterator) {
		return new DistinctIterator(iterator);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements from a Java Iterator.
	 * @param iterator that should be distinct
	 * @return a distinct iterator
	 */
	public static BooleanIterator distinct(Iterator<? extends Boolean> iterator) {
		return new DistinctIterator(wrap(iterator));
	}
	
	/**
	 * A Helper function that hard limits the Iterator to a specific size
	 * @param iterator that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterator
	 */
	public static BooleanIterator limit(BooleanIterator iterator, long limit) {
		return new LimitedIterator(iterator, limit);
	}
	
	/**
	 * A Helper function that hard limits the Iterator to a specific size from a Java Iterator
	 * @param iterator that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterator
	 */
	public static BooleanIterator limit(Iterator<? extends Boolean> iterator, long limit) {
		return new LimitedIterator(wrap(iterator), limit);
	}
	
	/**
	 * A Helper function that sorts the Iterator beforehand.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterator that should be sorted.
	 * @param sorter the sorter of the iterator. Can be null.
	 * @return a new sorted iterator
	 */
	public static BooleanIterator sorted(BooleanIterator iterator, BooleanComparator sorter) {
		return new SortedIterator(iterator, sorter);
	}
	
	/**
	 * A Helper function that sorts the Iterator beforehand from a Java Iterator.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterator that should be sorted.
	 * @param sorter the sorter of the iterator. Can be null.
	 * @return a new sorted iterator
	 */
	public static BooleanIterator sorted(Iterator<? extends Boolean> iterator, BooleanComparator sorter) {
		return new SortedIterator(wrap(iterator), sorter);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterator.
	 * @param iterator that should be peeked at
	 * @param action callback that receives the value before the iterator returns it
	 * @return a peeked iterator
	 */
	public static BooleanIterator peek(BooleanIterator iterator, BooleanConsumer action) {
		return new PeekIterator(iterator, action);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterator  from a Java Iterator
	 * @param iterator that should be peeked at
	 * @param action callback that receives the value before the iterator returns it
	 * @return a peeked iterator
	 */
	public static BooleanIterator peek(Iterator<? extends Boolean> iterator, BooleanConsumer action) {
		return new PeekIterator(wrap(iterator), action);
	}
	
	/**
	 * Helper function to convert a Object Iterator into a Primitive Iterator
	 * @param iterator that should be converted to a unboxing iterator
	 * @return a primitive iterator
	 */
	public static BooleanIterator wrap(Iterator<? extends Boolean> iterator) {
		return iterator instanceof BooleanIterator ? (BooleanIterator)iterator : new IteratorWrapper(iterator);
	}
	
	/**
	 * Returns a Array Wrapping iterator
	 * @param a the array that should be wrapped
	 * @return a Iterator that is wrapping a array.
	 */
	public static ArrayIterator wrap(boolean[] a) {
		return wrap(a, 0, a.length);
	}
	
	/**
	 * Returns a Array Wrapping iterator
	 * @param a the array that should be wrapped.
	 * @param start the index to be started from.
	 * @param end the index that should be ended.
	 * @return a Iterator that is wrapping a array.
	 */
	public static ArrayIterator wrap(boolean[] a, int start, int end) {
		return new ArrayIterator(a, start, end);
	}
	
	/**
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(boolean[] a, Iterator<? extends Boolean> i) {
		return unwrap(a, i, 0, a.length);
	}
	
	/**
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(boolean[] a, Iterator<? extends Boolean> i, int offset) {
		return unwrap(a, i, offset, a.length - offset);
	}
	
	/**
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @param max the maximum values that should be extracted from the source
	 * @return the amount of elements that were inserted into the array.
	 * @throws IllegalStateException if max is smaller the 0 or if the maximum index is larger then the array
	 */
	public static int unwrap(boolean[] a, Iterator<? extends Boolean> i, int offset, int max) {
		if(max < 0) throw new IllegalStateException("The max size is smaller then 0");
		if(offset + max > a.length) throw new IllegalStateException("largest array index exceeds array size");
		int index = 0;
		for(;index<max && i.hasNext();index++) a[index+offset] = i.next().booleanValue();
		return index;
	}
	
	/**
	 * A Primitive iterator variant of the BooleanIterators unwrap function
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(boolean[] a, BooleanIterator i) {
		return unwrap(a, i, 0, a.length);
	}
	
	/**
	 * A Primitive iterator variant of the BooleanIterators unwrap function
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(boolean[] a, BooleanIterator i, int offset) {
		return unwrap(a, i, offset, a.length - offset);
	}
	
	/**
	 * A Primitive iterator variant of the BooleanIterators unwrap function
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @param max the maximum values that should be extracted from the source
	 * @return the amount of elements that were inserted into the array.
	 * @throws IllegalStateException if max is smaller the 0 or if the maximum index is larger then the array
	 */
	public static int unwrap(boolean[] a, BooleanIterator i, int offset, int max) {
		if(max < 0) throw new IllegalStateException("The max size is smaller then 0");
		if(offset + max > a.length) throw new IllegalStateException("largest array index exceeds array size");
		int index = 0;
		for(;index<max && i.hasNext();index++) a[index+offset] = i.nextBoolean();
		return index;
	}
	
	/**
	 * A Function to convert a Primitive Iterator to a Object array.
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(Boolean[] a, BooleanIterator i) {
		return unwrap(a, i, 0, a.length);
	}
	
	/**
	 * A Function to convert a Primitive Iterator to a Object array.
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(Boolean[] a, BooleanIterator i, int offset) {
		return unwrap(a, i, offset, a.length - offset);
	}
	
	/**
	 * A Function to convert a Primitive Iterator to a Object array.
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @param max the maximum values that should be extracted from the source
	 * @return the amount of elements that were inserted into the array.
	 * @throws IllegalStateException if max is smaller the 0 or if the maximum index is larger then the array
	 */
	public static int unwrap(Boolean[] a, BooleanIterator i, int offset, int max) {
		if(max < 0) throw new IllegalStateException("The max size is smaller then 0");
		if(offset + max > a.length) throw new IllegalStateException("largest array index exceeds array size");
		int index = 0;
		for(;index<max && i.hasNext();index++) a[index+offset] = Boolean.valueOf(i.nextBoolean());
		return index;
	}
	
	/**
	 * A Helper function to pours all elements of a Iterator into a List
	 * @param iter the elements that should be poured into list.
	 * @return A list of all elements of the Iterator
	 */
	public static BooleanList pour(BooleanIterator iter) {
		return pour(iter, Integer.MAX_VALUE);
	}
	
	/**
	 * A Helper function to pours all elements of a Iterator into a List
	 * @param iter the elements that should be poured into list.
	 * @param max the maximum amount of elements that should be collected
	 * @return A list of all requested elements of the Iterator
	 */
	public static BooleanList pour(BooleanIterator iter, int max) {
		BooleanArrayList list = new BooleanArrayList();
		pour(iter, list, max);
		list.trim();
		return list;
	}
	
	/**
	 * A Helper function to pours all elements of a Iterator into a Collection
	 * @param iter the elements that should be poured into list.
	 * @param c the collection where the elements should be poured into
	 * @return the amount of elements that were added
	 */
	public static int pour(BooleanIterator iter, BooleanCollection c) {
		return pour(iter, c, Integer.MAX_VALUE);
	}
	
	/**
	 * A Helper function to pours all elements of a Iterator into a Collection
	 * @param iter the elements that should be poured into list.
	 * @param c the collection where the elements should be poured into
	 * @param max the maximum amount of elements that should be collected
	 * @return the amount of elements that were added
	 */
	public static int pour(BooleanIterator iter, BooleanCollection c, int max) {
		if(max < 0) throw new IllegalStateException("Max is negative");
		int done = 0;
		for(;done<max && iter.hasNext();done++, c.add(iter.nextBoolean()));
		return done;
	}
	
	/**
	 * Helper Iterator that concats other iterators together
	 * @param array the Iterators that should be concatenated
	 * @return iterator of the inputted iterators
	 */
	public static BooleanIterator concat(BooleanIterator... array) {
		return concat(array, 0, array.length);
	}
	
	/**
	 * Helper Iterator that concats other iterators together
	 * @param array the Iterators that should be concatenated
	 * @param offset where to start within the array
	 * @param length the length of the array
	 * @return iterator of the inputted iterators
	 */
	public static BooleanIterator concat(BooleanIterator[] array, int offset, int length) {
		return new ConcatIterator(array, offset, length);
	}
	
	private static class IteratorWrapper implements BooleanIterator
	{
		Iterator<? extends Boolean> iter;
		
		public IteratorWrapper(Iterator<? extends Boolean> iter) {
			this.iter = iter;
		}
		
		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}
		
		@Override
		public boolean nextBoolean() {
			return iter.next().booleanValue();
		}
		
		@Override
		@Deprecated
		public Boolean next() {
			return iter.next();
		}
	}
	
	private static class ConcatIterator implements BooleanIterator
	{
		BooleanIterator[] iters;
		int offset;
		int lastOffset = -1;
		int length;
		
		public ConcatIterator(BooleanIterator[] iters, int offset, int length) {
			this.iters = iters;
			this.offset = offset;
			this.length = length;
			find();
		}
		
		private void find() {
			for(;length != 0 && !iters[offset].hasNext();length--, offset++);
		}
		
		@Override
		public boolean hasNext() {
			return length > 0;
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new NoSuchElementException();
			boolean result = iters[lastOffset = offset].nextBoolean();
			find();
			return result;
		}
		
		@Override
		public void remove() {
			if(lastOffset == -1) throw new IllegalStateException();
			iters[lastOffset].remove();
			lastOffset = -1;
		}
	}
	
	private static class ReverseBiIterator implements BooleanBidirectionalIterator {
		BooleanBidirectionalIterator it;
		
		ReverseBiIterator(BooleanBidirectionalIterator it) {
			this.it = it;
		}
		
		@Override
		public boolean nextBoolean() { return it.previousBoolean(); }
		@Override
		public boolean hasNext() { return it.hasPrevious(); }
		@Override
		public boolean hasPrevious() { return it.hasNext(); }
		@Override
		public boolean previousBoolean() { return it.nextBoolean(); }
		@Override
		public void remove() { it.remove(); }
	}

	private static class ReverseListIterator implements BooleanListIterator {
		BooleanListIterator it;
		
		ReverseListIterator(BooleanListIterator it) {
			this.it = it;
		}
	
		@Override
		public boolean nextBoolean() { return it.previousBoolean(); }
		@Override
		public boolean hasNext() { return it.hasPrevious(); }
		@Override
		public boolean hasPrevious() { return it.hasNext(); }
		@Override
		public boolean previousBoolean() { return it.nextBoolean(); }
		@Override
		public void remove() { it.remove(); }
		@Override
		public int nextIndex() { return it.previousIndex(); }
		@Override
		public int previousIndex() { return it.nextIndex(); }
		@Override
		public void set(boolean e) { it.set(e); }
		@Override
		public void add(boolean e) { it.add(e); }
	}
	
	private static class UnmodifiableListIterator implements BooleanListIterator
	{
		BooleanListIterator iter;
	
		UnmodifiableListIterator(BooleanListIterator iter) {
			this.iter = iter;
		}
		
		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}
		
		@Override
		public boolean hasPrevious() {
			return iter.hasPrevious();
		}
		
		@Override
		public int nextIndex() {
			return iter.nextIndex();
		}
		
		@Override
		public int previousIndex() {
			return iter.previousIndex();
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean previousBoolean() {
			return iter.previousBoolean();
		}
		
		@Override
		public boolean nextBoolean() {
			return iter.nextBoolean();
		}

		@Override
		public void set(boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(boolean e) { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableBiIterator implements BooleanBidirectionalIterator
	{
		BooleanBidirectionalIterator iter;
		
		UnmodifiableBiIterator(BooleanBidirectionalIterator iter) {
			this.iter = iter;
		}
		
		@Override
		public boolean nextBoolean() {
			return iter.nextBoolean();
		}
		
		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}
		
		@Override
		public boolean hasPrevious() {
			return iter.hasPrevious();
		}
		
		@Override
		public boolean previousBoolean() {
			return iter.previousBoolean();
		}
	}
	
	private static class UnmodifiableIterator implements BooleanIterator
	{
		BooleanIterator iterator;
	
		UnmodifiableIterator(BooleanIterator iterator) {
			this.iterator = iterator;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		@Override
		public boolean nextBoolean() {
			return iterator.nextBoolean();
		}
	}

	private static class EmptyIterator implements BooleanListIterator
	{
		@Override
		public boolean hasNext() {
			return false;
		}
	
		@Override
		public boolean nextBoolean() {
			return false;
		}

		@Override
		public boolean hasPrevious() {
			return false;
		}
		
		@Override
		public boolean previousBoolean() {
			return false;
		}
		
		@Override
		public int nextIndex() {
			return 0;
		}
		
		@Override
		public int previousIndex() {
			return 0;
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(boolean e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(boolean e) { throw new UnsupportedOperationException(); }
	}
	
	private static class ArrayIterator implements BooleanIterator
	{
		boolean[] a;
		int from;
		int to;
		
		ArrayIterator(boolean[] a, int from, int to) {
			this.a = a;
			this.from = from;
			this.to = to;
		}
		
		@Override
		public boolean hasNext() {
			return from < to;
		}
		
		@Override
		public boolean nextBoolean() {
			return a[from++];
		}
		
		@Override
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int left = Math.min(amount, to - from);
			from += left;
			return amount - left;
		}
	}
	
	private static class MappedIterator<T> implements ObjectIterator<T>
	{
		BooleanIterator iterator;
		Boolean2ObjectFunction<T> mapper;
		
		MappedIterator(BooleanIterator iterator, Boolean2ObjectFunction<T> mapper) {
			this.iterator = iterator;
			this.mapper = mapper;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		@Override
		public T next() {
			return mapper.get(iterator.nextBoolean());
		}
		
		@Override
		public int skip(int amount) {
			return iterator.skip(amount);
		}
	}
	
	private static class FlatMappedIterator<T, V extends Iterable<T>> implements ObjectIterator<T>
	{
		BooleanIterator iterator;
		Iterator<T> last = null;
		Boolean2ObjectFunction<V> mapper;
		boolean foundNext = false;
		
		FlatMappedIterator(BooleanIterator iterator, Boolean2ObjectFunction<V> mapper) {
			this.iterator = iterator;
			this.mapper = mapper;
		}
		
		void compute() {
			if(foundNext) return;
			foundNext = true;
			while(iterator.hasNext()) {
				if(last != null && last.hasNext()) return;
				last = mapper.get(iterator.nextBoolean()).iterator();
			}
		}
		
		@Override
		public boolean hasNext() {
			compute();
			return last != null && last.hasNext();
		}
		
		@Override
		public T next() {
			if(!hasNext()) throw new IllegalStateException("End of Iterator");
			T result = last.next();
			foundNext = false;
			return result;
		}
	}
	
	private static class FlatMappedArrayIterator<T> implements ObjectIterator<T>
	{
		BooleanIterator iterator;
		Iterator<T> last = null;
		Boolean2ObjectFunction<T[]> mapper;
		boolean foundNext = false;
		
		FlatMappedArrayIterator(BooleanIterator iterator, Boolean2ObjectFunction<T[]> mapper) {
			this.iterator = iterator;
			this.mapper = mapper;
		}
		
		void compute() {
			if(foundNext) return;
			foundNext = true;
			while(iterator.hasNext()) {
				if(last != null && last.hasNext()) return;
				last = ObjectIterators.wrap(mapper.get(iterator.nextBoolean()));
			}
		}
		
		@Override
		public boolean hasNext() {
			compute();
			return last != null && last.hasNext();
		}
		
		@Override
		public T next() {
			if(!hasNext()) throw new IllegalStateException("End of Iterator");
			T result = last.next();
			foundNext = false;
			return result;
		}
	}
	
	private static class SortedIterator implements BooleanIterator
	{
		BooleanIterator iterator;
		BooleanComparator sorter;
		BooleanList sortedElements = null;
		int index = 0;
		
		public SortedIterator(BooleanIterator iterator, BooleanComparator sorter) {
			this.iterator = iterator;
			this.sorter = sorter;
		}
		
		@Override
		public boolean hasNext() {
			if(sortedElements == null) {
				boolean hasNext = iterator.hasNext();
				sortedElements = hasNext ? pour(iterator) : BooleanLists.empty();
				if(hasNext) sortedElements.unstableSort(sorter);
			}
			return index < sortedElements.size();
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new IllegalStateException("End of Iterator");
			return sortedElements.getBoolean(index++);
		}
	}
	
	private static class DistinctIterator implements BooleanIterator
	{
		BooleanIterator iterator;
		int filtered;
		boolean lastFound;
		boolean foundNext = false;
		
		public DistinctIterator(BooleanIterator iterator) {
			this.iterator = iterator;
		}
		
		void compute() {
			if(foundNext) return;
			if(filtered == 3) return;
			while(iterator.hasNext()) {
				lastFound = iterator.nextBoolean();
				if((filtered & (lastFound ? 1 : 2)) == 0) {
					filtered |= (lastFound ? 1 : 2);
					foundNext = true;
					break;
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			compute();
			return foundNext;
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new IllegalStateException("End of Iterator");
			foundNext = false;
			return lastFound;
		}
	}
	
	private static class FilteredIterator implements BooleanIterator
	{
		BooleanIterator iterator;
		Boolean2BooleanFunction filter;
		boolean lastFound;
		boolean foundNext = false;
		
		public FilteredIterator(BooleanIterator iterator, Boolean2BooleanFunction filter) {
			this.iterator = iterator;
			this.filter = filter;
		}
		
		void compute() {
			if(foundNext) return;
			while(iterator.hasNext()) {
				lastFound = iterator.nextBoolean();
				if(filter.get(lastFound)) {
					foundNext = true;
					break;
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			compute();
			return foundNext;
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new IllegalStateException("End of Iterator");
			foundNext = false;
			return lastFound;
		}
	}
	
	private static class LimitedIterator implements BooleanIterator
	{
		BooleanIterator iterator;
		long limit;
		
		public LimitedIterator(BooleanIterator iterator, long limit) {
			this.iterator = iterator;
			this.limit = limit;
		}
		
		@Override
		public boolean hasNext() {
			return limit > 0 && iterator.hasNext();
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new IllegalStateException("End of Iterator");
			limit--;
			return iterator.nextBoolean();
		}
	}
	
	private static class PeekIterator implements BooleanIterator
	{
		BooleanIterator iterator;
		BooleanConsumer action;
		
		public PeekIterator(BooleanIterator iterator, BooleanConsumer action) {
			this.iterator = iterator;
			this.action = action;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new IllegalStateException("End of Iterator");
			boolean result = iterator.nextBoolean();
			action.accept(result);
			return result;
		}
	}
}