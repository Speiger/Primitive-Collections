package speiger.src.collections.booleans.lists;

import java.util.ListIterator;

import speiger.src.collections.booleans.collections.BooleanBidirectionalIterator;

/**
 * A Type Specific ListIterator that reduces boxing/unboxing
 */
public interface BooleanListIterator extends ListIterator<Boolean>, BooleanBidirectionalIterator
{
	/**
	 * A Primitive set function to reduce (un)boxing
	 * @param e the element that should replace the last returned value
	 * @see ListIterator#set(Object)
	 */
	public void set(boolean e);
	
	/**
	 * A Primitive set function to reduce (un)boxing
	 * @param e the element that should be inserted before the last returned value
	 * @see ListIterator#set(Object)
	 */
	public void add(boolean e);
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Boolean previous() {
		return BooleanBidirectionalIterator.super.previous();
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Boolean next() {
		return BooleanBidirectionalIterator.super.next();
	}	
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default void set(Boolean e) {
		set(e.booleanValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default void add(Boolean e) {
		add(e.booleanValue());
	}
}