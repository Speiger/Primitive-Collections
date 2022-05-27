package speiger.src.testers.floats.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.generators.maps.TestFloat2ObjectMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2ObjectSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleFloat2ObjectMapTestGenerator<V, E extends Float2ObjectMap<V>>
{
	BiFunction<float[], V[], E> mapper;
	float[] keys = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	V[] values;
	
	public SimpleFloat2ObjectMapTestGenerator(BiFunction<float[], V[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setValues(V... values) {
		this.values = values;
	}
	
	public ObjectSamples<Float2ObjectMap.Entry<V>> getSamples() {
		return new ObjectSamples<Float2ObjectMap.Entry<V>>(
			new AbstractFloat2ObjectMap.BasicEntry<>(keys[0], values[0]),
			new AbstractFloat2ObjectMap.BasicEntry<>(keys[1], values[1]),
			new AbstractFloat2ObjectMap.BasicEntry<>(keys[2], values[2]),
			new AbstractFloat2ObjectMap.BasicEntry<>(keys[3], values[3]),
			new AbstractFloat2ObjectMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Float2ObjectMap.Entry<V>... elements) {
		float[] keys = new float[elements.length];
		V[] values = (V[])ObjectArrays.newArray(getSamples().e0().getValue().getClass(), elements.length);
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getFloatKey();
			values[i] = elements[i].getValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Float, V>> order(List<Map.Entry<Float, V>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Float2ObjectMap.Entry<V>> order(ObjectList<Float2ObjectMap.Entry<V>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<V> extends SimpleFloat2ObjectMapTestGenerator<V, Float2ObjectMap<V>> implements TestFloat2ObjectMapGenerator<V>
	{
		public Maps(BiFunction<float[], V[], Float2ObjectMap<V>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<V> extends SimpleFloat2ObjectMapTestGenerator<V, Float2ObjectSortedMap<V>> implements TestFloat2ObjectSortedMapGenerator<V>
	{
		public SortedMaps(BiFunction<float[], V[], Float2ObjectSortedMap<V>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Float, V>> order(List<Map.Entry<Float, V>> insertionOrder) {
			insertionOrder.sort(DerivedFloat2ObjectMapGenerators.entryObjectComparator(Float::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Float2ObjectMap.Entry<V>> order(ObjectList<Float2ObjectMap.Entry<V>> insertionOrder) {
			insertionOrder.sort(DerivedFloat2ObjectMapGenerators.entryComparator(Float::compare));
			return insertionOrder;
		}
		
		@Override
		public Float2ObjectMap.Entry<V> belowSamplesLesser() { return new AbstractFloat2ObjectMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Float2ObjectMap.Entry<V> belowSamplesGreater() { return new AbstractFloat2ObjectMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Float2ObjectMap.Entry<V> aboveSamplesLesser() { return new AbstractFloat2ObjectMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Float2ObjectMap.Entry<V> aboveSamplesGreater() { return new AbstractFloat2ObjectMap.BasicEntry<>(keys[8], values[8]); }
	}
}