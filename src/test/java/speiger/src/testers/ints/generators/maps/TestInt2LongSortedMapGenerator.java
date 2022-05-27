package speiger.src.testers.ints.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2LongMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2LongSortedMap;

public interface TestInt2LongSortedMapGenerator extends TestInt2LongMapGenerator, TestSortedMapGenerator<Integer, Long>
{
	@Override
	default Int2LongSortedMap create(Object... elements) { return (Int2LongSortedMap) TestInt2LongMapGenerator.super.create(elements); }
	@Override
	Int2LongSortedMap create(Int2LongMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}