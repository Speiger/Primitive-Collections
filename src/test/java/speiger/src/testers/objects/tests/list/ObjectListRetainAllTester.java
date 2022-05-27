package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.objects.tests.base.AbstractObjectListTester;
import speiger.src.testers.objects.utils.ObjectHelpers;
import speiger.src.testers.objects.utils.MinimalObjectCollection;

@Ignore
public class ObjectListRetainAllTester<T> extends AbstractObjectListTester<T>
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAll_duplicatesKept() {
		T[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		assertFalse("containsDuplicates.retainAll(superset) should return false", collection.retainAll(MinimalObjectCollection.of(createSamplesArray())));
		expectContents(array);
	}

	@SuppressWarnings("unchecked")
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testRetainAll_duplicatesRemoved() {
		T[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		assertTrue("containsDuplicates.retainAll(subset) should return true", collection.retainAll(MinimalObjectCollection.of(e2())));
		expectContents(e2());
	}

	@SuppressWarnings("unchecked")
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testRetainAll_countIgnored() {
		resetContainer(primitiveGenerator.create(createArray(e0(), e2(), e1(), e0())));
		assertTrue(getList().retainAll(ObjectArrayList.wrap(e0(), e1())));
		ObjectHelpers.assertContentsInOrder(getList(), e0(), e1(), e0());
	}
}