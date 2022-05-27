package speiger.src.testers.doubles.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2FloatMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatSortedMap;

public interface TestDouble2FloatSortedMapGenerator extends TestDouble2FloatMapGenerator, TestSortedMapGenerator<Double, Float>
{
	@Override
	default Double2FloatSortedMap create(Object... elements) { return (Double2FloatSortedMap) TestDouble2FloatMapGenerator.super.create(elements); }
	@Override
	Double2FloatSortedMap create(Double2FloatMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}