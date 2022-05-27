package speiger.src.testers.floats.generators;

import speiger.src.collections.floats.sets.FloatNavigableSet;

@SuppressWarnings("javadoc")
public interface TestFloatNavigableSetGenerator extends TestFloatSortedSetGenerator {
	@Override
	FloatNavigableSet create(float... elements);
	@Override
	FloatNavigableSet create(Object... elements);
}