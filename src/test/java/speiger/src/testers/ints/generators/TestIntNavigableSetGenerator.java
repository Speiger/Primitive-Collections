package speiger.src.testers.ints.generators;

import speiger.src.collections.ints.sets.IntNavigableSet;

@SuppressWarnings("javadoc")
public interface TestIntNavigableSetGenerator extends TestIntSortedSetGenerator {
	@Override
	IntNavigableSet create(int... elements);
	@Override
	IntNavigableSet create(Object... elements);
}