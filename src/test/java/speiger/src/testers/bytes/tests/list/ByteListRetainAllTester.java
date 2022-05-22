package speiger.src.testers.bytes.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.testers.bytes.tests.base.AbstractByteListTester;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.bytes.utils.MinimalByteCollection;

@Ignore
public class ByteListRetainAllTester extends AbstractByteListTester {
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAll_duplicatesKept() {
		byte[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		assertFalse("containsDuplicates.retainAll(superset) should return false", collection.retainAll(MinimalByteCollection.of(createSamplesArray())));
		expectContents(array);
	}

	@SuppressWarnings("unchecked")
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testRetainAll_duplicatesRemoved() {
		byte[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		assertTrue("containsDuplicates.retainAll(subset) should return true", collection.retainAll(MinimalByteCollection.of(e2())));
		expectContents(e2());
	}

	@SuppressWarnings("unchecked")
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testRetainAll_countIgnored() {
		resetContainer(primitiveGenerator.create(new byte[]{e0(), e2(), e1(), e0()}));
		assertTrue(getList().retainAll(ByteArrayList.wrap(e0(), e1())));
		ByteHelpers.assertContentsInOrder(getList(), e0(), e1(), e0());
	}
}