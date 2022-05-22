package speiger.src.testers.shorts.generators;

import com.google.common.collect.testing.TestSetGenerator;

import speiger.src.collections.shorts.sets.ShortSet;

public interface TestShortSetGenerator extends TestShortCollectionGenerator, TestSetGenerator<Short> {
	@Override
	ShortSet create(short...elements);
	@Override
	ShortSet create(Object...elements);
}