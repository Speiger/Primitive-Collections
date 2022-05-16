package speiger.src.collections.shorts.sets;

import java.util.Set;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.collections.ShortSplititerator;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.shorts.utils.ShortSplititerators;


/**
 * A Type Specific Set class to reduce boxing/unboxing
 */
public interface ShortSet extends Set<Short>, ShortCollection
{	
	@Override
	public ShortIterator iterator();
	
	@Override
	public ShortSet copy();
	
	/**
	 * A Type Specific remove function to reduce boxing/unboxing
	 * @param o the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean remove(short o);
	
	@Override
	public default boolean remShort(short o) {
		return remove(o);
	}
	
	@Override
	@Deprecated
	public default boolean add(Short e) {
		return ShortCollection.super.add(e);
	}
	
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return ShortCollection.super.contains(o);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return ShortCollection.super.remove(o);
	}
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @return a new Set that is synchronized
	 * @see ShortSets#synchronize
	 */
	public default ShortSet synchronize() { return ShortSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Set Wrapper that is synchronized
	 * @see ShortSets#synchronize
	 */
	public default ShortSet synchronize(Object mutex) { return ShortSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Set that is unmodifiable
	 * @return a new Set Wrapper that is unmodifiable
	 * @see ShortSets#unmodifiable
	 */
	public default ShortSet unmodifiable() { return ShortSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ShortSplititerator spliterator() { return ShortSplititerators.createSplititerator(this, 0); }
}