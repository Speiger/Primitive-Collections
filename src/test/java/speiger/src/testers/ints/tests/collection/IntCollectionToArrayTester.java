package speiger.src.testers.ints.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.testers.ints.tests.base.AbstractIntCollectionTester;
import speiger.src.testers.ints.utils.IntHelpers;

public class IntCollectionToArrayTester extends AbstractIntCollectionTester{
	public void testToArray_noArgs() {
		int[] array = collection.toIntArray();
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_emptyArray() {
		int[] empty = emptyArray();
		int[] array = collection.toIntArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_emptyArray_ordered() {
		int[] empty = emptyArray();
		int[] array = collection.toIntArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_emptyArrayOfObject() {
		int[] in = emptyArray();
		int[] array = collection.toIntArray(in);
		assertEquals("toLongArray(emptyObject[]).length", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_rightSizedArray() {
		int[] array = new int[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toIntArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArray_ordered() {
		int[] array = new int[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toIntArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_rightSizedArrayOfObject() {
		int[] array = new int[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toIntArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArrayOfObject_ordered() {
		int[] array = new int[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toIntArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_oversizedArray() {
		int[] array = new int[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toIntArray(array));

		IntList subArray = IntArrayList.wrap(array).subList(0, getNumElements());
		int[] expectedSubArray = createSamplesArray();
		for (int i = 0; i < getNumElements(); i++) {
			assertTrue("toLongArray(overSizedE[]) should contain element " + expectedSubArray[i], subArray.contains(expectedSubArray[i]));
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", 0, array[getNumElements()]);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_oversizedArray_ordered() {
		int[] array = new int[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toIntArray(array));

		IntList expected = getOrderedElements();
		for (int i = 0; i < getNumElements(); i++) {
			assertEquals(expected.getInt(i), array[i]);
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", 0, array[getNumElements()]);
	}

	private void expectArrayContentsAnyOrder(int[] expected, int[] actual) {
		IntHelpers.assertEqualIgnoringOrder(IntArrayList.wrap(expected), IntArrayList.wrap(actual));
	}

	private void expectArrayContentsInOrder(IntList expected, int[] actual) {
		assertEquals("toIntArray() ordered contents: ", expected, IntArrayList.wrap(actual));
	}
}