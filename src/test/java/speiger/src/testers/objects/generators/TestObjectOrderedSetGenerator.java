package speiger.src.testers.objects.generators;

import speiger.src.collections.objects.sets.ObjectOrderedSet;

@SuppressWarnings("javadoc")
public interface TestObjectOrderedSetGenerator<T> extends TestObjectSetGenerator<T> {
	@Override
	ObjectOrderedSet<T> create(Object... elements);
}