package speiger.src.testers.doubles.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2CharMap;
import speiger.src.collections.doubles.maps.interfaces.Double2CharMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2CharSortedMap;

@SuppressWarnings("javadoc")
public interface TestDouble2CharSortedMapGenerator extends TestDouble2CharMapGenerator, TestSortedMapGenerator<Double, Character>
{
	@Override
	default Double2CharSortedMap create(Object... elements) { return (Double2CharSortedMap) TestDouble2CharMapGenerator.super.create(elements); }
	@Override
	Double2CharSortedMap create(Double2CharMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}