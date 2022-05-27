package speiger.src.testers.longs.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.generators.maps.TestLong2ObjectMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2ObjectSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleLong2ObjectMapTestGenerator<V, E extends Long2ObjectMap<V>>
{
	BiFunction<long[], V[], E> mapper;
	long[] keys = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	V[] values;
	
	public SimpleLong2ObjectMapTestGenerator(BiFunction<long[], V[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setValues(V... values) {
		this.values = values;
	}
	
	public ObjectSamples<Long2ObjectMap.Entry<V>> getSamples() {
		return new ObjectSamples<Long2ObjectMap.Entry<V>>(
			new AbstractLong2ObjectMap.BasicEntry<>(keys[0], values[0]),
			new AbstractLong2ObjectMap.BasicEntry<>(keys[1], values[1]),
			new AbstractLong2ObjectMap.BasicEntry<>(keys[2], values[2]),
			new AbstractLong2ObjectMap.BasicEntry<>(keys[3], values[3]),
			new AbstractLong2ObjectMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Long2ObjectMap.Entry<V>... elements) {
		long[] keys = new long[elements.length];
		V[] values = (V[])ObjectArrays.newArray(getSamples().e0().getValue().getClass(), elements.length);
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getLongKey();
			values[i] = elements[i].getValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Long, V>> order(List<Map.Entry<Long, V>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Long2ObjectMap.Entry<V>> order(ObjectList<Long2ObjectMap.Entry<V>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<V> extends SimpleLong2ObjectMapTestGenerator<V, Long2ObjectMap<V>> implements TestLong2ObjectMapGenerator<V>
	{
		public Maps(BiFunction<long[], V[], Long2ObjectMap<V>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<V> extends SimpleLong2ObjectMapTestGenerator<V, Long2ObjectSortedMap<V>> implements TestLong2ObjectSortedMapGenerator<V>
	{
		public SortedMaps(BiFunction<long[], V[], Long2ObjectSortedMap<V>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Long, V>> order(List<Map.Entry<Long, V>> insertionOrder) {
			insertionOrder.sort(DerivedLong2ObjectMapGenerators.entryObjectComparator(Long::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Long2ObjectMap.Entry<V>> order(ObjectList<Long2ObjectMap.Entry<V>> insertionOrder) {
			insertionOrder.sort(DerivedLong2ObjectMapGenerators.entryComparator(Long::compare));
			return insertionOrder;
		}
		
		@Override
		public Long2ObjectMap.Entry<V> belowSamplesLesser() { return new AbstractLong2ObjectMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Long2ObjectMap.Entry<V> belowSamplesGreater() { return new AbstractLong2ObjectMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Long2ObjectMap.Entry<V> aboveSamplesLesser() { return new AbstractLong2ObjectMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Long2ObjectMap.Entry<V> aboveSamplesGreater() { return new AbstractLong2ObjectMap.BasicEntry<>(keys[8], values[8]); }
	}
}