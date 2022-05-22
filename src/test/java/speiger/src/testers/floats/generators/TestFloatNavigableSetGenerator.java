package speiger.src.testers.floats.generators;

import speiger.src.collections.floats.sets.FloatNavigableSet;

public interface TestFloatNavigableSetGenerator extends TestFloatSortedSetGenerator {
	@Override
	FloatNavigableSet create(float... elements);
	@Override
	FloatNavigableSet create(Object... elements);
}