package speiger.src.collections.longs.sets;

import java.util.Set;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.collections.LongSplititerator;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.longs.utils.LongSplititerators;


/**
 * A Type Specific Set class to reduce boxing/unboxing
 */
public interface LongSet extends Set<Long>, LongCollection
{	
	@Override
	public LongIterator iterator();
	
	@Override
	public LongSet copy();
	
	/**
	 * A Type Specific remove function to reduce boxing/unboxing
	 * @param o the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean remove(long o);
	
	@Override
	public default boolean remLong(long o) {
		return remove(o);
	}
	
	@Override
	@Deprecated
	public default boolean add(Long e) {
		return LongCollection.super.add(e);
	}
	
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return LongCollection.super.contains(o);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return LongCollection.super.remove(o);
	}
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @return a new Set that is synchronized
	 * @see LongSets#synchronize
	 */
	public default LongSet synchronize() { return LongSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Set Wrapper that is synchronized
	 * @see LongSets#synchronize
	 */
	public default LongSet synchronize(Object mutex) { return LongSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Set that is unmodifiable
	 * @return a new Set Wrapper that is unmodifiable
	 * @see LongSets#unmodifiable
	 */
	public default LongSet unmodifiable() { return LongSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default LongSplititerator spliterator() { return LongSplititerators.createSplititerator(this, 0); }
}