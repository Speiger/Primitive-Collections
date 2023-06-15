package speiger.src.collections.floats.lists;

import java.util.ListIterator;

import speiger.src.collections.floats.collections.FloatBidirectionalIterator;

/**
 * A Type Specific ListIterator that reduces boxing/unboxing
 */
public interface FloatListIterator extends ListIterator<Float>, FloatBidirectionalIterator
{
	/**
	 * A Primitive set function to reduce (un)boxing
	 * @param e the element that should replace the last returned value
	 * @see ListIterator#set(Object)
	 */
	public void set(float e);
	
	/**
	 * A Primitive set function to reduce (un)boxing
	 * @param e the element that should be inserted before the last returned value
	 * @see ListIterator#set(Object)
	 */
	public void add(float e);
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Float previous() {
		return FloatBidirectionalIterator.super.previous();
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Float next() {
		return FloatBidirectionalIterator.super.next();
	}	
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default void set(Float e) {
		set(e.floatValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default void add(Float e) {
		add(e.floatValue());
	}
}