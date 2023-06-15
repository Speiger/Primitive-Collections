package speiger.src.collections.objects.sets;

import java.util.NavigableSet;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.collections.ObjectSplititerator;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.utils.ObjectSplititerators;

/**
 * A Type Specific Navigable Set interface with a couple helper methods
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface ObjectNavigableSet<T> extends NavigableSet<T>, ObjectSortedSet<T>
{
	@Override
	public default ObjectNavigableSet<T> subSet(T fromElement, T toElement) { return subSet(fromElement, true, toElement, false); }
	@Override
	public default ObjectNavigableSet<T> headSet(T toElement) { return headSet(toElement, false); }
	@Override
	public default ObjectNavigableSet<T> tailSet(T fromElement) { return tailSet(fromElement, true); }
	@Override
	public ObjectNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive);
	@Override
	public ObjectNavigableSet<T> headSet(T toElement, boolean inclusive);
	@Override
	public ObjectNavigableSet<T> tailSet(T fromElement, boolean inclusive);
	
	/** @return a Type Specific iterator */
	@Override
	public ObjectBidirectionalIterator<T> iterator();
	/** @return a Type Specific desendingIterator */
	@Override
	public ObjectBidirectionalIterator<T> descendingIterator();
	/** @return a Type Specific desendingSet */
	@Override
	public ObjectNavigableSet<T> descendingSet();
	@Override
	public ObjectNavigableSet<T> copy();
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @return a new NavigableSet that is synchronized
	 * @see ObjectSets#synchronize
	 */
	public default ObjectNavigableSet<T> synchronize() { return ObjectSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableSet Wrapper that is synchronized
	 * @see ObjectSets#synchronize
	 */
	public default ObjectNavigableSet<T> synchronize(Object mutex) { return ObjectSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableSet that is unmodifiable
	 * @return a new NavigableSet Wrapper that is unmodifiable
	 * @see ObjectSets#unmodifiable
	 */
	public default ObjectNavigableSet<T> unmodifiable() { return ObjectSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ObjectSplititerator<T> spliterator() { return ObjectSplititerators.createSplititerator(this, 0); }
	
}