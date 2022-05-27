package speiger.src.testers.doubles.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleSortedMap;

@SuppressWarnings("javadoc")
public interface TestDouble2DoubleSortedMapGenerator extends TestDouble2DoubleMapGenerator, TestSortedMapGenerator<Double, Double>
{
	@Override
	default Double2DoubleSortedMap create(Object... elements) { return (Double2DoubleSortedMap) TestDouble2DoubleMapGenerator.super.create(elements); }
	@Override
	Double2DoubleSortedMap create(Double2DoubleMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}