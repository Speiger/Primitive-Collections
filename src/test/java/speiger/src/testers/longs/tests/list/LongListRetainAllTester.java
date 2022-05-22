package speiger.src.testers.longs.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.testers.longs.tests.base.AbstractLongListTester;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.longs.utils.MinimalLongCollection;

@Ignore
public class LongListRetainAllTester extends AbstractLongListTester {
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAll_duplicatesKept() {
		long[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		assertFalse("containsDuplicates.retainAll(superset) should return false", collection.retainAll(MinimalLongCollection.of(createSamplesArray())));
		expectContents(array);
	}

	@SuppressWarnings("unchecked")
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testRetainAll_duplicatesRemoved() {
		long[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		assertTrue("containsDuplicates.retainAll(subset) should return true", collection.retainAll(MinimalLongCollection.of(e2())));
		expectContents(e2());
	}

	@SuppressWarnings("unchecked")
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testRetainAll_countIgnored() {
		resetContainer(primitiveGenerator.create(new long[]{e0(), e2(), e1(), e0()}));
		assertTrue(getList().retainAll(LongArrayList.wrap(e0(), e1())));
		LongHelpers.assertContentsInOrder(getList(), e0(), e1(), e0());
	}
}