package speiger.src.testers.floats.generators;

import speiger.src.collections.floats.sets.FloatOrderedSet;

public interface TestFloatOrderedSetGenerator extends TestFloatSetGenerator {
	@Override
	FloatOrderedSet create(float... elements);
	@Override
	FloatOrderedSet create(Object... elements);
}