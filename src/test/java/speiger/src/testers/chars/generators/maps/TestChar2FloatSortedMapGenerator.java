package speiger.src.testers.chars.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2FloatMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2FloatSortedMap;

@SuppressWarnings("javadoc")
public interface TestChar2FloatSortedMapGenerator extends TestChar2FloatMapGenerator, TestSortedMapGenerator<Character, Float>
{
	@Override
	default Char2FloatSortedMap create(Object... elements) { return (Char2FloatSortedMap) TestChar2FloatMapGenerator.super.create(elements); }
	@Override
	Char2FloatSortedMap create(Char2FloatMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}