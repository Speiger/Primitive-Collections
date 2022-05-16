package speiger.src.collections.longs.collections;

import java.util.Objects;
import java.util.function.Consumer;

import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.longs.functions.function.Long2ObjectFunction;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.sets.LongLinkedOpenHashSet;
import speiger.src.collections.longs.utils.LongAsyncBuilder;
import speiger.src.collections.longs.utils.LongSplititerators;
import speiger.src.collections.longs.utils.LongIterables;
import speiger.src.collections.longs.utils.LongIterators;

/**
 * A Type-Specific {@link Iterable} that reduces (un)boxing
 */
public interface LongIterable extends Iterable<Long>
{
	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	LongIterator iterator();
	
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
	default void forEach(LongConsumer action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/** {@inheritDoc}
	* <p>This default implementation delegates to the corresponding type-specific function.
	* @deprecated Please use the corresponding type-specific function instead. 
	*/
	@Deprecated
	@Override
	default void forEach(Consumer<? super Long> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/**
	 * Helper function to reduce Lambda usage and allow for more method references, since these are faster/cleaner.
	 * @param input the object that should be included
	 * @param action The action to be performed for each element
	 * @param <E> the generic type of the Object
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	default <E> void forEach(E input, ObjectLongConsumer<E> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(input, action);
	}
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default LongSplititerator spliterator() { return LongSplititerators.createUnknownSplititerator(iterator(), 0); }
	
	/**
	 * Creates a Async Builder for moving work of the thread.
	 * It is not designed to split the work to multithreaded work, so using this keep it singlethreaded, but it allows to be moved to another thread.
	 * @see LongAsyncBuilder
	 * @return a AsyncBuilder
	 */
	default LongAsyncBuilder asAsync() {
		return new LongAsyncBuilder(this);
	}

	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the mapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E> ObjectIterable<E> map(Long2ObjectFunction<E> mapper) {
		return LongIterables.map(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E, V extends Iterable<E>> ObjectIterable<E> flatMap(Long2ObjectFunction<V> mapper) {
		return LongIterables.flatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E> ObjectIterable<E> arrayflatMap(Long2ObjectFunction<E[]> mapper) {
		return LongIterables.arrayFlatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out unwanted elements
	 * @param filter the elements that should be kept.
	 * @return a Iterable that filtered out all unwanted elements
	 */
	default LongIterable filter(Long2BooleanFunction filter) {
		return LongIterables.filter(this, filter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out duplicated elements
	 * @return a Iterable that filtered out all duplicated elements
	 */
	default LongIterable distinct() {
		return LongIterables.distinct(this);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to limit the amount of elements
	 * @param limit the amount of elements it should be limited to
	 * @return a Iterable that is limited in length
	 */
	default LongIterable limit(long limit) {
		return LongIterables.limit(this, limit);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to sort the elements
	 * @param sorter that sorts the elements.
	 * @return a Iterable that is sorted
	 */
	default LongIterable sorted(LongComparator sorter) {
		return LongIterables.sorted(this, sorter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to preview elements before they are iterated through
	 * @param action the action that should be applied
	 * @return a Peeked Iterable
	 */
	default LongIterable peek(LongConsumer action) {
		return LongIterables.peek(this, action);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to collect all elements
	 * @param collection that the elements should be inserted to
	 * @param <E> the collection type
	 * @return the input with the desired elements
	 */
	default <E extends LongCollection> E pour(E collection) {
		LongIterators.pour(iterator(), collection);
		return collection;
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a ArrayList
	 * @return a new ArrayList of all elements
	 */
	default LongList pourAsList() {
		return pour(new LongArrayList()); 
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a LinkedHashSet
	 * @return a new LinkedHashSet of all elements
	 */
	default LongSet pourAsSet() { 
		return pour(new LongLinkedOpenHashSet()); 		
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for any matches.
	 * @param filter that should be applied
	 * @return true if any matches were found
	 */
	default boolean matchesAny(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(LongIterator iter = iterator();iter.hasNext();) {
			if(filter.get(iter.nextLong())) return true;
		}
		return false;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for no matches.
	 * @param filter that should be applied
	 * @return true if no matches were found
	 */
	default boolean matchesNone(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(LongIterator iter = iterator();iter.hasNext();) {
			if(filter.get(iter.nextLong())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for all matches.
	 * @param filter that should be applied
	 * @return true if all matches.
	 */
	default boolean matchesAll(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(LongIterator iter = iterator();iter.hasNext();) {
			if(!filter.get(iter.nextLong())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for the first match.
	 * @param filter that should be applied
	 * @return the found value or the null equivalent variant.
	 */
	default long findFirst(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(LongIterator iter = iterator();iter.hasNext();) {
			long entry = iter.nextLong();
			if(filter.get(entry)) return entry;
		}
		return 0L;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
     * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @param identity the start value
	 * @return the reduction result, returns identity if nothing was found
	 */
	default long reduce(long identity, LongLongUnaryOperator operator) {
		Objects.requireNonNull(operator);
		long state = identity;
		for(LongIterator iter = iterator();iter.hasNext();) {
			state = operator.applyAsLong(state, iter.nextLong());
		}
		return state;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
	 * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @return the reduction result, returns null value if nothing was found
	 */
	default long reduce(LongLongUnaryOperator operator) {
		Objects.requireNonNull(operator);
		long state = 0L;
		boolean empty = true;
		for(LongIterator iter = iterator();iter.hasNext();) {
			if(empty) {
				empty = false;
				state = iter.nextLong();
				continue;
			}
			state = operator.applyAsLong(state, iter.nextLong());
		}
		return state;
	}	
	
	/**
	 * Helper function to reduce stream usage that allows to count the valid elements.
	 * @param filter that should be applied
	 * @return the amount of Valid Elements
	 */
	default int count(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(LongIterator iter = iterator();iter.hasNext();) {
			if(filter.get(iter.nextLong())) result++;
		}
		return result;
	}
}