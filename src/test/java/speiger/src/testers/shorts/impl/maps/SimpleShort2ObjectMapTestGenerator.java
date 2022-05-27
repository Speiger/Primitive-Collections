package speiger.src.testers.shorts.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2ObjectMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.generators.maps.TestShort2ObjectMapGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2ObjectSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleShort2ObjectMapTestGenerator<V, E extends Short2ObjectMap<V>>
{
	BiFunction<short[], V[], E> mapper;
	short[] keys = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	V[] values;
	
	public SimpleShort2ObjectMapTestGenerator(BiFunction<short[], V[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setValues(V... values) {
		this.values = values;
	}
	
	public ObjectSamples<Short2ObjectMap.Entry<V>> getSamples() {
		return new ObjectSamples<Short2ObjectMap.Entry<V>>(
			new AbstractShort2ObjectMap.BasicEntry<>(keys[0], values[0]),
			new AbstractShort2ObjectMap.BasicEntry<>(keys[1], values[1]),
			new AbstractShort2ObjectMap.BasicEntry<>(keys[2], values[2]),
			new AbstractShort2ObjectMap.BasicEntry<>(keys[3], values[3]),
			new AbstractShort2ObjectMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Short2ObjectMap.Entry<V>... elements) {
		short[] keys = new short[elements.length];
		V[] values = (V[])ObjectArrays.newArray(getSamples().e0().getValue().getClass(), elements.length);
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getShortKey();
			values[i] = elements[i].getValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Short, V>> order(List<Map.Entry<Short, V>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Short2ObjectMap.Entry<V>> order(ObjectList<Short2ObjectMap.Entry<V>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<V> extends SimpleShort2ObjectMapTestGenerator<V, Short2ObjectMap<V>> implements TestShort2ObjectMapGenerator<V>
	{
		public Maps(BiFunction<short[], V[], Short2ObjectMap<V>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<V> extends SimpleShort2ObjectMapTestGenerator<V, Short2ObjectSortedMap<V>> implements TestShort2ObjectSortedMapGenerator<V>
	{
		public SortedMaps(BiFunction<short[], V[], Short2ObjectSortedMap<V>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Short, V>> order(List<Map.Entry<Short, V>> insertionOrder) {
			insertionOrder.sort(DerivedShort2ObjectMapGenerators.entryObjectComparator(Short::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Short2ObjectMap.Entry<V>> order(ObjectList<Short2ObjectMap.Entry<V>> insertionOrder) {
			insertionOrder.sort(DerivedShort2ObjectMapGenerators.entryComparator(Short::compare));
			return insertionOrder;
		}
		
		@Override
		public Short2ObjectMap.Entry<V> belowSamplesLesser() { return new AbstractShort2ObjectMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Short2ObjectMap.Entry<V> belowSamplesGreater() { return new AbstractShort2ObjectMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Short2ObjectMap.Entry<V> aboveSamplesLesser() { return new AbstractShort2ObjectMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Short2ObjectMap.Entry<V> aboveSamplesGreater() { return new AbstractShort2ObjectMap.BasicEntry<>(keys[8], values[8]); }
	}
}