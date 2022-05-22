package speiger.src.testers.chars.generators;

import com.google.common.collect.testing.TestSetGenerator;

import speiger.src.collections.chars.sets.CharSet;

public interface TestCharSetGenerator extends TestCharCollectionGenerator, TestSetGenerator<Character> {
	@Override
	CharSet create(char...elements);
	@Override
	CharSet create(Object...elements);
}