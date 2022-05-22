package speiger.src.testers.shorts.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.shorts.tests.base.AbstractShortListTester;
import speiger.src.testers.shorts.utils.MinimalShortCollection;

@Ignore
public class ShortListRemoveAllTester extends AbstractShortListTester {
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRemoveAll_duplicate() {
		ArrayWithDuplicate arrayAndDuplicate = createArrayWithDuplicateElement();
		collection = primitiveGenerator.create(arrayAndDuplicate.elements);
		short duplicate = arrayAndDuplicate.duplicate;
		assertTrue("removeAll(intersectingCollection) should return true", getList().removeAll(MinimalShortCollection.of(duplicate)));
		assertFalse("after removeAll(e), a collection should not contain e even if it initially contained e more than once.", getList().contains(duplicate));
	}
}