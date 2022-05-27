package speiger.src.testers.longs.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2FloatMap;
import speiger.src.collections.longs.maps.interfaces.Long2FloatMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2FloatSortedMap;

public interface TestLong2FloatSortedMapGenerator extends TestLong2FloatMapGenerator, TestSortedMapGenerator<Long, Float>
{
	@Override
	default Long2FloatSortedMap create(Object... elements) { return (Long2FloatSortedMap) TestLong2FloatMapGenerator.super.create(elements); }
	@Override
	Long2FloatSortedMap create(Long2FloatMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}