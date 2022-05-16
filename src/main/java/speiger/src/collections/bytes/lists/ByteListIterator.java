package speiger.src.collections.bytes.lists;

import java.util.ListIterator;

import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;

/**
 * A Type Specific ListIterator that reduces boxing/unboxing
 */
public interface ByteListIterator extends ListIterator<Byte>, ByteBidirectionalIterator
{
	/**
	 * A Primitive set function to reduce (un)boxing
	 * @param e the element that should replace the last returned value
	 * @see ListIterator#set(Object)
	 */
	public void set(byte e);
	
	/**
	 * A Primitive set function to reduce (un)boxing
	 * @param e the element that should be inserted before the last returned value
	 * @see ListIterator#set(Object)
	 */
	public void add(byte e);
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Byte previous() {
		return ByteBidirectionalIterator.super.previous();
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Byte next() {
		return ByteBidirectionalIterator.super.next();
	}	
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default void set(Byte e) {
		set(e.byteValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default void add(Byte e) {
		add(e.byteValue());
	}
}