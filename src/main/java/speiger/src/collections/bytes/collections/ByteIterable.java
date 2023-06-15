package speiger.src.collections.bytes.collections;

import java.util.Objects;
import java.util.function.Consumer;


import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.bytes.functions.function.ByteFunction;
import speiger.src.collections.ints.functions.consumer.IntByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.function.BytePredicate;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.sets.ByteLinkedOpenHashSet;
import speiger.src.collections.bytes.utils.ByteArrays;
import speiger.src.collections.bytes.utils.ByteAsyncBuilder;
import speiger.src.collections.bytes.utils.ByteSplititerators;
import speiger.src.collections.bytes.utils.ByteIterables;
import speiger.src.collections.bytes.utils.ByteIterators;
import speiger.src.collections.utils.ISizeProvider;

/**
 * A Type-Specific {@link Iterable} that reduces (un)boxing
 */
public interface ByteIterable extends Iterable<Byte>
{
	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	ByteIterator iterator();
	
	/**
	 * A Type Specific foreach function that reduces (un)boxing
	 * 
	 * @implSpec
	 * <p>The default implementation behaves as if:
	 * <pre>{@code
	 *	iterator().forEachRemaining(action);
	 * }</pre>
	 *
	 * @param action The action to be performed for each element
	 * @throws NullPointerException if the specified action is null
	 * @see Iterable#forEach(Consumer)
	 */
	default void forEach(ByteConsumer action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/** {@inheritDoc}
	* <p>This default implementation delegates to the corresponding type-specific function.
	* @deprecated Please use the corresponding type-specific function instead. 
	*/
	@Deprecated
	@Override
	default void forEach(Consumer<? super Byte> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/**
	 * A Indexed forEach implementation that allows you to keep track of how many elements were already iterated over.
	 * @param action The action to be performed for each element
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	public default void forEachIndexed(IntByteConsumer action) {
		Objects.requireNonNull(action);
		int index = 0;
		for(ByteIterator iter = iterator();iter.hasNext();action.accept(index++, iter.nextByte()));
	}
	
	/**
	 * Helper function to reduce Lambda usage and allow for more method references, since these are faster/cleaner.
	 * @param input the object that should be included
	 * @param action The action to be performed for each element
	 * @param <E> the generic type of the Object
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	default <E> void forEach(E input, ObjectByteConsumer<E> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(input, action);
	}
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ByteSplititerator spliterator() { return ByteSplititerators.createUnknownSplititerator(iterator(), 0); }
	
	/**
	 * Creates a Async Builder for moving work of the thread.
	 * It is not designed to split the work to multithreaded work, so using this keep it singlethreaded, but it allows to be moved to another thread.
	 * @see ByteAsyncBuilder
	 * @return a AsyncBuilder
	 */
	default ByteAsyncBuilder asAsync() {
		return new ByteAsyncBuilder(this);
	}

	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the mapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E> ObjectIterable<E> map(ByteFunction<E> mapper) {
		return ByteIterables.map(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 * @note does not support toByteArray optimizations.
	 */
	default <E, V extends Iterable<E>> ObjectIterable<E> flatMap(ByteFunction<V> mapper) {
		return ByteIterables.flatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 * @note does not support toByteArray optimizations.
	 */
	default <E> ObjectIterable<E> arrayflatMap(ByteFunction<E[]> mapper) {
		return ByteIterables.arrayFlatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out unwanted elements
	 * @param filter the elements that should be kept.
	 * @return a Iterable that filtered out all unwanted elements
	 * @note does not support toByteArray optimizations.
	 */
	default ByteIterable filter(BytePredicate filter) {
		return ByteIterables.filter(this, filter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out duplicated elements
	 * @return a Iterable that filtered out all duplicated elements
	 * @note does not support toByteArray optimizations.
	 */
	default ByteIterable distinct() {
		return ByteIterables.distinct(this);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to repeat elements a desired amount of times
	 * @param repeats how many times the elements should be repeated
	 * @return a Iterable that is repeating multiple times
	 */
	default ByteIterable repeat(int repeats) {
		return ByteIterables.repeat(this, repeats);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to limit the amount of elements
	 * @param limit the amount of elements it should be limited to
	 * @return a Iterable that is limited in length
	 */
	default ByteIterable limit(long limit) {
		return ByteIterables.limit(this, limit);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to sort the elements
	 * @param sorter that sorts the elements.
	 * @return a Iterable that is sorted
	 */
	default ByteIterable sorted(ByteComparator sorter) {
		return ByteIterables.sorted(this, sorter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to preview elements before they are iterated through
	 * @param action the action that should be applied
	 * @return a Peeked Iterable
	 */
	default ByteIterable peek(ByteConsumer action) {
		return ByteIterables.peek(this, action);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to collect all elements
	 * @param collection that the elements should be inserted to
	 * @param <E> the collection type
	 * @return the input with the desired elements
	 */
	default <E extends ByteCollection> E pour(E collection) {
		ByteIterators.pour(iterator(), collection);
		return collection;
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a ArrayList
	 * @return a new ArrayList of all elements
	 */
	default ByteList pourAsList() {
		return pour(new ByteArrayList());
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a LinkedHashSet
	 * @return a new LinkedHashSet of all elements
	 */
	default ByteSet pourAsSet() { 
		return pour(new ByteLinkedOpenHashSet());
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a Array
	 * @return a new Array of all elements
	 */
	default byte[] toByteArray() {
		ISizeProvider prov = ISizeProvider.of(this);
		if(prov != null) {
			int size = prov.size();
			if(size >= 0) {
				byte[] array = new byte[size];
				ByteIterators.unwrap(array, iterator());
				return array;
			}
		}
		return ByteArrays.pour(iterator());
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for any matches.
	 * @param filter that should be applied
	 * @return true if any matches were found
	 */
	default boolean matchesAny(BytePredicate filter) {
		Objects.requireNonNull(filter);
		for(ByteIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextByte())) return true;
		}
		return false;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for no matches.
	 * @param filter that should be applied
	 * @return true if no matches were found
	 */
	default boolean matchesNone(BytePredicate filter) {
		Objects.requireNonNull(filter);
		for(ByteIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextByte())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for all matches.
	 * @param filter that should be applied
	 * @return true if all matches.
	 */
	default boolean matchesAll(BytePredicate filter) {
		Objects.requireNonNull(filter);
		for(ByteIterator iter = iterator();iter.hasNext();) {
			if(!filter.test(iter.nextByte())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for the first match.
	 * @param filter that should be applied
	 * @return the found value or the null equivalent variant.
	 */
	default byte findFirst(BytePredicate filter) {
		Objects.requireNonNull(filter);
		for(ByteIterator iter = iterator();iter.hasNext();) {
			byte entry = iter.nextByte();
			if(filter.test(entry)) return entry;
		}
		return (byte)0;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
     * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @param identity the start value
	 * @return the reduction result, returns identity if nothing was found
	 */
	default byte reduce(byte identity, ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte state = identity;
		for(ByteIterator iter = iterator();iter.hasNext();) {
			state = operator.applyAsByte(state, iter.nextByte());
		}
		return state;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
	 * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @return the reduction result, returns null value if nothing was found
	 */
	default byte reduce(ByteByteUnaryOperator operator) {
		Objects.requireNonNull(operator);
		byte state = (byte)0;
		boolean empty = true;
		for(ByteIterator iter = iterator();iter.hasNext();) {
			if(empty) {
				empty = false;
				state = iter.nextByte();
				continue;
			}
			state = operator.applyAsByte(state, iter.nextByte());
		}
		return state;
	}	
	
	/**
	 * Helper function to reduce stream usage that allows to count the valid elements.
	 * @param filter that should be applied
	 * @return the amount of Valid Elements
	 */
	default int count(BytePredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(ByteIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextByte())) result++;
		}
		return result;
	}
}