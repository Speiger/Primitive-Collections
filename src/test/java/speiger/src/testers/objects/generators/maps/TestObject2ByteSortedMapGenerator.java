package speiger.src.testers.objects.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2ByteSortedMap;

@SuppressWarnings("javadoc")
public interface TestObject2ByteSortedMapGenerator<T> extends TestObject2ByteMapGenerator<T>, TestSortedMapGenerator<T, Byte>
{
	@Override
	default Object2ByteSortedMap<T> create(Object... elements) { return (Object2ByteSortedMap<T>) TestObject2ByteMapGenerator.super.create(elements); }
	@Override
	Object2ByteSortedMap<T> create(Object2ByteMap.Entry<T>... elements);
	
	@Override
	Entry<T> belowSamplesLesser();
	@Override
	Entry<T> belowSamplesGreater();
	@Override
	Entry<T> aboveSamplesLesser();
	@Override
	Entry<T> aboveSamplesGreater();
}