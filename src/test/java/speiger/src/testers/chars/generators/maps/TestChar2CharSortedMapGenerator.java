package speiger.src.testers.chars.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2CharSortedMap;

public interface TestChar2CharSortedMapGenerator extends TestChar2CharMapGenerator, TestSortedMapGenerator<Character, Character>
{
	@Override
	default Char2CharSortedMap create(Object... elements) { return (Char2CharSortedMap) TestChar2CharMapGenerator.super.create(elements); }
	@Override
	Char2CharSortedMap create(Char2CharMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}