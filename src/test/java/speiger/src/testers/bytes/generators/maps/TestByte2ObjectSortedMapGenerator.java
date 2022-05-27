package speiger.src.testers.bytes.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectSortedMap;

public interface TestByte2ObjectSortedMapGenerator<V> extends TestByte2ObjectMapGenerator<V>, TestSortedMapGenerator<Byte, V>
{
	@Override
	default Byte2ObjectSortedMap<V> create(Object... elements) { return (Byte2ObjectSortedMap<V>) TestByte2ObjectMapGenerator.super.create(elements); }
	@Override
	Byte2ObjectSortedMap<V> create(Byte2ObjectMap.Entry<V>... elements);
	
	@Override
	Entry<V> belowSamplesLesser();
	@Override
	Entry<V> belowSamplesGreater();
	@Override
	Entry<V> aboveSamplesLesser();
	@Override
	Entry<V> aboveSamplesGreater();
}