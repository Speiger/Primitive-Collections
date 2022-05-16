package speiger.src.collections.bytes.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Byte2DoubleConcurrentMap extends ConcurrentMap<Byte, Double>, Byte2DoubleMap
{
	@Override
	@Deprecated
	public default Double compute(Byte key, BiFunction<? super Byte, ? super Double, ? extends Double> mappingFunction) {
		return Byte2DoubleMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfAbsent(Byte key, Function<? super Byte, ? extends Double> mappingFunction) {
		return Byte2DoubleMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfPresent(Byte key, BiFunction<? super Byte, ? super Double, ? extends Double> mappingFunction) {
		return Byte2DoubleMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Double> action) {
		Byte2DoubleMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Double merge(Byte key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		return Byte2DoubleMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Double getOrDefault(Object key, Double defaultValue) {
		return Byte2DoubleMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Double putIfAbsent(Byte key, Double value) {
		return Byte2DoubleMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Byte2DoubleMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Double oldValue, Double newValue) {
		return Byte2DoubleMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Double replace(Byte key, Double value) {
		return Byte2DoubleMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Double, ? extends Double> mappingFunction) {
		Byte2DoubleMap.super.replaceAll(mappingFunction);
	}
}