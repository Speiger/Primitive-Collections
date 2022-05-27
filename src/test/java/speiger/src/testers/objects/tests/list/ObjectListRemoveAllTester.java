package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.objects.tests.base.AbstractObjectListTester;
import speiger.src.testers.objects.utils.MinimalObjectCollection;

@Ignore
public class ObjectListRemoveAllTester<T> extends AbstractObjectListTester<T>
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRemoveAll_duplicate() {
		ArrayWithDuplicate<T> arrayAndDuplicate = createArrayWithDuplicateElement();
		collection = primitiveGenerator.create(arrayAndDuplicate.elements);
		T duplicate = arrayAndDuplicate.duplicate;
		assertTrue("removeAll(intersectingCollection) should return true", getList().removeAll(MinimalObjectCollection.of(duplicate)));
		assertFalse("after removeAll(e), a collection should not contain e even if it initially contained e more than once.", getList().contains(duplicate));
	}
}