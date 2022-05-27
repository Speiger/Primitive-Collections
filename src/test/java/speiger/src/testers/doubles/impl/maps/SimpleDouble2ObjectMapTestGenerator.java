package speiger.src.testers.doubles.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ObjectMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.generators.maps.TestDouble2ObjectMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ObjectSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleDouble2ObjectMapTestGenerator<V, E extends Double2ObjectMap<V>>
{
	BiFunction<double[], V[], E> mapper;
	double[] keys = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	V[] values;
	
	public SimpleDouble2ObjectMapTestGenerator(BiFunction<double[], V[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setValues(V... values) {
		this.values = values;
	}
	
	public ObjectSamples<Double2ObjectMap.Entry<V>> getSamples() {
		return new ObjectSamples<Double2ObjectMap.Entry<V>>(
			new AbstractDouble2ObjectMap.BasicEntry<>(keys[0], values[0]),
			new AbstractDouble2ObjectMap.BasicEntry<>(keys[1], values[1]),
			new AbstractDouble2ObjectMap.BasicEntry<>(keys[2], values[2]),
			new AbstractDouble2ObjectMap.BasicEntry<>(keys[3], values[3]),
			new AbstractDouble2ObjectMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Double2ObjectMap.Entry<V>... elements) {
		double[] keys = new double[elements.length];
		V[] values = (V[])ObjectArrays.newArray(getSamples().e0().getValue().getClass(), elements.length);
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getDoubleKey();
			values[i] = elements[i].getValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Double, V>> order(List<Map.Entry<Double, V>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Double2ObjectMap.Entry<V>> order(ObjectList<Double2ObjectMap.Entry<V>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<V> extends SimpleDouble2ObjectMapTestGenerator<V, Double2ObjectMap<V>> implements TestDouble2ObjectMapGenerator<V>
	{
		public Maps(BiFunction<double[], V[], Double2ObjectMap<V>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<V> extends SimpleDouble2ObjectMapTestGenerator<V, Double2ObjectSortedMap<V>> implements TestDouble2ObjectSortedMapGenerator<V>
	{
		public SortedMaps(BiFunction<double[], V[], Double2ObjectSortedMap<V>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Double, V>> order(List<Map.Entry<Double, V>> insertionOrder) {
			insertionOrder.sort(DerivedDouble2ObjectMapGenerators.entryObjectComparator(Double::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Double2ObjectMap.Entry<V>> order(ObjectList<Double2ObjectMap.Entry<V>> insertionOrder) {
			insertionOrder.sort(DerivedDouble2ObjectMapGenerators.entryComparator(Double::compare));
			return insertionOrder;
		}
		
		@Override
		public Double2ObjectMap.Entry<V> belowSamplesLesser() { return new AbstractDouble2ObjectMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Double2ObjectMap.Entry<V> belowSamplesGreater() { return new AbstractDouble2ObjectMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Double2ObjectMap.Entry<V> aboveSamplesLesser() { return new AbstractDouble2ObjectMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Double2ObjectMap.Entry<V> aboveSamplesGreater() { return new AbstractDouble2ObjectMap.BasicEntry<>(keys[8], values[8]); }
	}
}