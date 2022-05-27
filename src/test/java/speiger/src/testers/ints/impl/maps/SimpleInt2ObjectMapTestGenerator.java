package speiger.src.testers.ints.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.generators.maps.TestInt2ObjectMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2ObjectSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleInt2ObjectMapTestGenerator<V, E extends Int2ObjectMap<V>>
{
	BiFunction<int[], V[], E> mapper;
	int[] keys = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	V[] values;
	
	public SimpleInt2ObjectMapTestGenerator(BiFunction<int[], V[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setValues(V... values) {
		this.values = values;
	}
	
	public ObjectSamples<Int2ObjectMap.Entry<V>> getSamples() {
		return new ObjectSamples<Int2ObjectMap.Entry<V>>(
			new AbstractInt2ObjectMap.BasicEntry<>(keys[0], values[0]),
			new AbstractInt2ObjectMap.BasicEntry<>(keys[1], values[1]),
			new AbstractInt2ObjectMap.BasicEntry<>(keys[2], values[2]),
			new AbstractInt2ObjectMap.BasicEntry<>(keys[3], values[3]),
			new AbstractInt2ObjectMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Int2ObjectMap.Entry<V>... elements) {
		int[] keys = new int[elements.length];
		V[] values = (V[])ObjectArrays.newArray(getSamples().e0().getValue().getClass(), elements.length);
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getIntKey();
			values[i] = elements[i].getValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Integer, V>> order(List<Map.Entry<Integer, V>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Int2ObjectMap.Entry<V>> order(ObjectList<Int2ObjectMap.Entry<V>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<V> extends SimpleInt2ObjectMapTestGenerator<V, Int2ObjectMap<V>> implements TestInt2ObjectMapGenerator<V>
	{
		public Maps(BiFunction<int[], V[], Int2ObjectMap<V>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<V> extends SimpleInt2ObjectMapTestGenerator<V, Int2ObjectSortedMap<V>> implements TestInt2ObjectSortedMapGenerator<V>
	{
		public SortedMaps(BiFunction<int[], V[], Int2ObjectSortedMap<V>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Integer, V>> order(List<Map.Entry<Integer, V>> insertionOrder) {
			insertionOrder.sort(DerivedInt2ObjectMapGenerators.entryObjectComparator(Integer::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Int2ObjectMap.Entry<V>> order(ObjectList<Int2ObjectMap.Entry<V>> insertionOrder) {
			insertionOrder.sort(DerivedInt2ObjectMapGenerators.entryComparator(Integer::compare));
			return insertionOrder;
		}
		
		@Override
		public Int2ObjectMap.Entry<V> belowSamplesLesser() { return new AbstractInt2ObjectMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Int2ObjectMap.Entry<V> belowSamplesGreater() { return new AbstractInt2ObjectMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Int2ObjectMap.Entry<V> aboveSamplesLesser() { return new AbstractInt2ObjectMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Int2ObjectMap.Entry<V> aboveSamplesGreater() { return new AbstractInt2ObjectMap.BasicEntry<>(keys[8], values[8]); }
	}
}