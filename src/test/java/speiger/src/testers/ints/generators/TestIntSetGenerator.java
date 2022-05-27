package speiger.src.testers.ints.generators;

import com.google.common.collect.testing.TestSetGenerator;

import speiger.src.collections.ints.sets.IntSet;

@SuppressWarnings("javadoc")
public interface TestIntSetGenerator extends TestIntCollectionGenerator, TestSetGenerator<Integer> {
	@Override
	IntSet create(int...elements);
	@Override
	IntSet create(Object...elements);
}