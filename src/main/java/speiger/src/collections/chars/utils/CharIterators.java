package speiger.src.collections.chars.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.functions.function.Char2ObjectFunction;
import speiger.src.collections.chars.functions.function.Char2BooleanFunction;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.lists.CharListIterator;
import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.sets.CharOpenHashSet;
import speiger.src.collections.chars.sets.CharSet;

/**
 * A Helper class for Iterators
 */
public class CharIterators
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
	public static CharBidirectionalIterator invert(CharBidirectionalIterator it) {
		return it instanceof ReverseBiIterator ? ((ReverseBiIterator)it).it : new ReverseBiIterator(it);
	}
	
	/**
	 * Inverter function for List Iterators
	 * @param it the iterator that should be inverted
	 * @return a Inverted List Iterator. If it was inverted then it just gives back the original reference
	 */
	public static CharListIterator invert(CharListIterator it) {
		return it instanceof ReverseListIterator ? ((ReverseListIterator)it).it : new ReverseListIterator(it);
	}
	
	/**
	 * Returns a Immutable Iterator instance based on the instance given.
	 * @param iterator that should be made immutable/unmodifiable
	 * @return a unmodifiable iterator wrapper. If the Iterator already a unmodifiable wrapper then it just returns itself.
	 */
	public static CharIterator unmodifiable(CharIterator iterator) {
		return iterator instanceof UnmodifiableIterator ? iterator : new UnmodifiableIterator(iterator);
	}
	
	/**
	 * Returns a Immutable Iterator instance based on the instance given.
	 * @param iterator that should be made immutable/unmodifiable
	 * @return a unmodifiable iterator wrapper. If the Iterator already a unmodifiable wrapper then it just returns itself.
	 */
	public static CharBidirectionalIterator unmodifiable(CharBidirectionalIterator iterator) {
		return iterator instanceof UnmodifiableBiIterator ? iterator : new UnmodifiableBiIterator(iterator);
	}
	
	/**
	 * Returns a Immutable ListIterator instance based on the instance given.
	 * @param iterator that should be made immutable/unmodifiable
	 * @return a unmodifiable listiterator wrapper. If the ListIterator already a unmodifiable wrapper then it just returns itself.
	 */
	public static CharListIterator unmodifiable(CharListIterator iterator) {
		return iterator instanceof UnmodifiableListIterator ? iterator : new UnmodifiableListIterator(iterator);
	}
	
	/**
	 * A Helper function that maps a Java-Iterator into a new Type.
	 * @param iterator that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is mapped to a new result
	 */
	public static <E> ObjectIterator<E> map(Iterator<? extends Character> iterator, Char2ObjectFunction<E> mapper) {
		return new MappedIterator<>(wrap(iterator), mapper);
	}
	
	/**
	 * A Helper function that maps a Iterator into a new Type.
	 * @param iterator that should be mapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is mapped to a new result
	 */
	public static <E> ObjectIterator<E> map(CharIterator iterator, Char2ObjectFunction<E> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterator<E> flatMap(Iterator<? extends Character> iterator, Char2ObjectFunction<V> mapper) {
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
	public static <E, V extends Iterable<E>> ObjectIterator<E> flatMap(CharIterator iterator, Char2ObjectFunction<V> mapper) {
		return new FlatMappedIterator<>(iterator, mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Java-Iterator into a new Type.
	 * @param iterator that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is flatMapped to a new result
	 */
	public static <E> ObjectIterator<E> arrayFlatMap(Iterator<? extends Character> iterator, Char2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterator<>(wrap(iterator), mapper);
	}
	
	/**
	 * A Helper function that flatMaps a Iterator into a new Type.
	 * @param iterator that should be flatMapped
	 * @param mapper the function that decides what the result turns into.
	 * @param <E> The return type.
	 * @return a iterator that is flatMapped to a new result
	 */
	public static <E> ObjectIterator<E> arrayFlatMap(CharIterator iterator, Char2ObjectFunction<E[]> mapper) {
		return new FlatMappedArrayIterator<>(iterator, mapper);
	}
	
	/**
	 * A Helper function that filters out all desired elements from a Java-Iterator
	 * @param iterator that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterator
	 */
	public static CharIterator filter(Iterator<? extends Character> iterator, Char2BooleanFunction filter) {
		return new FilteredIterator(wrap(iterator), filter);
	}
	
	/**
	 * A Helper function that filters out all desired elements
	 * @param iterator that should be filtered.
	 * @param filter the filter that decides that should be let through
	 * @return a filtered iterator
	 */
	public static CharIterator filter(CharIterator iterator, Char2BooleanFunction filter) {
		return new FilteredIterator(iterator, filter);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements.
	 * @param iterator that should be distinct
	 * @return a distinct iterator
	 */
	public static CharIterator distinct(CharIterator iterator) {
		return new DistinctIterator(iterator);
	}
	
	/**
	 * A Helper function that filters out all duplicated elements from a Java Iterator.
	 * @param iterator that should be distinct
	 * @return a distinct iterator
	 */
	public static CharIterator distinct(Iterator<? extends Character> iterator) {
		return new DistinctIterator(wrap(iterator));
	}
	
	/**
	 * A Helper function that hard limits the Iterator to a specific size
	 * @param iterator that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterator
	 */
	public static CharIterator limit(CharIterator iterator, long limit) {
		return new LimitedIterator(iterator, limit);
	}
	
	/**
	 * A Helper function that hard limits the Iterator to a specific size from a Java Iterator
	 * @param iterator that should be limited
	 * @param limit the amount of elements it should be limited to
	 * @return a limited iterator
	 */
	public static CharIterator limit(Iterator<? extends Character> iterator, long limit) {
		return new LimitedIterator(wrap(iterator), limit);
	}
	
	/**
	 * A Helper function that sorts the Iterator beforehand.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterator that should be sorted.
	 * @param sorter the sorter of the iterator. Can be null.
	 * @return a new sorted iterator
	 */
	public static CharIterator sorted(CharIterator iterator, CharComparator sorter) {
		return new SortedIterator(iterator, sorter);
	}
	
	/**
	 * A Helper function that sorts the Iterator beforehand from a Java Iterator.
	 * This operation is heavily hurting performance because it rebuilds the entire iterator and then sorts it.
	 * @param iterator that should be sorted.
	 * @param sorter the sorter of the iterator. Can be null.
	 * @return a new sorted iterator
	 */
	public static CharIterator sorted(Iterator<? extends Character> iterator, CharComparator sorter) {
		return new SortedIterator(wrap(iterator), sorter);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterator.
	 * @param iterator that should be peeked at
	 * @param action callback that receives the value before the iterator returns it
	 * @return a peeked iterator
	 */
	public static CharIterator peek(CharIterator iterator, CharConsumer action) {
		return new PeekIterator(iterator, action);
	}
	
	/**
	 * A Helper function that allows to preview the result of a Iterator  from a Java Iterator
	 * @param iterator that should be peeked at
	 * @param action callback that receives the value before the iterator returns it
	 * @return a peeked iterator
	 */
	public static CharIterator peek(Iterator<? extends Character> iterator, CharConsumer action) {
		return new PeekIterator(wrap(iterator), action);
	}
	
	/**
	 * Helper function to convert a Object Iterator into a Primitive Iterator
	 * @param iterator that should be converted to a unboxing iterator
	 * @return a primitive iterator
	 */
	public static CharIterator wrap(Iterator<? extends Character> iterator) {
		return iterator instanceof CharIterator ? (CharIterator)iterator : new IteratorWrapper(iterator);
	}
	
	/**
	 * Returns a Array Wrapping iterator
	 * @param a the array that should be wrapped
	 * @return a Iterator that is wrapping a array.
	 */
	public static ArrayIterator wrap(char[] a) {
		return wrap(a, 0, a.length);
	}
	
	/**
	 * Returns a Array Wrapping iterator
	 * @param a the array that should be wrapped.
	 * @param start the index to be started from.
	 * @param end the index that should be ended.
	 * @return a Iterator that is wrapping a array.
	 */
	public static ArrayIterator wrap(char[] a, int start, int end) {
		return new ArrayIterator(a, start, end);
	}
	
	/**
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(char[] a, Iterator<? extends Character> i) {
		return unwrap(a, i, 0, a.length);
	}
	
	/**
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(char[] a, Iterator<? extends Character> i, int offset) {
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
	public static int unwrap(char[] a, Iterator<? extends Character> i, int offset, int max) {
		if(max < 0) throw new IllegalStateException("The max size is smaller then 0");
		if(offset + max > a.length) throw new IllegalStateException("largest array index exceeds array size");
		int index = 0;
		for(;index<max && i.hasNext();index++) a[index+offset] = i.next().charValue();
		return index;
	}
	
	/**
	 * A Primitive iterator variant of the CharIterators unwrap function
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(char[] a, CharIterator i) {
		return unwrap(a, i, 0, a.length);
	}
	
	/**
	 * A Primitive iterator variant of the CharIterators unwrap function
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(char[] a, CharIterator i, int offset) {
		return unwrap(a, i, offset, a.length - offset);
	}
	
	/**
	 * A Primitive iterator variant of the CharIterators unwrap function
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @param offset the array offset where the start should be
	 * @param max the maximum values that should be extracted from the source
	 * @return the amount of elements that were inserted into the array.
	 * @throws IllegalStateException if max is smaller the 0 or if the maximum index is larger then the array
	 */
	public static int unwrap(char[] a, CharIterator i, int offset, int max) {
		if(max < 0) throw new IllegalStateException("The max size is smaller then 0");
		if(offset + max > a.length) throw new IllegalStateException("largest array index exceeds array size");
		int index = 0;
		for(;index<max && i.hasNext();index++) a[index+offset] = i.nextChar();
		return index;
	}
	
	/**
	 * A Function to convert a Primitive Iterator to a Object array.
	 * Iterates over a iterator and inserts the values into the array and returns the amount that was inserted
	 * @param a where the elements should be inserted
	 * @param i the source iterator
	 * @return the amount of elements that were inserted into the array.
	 */
	public static int unwrap(Character[] a, CharIterator i) {
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
	public static int unwrap(Character[] a, CharIterator i, int offset) {
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
	public static int unwrap(Character[] a, CharIterator i, int offset, int max) {
		if(max < 0) throw new IllegalStateException("The max size is smaller then 0");
		if(offset + max > a.length) throw new IllegalStateException("largest array index exceeds array size");
		int index = 0;
		for(;index<max && i.hasNext();index++) a[index+offset] = Character.valueOf(i.nextChar());
		return index;
	}
	
	/**
	 * A Helper function to pours all elements of a Iterator into a List
	 * @param iter the elements that should be poured into list.
	 * @return A list of all elements of the Iterator
	 */
	public static CharList pour(CharIterator iter) {
		return pour(iter, Integer.MAX_VALUE);
	}
	
	/**
	 * A Helper function to pours all elements of a Iterator into a List
	 * @param iter the elements that should be poured into list.
	 * @param max the maximum amount of elements that should be collected
	 * @return A list of all requested elements of the Iterator
	 */
	public static CharList pour(CharIterator iter, int max) {
		CharArrayList list = new CharArrayList();
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
	public static int pour(CharIterator iter, CharCollection c) {
		return pour(iter, c, Integer.MAX_VALUE);
	}
	
	/**
	 * A Helper function to pours all elements of a Iterator into a Collection
	 * @param iter the elements that should be poured into list.
	 * @param c the collection where the elements should be poured into
	 * @param max the maximum amount of elements that should be collected
	 * @return the amount of elements that were added
	 */
	public static int pour(CharIterator iter, CharCollection c, int max) {
		if(max < 0) throw new IllegalStateException("Max is negative");
		int done = 0;
		for(;done<max && iter.hasNext();done++, c.add(iter.nextChar()));
		return done;
	}
	
	/**
	 * Helper Iterator that concats other iterators together
	 * @param array the Iterators that should be concatenated
	 * @return iterator of the inputted iterators
	 */
	public static CharIterator concat(CharIterator... array) {
		return concat(array, 0, array.length);
	}
	
	/**
	 * Helper Iterator that concats other iterators together
	 * @param array the Iterators that should be concatenated
	 * @param offset where to start within the array
	 * @param length the length of the array
	 * @return iterator of the inputted iterators
	 */
	public static CharIterator concat(CharIterator[] array, int offset, int length) {
		return new ConcatIterator(array, offset, length);
	}
	
	private static class IteratorWrapper implements CharIterator
	{
		Iterator<? extends Character> iter;
		
		public IteratorWrapper(Iterator<? extends Character> iter) {
			this.iter = iter;
		}
		
		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}
		
		@Override
		public char nextChar() {
			return iter.next().charValue();
		}
		
		@Override
		@Deprecated
		public Character next() {
			return iter.next();
		}
	}
	
	private static class ConcatIterator implements CharIterator
	{
		CharIterator[] iters;
		int offset;
		int lastOffset = -1;
		int length;
		
		public ConcatIterator(CharIterator[] iters, int offset, int length) {
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
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			char result = iters[lastOffset = offset].nextChar();
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
	
	private static class ReverseBiIterator implements CharBidirectionalIterator {
		CharBidirectionalIterator it;
		
		ReverseBiIterator(CharBidirectionalIterator it) {
			this.it = it;
		}
		
		@Override
		public char nextChar() { return it.previousChar(); }
		@Override
		public boolean hasNext() { return it.hasPrevious(); }
		@Override
		public boolean hasPrevious() { return it.hasNext(); }
		@Override
		public char previousChar() { return it.nextChar(); }
		@Override
		public void remove() { it.remove(); }
	}

	private static class ReverseListIterator implements CharListIterator {
		CharListIterator it;
		
		ReverseListIterator(CharListIterator it) {
			this.it = it;
		}
	
		@Override
		public char nextChar() { return it.previousChar(); }
		@Override
		public boolean hasNext() { return it.hasPrevious(); }
		@Override
		public boolean hasPrevious() { return it.hasNext(); }
		@Override
		public char previousChar() { return it.nextChar(); }
		@Override
		public void remove() { it.remove(); }
		@Override
		public int nextIndex() { return it.previousIndex(); }
		@Override
		public int previousIndex() { return it.nextIndex(); }
		@Override
		public void set(char e) { it.set(e); }
		@Override
		public void add(char e) { it.add(e); }
	}
	
	private static class UnmodifiableListIterator implements CharListIterator
	{
		CharListIterator iter;
	
		UnmodifiableListIterator(CharListIterator iter) {
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
		public char previousChar() {
			return iter.previousChar();
		}
		
		@Override
		public char nextChar() {
			return iter.nextChar();
		}

		@Override
		public void set(char e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(char e) { throw new UnsupportedOperationException(); }
	}
	
	private static class UnmodifiableBiIterator implements CharBidirectionalIterator
	{
		CharBidirectionalIterator iter;
		
		UnmodifiableBiIterator(CharBidirectionalIterator iter) {
			this.iter = iter;
		}
		
		@Override
		public char nextChar() {
			return iter.nextChar();
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
		public char previousChar() {
			return iter.previousChar();
		}
	}
	
	private static class UnmodifiableIterator implements CharIterator
	{
		CharIterator iterator;
	
		UnmodifiableIterator(CharIterator iterator) {
			this.iterator = iterator;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		@Override
		public char nextChar() {
			return iterator.nextChar();
		}
	}

	private static class EmptyIterator implements CharListIterator
	{
		@Override
		public boolean hasNext() {
			return false;
		}
	
		@Override
		public char nextChar() {
			throw new NoSuchElementException();
		}

		@Override
		public boolean hasPrevious() {
			return false;
		}
		
		@Override
		public char previousChar() {
			throw new NoSuchElementException();
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
		public void set(char e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(char e) { throw new UnsupportedOperationException(); }
	}
	
	private static class ArrayIterator implements CharIterator
	{
		char[] a;
		int from;
		int to;
		
		ArrayIterator(char[] a, int from, int to) {
			this.a = a;
			this.from = from;
			this.to = to;
		}
		
		@Override
		public boolean hasNext() {
			return from < to;
		}
		
		@Override
		public char nextChar() {
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
		CharIterator iterator;
		Char2ObjectFunction<T> mapper;
		
		MappedIterator(CharIterator iterator, Char2ObjectFunction<T> mapper) {
			this.iterator = iterator;
			this.mapper = mapper;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		@Override
		public T next() {
			return mapper.get(iterator.nextChar());
		}
		
		@Override
		public int skip(int amount) {
			return iterator.skip(amount);
		}
	}
	
	private static class FlatMappedIterator<T, V extends Iterable<T>> implements ObjectIterator<T>
	{
		CharIterator iterator;
		Iterator<T> last = null;
		Char2ObjectFunction<V> mapper;
		boolean foundNext = false;
		
		FlatMappedIterator(CharIterator iterator, Char2ObjectFunction<V> mapper) {
			this.iterator = iterator;
			this.mapper = mapper;
		}
		
		void compute() {
			if(foundNext) return;
			foundNext = true;
			while(iterator.hasNext()) {
				if(last != null && last.hasNext()) return;
				last = mapper.get(iterator.nextChar()).iterator();
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
		CharIterator iterator;
		Iterator<T> last = null;
		Char2ObjectFunction<T[]> mapper;
		boolean foundNext = false;
		
		FlatMappedArrayIterator(CharIterator iterator, Char2ObjectFunction<T[]> mapper) {
			this.iterator = iterator;
			this.mapper = mapper;
		}
		
		void compute() {
			if(foundNext) return;
			foundNext = true;
			while(iterator.hasNext()) {
				if(last != null && last.hasNext()) return;
				last = ObjectIterators.wrap(mapper.get(iterator.nextChar()));
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
	
	private static class SortedIterator implements CharIterator
	{
		CharIterator iterator;
		CharComparator sorter;
		CharList sortedElements = null;
		int index = 0;
		
		public SortedIterator(CharIterator iterator, CharComparator sorter) {
			this.iterator = iterator;
			this.sorter = sorter;
		}
		
		@Override
		public boolean hasNext() {
			if(sortedElements == null) {
				boolean hasNext = iterator.hasNext();
				sortedElements = hasNext ? pour(iterator) : CharLists.empty();
				if(hasNext) sortedElements.unstableSort(sorter);
			}
			return index < sortedElements.size();
		}
		
		@Override
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			return sortedElements.getChar(index++);
		}
	}
	
	private static class DistinctIterator implements CharIterator
	{
		CharIterator iterator;
		CharSet filtered = new CharOpenHashSet();
		char lastFound;
		boolean foundNext = false;
		
		public DistinctIterator(CharIterator iterator) {
			this.iterator = iterator;
		}
		
		void compute() {
			if(foundNext) return;
			while(iterator.hasNext()) {
				lastFound = iterator.nextChar();
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
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			foundNext = false;
			return lastFound;
		}
	}
	
	private static class FilteredIterator implements CharIterator
	{
		CharIterator iterator;
		Char2BooleanFunction filter;
		char lastFound;
		boolean foundNext = false;
		
		public FilteredIterator(CharIterator iterator, Char2BooleanFunction filter) {
			this.iterator = iterator;
			this.filter = filter;
		}
		
		void compute() {
			if(foundNext) return;
			while(iterator.hasNext()) {
				lastFound = iterator.nextChar();
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
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			foundNext = false;
			return lastFound;
		}
	}
	
	private static class LimitedIterator implements CharIterator
	{
		CharIterator iterator;
		long limit;
		
		public LimitedIterator(CharIterator iterator, long limit) {
			this.iterator = iterator;
			this.limit = limit;
		}
		
		@Override
		public boolean hasNext() {
			return limit > 0 && iterator.hasNext();
		}
		
		@Override
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			limit--;
			return iterator.nextChar();
		}
	}
	
	private static class PeekIterator implements CharIterator
	{
		CharIterator iterator;
		CharConsumer action;
		
		public PeekIterator(CharIterator iterator, CharConsumer action) {
			this.iterator = iterator;
			this.action = action;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		@Override
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			char result = iterator.nextChar();
			action.accept(result);
			return result;
		}
	}
}