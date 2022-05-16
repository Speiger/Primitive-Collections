package speiger.src.collections.doubles.sets;

import java.util.Set;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.collections.DoubleSplititerator;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.doubles.utils.DoubleSplititerators;


/**
 * A Type Specific Set class to reduce boxing/unboxing
 */
public interface DoubleSet extends Set<Double>, DoubleCollection
{	
	@Override
	public DoubleIterator iterator();
	
	@Override
	public DoubleSet copy();
	
	/**
	 * A Type Specific remove function to reduce boxing/unboxing
	 * @param o the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean remove(double o);
	
	@Override
	public default boolean remDouble(double o) {
		return remove(o);
	}
	
	@Override
	@Deprecated
	public default boolean add(Double e) {
		return DoubleCollection.super.add(e);
	}
	
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return DoubleCollection.super.contains(o);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return DoubleCollection.super.remove(o);
	}
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @return a new Set that is synchronized
	 * @see DoubleSets#synchronize
	 */
	public default DoubleSet synchronize() { return DoubleSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Set Wrapper that is synchronized
	 * @see DoubleSets#synchronize
	 */
	public default DoubleSet synchronize(Object mutex) { return DoubleSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Set that is unmodifiable
	 * @return a new Set Wrapper that is unmodifiable
	 * @see DoubleSets#unmodifiable
	 */
	public default DoubleSet unmodifiable() { return DoubleSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default DoubleSplititerator spliterator() { return DoubleSplititerators.createSplititerator(this, 0); }
}