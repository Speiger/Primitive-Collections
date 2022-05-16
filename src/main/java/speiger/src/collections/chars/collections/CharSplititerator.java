package speiger.src.collections.chars.collections;

import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

import speiger.src.collections.chars.functions.CharConsumer;

/**
 * A Type Specific Split-Iterator that reduces boxing/unboxing
 * It fills the gaps of the java and uses this collection interfaces
 */
public interface CharSplititerator extends OfPrimitive<Character, CharConsumer, CharSplititerator>, CharIterator
{
	@Override
	default void forEachRemaining(CharConsumer action) { CharIterator.super.forEachRemaining(action); }
	@Override
	@Deprecated
	default void forEachRemaining(Consumer<? super Character> action) { CharIterator.super.forEachRemaining(action); }
}