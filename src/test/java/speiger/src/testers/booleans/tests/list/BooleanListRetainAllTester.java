package speiger.src.testers.booleans.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.testers.booleans.tests.base.AbstractBooleanListTester;
import speiger.src.testers.booleans.utils.BooleanHelpers;
import speiger.src.testers.booleans.utils.MinimalBooleanCollection;

@Ignore
public class BooleanListRetainAllTester extends AbstractBooleanListTester
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAll_duplicatesKept() {
		boolean[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		assertFalse("containsDuplicates.retainAll(superset) should return false", collection.retainAll(MinimalBooleanCollection.of(createSamplesArray())));
		expectContents(array);
	}

	@SuppressWarnings("unchecked")
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testRetainAll_duplicatesRemoved() {
		boolean[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		assertTrue("containsDuplicates.retainAll(subset) should return true", collection.retainAll(MinimalBooleanCollection.of(e2())));
		expectContents(e2());
	}

	@SuppressWarnings("unchecked")
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testRetainAll_countIgnored() {
		resetContainer(primitiveGenerator.create(createArray(e0(), e2(), e1(), e0())));
		assertTrue(getList().retainAll(BooleanArrayList.wrap(e0(), e1())));
		BooleanHelpers.assertContentsInOrder(getList(), e0(), e1(), e0());
	}
}