package speiger.src.collections.floats.collections;

import java.util.Objects;
import java.util.function.Consumer;


import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.floats.functions.function.FloatFunction;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.sets.FloatLinkedOpenHashSet;
import speiger.src.collections.floats.utils.FloatArrays;
import speiger.src.collections.floats.utils.FloatAsyncBuilder;
import speiger.src.collections.floats.utils.FloatSplititerators;
import speiger.src.collections.floats.utils.FloatIterables;
import speiger.src.collections.floats.utils.FloatIterators;
import speiger.src.collections.utils.ISizeProvider;

/**
 * A Type-Specific {@link Iterable} that reduces (un)boxing
 */
public interface FloatIterable extends Iterable<Float>
{
	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	FloatIterator iterator();
	
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
	default void forEach(FloatConsumer action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/** {@inheritDoc}
	* <p>This default implementation delegates to the corresponding type-specific function.
	* @deprecated Please use the corresponding type-specific function instead. 
	*/
	@Deprecated
	@Override
	default void forEach(Consumer<? super Float> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/**
	 * A Indexed forEach implementation that allows you to keep track of how many elements were already iterated over.
	 * @param action The action to be performed for each element
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	public default void forEachIndexed(IntFloatConsumer action) {
		Objects.requireNonNull(action);
		int index = 0;
		for(FloatIterator iter = iterator();iter.hasNext();action.accept(index++, iter.nextFloat()));
	}
	
	/**
	 * Helper function to reduce Lambda usage and allow for more method references, since these are faster/cleaner.
	 * @param input the object that should be included
	 * @param action The action to be performed for each element
	 * @param <E> the generic type of the Object
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	default <E> void forEach(E input, ObjectFloatConsumer<E> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(input, action);
	}
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default FloatSplititerator spliterator() { return FloatSplititerators.createUnknownSplititerator(iterator(), 0); }
	
	/**
	 * Creates a Async Builder for moving work of the thread.
	 * It is not designed to split the work to multithreaded work, so using this keep it singlethreaded, but it allows to be moved to another thread.
	 * @see FloatAsyncBuilder
	 * @return a AsyncBuilder
	 */
	default FloatAsyncBuilder asAsync() {
		return new FloatAsyncBuilder(this);
	}

	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the mapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E> ObjectIterable<E> map(FloatFunction<E> mapper) {
		return FloatIterables.map(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 * @note does not support toFloatArray optimizations.
	 */
	default <E, V extends Iterable<E>> ObjectIterable<E> flatMap(FloatFunction<V> mapper) {
		return FloatIterables.flatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 * @note does not support toFloatArray optimizations.
	 */
	default <E> ObjectIterable<E> arrayflatMap(FloatFunction<E[]> mapper) {
		return FloatIterables.arrayFlatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out unwanted elements
	 * @param filter the elements that should be kept.
	 * @return a Iterable that filtered out all unwanted elements
	 * @note does not support toFloatArray optimizations.
	 */
	default FloatIterable filter(FloatPredicate filter) {
		return FloatIterables.filter(this, filter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out duplicated elements
	 * @return a Iterable that filtered out all duplicated elements
	 * @note does not support toFloatArray optimizations.
	 */
	default FloatIterable distinct() {
		return FloatIterables.distinct(this);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to repeat elements a desired amount of times
	 * @param repeats how many times the elements should be repeated
	 * @return a Iterable that is repeating multiple times
	 */
	default FloatIterable repeat(int repeats) {
		return FloatIterables.repeat(this, repeats);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to limit the amount of elements
	 * @param limit the amount of elements it should be limited to
	 * @return a Iterable that is limited in length
	 */
	default FloatIterable limit(long limit) {
		return FloatIterables.limit(this, limit);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to sort the elements
	 * @param sorter that sorts the elements.
	 * @return a Iterable that is sorted
	 */
	default FloatIterable sorted(FloatComparator sorter) {
		return FloatIterables.sorted(this, sorter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to preview elements before they are iterated through
	 * @param action the action that should be applied
	 * @return a Peeked Iterable
	 */
	default FloatIterable peek(FloatConsumer action) {
		return FloatIterables.peek(this, action);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to collect all elements
	 * @param collection that the elements should be inserted to
	 * @param <E> the collection type
	 * @return the input with the desired elements
	 */
	default <E extends FloatCollection> E pour(E collection) {
		FloatIterators.pour(iterator(), collection);
		return collection;
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a ArrayList
	 * @return a new ArrayList of all elements
	 */
	default FloatList pourAsList() {
		return pour(new FloatArrayList());
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a LinkedHashSet
	 * @return a new LinkedHashSet of all elements
	 */
	default FloatSet pourAsSet() { 
		return pour(new FloatLinkedOpenHashSet());
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a Array
	 * @return a new Array of all elements
	 */
	default float[] toFloatArray() {
		ISizeProvider prov = ISizeProvider.of(this);
		if(prov != null) {
			int size = prov.size();
			if(size >= 0) {
				float[] array = new float[size];
				FloatIterators.unwrap(array, iterator());
				return array;
			}
		}
		return FloatArrays.pour(iterator());
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for any matches.
	 * @param filter that should be applied
	 * @return true if any matches were found
	 */
	default boolean matchesAny(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(FloatIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextFloat())) return true;
		}
		return false;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for no matches.
	 * @param filter that should be applied
	 * @return true if no matches were found
	 */
	default boolean matchesNone(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(FloatIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextFloat())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for all matches.
	 * @param filter that should be applied
	 * @return true if all matches.
	 */
	default boolean matchesAll(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(FloatIterator iter = iterator();iter.hasNext();) {
			if(!filter.test(iter.nextFloat())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for the first match.
	 * @param filter that should be applied
	 * @return the found value or the null equivalent variant.
	 */
	default float findFirst(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		for(FloatIterator iter = iterator();iter.hasNext();) {
			float entry = iter.nextFloat();
			if(filter.test(entry)) return entry;
		}
		return 0F;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
     * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @param identity the start value
	 * @return the reduction result, returns identity if nothing was found
	 */
	default float reduce(float identity, FloatFloatUnaryOperator operator) {
		Objects.requireNonNull(operator);
		float state = identity;
		for(FloatIterator iter = iterator();iter.hasNext();) {
			state = operator.applyAsFloat(state, iter.nextFloat());
		}
		return state;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
	 * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @return the reduction result, returns null value if nothing was found
	 */
	default float reduce(FloatFloatUnaryOperator operator) {
		Objects.requireNonNull(operator);
		float state = 0F;
		boolean empty = true;
		for(FloatIterator iter = iterator();iter.hasNext();) {
			if(empty) {
				empty = false;
				state = iter.nextFloat();
				continue;
			}
			state = operator.applyAsFloat(state, iter.nextFloat());
		}
		return state;
	}	
	
	/**
	 * Helper function to reduce stream usage that allows to count the valid elements.
	 * @param filter that should be applied
	 * @return the amount of Valid Elements
	 */
	default int count(FloatPredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(FloatIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextFloat())) result++;
		}
		return result;
	}
}