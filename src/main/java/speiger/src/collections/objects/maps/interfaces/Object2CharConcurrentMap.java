package speiger.src.collections.objects.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface Object2CharConcurrentMap<T> extends ConcurrentMap<T, Character>, Object2CharMap<T>
{
	@Override
	public default Character compute(T key, BiFunction<? super T, ? super Character, ? extends Character> mappingFunction) {
		return Object2CharMap.super.compute(key, mappingFunction);
	}

	@Override
	public default Character computeIfAbsent(T key, Function<? super T, ? extends Character> mappingFunction) {
		return Object2CharMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public default Character computeIfPresent(T key, BiFunction<? super T, ? super Character, ? extends Character> mappingFunction) {
		return Object2CharMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	public default void forEach(BiConsumer<? super T, ? super Character> action) {
		Object2CharMap.super.forEach(action);
	}

	@Override
	public default Character merge(T key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> mappingFunction) {
		return Object2CharMap.super.merge(key, value, mappingFunction);
	}
	
	@Override
	public default Character getOrDefault(Object key, Character defaultValue) {
		return Object2CharMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	public default Character putIfAbsent(T key, Character value) {
		return Object2CharMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Object2CharMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(T key, Character oldValue, Character newValue) {
		return Object2CharMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Character replace(T key, Character value) {
		return Object2CharMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super T, ? super Character, ? extends Character> mappingFunction) {
		Object2CharMap.super.replaceAll(mappingFunction);
	}
}