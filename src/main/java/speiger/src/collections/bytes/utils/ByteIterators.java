package speiger.src.collections.bytes.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.functions.function.ByteFunction;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.function.BytePredicate;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.lists.ByteArrayList;

import speiger.src.collections.bytes.lists.ByteListIterator;
import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.utils.ByteCollections.CollectionWrapper;

/**
 * A Helper class for Iterators
 */
public class ByteIterators
{
	/**
	 * Empty Iterator Reference
	 */
	private static final EmptyIterator EMPTY = new EmptyIterator();
	
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
	public static ByteBidirectionalIterator invert(ByteBidirectionalIterator it) {
		return it instanceof ReverseBiIterator ? ((ReverseBiIterator)it).it : new ReverseBiIterator(it);
	}
	
	/**
	 * Inverter function for List Iterators
	 * @param it the iterator that should be inverted
	 * @return a Inverted List Iterator. If it was inverted then it just gives back the original reference
	 */
	public static ByteListIterator invert(ByteListIterator it) {
		return it instanceof ReverseListIterator ? ((ReverseListIterator)it).it : new ReverseListIterator(it);
	}
	
	/**
	 * Returns a Immutable Iterator instance based on the instance given.
	 * @param iterator that should be made immutable/unmodifiable
	 * @return a unmodifiable iterator wrapper. If the Iterator already a unmodifiable wrapper then it just returns itself.
	 */
	public static ByteIterator unmodifiable(ByteIterator iterator) {
		return iterator instanceof UnmodifiableIterator ? iterator : new UnmodifiableIterator(iterator);
	}
	
	/**
	 * Returns a Immutable Iterator instance based on the instance given.
	 * @param iterator that should be made immutable/unmodifiable
	 * @return a unmodifiable iterator wrapper. If the Iterator already a unmodifiable wrapper then it just returns itself.
	 */
	public static ByteBidirectionalIterator unmodifiable(ByteBidirectionalIterator iterator) {
		return iterator instanceof UnmodifiableBiIterator ? iterator : new UnmodifiableBiIterator(iterator);
	}
	
	/**
	 * Returns a Immutable ListIterator instance based on the instance given.
	 * @param iterator that should be made immutable/unmodifiable
	 * @return a unmodifiable listiterator wrapper. If the ListIterator already a unmodifiable wrapper then it just returns itself.
	 */
	public static ByteListIterator unmodifiable(ByteListIterator iterator) {
		return iterator instanceof UnmodifiableListIterator ? iterator : new UnmodifiableListIterator(iterator);
	}
	
	/**
	 * A Helper function that maps a Java-Iterator into a new Type.
	 * @param iterator that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is mapped to a new result
	 */
	public static <E> ObjectIterator<E> map(Iterator<? extends Byte> iterator, ByteFunction<E> mapper) {
		return new MappedIterator<>(wrap(iterator), mapper);
	}
	
	/**
	 * A Helper function that maps a Iterator into a new Type.
	 * @param iterator that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is mapped to a new result
	 */
	public static <E> ObjectIterator<E> map(ByteIterator iterator, ByteFunction<E> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterator<E> flatMap(Iterator<? extends Byte> iterator, ByteFunction<V> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterator<E> flatMap(ByteIterator iterator, ByteFunction<V> mapper) {
		return new FlatMappedIterator<>(iterator, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterator into a new Type.
	 * @param iterator that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is flatMapped to a new result
	 */
	public static <E> ObjectIterator<E> arrayFlatMap(Iterator<? extends Byte> iterator, ByteFunction<E[]> mapper) {
		return new FlatMappedArrayIterator<>(wrap(iterator), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterator into a new Type.
	 * @param iterator that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is flatMapped to a new result
	 */
	public static <E> ObjectIterator<E> arrayFlatMap(ByteIterator iterator, ByteFunction<E[]> mapper) {
		return new FlatMappedArrayIterator<>(iterator, mapper);
	}
	
	/**
	 * A Helper function that filters out all desired elements from a Java-Iterator
	 * @param iterator that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterator
	 */
	public static ByteIterator filter(Iterator<? extends Byte> iterator, BytePredicate filter) {
		return new FilteredIterator(wrap(iterator), filter);
	}
	
	/**
	 * A Helper function that filters out all desired elements
	 * @param iterator that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterator
	 */
	public static ByteIterator filter(ByteIterator iterator, BytePredicate filter) {
		return new FilteredIterator(iterator, filter);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements.
	 * @param iterator that should be distinct
	 * @return a distinct iterator
	 */
	public static ByteIterator distinct(ByteIterator iterator) {
		return new DistinctIterator(iterator);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements from a Java Iterator.
	 * @param iterator that should be distinct
	 * @return a distinct iterator
	 */
	public static ByteIterator distinct(Iterator<? extends Byte> iterator) {
		return new DistinctIterator(wrap(iterator));
	}
	
	/**
	 * A Helper function that repeats the Iterator a specific amount of times
	 * @param iterator that should be repeated
	 * @param repeats the amount of times the iterator should be repeated
	 * @return a repeating iterator
	 */
	public static ByteIterator repeat(ByteIterator iterator, int repeats) {
		return new RepeatingIterator(iterator, repeats);
	}
	
	/**
	 * A Helper function that repeats the Iterator a specific amount of times from a Java Iterator
	 * @param iterator that should be repeated
	 * @param repeats the amount of times the iterator should be repeated
	 * @return a repeating iterator
	 */
	public static ByteIterator repeat(Iterator<? extends Byte> iterator, int repeats) {
		return new RepeatingIterator(wrap(iterator), repeats);
	}
	
	/**
	 * A Helper function that creates a infinitely looping iterator
	 * @param iterator that should be looping infinitely
	 * @return a infinitely looping iterator
	 */
	public static ByteIterator infinite(ByteIterator iterator) {
		return new InfiniteIterator(iterator);
	}
	
	/**
	 * A Helper function that creates a infinitely looping iterator from a Java Iterator
	 * @param iterator that should be looping infinitely
	 * @return a infinitely looping iterator
	 */
	public static ByteIterator infinite(Iterator<? extends Byte> iterator) {
		return new InfiniteIterator(wrap(iterator));
	}
	
	/**
	 * A Helper function that hard limits the Iterator to a specific size
	 * @param iterator that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterator
	 */
	public static ByteIterator limit(ByteIterator iterator, long limit) {
		return new LimitedIterator(iterator, limit);
	}
	
	/**
	 * A Helper function that hard limits the Iterator to a specific size from a Java Iterator
	 * @param iterator that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterator
	 */
	public static ByteIterator limit(Iterator<? extends Byte> iterator, long limit) {
		return new LimitedIterator(wrap(iterator), limit);
	}
	
	/**
	 * A Helper function that sorts the Iterator beforehand.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterator that should be sorted.
	 * @param sorter the sorter of the iterator. Can be null.
	 * @return a new sorted iterator
	 */
	public static ByteIterator sorted(ByteIterator iterator, ByteComparator sorter) {
		return new SortedIterator(iterator, sorter);
	}
	
	/**
	 * A Helper function that sorts the Iterator beforehand from a Java Iterator.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterator that should be sorted.
	 * @param sorter the sorter of the iterator. Can be null.
	 * @return a new sorted iterator
	 */
	public static ByteIterator sorted(Iterator<? extends Byte> iterator, ByteComparator sorter) {
		return new SortedIterator(wrap(iterator), sorter);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterator.
	 * @param iterator that should be peeked at
	 * @param action callback that receives the value before the iterator returns it
	 * @return a peeked iterator
	 */
	public static ByteIterator peek(ByteIterator iterator, ByteConsumer action) {
		return new PeekIterator(iterator, action);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterator  from a Java Iterator
	 * @param iterator that should be peeked at
	 * @param action callback that receives the value before the iterator returns it
	 * @return a peeked iterator
	 */
	public static ByteIterator peek(Iterator<? extends Byte> iterator, ByteConsumer action) {
		return new PeekIterator(wrap(iterator), action);
	}
	
	/**
	 * Helper function to convert a Object Iterator into a Primitive Iterator
	 * @param iterator that should be converted to a unboxing iterator
	 * @return a primitive iterator
	 */
	public static ByteIterator wrap(Iterator<? extends Byte> iterator) {
		return iterator instanceof ByteIterator ? (ByteIterator)iterator : new IteratorWrapper(iterator);
	}
	
	/**
	 * Returns a Array Wrapping iterator
	 * @param a the array that should be wrapped
	 * @return a Iterator that is wrapping a array.
	 */
	public static ArrayIterator wrap(byte... a) {
		return wrap(a, 0, a.length);
	}
	
	/**
	 * Returns a Array Wrapping iterator
	 * @param a the array that should be wrapped.
	 * @param start the index to be started from.
	 * @param end the index that should be ended.
	 * @return a Iterator that is wrapping a array.
	 */
	public static ArrayIterator wrap(byte[] a, int start, int end) {
		return new ArrayIterator(a, start, end);
	}
	
	/**
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(byte[] a, Iterator<? extends Byte> i) {
		return unwrap(a, i, 0, a.length);
	}
	
	/**
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(byte[] a, Iterator<? extends Byte> i, int offset) {
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
	public static int unwrap(byte[] a, Iterator<? extends Byte> i, int offset, int max) {
		if(max < 0) throw new IllegalStateException("The max size is smaller then 0");
		if(offset + max > a.length) throw new IllegalStateException("largest array index exceeds array size");
		int index = 0;
		for(;index<max && i.hasNext();index++) a[index+offset] = i.next().byteValue();
		return index;
	}
	
	/**
	 * A Primitive iterator variant of the ByteIterators unwrap function
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(byte[] a, ByteIterator i) {
		return unwrap(a, i, 0, a.length);
	}
	
	/**
	 * A Primitive iterator variant of the ByteIterators unwrap function
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(byte[] a, ByteIterator i, int offset) {
		return unwrap(a, i, offset, a.length - offset);
	}
	
	/**
	 * A Primitive iterator variant of the ByteIterators unwrap function
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @param max the maximum values that should be extracted from the source
	 * @return the amount of elements that were inserted into the array.
	 * @throws IllegalStateException if max is smaller the 0 or if the maximum index is larger then the array
	 */
	public static int unwrap(byte[] a, ByteIterator i, int offset, int max) {
		if(max < 0) throw new IllegalStateException("The max size is smaller then 0");
		if(offset + max > a.length) throw new IllegalStateException("largest array index exceeds array size");
		int index = 0;
		for(;index<max && i.hasNext();index++) a[index+offset] = i.nextByte();
		return index;
	}
	
	/**
	 * A Function to convert a Primitive Iterator to a Object array.
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(Byte[] a, ByteIterator i) {
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
	public static int unwrap(Byte[] a, ByteIterator i, int offset) {
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
	public static int unwrap(Byte[] a, ByteIterator i, int offset, int max) {
		if(max < 0) throw new IllegalStateException("The max size is smaller then 0");
		if(offset + max > a.length) throw new IllegalStateException("largest array index exceeds array size");
		int index = 0;
		for(;index<max && i.hasNext();index++) a[index+offset] = Byte.valueOf(i.nextByte());
		return index;
	}
	
	/**
	 * A Helper function to pours all elements of a Iterator into a List
	 * @param iter the elements that should be poured into list.
	 * @return A list of all elements of the Iterator
	 */
	public static ByteList pour(ByteIterator iter) {
		return pour(iter, Integer.MAX_VALUE);
	}
	
	/**
	 * A Helper function to pours all elements of a Iterator into a List
	 * @param iter the elements that should be poured into list.
	 * @param max the maximum amount of elements that should be collected
	 * @return A list of all requested elements of the Iterator
	 */
	public static ByteList pour(ByteIterator iter, int max) {
		ByteArrayList list = new ByteArrayList();
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
	public static int pour(ByteIterator iter, ByteCollection c) {
		return pour(iter, c, Integer.MAX_VALUE);
	}
	
	/**
	 * A Helper function to pours all elements of a Iterator into a Collection
	 * @param iter the elements that should be poured into list.
	 * @param c the collection where the elements should be poured into
	 * @param max the maximum amount of elements that should be collected
	 * @return the amount of elements that were added
	 */
	public static int pour(ByteIterator iter, ByteCollection c, int max) {
		if(max < 0) throw new IllegalStateException("Max is negative");
		int done = 0;
		for(;done<max && iter.hasNext();done++, c.add(iter.nextByte()));
		return done;
	}
	
	/**
	 * Helper Iterator that concats other iterators together
	 * @param array the Iterators that should be concatenated
	 * @return iterator of the inputted iterators
	 */
	public static ByteIterator concat(ByteIterator... array) {
		return concat(array, 0, array.length);
	}
	
	/**
	 * Helper Iterator that concats other iterators together
	 * @param array the Iterators that should be concatenated
	 * @param offset where to start within the array
	 * @param length the length of the array
	 * @return iterator of the inputted iterators
	 */
	public static ByteIterator concat(ByteIterator[] array, int offset, int length) {
		return new ConcatIterator(array, offset, length);
	}
	
	private static class IteratorWrapper implements ByteIterator
	{
		Iterator<? extends Byte> iter;
		
		public IteratorWrapper(Iterator<? extends Byte> iter) {
			this.iter = iter;
		}
		
		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}
		
		@Override
		public byte nextByte() {
			return iter.next().byteValue();
		}
		
		@Override
		@Deprecated
		public Byte next() {
			return iter.next();
		}
	}
	
	private static class ConcatIterator implements ByteIterator
	{
		ByteIterator[] iters;
		int offset;
		int lastOffset = -1;
		int length;
		
		public ConcatIterator(ByteIterator[] iters, int offset, int length) {
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
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			byte result = iters[lastOffset = offset].nextByte();
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
	
	private static class ReverseBiIterator implements ByteBidirectionalIterator {
		ByteBidirectionalIterator it;
		
		ReverseBiIterator(ByteBidirectionalIterator it) {
			this.it = it;
		}
		
		@Override
		public byte nextByte() { return it.previousByte(); }
		@Override
		public boolean hasNext() { return it.hasPrevious(); }
		@Override
		public boolean hasPrevious() { return it.hasNext(); }
		@Override
		public byte previousByte() { return it.nextByte(); }
		@Override
		public void remove() { it.remove(); }
	}
	
	private static class ReverseListIterator implements ByteListIterator {
		ByteListIterator it;
		
		ReverseListIterator(ByteListIterator it) {
			this.it = it;
		}
	
		@Override
		public byte nextByte() { return it.previousByte(); }
		@Override
		public boolean hasNext() { return it.hasPrevious(); }
		@Override
		public boolean hasPrevious() { return it.hasNext(); }
		@Override
		public byte previousByte() { return it.nextByte(); }
		@Override
		public void remove() { it.remove(); }
		@Override
		public int nextIndex() { return it.previousIndex(); }
		@Override
		public int previousIndex() { return it.nextIndex(); }
		@Override
		public void set(byte e) { it.set(e); }
		@Override
		public void add(byte e) { it.add(e); }
	}
	
	private static class UnmodifiableListIterator implements ByteListIterator
	{
		ByteListIterator iter;
	
		UnmodifiableListIterator(ByteListIterator iter) {
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
		public byte previousByte() {
			return iter.previousByte();
		}
		
		@Override
		public byte nextByte() {
			return iter.nextByte();
		}

		@Override
		public void set(byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(byte e) { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableBiIterator implements ByteBidirectionalIterator
	{
		ByteBidirectionalIterator iter;
		
		UnmodifiableBiIterator(ByteBidirectionalIterator iter) {
			this.iter = iter;
		}
		
		@Override
		public byte nextByte() {
			return iter.nextByte();
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
		public byte previousByte() {
			return iter.previousByte();
		}
	}
	
	private static class UnmodifiableIterator implements ByteIterator
	{
		ByteIterator iterator;
	
		UnmodifiableIterator(ByteIterator iterator) {
			this.iterator = iterator;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		@Override
		public byte nextByte() {
			return iterator.nextByte();
		}
	}
	
	private static class EmptyIterator implements ByteListIterator
	{
		@Override
		public boolean hasNext() { return false; }
		@Override
		public byte nextByte() { throw new NoSuchElementException(); }
		@Override
		public boolean hasPrevious() { return false; }
		@Override
		public byte previousByte() { throw new NoSuchElementException(); }
		@Override
		public int nextIndex() { return 0; }
		@Override
		public int previousIndex() { return -1; }
		@Override
		public void remove() { throw new UnsupportedOperationException(); }
		@Override
		public void set(byte e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(byte e) { throw new UnsupportedOperationException(); }
	}
	
	private static class ArrayIterator implements ByteIterator
	{
		byte[] a;
		int from;
		int to;
		
		ArrayIterator(byte[] a, int from, int to) {
			this.a = a;
			this.from = from;
			this.to = to;
		}
		
		@Override
		public boolean hasNext() {
			return from < to;
		}
		
		@Override
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
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
		ByteIterator iterator;
		ByteFunction<T> mapper;
		
		MappedIterator(ByteIterator iterator, ByteFunction<T> mapper) {
			this.iterator = iterator;
			this.mapper = mapper;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		@Override
		public T next() {
			return mapper.apply(iterator.nextByte());
		}
		
		@Override
		public int skip(int amount) {
			return iterator.skip(amount);
		}
	}
	
	private static class FlatMappedIterator<T, V extends Iterable<T>> implements ObjectIterator<T>
	{
		ByteIterator iterator;
		Iterator<T> last = null;
		ByteFunction<V> mapper;
		boolean foundNext = false;
		
		FlatMappedIterator(ByteIterator iterator, ByteFunction<V> mapper) {
			this.iterator = iterator;
			this.mapper = mapper;
		}
		
		void compute() {
			if(foundNext) return;
			foundNext = true;
			while(iterator.hasNext()) {
				if(last != null && last.hasNext()) return;
				last = mapper.apply(iterator.nextByte()).iterator();
			}
		}
		
		@Override
		public boolean hasNext() {
			compute();
			return last != null && last.hasNext();
		}
		
		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			T result = last.next();
			foundNext = false;
			return result;
		}
	}
	
	private static class FlatMappedArrayIterator<T> implements ObjectIterator<T>
	{
		ByteIterator iterator;
		Iterator<T> last = null;
		ByteFunction<T[]> mapper;
		boolean foundNext = false;
		
		FlatMappedArrayIterator(ByteIterator iterator, ByteFunction<T[]> mapper) {
			this.iterator = iterator;
			this.mapper = mapper;
		}
		
		void compute() {
			if(foundNext) return;
			foundNext = true;
			while(iterator.hasNext()) {
				if(last != null && last.hasNext()) return;
				last = ObjectIterators.wrap(mapper.apply(iterator.nextByte()));
			}
		}
		
		@Override
		public boolean hasNext() {
			compute();
			return last != null && last.hasNext();
		}
		
		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			T result = last.next();
			foundNext = false;
			return result;
		}
	}
	
	private static class InfiniteIterator implements ByteIterator
	{
		ByteIterator iter;
		CollectionWrapper looper = ByteCollections.wrapper();
		int index = 0;
		
		public InfiniteIterator(ByteIterator iter) {
			this.iter = iter;
		}
		
		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public byte nextByte() {
			if(iter != null) {
				if(iter.hasNext()) {
					byte value = iter.nextByte();
					looper.add(value);
					return value;
				}
				else iter = null;
			}
			return looper.getByte((index++) % looper.size());
		}
		
		@Override
		public void forEachRemaining(ByteConsumer action) { throw new UnsupportedOperationException("This is a instant deadlock, so unsupported"); }
		public void forEachRemaining(Consumer<? super Byte> action) { throw new UnsupportedOperationException("This is a instant deadlock, so unsupported"); }
		public <E> void forEachRemaining(E input, ObjectByteConsumer<E> action) { throw new UnsupportedOperationException("This is a instant deadlock, so unsupported"); }
	}
	
	private static class RepeatingIterator implements ByteIterator
	{
		final int repeats;
		int index = 0;
		ByteIterator iter;
		ByteCollection repeater = ByteCollections.wrapper();
		
		public RepeatingIterator(ByteIterator iter, int repeat) {
			this.iter = iter;
			this.repeats = repeat;
		}
		
		@Override
		public boolean hasNext() {
			if(iter.hasNext()) return true;
			if(index < repeats) {
				index++;
				iter = repeater.iterator();
				return iter.hasNext();
			}
			return false;
		}

		@Override
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			byte value = iter.nextByte();
			if(index == 0) repeater.add(value);
			return value;
		}
	}
	
	private static class SortedIterator implements ByteIterator
	{
		ByteIterator iterator;
		ByteComparator sorter;
		ByteCollections.CollectionWrapper sortedElements = null;
		int index = 0;
		
		public SortedIterator(ByteIterator iterator, ByteComparator sorter) {
			this.iterator = iterator;
			this.sorter = sorter;
		}
		
		@Override
		public boolean hasNext() {
			if(sortedElements == null) {
				boolean hasNext = iterator.hasNext();
				if(hasNext) {
					sortedElements = ByteCollections.wrapper();
					pour(iterator, sortedElements);
				}
				else sortedElements = ByteCollections.wrapper();
				if(hasNext) sortedElements.unstableSort(sorter);
			}
			return index < sortedElements.size();
		}
		
		@Override
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			return sortedElements.getByte(index++);
		}
	}
	
	private static class DistinctIterator implements ByteIterator
	{
		ByteIterator iterator;
		ByteCollection filtered = ByteCollections.distinctWrapper();
		byte lastFound;
		boolean foundNext = false;
		
		public DistinctIterator(ByteIterator iterator) {
			this.iterator = iterator;
		}
		
		void compute() {
			if(foundNext) return;
			while(iterator.hasNext()) {
				lastFound = iterator.nextByte();
				if(filtered.add(lastFound)) {
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
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			foundNext = false;
			return lastFound;
		}
	}
	
	private static class FilteredIterator implements ByteIterator
	{
		ByteIterator iterator;
		BytePredicate filter;
		byte lastFound;
		boolean foundNext = false;
		
		public FilteredIterator(ByteIterator iterator, BytePredicate filter) {
			this.iterator = iterator;
			this.filter = filter;
		}
		
		void compute() {
			if(foundNext) return;
			while(iterator.hasNext()) {
				lastFound = iterator.nextByte();
				if(filter.test(lastFound)) {
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
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			foundNext = false;
			return lastFound;
		}
	}
	
	private static class LimitedIterator implements ByteIterator
	{
		ByteIterator iterator;
		long limit;
		
		public LimitedIterator(ByteIterator iterator, long limit) {
			this.iterator = iterator;
			this.limit = limit;
		}
		
		@Override
		public boolean hasNext() {
			return limit > 0 && iterator.hasNext();
		}
		
		@Override
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			limit--;
			return iterator.nextByte();
		}
	}
	
	private static class PeekIterator implements ByteIterator
	{
		ByteIterator iterator;
		ByteConsumer action;
		
		public PeekIterator(ByteIterator iterator, ByteConsumer action) {
			this.iterator = iterator;
			this.action = action;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		@Override
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			byte result = iterator.nextByte();
			action.accept(result);
			return result;
		}
	}
}