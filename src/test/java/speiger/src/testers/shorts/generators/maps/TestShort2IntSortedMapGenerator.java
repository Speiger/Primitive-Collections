package speiger.src.testers.shorts.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2IntSortedMap;

@SuppressWarnings("javadoc")
public interface TestShort2IntSortedMapGenerator extends TestShort2IntMapGenerator, TestSortedMapGenerator<Short, Integer>
{
	@Override
	default Short2IntSortedMap create(Object... elements) { return (Short2IntSortedMap) TestShort2IntMapGenerator.super.create(elements); }
	@Override
	Short2IntSortedMap create(Short2IntMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}