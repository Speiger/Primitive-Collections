package speiger.src.collections.objects.sets;

import java.util.Set;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.collections.ObjectSplititerator;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.utils.ObjectSplititerators;


/**
 * A Type Specific Set class to reduce boxing/unboxing
 * @param <T> the type of elements maintained by this Collection
 */
public interface ObjectSet<T> extends Set<T>, ObjectCollection<T>
{	
	@Override
	public ObjectIterator<T> iterator();
	
	@Override
	public ObjectSet<T> copy();
	
	/**
	 * A Helper method that allows to add a element or getting the already present implement.
	 * Allowing to make unique references reuseable.
	 * @param o the element to add
	 * @return either the inserted element or the present element.
	 */
	public T addOrGet(T o);
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @return a new Set that is synchronized
	 * @see ObjectSets#synchronize
	 */
	public default ObjectSet<T> synchronize() { return ObjectSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Set Wrapper that is synchronized
	 * @see ObjectSets#synchronize
	 */
	public default ObjectSet<T> synchronize(Object mutex) { return ObjectSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Set that is unmodifiable
	 * @return a new Set Wrapper that is unmodifiable
	 * @see ObjectSets#unmodifiable
	 */
	public default ObjectSet<T> unmodifiable() { return ObjectSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ObjectSplititerator<T> spliterator() { return ObjectSplititerators.createSplititerator(this, 0); }
}