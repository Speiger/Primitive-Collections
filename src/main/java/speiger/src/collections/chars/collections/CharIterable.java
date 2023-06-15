package speiger.src.collections.chars.collections;

import java.util.Objects;
import java.util.function.Consumer;


import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.chars.functions.function.CharFunction;
import speiger.src.collections.ints.functions.consumer.IntCharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.chars.functions.function.CharPredicate;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.sets.CharLinkedOpenHashSet;
import speiger.src.collections.chars.utils.CharArrays;
import speiger.src.collections.chars.utils.CharAsyncBuilder;
import speiger.src.collections.chars.utils.CharSplititerators;
import speiger.src.collections.chars.utils.CharIterables;
import speiger.src.collections.chars.utils.CharIterators;
import speiger.src.collections.utils.ISizeProvider;

/**
 * A Type-Specific {@link Iterable} that reduces (un)boxing
 */
public interface CharIterable extends Iterable<Character>
{
	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	CharIterator iterator();
	
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
	default void forEach(CharConsumer action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/** {@inheritDoc}
	* <p>This default implementation delegates to the corresponding type-specific function.
	* @deprecated Please use the corresponding type-specific function instead. 
	*/
	@Deprecated
	@Override
	default void forEach(Consumer<? super Character> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
	
	/**
	 * A Indexed forEach implementation that allows you to keep track of how many elements were already iterated over.
	 * @param action The action to be performed for each element
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	public default void forEachIndexed(IntCharConsumer action) {
		Objects.requireNonNull(action);
		int index = 0;
		for(CharIterator iter = iterator();iter.hasNext();action.accept(index++, iter.nextChar()));
	}
	
	/**
	 * Helper function to reduce Lambda usage and allow for more method references, since these are faster/cleaner.
	 * @param input the object that should be included
	 * @param action The action to be performed for each element
	 * @param <E> the generic type of the Object
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	default <E> void forEach(E input, ObjectCharConsumer<E> action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(input, action);
	}
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default CharSplititerator spliterator() { return CharSplititerators.createUnknownSplititerator(iterator(), 0); }
	
	/**
	 * Creates a Async Builder for moving work of the thread.
	 * It is not designed to split the work to multithreaded work, so using this keep it singlethreaded, but it allows to be moved to another thread.
	 * @see CharAsyncBuilder
	 * @return a AsyncBuilder
	 */
	default CharAsyncBuilder asAsync() {
		return new CharAsyncBuilder(this);
	}

	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the mapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 */
	default <E> ObjectIterable<E> map(CharFunction<E> mapper) {
		return CharIterables.map(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <V> The return type supplier.
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 * @note does not support toCharArray optimizations.
	 */
	default <E, V extends Iterable<E>> ObjectIterable<E> flatMap(CharFunction<V> mapper) {
		return CharIterables.flatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to convert a Iterable to something else.
	 * @param mapper the flatMapping function
	 * @param <E> The return type.
	 * @return a new Iterable that returns the desired result
	 * @note does not support toCharArray optimizations.
	 */
	default <E> ObjectIterable<E> arrayflatMap(CharFunction<E[]> mapper) {
		return CharIterables.arrayFlatMap(this, mapper);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out unwanted elements
	 * @param filter the elements that should be kept.
	 * @return a Iterable that filtered out all unwanted elements
	 * @note does not support toCharArray optimizations.
	 */
	default CharIterable filter(CharPredicate filter) {
		return CharIterables.filter(this, filter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to filter out duplicated elements
	 * @return a Iterable that filtered out all duplicated elements
	 * @note does not support toCharArray optimizations.
	 */
	default CharIterable distinct() {
		return CharIterables.distinct(this);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to repeat elements a desired amount of times
	 * @param repeats how many times the elements should be repeated
	 * @return a Iterable that is repeating multiple times
	 */
	default CharIterable repeat(int repeats) {
		return CharIterables.repeat(this, repeats);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to limit the amount of elements
	 * @param limit the amount of elements it should be limited to
	 * @return a Iterable that is limited in length
	 */
	default CharIterable limit(long limit) {
		return CharIterables.limit(this, limit);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to sort the elements
	 * @param sorter that sorts the elements.
	 * @return a Iterable that is sorted
	 */
	default CharIterable sorted(CharComparator sorter) {
		return CharIterables.sorted(this, sorter);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to preview elements before they are iterated through
	 * @param action the action that should be applied
	 * @return a Peeked Iterable
	 */
	default CharIterable peek(CharConsumer action) {
		return CharIterables.peek(this, action);
	}
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to collect all elements
	 * @param collection that the elements should be inserted to
	 * @param <E> the collection type
	 * @return the input with the desired elements
	 */
	default <E extends CharCollection> E pour(E collection) {
		CharIterators.pour(iterator(), collection);
		return collection;
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a ArrayList
	 * @return a new ArrayList of all elements
	 */
	default CharList pourAsList() {
		return pour(new CharArrayList());
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a LinkedHashSet
	 * @return a new LinkedHashSet of all elements
	 */
	default CharSet pourAsSet() { 
		return pour(new CharLinkedOpenHashSet());
	}
	
	/**
	 * A Helper function that reduces the usage of streams and allows to collect all elements as a Array
	 * @return a new Array of all elements
	 */
	default char[] toCharArray() {
		ISizeProvider prov = ISizeProvider.of(this);
		if(prov != null) {
			int size = prov.size();
			if(size >= 0) {
				char[] array = new char[size];
				CharIterators.unwrap(array, iterator());
				return array;
			}
		}
		return CharArrays.pour(iterator());
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for any matches.
	 * @param filter that should be applied
	 * @return true if any matches were found
	 */
	default boolean matchesAny(CharPredicate filter) {
		Objects.requireNonNull(filter);
		for(CharIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextChar())) return true;
		}
		return false;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for no matches.
	 * @param filter that should be applied
	 * @return true if no matches were found
	 */
	default boolean matchesNone(CharPredicate filter) {
		Objects.requireNonNull(filter);
		for(CharIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextChar())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for all matches.
	 * @param filter that should be applied
	 * @return true if all matches.
	 */
	default boolean matchesAll(CharPredicate filter) {
		Objects.requireNonNull(filter);
		for(CharIterator iter = iterator();iter.hasNext();) {
			if(!filter.test(iter.nextChar())) return false;
		}
		return true;
	}
	
	/**
	 * Helper function to reduce stream usage that allows to filter for the first match.
	 * @param filter that should be applied
	 * @return the found value or the null equivalent variant.
	 */
	default char findFirst(CharPredicate filter) {
		Objects.requireNonNull(filter);
		for(CharIterator iter = iterator();iter.hasNext();) {
			char entry = iter.nextChar();
			if(filter.test(entry)) return entry;
		}
		return (char)0;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
     * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @param identity the start value
	 * @return the reduction result, returns identity if nothing was found
	 */
	default char reduce(char identity, CharCharUnaryOperator operator) {
		Objects.requireNonNull(operator);
		char state = identity;
		for(CharIterator iter = iterator();iter.hasNext();) {
			state = operator.applyAsChar(state, iter.nextChar());
		}
		return state;
	}
	
	/**
	 * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
	 * elements of this Iterable
	 * @param operator the operation that should be applied
	 * @return the reduction result, returns null value if nothing was found
	 */
	default char reduce(CharCharUnaryOperator operator) {
		Objects.requireNonNull(operator);
		char state = (char)0;
		boolean empty = true;
		for(CharIterator iter = iterator();iter.hasNext();) {
			if(empty) {
				empty = false;
				state = iter.nextChar();
				continue;
			}
			state = operator.applyAsChar(state, iter.nextChar());
		}
		return state;
	}	
	
	/**
	 * Helper function to reduce stream usage that allows to count the valid elements.
	 * @param filter that should be applied
	 * @return the amount of Valid Elements
	 */
	default int count(CharPredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(CharIterator iter = iterator();iter.hasNext();) {
			if(filter.test(iter.nextChar())) result++;
		}
		return result;
	}
}