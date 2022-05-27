package speiger.src.testers.objects.generators;

import speiger.src.collections.objects.sets.ObjectNavigableSet;

@SuppressWarnings("javadoc")
public interface TestObjectNavigableSetGenerator<T> extends TestObjectSortedSetGenerator<T> {
	@Override
	ObjectNavigableSet<T> create(Object... elements);
}