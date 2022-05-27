package speiger.src.testers.doubles.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2BooleanMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanSortedMap;

@SuppressWarnings("javadoc")
public interface TestDouble2BooleanSortedMapGenerator extends TestDouble2BooleanMapGenerator, TestSortedMapGenerator<Double, Boolean>
{
	@Override
	default Double2BooleanSortedMap create(Object... elements) { return (Double2BooleanSortedMap) TestDouble2BooleanMapGenerator.super.create(elements); }
	@Override
	Double2BooleanSortedMap create(Double2BooleanMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}