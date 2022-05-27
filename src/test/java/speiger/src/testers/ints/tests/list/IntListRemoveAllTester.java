package speiger.src.testers.ints.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.ints.tests.base.AbstractIntListTester;
import speiger.src.testers.ints.utils.MinimalIntCollection;

@Ignore
@SuppressWarnings("javadoc")
public class IntListRemoveAllTester extends AbstractIntListTester
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRemoveAll_duplicate() {
		ArrayWithDuplicate arrayAndDuplicate = createArrayWithDuplicateElement();
		collection = primitiveGenerator.create(arrayAndDuplicate.elements);
		int duplicate = arrayAndDuplicate.duplicate;
		assertTrue("removeAll(intersectingCollection) should return true", getList().removeAll(MinimalIntCollection.of(duplicate)));
		assertFalse("after removeAll(e), a collection should not contain e even if it initially contained e more than once.", getList().contains(duplicate));
	}
}