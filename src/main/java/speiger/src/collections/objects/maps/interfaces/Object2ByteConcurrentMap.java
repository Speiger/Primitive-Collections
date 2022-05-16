package speiger.src.collections.objects.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2ByteConcurrentMap<T> extends ConcurrentMap<T, Byte>, Object2ByteMap<T>
{
	@Override
	public default Byte compute(T key, BiFunction<? super T, ? super Byte, ? extends Byte> mappingFunction) {
		return Object2ByteMap.super.compute(key, mappingFunction);
	}

	@Override
	public default Byte computeIfAbsent(T key, Function<? super T, ? extends Byte> mappingFunction) {
		return Object2ByteMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public default Byte computeIfPresent(T key, BiFunction<? super T, ? super Byte, ? extends Byte> mappingFunction) {
		return Object2ByteMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	public default void forEach(BiConsumer<? super T, ? super Byte> action) {
		Object2ByteMap.super.forEach(action);
	}

	@Override
	public default Byte merge(T key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		return Object2ByteMap.super.merge(key, value, mappingFunction);
	}
	
	@Override
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		return Object2ByteMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	public default Byte putIfAbsent(T key, Byte value) {
		return Object2ByteMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Object2ByteMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(T key, Byte oldValue, Byte newValue) {
		return Object2ByteMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Byte replace(T key, Byte value) {
		return Object2ByteMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super T, ? super Byte, ? extends Byte> mappingFunction) {
		Object2ByteMap.super.replaceAll(mappingFunction);
	}
}