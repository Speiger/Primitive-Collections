package speiger.src.collections.booleans.collections;

import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

import speiger.src.collections.booleans.functions.BooleanConsumer;

/**
 * A Type Specific Split-Iterator that reduces boxing/unboxing
 * It fills the gaps of the java and uses this collection interfaces
 */
public interface BooleanSplititerator extends OfPrimitive<Boolean, BooleanConsumer, BooleanSplititerator>, BooleanIterator
{
	@Override
	default void forEachRemaining(BooleanConsumer action) { BooleanIterator.super.forEachRemaining(action); }
	@Override
	@Deprecated
	default void forEachRemaining(Consumer<? super Boolean> action) { BooleanIterator.super.forEachRemaining(action); }
}