package speiger.src.testers.longs.generators;

import com.google.common.collect.testing.TestSetGenerator;

import speiger.src.collections.longs.sets.LongSet;

public interface TestLongSetGenerator extends TestLongCollectionGenerator, TestSetGenerator<Long> {
	@Override
	LongSet create(long...elements);
	@Override
	LongSet create(Object...elements);
}