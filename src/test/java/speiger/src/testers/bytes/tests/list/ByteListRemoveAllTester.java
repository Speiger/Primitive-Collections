package speiger.src.testers.bytes.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.bytes.tests.base.AbstractByteListTester;
import speiger.src.testers.bytes.utils.MinimalByteCollection;

@Ignore
public class ByteListRemoveAllTester extends AbstractByteListTester
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRemoveAll_duplicate() {
		ArrayWithDuplicate arrayAndDuplicate = createArrayWithDuplicateElement();
		collection = primitiveGenerator.create(arrayAndDuplicate.elements);
		byte duplicate = arrayAndDuplicate.duplicate;
		assertTrue("removeAll(intersectingCollection) should return true", getList().removeAll(MinimalByteCollection.of(duplicate)));
		assertFalse("after removeAll(e), a collection should not contain e even if it initially contained e more than once.", getList().contains(duplicate));
	}
}