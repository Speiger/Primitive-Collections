package speiger.src.testers.objects.generators;

import com.google.common.collect.testing.TestListGenerator;

import speiger.src.collections.objects.lists.ObjectList;

public interface TestObjectListGenerator<T> extends TestListGenerator<T>, TestObjectCollectionGenerator<T>
{
	@Override
	ObjectList<T> create(Object... elements);
}