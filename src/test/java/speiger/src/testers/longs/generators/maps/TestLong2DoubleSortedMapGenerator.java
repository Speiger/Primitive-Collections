package speiger.src.testers.longs.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleSortedMap;

@SuppressWarnings("javadoc")
public interface TestLong2DoubleSortedMapGenerator extends TestLong2DoubleMapGenerator, TestSortedMapGenerator<Long, Double>
{
	@Override
	default Long2DoubleSortedMap create(Object... elements) { return (Long2DoubleSortedMap) TestLong2DoubleMapGenerator.super.create(elements); }
	@Override
	Long2DoubleSortedMap create(Long2DoubleMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}