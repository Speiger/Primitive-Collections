package speiger.src.collections.shorts.collections;

import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

import speiger.src.collections.shorts.functions.ShortConsumer;

/**
 * A Type Specific Split-Iterator that reduces boxing/unboxing
 * It fills the gaps of the java and uses this collection interfaces
 */
public interface ShortSplititerator extends OfPrimitive<Short, ShortConsumer, ShortSplititerator>, ShortIterator
{
	@Override
	default void forEachRemaining(ShortConsumer action) { ShortIterator.super.forEachRemaining(action); }
	@Override
	@Deprecated
	default void forEachRemaining(Consumer<? super Short> action) { ShortIterator.super.forEachRemaining(action); }
}