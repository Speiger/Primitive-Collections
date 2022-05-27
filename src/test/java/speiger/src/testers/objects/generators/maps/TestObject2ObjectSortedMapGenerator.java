package speiger.src.testers.objects.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectSortedMap;

@SuppressWarnings("javadoc")
public interface TestObject2ObjectSortedMapGenerator<T, V> extends TestObject2ObjectMapGenerator<T, V>, TestSortedMapGenerator<T, V>
{
	@Override
	default Object2ObjectSortedMap<T, V> create(Object... elements) { return (Object2ObjectSortedMap<T, V>) TestObject2ObjectMapGenerator.super.create(elements); }
	@Override
	Object2ObjectSortedMap<T, V> create(Object2ObjectMap.Entry<T, V>... elements);
	
	@Override
	Entry<T, V> belowSamplesLesser();
	@Override
	Entry<T, V> belowSamplesGreater();
	@Override
	Entry<T, V> aboveSamplesLesser();
	@Override
	Entry<T, V> aboveSamplesGreater();
}