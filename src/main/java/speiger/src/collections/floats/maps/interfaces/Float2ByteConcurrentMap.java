package speiger.src.collections.floats.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Float2ByteConcurrentMap extends ConcurrentMap<Float, Byte>, Float2ByteMap
{
	@Override
	@Deprecated
	public default Byte compute(Float key, BiFunction<? super Float, ? super Byte, ? extends Byte> mappingFunction) {
		return Float2ByteMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfAbsent(Float key, Function<? super Float, ? extends Byte> mappingFunction) {
		return Float2ByteMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfPresent(Float key, BiFunction<? super Float, ? super Byte, ? extends Byte> mappingFunction) {
		return Float2ByteMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Byte> action) {
		Float2ByteMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Byte merge(Float key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		return Float2ByteMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		return Float2ByteMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Byte putIfAbsent(Float key, Byte value) {
		return Float2ByteMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Float2ByteMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Byte oldValue, Byte newValue) {
		return Float2ByteMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Byte replace(Float key, Byte value) {
		return Float2ByteMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Byte, ? extends Byte> mappingFunction) {
		Float2ByteMap.super.replaceAll(mappingFunction);
	}
}