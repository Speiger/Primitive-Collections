package speiger.src.testers.longs.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;
import speiger.src.testers.longs.utils.LongHelpers;

@Ignore
public class LongCollectionToArrayTester extends AbstractLongCollectionTester
{
	public void testToArray_noArgs() {
		long[] array = collection.toLongArray();
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_emptyArray() {
		long[] empty = emptyArray();
		long[] array = collection.toLongArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_emptyArray_ordered() {
		long[] empty = emptyArray();
		long[] array = collection.toLongArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_emptyArrayOfObject() {
		long[] in = emptyArray();
		long[] array = collection.toLongArray(in);
		assertEquals("toLongArray(emptyObject[]).length", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_rightSizedArray() {
		long[] array = new long[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toLongArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArray_ordered() {
		long[] array = new long[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toLongArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_rightSizedArrayOfObject() {
		long[] array = new long[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toLongArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArrayOfObject_ordered() {
		long[] array = new long[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toLongArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_oversizedArray() {
		long[] array = new long[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toLongArray(array));

		LongList subArray = LongArrayList.wrap(array).subList(0, getNumElements());
		long[] expectedSubArray = createSamplesArray();
		for (int i = 0; i < getNumElements(); i++) {
			assertTrue("toLongArray(overSizedE[]) should contain element " + expectedSubArray[i], subArray.contains(expectedSubArray[i]));
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", 0L, array[getNumElements()]);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_oversizedArray_ordered() {
		long[] array = new long[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toLongArray(array));

		LongList expected = getOrderedElements();
		for (int i = 0; i < getNumElements(); i++) {
			assertEquals(expected.getLong(i), array[i]);
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", 0L, array[getNumElements()]);
	}

	private void expectArrayContentsAnyOrder(long[] expected, long[] actual) {
		LongHelpers.assertEqualIgnoringOrder(LongArrayList.wrap(expected), LongArrayList.wrap(actual));
	}

	private void expectArrayContentsInOrder(LongList expected, long[] actual) {
		assertEquals("toLongArray() ordered contents: ", expected, LongArrayList.wrap(actual));
	}
}