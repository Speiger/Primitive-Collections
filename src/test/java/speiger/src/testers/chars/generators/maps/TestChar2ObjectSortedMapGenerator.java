package speiger.src.testers.chars.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectSortedMap;

@SuppressWarnings("javadoc")
public interface TestChar2ObjectSortedMapGenerator<V> extends TestChar2ObjectMapGenerator<V>, TestSortedMapGenerator<Character, V>
{
	@Override
	default Char2ObjectSortedMap<V> create(Object... elements) { return (Char2ObjectSortedMap<V>) TestChar2ObjectMapGenerator.super.create(elements); }
	@Override
	Char2ObjectSortedMap<V> create(Char2ObjectMap.Entry<V>... elements);
	
	@Override
	Entry<V> belowSamplesLesser();
	@Override
	Entry<V> belowSamplesGreater();
	@Override
	Entry<V> aboveSamplesLesser();
	@Override
	Entry<V> aboveSamplesGreater();
}