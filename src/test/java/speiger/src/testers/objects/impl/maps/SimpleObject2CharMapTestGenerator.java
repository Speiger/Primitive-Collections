package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.generators.maps.TestObject2CharMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2CharSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleObject2CharMapTestGenerator<T, E extends Object2CharMap<T>>
{
	BiFunction<T[], char[], E> mapper;
	T[] keys;
	char[] values = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	
	public SimpleObject2CharMapTestGenerator(BiFunction<T[], char[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setKeys(T... keys) {
		this.keys = keys;
	}
	
	public ObjectSamples<Object2CharMap.Entry<T>> getSamples() {
		return new ObjectSamples<Object2CharMap.Entry<T>>(
			new AbstractObject2CharMap.BasicEntry<>(keys[0], values[0]),
			new AbstractObject2CharMap.BasicEntry<>(keys[1], values[1]),
			new AbstractObject2CharMap.BasicEntry<>(keys[2], values[2]),
			new AbstractObject2CharMap.BasicEntry<>(keys[3], values[3]),
			new AbstractObject2CharMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Object2CharMap.Entry<T>... elements) {
		T[] keys = (T[])ObjectArrays.newArray(getSamples().e0().getKey().getClass(), elements.length);
		char[] values = new char[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getKey();
			values[i] = elements[i].getCharValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<T, Character>> order(List<Map.Entry<T, Character>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Object2CharMap.Entry<T>> order(ObjectList<Object2CharMap.Entry<T>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<T> extends SimpleObject2CharMapTestGenerator<T, Object2CharMap<T>> implements TestObject2CharMapGenerator<T>
	{
		public Maps(BiFunction<T[], char[], Object2CharMap<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<T> extends SimpleObject2CharMapTestGenerator<T, Object2CharSortedMap<T>> implements TestObject2CharSortedMapGenerator<T>
	{
		public SortedMaps(BiFunction<T[], char[], Object2CharSortedMap<T>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<T, Character>> order(List<Map.Entry<T, Character>> insertionOrder) {
			insertionOrder.sort(DerivedObject2CharMapGenerators.entryObjectComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		public ObjectIterable<Object2CharMap.Entry<T>> order(ObjectList<Object2CharMap.Entry<T>> insertionOrder) {
			insertionOrder.sort(DerivedObject2CharMapGenerators.entryComparator((Comparator<T>)Comparator.naturalOrder()));
			return insertionOrder;
		}
		
		@Override
		public Object2CharMap.Entry<T> belowSamplesLesser() { return new AbstractObject2CharMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Object2CharMap.Entry<T> belowSamplesGreater() { return new AbstractObject2CharMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Object2CharMap.Entry<T> aboveSamplesLesser() { return new AbstractObject2CharMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Object2CharMap.Entry<T> aboveSamplesGreater() { return new AbstractObject2CharMap.BasicEntry<>(keys[8], values[8]); }
	}
}