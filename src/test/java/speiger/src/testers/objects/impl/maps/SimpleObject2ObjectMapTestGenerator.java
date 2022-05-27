package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.generators.maps.TestObject2ObjectMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2ObjectSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleObject2ObjectMapTestGenerator<T, V, E extends Object2ObjectMap<T, V>>
{
	BiFunction<T[], V[], E> mapper;
	T[] keys;
	V[] values;
	
	public SimpleObject2ObjectMapTestGenerator(BiFunction<T[], V[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setKeys(T... keys) {
		this.keys = keys;
	}
	
	public void setValues(V... values) {
		this.values = values;
	}
	
	public ObjectSamples<Object2ObjectMap.Entry<T, V>> getSamples() {
		return new ObjectSamples<Object2ObjectMap.Entry<T, V>>(
			new AbstractObject2ObjectMap.BasicEntry<>(keys[0], values[0]),
			new AbstractObject2ObjectMap.BasicEntry<>(keys[1], values[1]),
			new AbstractObject2ObjectMap.BasicEntry<>(keys[2], values[2]),
			new AbstractObject2ObjectMap.BasicEntry<>(keys[3], values[3]),
			new AbstractObject2ObjectMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Object2ObjectMap.Entry<T, V>... elements) {
		T[] keys = (T[])ObjectArrays.newArray(getSamples().e0().getKey().getClass(), elements.length);
		V[] values = (V[])ObjectArrays.newArray(getSamples().e0().getValue().getClass(), elements.length);
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getKey();
			values[i] = elements[i].getValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<T, V>> order(List<Map.Entry<T, V>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Object2ObjectMap.Entry<T, V>> order(ObjectList<Object2ObjectMap.Entry<T, V>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<T, V> extends SimpleObject2ObjectMapTestGenerator<T, V, Object2ObjectMap<T, V>> implements TestObject2ObjectMapGenerator<T, V>
	{
		public Maps(BiFunction<T[], V[], Object2ObjectMap<T, V>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<T, V> extends SimpleObject2ObjectMapTestGenerator<T, V, Object2ObjectSortedMap<T, V>> implements TestObject2ObjectSortedMapGenerator<T, V>
	{
		public SortedMaps(BiFunction<T[], V[], Object2ObjectSortedMap<T, V>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<T, V>> order(List<Map.Entry<T, V>> insertionOrder) {
			insertionOrder.sort(DerivedObject2ObjectMapGenerators.entryObjectComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		public ObjectIterable<Object2ObjectMap.Entry<T, V>> order(ObjectList<Object2ObjectMap.Entry<T, V>> insertionOrder) {
			insertionOrder.sort(DerivedObject2ObjectMapGenerators.entryComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		@Override
		public Object2ObjectMap.Entry<T, V> belowSamplesLesser() { return new AbstractObject2ObjectMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Object2ObjectMap.Entry<T, V> belowSamplesGreater() { return new AbstractObject2ObjectMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Object2ObjectMap.Entry<T, V> aboveSamplesLesser() { return new AbstractObject2ObjectMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Object2ObjectMap.Entry<T, V> aboveSamplesGreater() { return new AbstractObject2ObjectMap.BasicEntry<>(keys[8], values[8]); }
	}
}