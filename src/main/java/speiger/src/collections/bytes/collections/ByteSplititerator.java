package speiger.src.collections.bytes.collections;

import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

import speiger.src.collections.bytes.functions.ByteConsumer;

/**
 * A Type Specific Split-Iterator that reduces boxing/unboxing
 * It fills the gaps of the java and uses this collection interfaces
 */
public interface ByteSplititerator extends OfPrimitive<Byte, ByteConsumer, ByteSplititerator>, ByteIterator
{
	@Override
	default void forEachRemaining(ByteConsumer action) { ByteIterator.super.forEachRemaining(action); }
	@Override
	@Deprecated
	default void forEachRemaining(Consumer<? super Byte> action) { ByteIterator.super.forEachRemaining(action); }
}