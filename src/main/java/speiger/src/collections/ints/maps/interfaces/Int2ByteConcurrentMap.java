package speiger.src.collections.ints.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Int2ByteConcurrentMap extends ConcurrentMap<Integer, Byte>, Int2ByteMap
{
	@Override
	@Deprecated
	public default Byte compute(Integer key, BiFunction<? super Integer, ? super Byte, ? extends Byte> mappingFunction) {
		return Int2ByteMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfAbsent(Integer key, Function<? super Integer, ? extends Byte> mappingFunction) {
		return Int2ByteMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfPresent(Integer key, BiFunction<? super Integer, ? super Byte, ? extends Byte> mappingFunction) {
		return Int2ByteMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Byte> action) {
		Int2ByteMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Byte merge(Integer key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		return Int2ByteMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		return Int2ByteMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Byte putIfAbsent(Integer key, Byte value) {
		return Int2ByteMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Int2ByteMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Byte oldValue, Byte newValue) {
		return Int2ByteMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Byte replace(Integer key, Byte value) {
		return Int2ByteMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Byte, ? extends Byte> mappingFunction) {
		Int2ByteMap.super.replaceAll(mappingFunction);
	}
}