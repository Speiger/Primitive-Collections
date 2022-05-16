package speiger.src.collections.longs.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Long2ByteConcurrentMap extends ConcurrentMap<Long, Byte>, Long2ByteMap
{
	@Override
	@Deprecated
	public default Byte compute(Long key, BiFunction<? super Long, ? super Byte, ? extends Byte> mappingFunction) {
		return Long2ByteMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfAbsent(Long key, Function<? super Long, ? extends Byte> mappingFunction) {
		return Long2ByteMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfPresent(Long key, BiFunction<? super Long, ? super Byte, ? extends Byte> mappingFunction) {
		return Long2ByteMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Byte> action) {
		Long2ByteMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Byte merge(Long key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		return Long2ByteMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		return Long2ByteMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Byte putIfAbsent(Long key, Byte value) {
		return Long2ByteMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Long2ByteMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Byte oldValue, Byte newValue) {
		return Long2ByteMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Byte replace(Long key, Byte value) {
		return Long2ByteMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Byte, ? extends Byte> mappingFunction) {
		Long2ByteMap.super.replaceAll(mappingFunction);
	}
}