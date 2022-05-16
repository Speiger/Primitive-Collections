package speiger.src.collections.bytes.sets;

import java.util.Set;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.collections.ByteSplititerator;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.bytes.utils.ByteSplititerators;


/**
 * A Type Specific Set class to reduce boxing/unboxing
 */
public interface ByteSet extends Set<Byte>, ByteCollection
{	
	@Override
	public ByteIterator iterator();
	
	@Override
	public ByteSet copy();
	
	/**
	 * A Type Specific remove function to reduce boxing/unboxing
	 * @param o the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean remove(byte o);
	
	@Override
	public default boolean remByte(byte o) {
		return remove(o);
	}
	
	@Override
	@Deprecated
	public default boolean add(Byte e) {
		return ByteCollection.super.add(e);
	}
	
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return ByteCollection.super.contains(o);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return ByteCollection.super.remove(o);
	}
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @return a new Set that is synchronized
	 * @see ByteSets#synchronize
	 */
	public default ByteSet synchronize() { return ByteSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Set Wrapper that is synchronized
	 * @see ByteSets#synchronize
	 */
	public default ByteSet synchronize(Object mutex) { return ByteSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Set that is unmodifiable
	 * @return a new Set Wrapper that is unmodifiable
	 * @see ByteSets#unmodifiable
	 */
	public default ByteSet unmodifiable() { return ByteSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ByteSplititerator spliterator() { return ByteSplititerators.createSplititerator(this, 0); }
}