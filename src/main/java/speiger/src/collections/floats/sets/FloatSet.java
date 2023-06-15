package speiger.src.collections.floats.sets;

import java.util.Set;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.collections.FloatSplititerator;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.floats.utils.FloatSplititerators;


/**
 * A Type Specific Set class to reduce boxing/unboxing
 */
public interface FloatSet extends Set<Float>, FloatCollection
{	
	@Override
	public FloatIterator iterator();
	
	@Override
	public FloatSet copy();
	
	/**
	 * A Type Specific remove function to reduce boxing/unboxing
	 * @param o the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean remove(float o);
	
	@Override
	public default boolean remFloat(float o) {
		return remove(o);
	}
	
	@Override
	@Deprecated
	public default boolean add(Float e) {
		return FloatCollection.super.add(e);
	}
	
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return FloatCollection.super.contains(o);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return FloatCollection.super.remove(o);
	}
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @return a new Set that is synchronized
	 * @see FloatSets#synchronize
	 */
	public default FloatSet synchronize() { return FloatSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Set Wrapper that is synchronized
	 * @see FloatSets#synchronize
	 */
	public default FloatSet synchronize(Object mutex) { return FloatSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Set that is unmodifiable
	 * @return a new Set Wrapper that is unmodifiable
	 * @see FloatSets#unmodifiable
	 */
	public default FloatSet unmodifiable() { return FloatSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default FloatSplititerator spliterator() { return FloatSplititerators.createSplititerator(this, 0); }
}