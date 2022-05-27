package speiger.src.testers.floats.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2ByteMap;
import speiger.src.collections.floats.maps.interfaces.Float2ByteMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2ByteSortedMap;

@SuppressWarnings("javadoc")
public interface TestFloat2ByteSortedMapGenerator extends TestFloat2ByteMapGenerator, TestSortedMapGenerator<Float, Byte>
{
	@Override
	default Float2ByteSortedMap create(Object... elements) { return (Float2ByteSortedMap) TestFloat2ByteMapGenerator.super.create(elements); }
	@Override
	Float2ByteSortedMap create(Float2ByteMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}