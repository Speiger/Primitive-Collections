package speiger.src.collections.floats.collections;

import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

import speiger.src.collections.floats.functions.FloatConsumer;

/**
 * A Type Specific Split-Iterator that reduces boxing/unboxing
 * It fills the gaps of the java and uses this collection interfaces
 */
public interface FloatSplititerator extends OfPrimitive<Float, FloatConsumer, FloatSplititerator>, FloatIterator
{
	@Override
	default void forEachRemaining(FloatConsumer action) { FloatIterator.super.forEachRemaining(action); }
	@Override
	@Deprecated
	default void forEachRemaining(Consumer<? super Float> action) { FloatIterator.super.forEachRemaining(action); }
}