package speiger.src.testers.ints.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2FloatMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2FloatSortedMap;

public interface TestInt2FloatSortedMapGenerator extends TestInt2FloatMapGenerator, TestSortedMapGenerator<Integer, Float>
{
	@Override
	default Int2FloatSortedMap create(Object... elements) { return (Int2FloatSortedMap) TestInt2FloatMapGenerator.super.create(elements); }
	@Override
	Int2FloatSortedMap create(Int2FloatMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}