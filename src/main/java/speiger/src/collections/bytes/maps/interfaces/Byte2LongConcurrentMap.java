package speiger.src.collections.bytes.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Byte2LongConcurrentMap extends ConcurrentMap<Byte, Long>, Byte2LongMap
{
	@Override
	@Deprecated
	public default Long compute(Byte key, BiFunction<? super Byte, ? super Long, ? extends Long> mappingFunction) {
		return Byte2LongMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfAbsent(Byte key, Function<? super Byte, ? extends Long> mappingFunction) {
		return Byte2LongMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfPresent(Byte key, BiFunction<? super Byte, ? super Long, ? extends Long> mappingFunction) {
		return Byte2LongMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Long> action) {
		Byte2LongMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Long merge(Byte key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		return Byte2LongMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Long getOrDefault(Object key, Long defaultValue) {
		return Byte2LongMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Long putIfAbsent(Byte key, Long value) {
		return Byte2LongMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Byte2LongMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Long oldValue, Long newValue) {
		return Byte2LongMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Long replace(Byte key, Long value) {
		return Byte2LongMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Long, ? extends Long> mappingFunction) {
		Byte2LongMap.super.replaceAll(mappingFunction);
	}
}