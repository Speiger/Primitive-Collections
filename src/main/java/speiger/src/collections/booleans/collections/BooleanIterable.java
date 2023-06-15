package speiger.src.collections.booleans.collections;

import java.util.Objects;
import java.util.function.Consumer;


import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.booleans.functions.function.BooleanFunction;
import speiger.src.collections.ints.functions.consumer.IntBooleanConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.booleans.functions.function.BooleanPredicate;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.utils.BooleanArrays;
import speiger.src.collections.booleans.utils.BooleanAsyncBuilder;
import speiger.src.collections.booleans.utils.BooleanSplititerators;
import speiger.src.collections.booleans.utils.BooleanIterables;
import speiger.src.collections.booleans.utils.BooleanIterators;
import speiger.src.collections.utils.ISizeProvider;

/**
 * A Type-Specific {@link Iterable} that reduces (un)boxing
 */
public interface BooleanIterable extends Iterable<Boolean>
{
	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	BooleanIterator iterator();
	
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
	default void forEach(BooleanConsumer action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/** {@inheritDoc}
	* <p>This default implementation delegates to the corresponding type-specific function.
	* @deprecated Please use the corresponding type-specific function instead. 
	*/
	@Deprecated
	@Override
	default void forEach(Consumer<? super Boolean> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/**
	 * A Indexed forEach implementation that allows you to keep track of how many elements were already iterated over.
	 * @param action The action to be performed for each element
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	public default void forEachIndexed(IntBooleanConsumer action) {
		Objects.requireNonNull(action);
		int index = 0;
		for(BooleanIterator iter = iterator();iter.hasNext();action.accept(index++, iter.nextBoolean()));
	}
	
	/**
	 * Helper function to reduce Lambda usage and allow for more method references, since these are faster/cleaner.
	 * @param input the object that should be included
	 * @param action The action to be performed for each element
	 * @param <E> the generic type of the Object
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	default <E> void forEach(E input, ObjectBooleanConsumer<E> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(input, action);
	}
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default BooleanSplititerator spliterator() { return BooleanSplititerators.createUnknownSplititerator(iterator(), 0); }
	
	/**
	 * Creates a Async Builder for moving work of the thread.
	 * It is not designed to split the work to multithreaded work, so using this keep it singlethreaded, but it allows to be moved to another thread.
	 * @see BooleanAsyncBuilder
	 * @return a AsyncBuilder
	 */
	default BooleanAsyncBuilder asAsync() {
		return new BooleanAsyncBuilder(this);
	}

	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the mapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E> ObjectIterable<E> map(BooleanFunction<E> mapper) {
		return BooleanIterables.map(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 * @note does not support toBooleanArray optimizations.
	 */
	default <E, V extends Iterable<E>> ObjectIterable<E> flatMap(BooleanFunction<V> mapper) {
		return BooleanIterables.flatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 * @note does not support toBooleanArray optimizations.
	 */
	default <E> ObjectIterable<E> arrayflatMap(BooleanFunction<E[]> mapper) {
		return BooleanIterables.arrayFlatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out unwanted elements
	 * @param filter the elements that should be kept.
	 * @return a Iterable that filtered out all unwanted elements
	 * @note does not support toBooleanArray optimizations.
	 */
	default BooleanIterable filter(BooleanPredicate filter) {
		return BooleanIterables.filter(this, filter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out duplicated elements
	 * @return a Iterable that filtered out all duplicated elements
	 * @note does not support toBooleanArray optimizations.
	 */
	default BooleanIterable distinct() {
		return BooleanIterables.distinct(this);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to repeat elements a desired amount of times
	 * @param repeats how many times the elements should be repeated
	 * @return a Iterable that is repeating multiple times
	 */
	default BooleanIterable repeat(int repeats) {
		return BooleanIterables.repeat(this, repeats);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to limit the amount of elements
	 * @param limit the amount of elements it should be limited to
	 * @return a Iterable that is limited in length
	 */
	default BooleanIterable limit(long limit) {
		return BooleanIterables.limit(this, limit);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to sort the elements
	 * @param sorter that sorts the elements.
	 * @return a Iterable that is sorted
	 */
	default BooleanIterable sorted(BooleanComparator sorter) {
		return BooleanIterables.sorted(this, sorter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to preview elements before they are iterated through
	 * @param action the action that should be applied
	 * @return a Peeked Iterable
	 */
	default BooleanIterable peek(BooleanConsumer action) {
		return BooleanIterables.peek(this, action);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to collect all elements
	 * @param collection that the elements should be inserted to
	 * @param <E> the collection type
	 * @return the input with the desired elements
	 */
	default <E extends BooleanCollection> E pour(E collection) {
		BooleanIterators.pour(iterator(), collection);
		return collection;
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a ArrayList
	 * @return a new ArrayList of all elements
	 */
	default BooleanList pourAsList() {
		return pour(new BooleanArrayList());
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a Array
	 * @return a new Array of all elements
	 */
	default boolean[] toBooleanArray() {
		ISizeProvider prov = ISizeProvider.of(this);
		if(prov != null) {
			int size = prov.size();
			if(size >= 0) {
				boolean[] array = new boolean[size];
				BooleanIterators.unwrap(array, iterator());
				return array;
			}
		}
		return BooleanArrays.pour(iterator());
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for any matches.
	 * @param filter that should be applied
	 * @return true if any matches were found
	 */
	default boolean matchesAny(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(BooleanIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextBoolean())) return true;
		}
		return false;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for no matches.
	 * @param filter that should be applied
	 * @return true if no matches were found
	 */
	default boolean matchesNone(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(BooleanIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextBoolean())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for all matches.
	 * @param filter that should be applied
	 * @return true if all matches.
	 */
	default boolean matchesAll(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(BooleanIterator iter = iterator();iter.hasNext();) {
			if(!filter.test(iter.nextBoolean())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for the first match.
	 * @param filter that should be applied
	 * @return the found value or the null equivalent variant.
	 */
	default boolean findFirst(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		for(BooleanIterator iter = iterator();iter.hasNext();) {
			boolean entry = iter.nextBoolean();
			if(filter.test(entry)) return entry;
		}
		return false;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
     * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @param identity the start value
	 * @return the reduction result, returns identity if nothing was found
	 */
	default boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) {
		Objects.requireNonNull(operator);
		boolean state = identity;
		for(BooleanIterator iter = iterator();iter.hasNext();) {
			state = operator.applyAsBoolean(state, iter.nextBoolean());
		}
		return state;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
	 * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @return the reduction result, returns null value if nothing was found
	 */
	default boolean reduce(BooleanBooleanUnaryOperator operator) {
		Objects.requireNonNull(operator);
		boolean state = false;
		boolean empty = true;
		for(BooleanIterator iter = iterator();iter.hasNext();) {
			if(empty) {
				empty = false;
				state = iter.nextBoolean();
				continue;
			}
			state = operator.applyAsBoolean(state, iter.nextBoolean());
		}
		return state;
	}	
	
	/**
	 * Helper function to reduce stream usage that allows to count the valid elements.
	 * @param filter that should be applied
	 * @return the amount of Valid Elements
	 */
	default int count(BooleanPredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(BooleanIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextBoolean())) result++;
		}
		return result;
	}
}