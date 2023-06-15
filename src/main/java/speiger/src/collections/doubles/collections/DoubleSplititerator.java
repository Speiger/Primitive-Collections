package speiger.src.collections.doubles.collections;

import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

import speiger.src.collections.doubles.functions.DoubleConsumer;

/**
 * A Type Specific Split-Iterator that reduces boxing/unboxing
 * It fills the gaps of the java and uses this collection interfaces
 */
public interface DoubleSplititerator extends OfPrimitive<Double, DoubleConsumer, DoubleSplititerator>, DoubleIterator
{
	@Override
	default void forEachRemaining(DoubleConsumer action) { DoubleIterator.super.forEachRemaining(action); }
	@Override
	@Deprecated
	default void forEachRemaining(Consumer<? super Double> action) { DoubleIterator.super.forEachRemaining(action); }
}