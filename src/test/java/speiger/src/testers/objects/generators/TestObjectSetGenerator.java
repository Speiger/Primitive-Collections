package speiger.src.testers.objects.generators;

import com.google.common.collect.testing.TestSetGenerator;

import speiger.src.collections.objects.sets.ObjectSet;

public interface TestObjectSetGenerator<T> extends TestObjectCollectionGenerator<T>, TestSetGenerator<T> {
	@Override
	ObjectSet<T> create(Object...elements);
}