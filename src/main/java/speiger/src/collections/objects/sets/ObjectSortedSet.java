package speiger.src.collections.objects.sets;

import java.util.SortedSet;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.collections.ObjectSplititerator;
import java.util.Comparator;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.utils.ObjectSplititerators;

/**
 * A Type Specific SortedSet implementation to reduce boxing/unboxing
 * with a couple extra methods that allow greater control over sets.
 * @param <T> the type of elements maintained by this Collection
 * @note ObjectOrderedSet is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement ObjectOrderedSet directly and will remove ObjectSortedSet implementations in favor of ObjectOrderedSet instead
 */
public interface ObjectSortedSet<T> extends ObjectSet<T>, SortedSet<T>
{
	/**
	 * A Type Specific Comparator method
	 * @return the type specific comparator
	 */
	@Override
	public Comparator<T> comparator();
	
	@Override
	public ObjectSortedSet<T> copy();
	
	@Override
	public ObjectBidirectionalIterator<T> iterator();
	/**
	 * A type Specific Iterator starting from a given key
	 * @param fromElement the element the iterator should start from
	 * @return a iterator starting from the given element
	 * @throws java.util.NoSuchElementException if fromElement isn't found
	 */
	public ObjectBidirectionalIterator<T> iterator(T fromElement);
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @return a new SortedSet that is synchronized
	 * @see ObjectSets#synchronize
	 */
	public default ObjectSortedSet<T> synchronize() { return ObjectSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedSet Wrapper that is synchronized
	 * @see ObjectSets#synchronize
	 */
	public default ObjectSortedSet<T> synchronize(Object mutex) { return ObjectSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedSet that is unmodifiable
	 * @return a new SortedSet Wrapper that is unmodifiable
	 * @see ObjectSets#unmodifiable
	 */
	public default ObjectSortedSet<T> unmodifiable() { return ObjectSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ObjectSplititerator<T> spliterator() { return ObjectSplititerators.createSplititerator(this, 0); }
	
	/**
	 * A method to get and remove the first element in the set
	 * @return first element in the set
	 */
	public T pollFirst();
	/**
	 * A method to get and remove the last element in the set
	 * @return last element in the set
	 */
	public T pollLast();
	
	@Override
	public ObjectSortedSet<T> subSet(T fromElement, T toElement);
	@Override
	public ObjectSortedSet<T> headSet(T toElement);
	@Override
	public ObjectSortedSet<T> tailSet(T fromElement);
}