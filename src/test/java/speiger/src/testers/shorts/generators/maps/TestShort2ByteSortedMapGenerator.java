package speiger.src.testers.shorts.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteSortedMap;

public interface TestShort2ByteSortedMapGenerator extends TestShort2ByteMapGenerator, TestSortedMapGenerator<Short, Byte>
{
	@Override
	default Short2ByteSortedMap create(Object... elements) { return (Short2ByteSortedMap) TestShort2ByteMapGenerator.super.create(elements); }
	@Override
	Short2ByteSortedMap create(Short2ByteMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}