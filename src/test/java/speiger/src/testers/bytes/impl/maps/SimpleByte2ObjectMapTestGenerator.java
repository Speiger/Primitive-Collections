package speiger.src.testers.bytes.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ObjectMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.generators.maps.TestByte2ObjectMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2ObjectSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleByte2ObjectMapTestGenerator<V, E extends Byte2ObjectMap<V>>
{
	BiFunction<byte[], V[], E> mapper;
	byte[] keys = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	V[] values;
	
	public SimpleByte2ObjectMapTestGenerator(BiFunction<byte[], V[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setValues(V... values) {
		this.values = values;
	}
	
	public ObjectSamples<Byte2ObjectMap.Entry<V>> getSamples() {
		return new ObjectSamples<Byte2ObjectMap.Entry<V>>(
			new AbstractByte2ObjectMap.BasicEntry<>(keys[0], values[0]),
			new AbstractByte2ObjectMap.BasicEntry<>(keys[1], values[1]),
			new AbstractByte2ObjectMap.BasicEntry<>(keys[2], values[2]),
			new AbstractByte2ObjectMap.BasicEntry<>(keys[3], values[3]),
			new AbstractByte2ObjectMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Byte2ObjectMap.Entry<V>... elements) {
		byte[] keys = new byte[elements.length];
		V[] values = (V[])ObjectArrays.newArray(getSamples().e0().getValue().getClass(), elements.length);
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getByteKey();
			values[i] = elements[i].getValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Byte, V>> order(List<Map.Entry<Byte, V>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Byte2ObjectMap.Entry<V>> order(ObjectList<Byte2ObjectMap.Entry<V>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<V> extends SimpleByte2ObjectMapTestGenerator<V, Byte2ObjectMap<V>> implements TestByte2ObjectMapGenerator<V>
	{
		public Maps(BiFunction<byte[], V[], Byte2ObjectMap<V>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<V> extends SimpleByte2ObjectMapTestGenerator<V, Byte2ObjectSortedMap<V>> implements TestByte2ObjectSortedMapGenerator<V>
	{
		public SortedMaps(BiFunction<byte[], V[], Byte2ObjectSortedMap<V>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Byte, V>> order(List<Map.Entry<Byte, V>> insertionOrder) {
			insertionOrder.sort(DerivedByte2ObjectMapGenerators.entryObjectComparator(Byte::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Byte2ObjectMap.Entry<V>> order(ObjectList<Byte2ObjectMap.Entry<V>> insertionOrder) {
			insertionOrder.sort(DerivedByte2ObjectMapGenerators.entryComparator(Byte::compare));
			return insertionOrder;
		}
		
		@Override
		public Byte2ObjectMap.Entry<V> belowSamplesLesser() { return new AbstractByte2ObjectMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Byte2ObjectMap.Entry<V> belowSamplesGreater() { return new AbstractByte2ObjectMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Byte2ObjectMap.Entry<V> aboveSamplesLesser() { return new AbstractByte2ObjectMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Byte2ObjectMap.Entry<V> aboveSamplesGreater() { return new AbstractByte2ObjectMap.BasicEntry<>(keys[8], values[8]); }
	}
}