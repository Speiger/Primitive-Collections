package speiger.src.testers.objects.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanSortedMap;

@SuppressWarnings("javadoc")
public interface TestObject2BooleanSortedMapGenerator<T> extends TestObject2BooleanMapGenerator<T>, TestSortedMapGenerator<T, Boolean>
{
	@Override
	default Object2BooleanSortedMap<T> create(Object... elements) { return (Object2BooleanSortedMap<T>) TestObject2BooleanMapGenerator.super.create(elements); }
	@Override
	Object2BooleanSortedMap<T> create(Object2BooleanMap.Entry<T>... elements);
	
	@Override
	Entry<T> belowSamplesLesser();
	@Override
	Entry<T> belowSamplesGreater();
	@Override
	Entry<T> aboveSamplesLesser();
	@Override
	Entry<T> aboveSamplesGreater();
}