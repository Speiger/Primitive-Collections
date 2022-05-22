package speiger.src.testers.shorts.tests.list;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.testers.shorts.tests.base.AbstractShortListTester;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.shorts.utils.MinimalShortCollection;

public class ShortListRetainAllTester extends AbstractShortListTester {
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAll_duplicatesKept() {
		short[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		assertFalse("containsDuplicates.retainAll(superset) should return false", collection.retainAll(MinimalShortCollection.of(createSamplesArray())));
		expectContents(array);
	}

	@SuppressWarnings("unchecked")
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testRetainAll_duplicatesRemoved() {
		short[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		assertTrue("containsDuplicates.retainAll(subset) should return true", collection.retainAll(MinimalShortCollection.of(e2())));
		expectContents(e2());
	}

	@SuppressWarnings("unchecked")
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testRetainAll_countIgnored() {
		resetContainer(primitiveGenerator.create(new short[]{e0(), e2(), e1(), e0()}));
		assertTrue(getList().retainAll(ShortArrayList.wrap(e0(), e1())));
		ShortHelpers.assertContentsInOrder(getList(), e0(), e1(), e0());
	}
}