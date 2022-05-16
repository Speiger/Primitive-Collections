package speiger.src.collections.doubles.collections;

import java.util.Objects;
import java.util.function.Consumer;

import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.doubles.functions.function.Double2ObjectFunction;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleLinkedOpenHashSet;
import speiger.src.collections.doubles.utils.DoubleAsyncBuilder;
import speiger.src.collections.doubles.utils.DoubleSplititerators;
import speiger.src.collections.doubles.utils.DoubleIterables;
import speiger.src.collections.doubles.utils.DoubleIterators;

/**
 * A Type-Specific {@link Iterable} that reduces (un)boxing
 */
public interface DoubleIterable extends Iterable<Double>
{
	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	DoubleIterator iterator();
	
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
	default void forEach(DoubleConsumer action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/** {@inheritDoc}
	* <p>This default implementation delegates to the corresponding type-specific function.
	* @deprecated Please use the corresponding type-specific function instead. 
	*/
	@Deprecated
	@Override
	default void forEach(Consumer<? super Double> action) {
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
	default <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(input, action);
	}
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default DoubleSplititerator spliterator() { return DoubleSplititerators.createUnknownSplititerator(iterator(), 0); }
	
	/**
	 * Creates a Async Builder for moving work of the thread.
	 * It is not designed to split the work to multithreaded work, so using this keep it singlethreaded, but it allows to be moved to another thread.
	 * @see DoubleAsyncBuilder
	 * @return a AsyncBuilder
	 */
	default DoubleAsyncBuilder asAsync() {
		return new DoubleAsyncBuilder(this);
	}

	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the mapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E> ObjectIterable<E> map(Double2ObjectFunction<E> mapper) {
		return DoubleIterables.map(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E, V extends Iterable<E>> ObjectIterable<E> flatMap(Double2ObjectFunction<V> mapper) {
		return DoubleIterables.flatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E> ObjectIterable<E> arrayflatMap(Double2ObjectFunction<E[]> mapper) {
		return DoubleIterables.arrayFlatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out unwanted elements
	 * @param filter the elements that should be kept.
	 * @return a Iterable that filtered out all unwanted elements
	 */
	default DoubleIterable filter(Double2BooleanFunction filter) {
		return DoubleIterables.filter(this, filter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out duplicated elements
	 * @return a Iterable that filtered out all duplicated elements
	 */
	default DoubleIterable distinct() {
		return DoubleIterables.distinct(this);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to limit the amount of elements
	 * @param limit the amount of elements it should be limited to
	 * @return a Iterable that is limited in length
	 */
	default DoubleIterable limit(long limit) {
		return DoubleIterables.limit(this, limit);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to sort the elements
	 * @param sorter that sorts the elements.
	 * @return a Iterable that is sorted
	 */
	default DoubleIterable sorted(DoubleComparator sorter) {
		return DoubleIterables.sorted(this, sorter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to preview elements before they are iterated through
	 * @param action the action that should be applied
	 * @return a Peeked Iterable
	 */
	default DoubleIterable peek(DoubleConsumer action) {
		return DoubleIterables.peek(this, action);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to collect all elements
	 * @param collection that the elements should be inserted to
	 * @param <E> the collection type
	 * @return the input with the desired elements
	 */
	default <E extends DoubleCollection> E pour(E collection) {
		DoubleIterators.pour(iterator(), collection);
		return collection;
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a ArrayList
	 * @return a new ArrayList of all elements
	 */
	default DoubleList pourAsList() {
		return pour(new DoubleArrayList()); 
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a LinkedHashSet
	 * @return a new LinkedHashSet of all elements
	 */
	default DoubleSet pourAsSet() { 
		return pour(new DoubleLinkedOpenHashSet()); 		
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for any matches.
	 * @param filter that should be applied
	 * @return true if any matches were found
	 */
	default boolean matchesAny(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			if(filter.get(iter.nextDouble())) return true;
		}
		return false;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for no matches.
	 * @param filter that should be applied
	 * @return true if no matches were found
	 */
	default boolean matchesNone(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			if(filter.get(iter.nextDouble())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for all matches.
	 * @param filter that should be applied
	 * @return true if all matches.
	 */
	default boolean matchesAll(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			if(!filter.get(iter.nextDouble())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for the first match.
	 * @param filter that should be applied
	 * @return the found value or the null equivalent variant.
	 */
	default double findFirst(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			double entry = iter.nextDouble();
			if(filter.get(entry)) return entry;
		}
		return 0D;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
     * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @param identity the start value
	 * @return the reduction result, returns identity if nothing was found
	 */
	default double reduce(double identity, DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = identity;
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			state = operator.applyAsDouble(state, iter.nextDouble());
		}
		return state;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
	 * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @return the reduction result, returns null value if nothing was found
	 */
	default double reduce(DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = 0D;
		boolean empty = true;
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			if(empty) {
				empty = false;
				state = iter.nextDouble();
				continue;
			}
			state = operator.applyAsDouble(state, iter.nextDouble());
		}
		return state;
	}	
	
	/**
	 * Helper function to reduce stream usage that allows to count the valid elements.
	 * @param filter that should be applied
	 * @return the amount of Valid Elements
	 */
	default int count(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			if(filter.get(iter.nextDouble())) result++;
		}
		return result;
	}
}