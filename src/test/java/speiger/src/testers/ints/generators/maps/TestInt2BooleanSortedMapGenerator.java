package speiger.src.testers.ints.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanSortedMap;

public interface TestInt2BooleanSortedMapGenerator extends TestInt2BooleanMapGenerator, TestSortedMapGenerator<Integer, Boolean>
{
	@Override
	default Int2BooleanSortedMap create(Object... elements) { return (Int2BooleanSortedMap) TestInt2BooleanMapGenerator.super.create(elements); }
	@Override
	Int2BooleanSortedMap create(Int2BooleanMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}