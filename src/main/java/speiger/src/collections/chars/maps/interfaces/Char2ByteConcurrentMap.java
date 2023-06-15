package speiger.src.collections.chars.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Char2ByteConcurrentMap extends ConcurrentMap<Character, Byte>, Char2ByteMap
{
	@Override
	@Deprecated
	public default Byte compute(Character key, BiFunction<? super Character, ? super Byte, ? extends Byte> mappingFunction) {
		return Char2ByteMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfAbsent(Character key, Function<? super Character, ? extends Byte> mappingFunction) {
		return Char2ByteMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfPresent(Character key, BiFunction<? super Character, ? super Byte, ? extends Byte> mappingFunction) {
		return Char2ByteMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Character, ? super Byte> action) {
		Char2ByteMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Byte merge(Character key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		return Char2ByteMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		return Char2ByteMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Byte putIfAbsent(Character key, Byte value) {
		return Char2ByteMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Char2ByteMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Character key, Byte oldValue, Byte newValue) {
		return Char2ByteMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Byte replace(Character key, Byte value) {
		return Char2ByteMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Character, ? super Byte, ? extends Byte> mappingFunction) {
		Char2ByteMap.super.replaceAll(mappingFunction);
	}
}