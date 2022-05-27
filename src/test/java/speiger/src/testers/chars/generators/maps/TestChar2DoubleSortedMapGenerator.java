package speiger.src.testers.chars.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleSortedMap;

@SuppressWarnings("javadoc")
public interface TestChar2DoubleSortedMapGenerator extends TestChar2DoubleMapGenerator, TestSortedMapGenerator<Character, Double>
{
	@Override
	default Char2DoubleSortedMap create(Object... elements) { return (Char2DoubleSortedMap) TestChar2DoubleMapGenerator.super.create(elements); }
	@Override
	Char2DoubleSortedMap create(Char2DoubleMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}