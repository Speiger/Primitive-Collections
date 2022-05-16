package speiger.src.collections.booleans.sets;

import java.util.Set;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.collections.BooleanSplititerator;
import speiger.src.collections.booleans.utils.BooleanSplititerators;


/**
 * A Type Specific Set class to reduce boxing/unboxing
 */
public interface BooleanSet extends Set<Boolean>, BooleanCollection
{	
	@Override
	public BooleanIterator iterator();
	
	@Override
	public BooleanSet copy();
	
	/**
	 * A Type Specific remove function to reduce boxing/unboxing
	 * @param o the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean remove(boolean o);
	
	@Override
	public default boolean remBoolean(boolean o) {
		return remove(o);
	}
	
	@Override
	@Deprecated
	public default boolean add(Boolean e) {
		return BooleanCollection.super.add(e);
	}
	
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return BooleanCollection.super.contains(o);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return BooleanCollection.super.remove(o);
	}
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default BooleanSplititerator spliterator() { return BooleanSplititerators.createSplititerator(this, 0); }
}