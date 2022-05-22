package speiger.src.testers.ints.generators;

import speiger.src.collections.ints.sets.IntOrderedSet;

public interface TestIntOrderedSetGenerator extends TestIntSetGenerator {
	@Override
	IntOrderedSet create(int... elements);
	@Override
	IntOrderedSet create(Object... elements);
}