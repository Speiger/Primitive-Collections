package speiger.src.testers.doubles.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2CharMap;
import speiger.src.collections.doubles.maps.interfaces.Double2CharMap;
import speiger.src.collections.doubles.maps.interfaces.Double2CharSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.generators.maps.TestDouble2CharMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2CharSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleDouble2CharMapTestGenerator<E extends Double2CharMap>
{
	BiFunction<double[], char[], E> mapper;
	double[] keys = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	char[] values = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	
	public SimpleDouble2CharMapTestGenerator(BiFunction<double[], char[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Double2CharMap.Entry> getSamples() {
		return new ObjectSamples<Double2CharMap.Entry>(
			new AbstractDouble2CharMap.BasicEntry(keys[0], values[0]),
			new AbstractDouble2CharMap.BasicEntry(keys[1], values[1]),
			new AbstractDouble2CharMap.BasicEntry(keys[2], values[2]),
			new AbstractDouble2CharMap.BasicEntry(keys[3], values[3]),
			new AbstractDouble2CharMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Double2CharMap.Entry... elements) {
		double[] keys = new double[elements.length];
		char[] values = new char[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getDoubleKey();
			values[i] = elements[i].getCharValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Double, Character>> order(List<Map.Entry<Double, Character>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Double2CharMap.Entry> order(ObjectList<Double2CharMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleDouble2CharMapTestGenerator<Double2CharMap> implements TestDouble2CharMapGenerator
	{
		public Maps(BiFunction<double[], char[], Double2CharMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleDouble2CharMapTestGenerator<Double2CharSortedMap> implements TestDouble2CharSortedMapGenerator
	{
		public SortedMaps(BiFunction<double[], char[], Double2CharSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Double, Character>> order(List<Map.Entry<Double, Character>> insertionOrder) {
			insertionOrder.sort(DerivedDouble2CharMapGenerators.entryObjectComparator(Double::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Double2CharMap.Entry> order(ObjectList<Double2CharMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedDouble2CharMapGenerators.entryComparator(Double::compare));
			return insertionOrder;
		}
		
		@Override
		public Double2CharMap.Entry belowSamplesLesser() { return new AbstractDouble2CharMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Double2CharMap.Entry belowSamplesGreater() { return new AbstractDouble2CharMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Double2CharMap.Entry aboveSamplesLesser() { return new AbstractDouble2CharMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Double2CharMap.Entry aboveSamplesGreater() { return new AbstractDouble2CharMap.BasicEntry(keys[8], values[8]); }
	}
}