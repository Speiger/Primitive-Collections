package speiger.src.collections.shorts.collections;

import java.util.Objects;
import java.util.function.Consumer;

import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.shorts.functions.function.Short2ObjectFunction;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.sets.ShortLinkedOpenHashSet;
import speiger.src.collections.shorts.utils.ShortAsyncBuilder;
import speiger.src.collections.shorts.utils.ShortSplititerators;
import speiger.src.collections.shorts.utils.ShortIterables;
import speiger.src.collections.shorts.utils.ShortIterators;

/**
 * A Type-Specific {@link Iterable} that reduces (un)boxing
 */
public interface ShortIterable extends Iterable<Short>
{
	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	ShortIterator iterator();
	
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
	default void forEach(ShortConsumer action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/** {@inheritDoc}
	* <p>This default implementation delegates to the corresponding type-specific function.
	* @deprecated Please use the corresponding type-specific function instead. 
	*/
	@Deprecated
	@Override
	default void forEach(Consumer<? super Short> action) {
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
	default <E> void forEach(E input, ObjectShortConsumer<E> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(input, action);
	}
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ShortSplititerator spliterator() { return ShortSplititerators.createUnknownSplititerator(iterator(), 0); }
	
	/**
	 * Creates a Async Builder for moving work of the thread.
	 * It is not designed to split the work to multithreaded work, so using this keep it singlethreaded, but it allows to be moved to another thread.
	 * @see ShortAsyncBuilder
	 * @return a AsyncBuilder
	 */
	default ShortAsyncBuilder asAsync() {
		return new ShortAsyncBuilder(this);
	}

	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the mapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E> ObjectIterable<E> map(Short2ObjectFunction<E> mapper) {
		return ShortIterables.map(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E, V extends Iterable<E>> ObjectIterable<E> flatMap(Short2ObjectFunction<V> mapper) {
		return ShortIterables.flatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E> ObjectIterable<E> arrayflatMap(Short2ObjectFunction<E[]> mapper) {
		return ShortIterables.arrayFlatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out unwanted elements
	 * @param filter the elements that should be kept.
	 * @return a Iterable that filtered out all unwanted elements
	 */
	default ShortIterable filter(Short2BooleanFunction filter) {
		return ShortIterables.filter(this, filter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out duplicated elements
	 * @return a Iterable that filtered out all duplicated elements
	 */
	default ShortIterable distinct() {
		return ShortIterables.distinct(this);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to limit the amount of elements
	 * @param limit the amount of elements it should be limited to
	 * @return a Iterable that is limited in length
	 */
	default ShortIterable limit(long limit) {
		return ShortIterables.limit(this, limit);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to sort the elements
	 * @param sorter that sorts the elements.
	 * @return a Iterable that is sorted
	 */
	default ShortIterable sorted(ShortComparator sorter) {
		return ShortIterables.sorted(this, sorter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to preview elements before they are iterated through
	 * @param action the action that should be applied
	 * @return a Peeked Iterable
	 */
	default ShortIterable peek(ShortConsumer action) {
		return ShortIterables.peek(this, action);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to collect all elements
	 * @param collection that the elements should be inserted to
	 * @param <E> the collection type
	 * @return the input with the desired elements
	 */
	default <E extends ShortCollection> E pour(E collection) {
		ShortIterators.pour(iterator(), collection);
		return collection;
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a ArrayList
	 * @return a new ArrayList of all elements
	 */
	default ShortList pourAsList() {
		return pour(new ShortArrayList()); 
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a LinkedHashSet
	 * @return a new LinkedHashSet of all elements
	 */
	default ShortSet pourAsSet() { 
		return pour(new ShortLinkedOpenHashSet()); 		
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for any matches.
	 * @param filter that should be applied
	 * @return true if any matches were found
	 */
	default boolean matchesAny(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(ShortIterator iter = iterator();iter.hasNext();) {
			if(filter.get(iter.nextShort())) return true;
		}
		return false;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for no matches.
	 * @param filter that should be applied
	 * @return true if no matches were found
	 */
	default boolean matchesNone(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(ShortIterator iter = iterator();iter.hasNext();) {
			if(filter.get(iter.nextShort())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for all matches.
	 * @param filter that should be applied
	 * @return true if all matches.
	 */
	default boolean matchesAll(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(ShortIterator iter = iterator();iter.hasNext();) {
			if(!filter.get(iter.nextShort())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for the first match.
	 * @param filter that should be applied
	 * @return the found value or the null equivalent variant.
	 */
	default short findFirst(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(ShortIterator iter = iterator();iter.hasNext();) {
			short entry = iter.nextShort();
			if(filter.get(entry)) return entry;
		}
		return (short)0;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
     * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @param identity the start value
	 * @return the reduction result, returns identity if nothing was found
	 */
	default short reduce(short identity, ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = identity;
		for(ShortIterator iter = iterator();iter.hasNext();) {
			state = operator.applyAsShort(state, iter.nextShort());
		}
		return state;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
	 * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @return the reduction result, returns null value if nothing was found
	 */
	default short reduce(ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = (short)0;
		boolean empty = true;
		for(ShortIterator iter = iterator();iter.hasNext();) {
			if(empty) {
				empty = false;
				state = iter.nextShort();
				continue;
			}
			state = operator.applyAsShort(state, iter.nextShort());
		}
		return state;
	}	
	
	/**
	 * Helper function to reduce stream usage that allows to count the valid elements.
	 * @param filter that should be applied
	 * @return the amount of Valid Elements
	 */
	default int count(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(ShortIterator iter = iterator();iter.hasNext();) {
			if(filter.get(iter.nextShort())) result++;
		}
		return result;
	}
}