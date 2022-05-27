package speiger.src.testers.booleans.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.testers.booleans.tests.base.AbstractBooleanCollectionTester;
import speiger.src.testers.booleans.utils.BooleanHelpers;

@Ignore
public class BooleanCollectionToArrayTester extends AbstractBooleanCollectionTester
{
	public void testToArray_noArgs() {
		boolean[] array = collection.toBooleanArray();
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_emptyArray() {
		boolean[] empty = emptyArray();
		boolean[] array = collection.toBooleanArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_emptyArray_ordered() {
		boolean[] empty = emptyArray();
		boolean[] array = collection.toBooleanArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_emptyArrayOfObject() {
		boolean[] in = emptyArray();
		boolean[] array = collection.toBooleanArray(in);
		assertEquals("toLongArray(emptyObject[]).length", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_rightSizedArray() {
		boolean[] array = new boolean[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toBooleanArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArray_ordered() {
		boolean[] array = new boolean[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toBooleanArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_rightSizedArrayOfObject() {
		boolean[] array = new boolean[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toBooleanArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArrayOfObject_ordered() {
		boolean[] array = new boolean[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toBooleanArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_oversizedArray() {
		boolean[] array = new boolean[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toBooleanArray(array));

		BooleanList subArray = BooleanArrayList.wrap(array).subList(0, getNumElements());
		boolean[] expectedSubArray = createSamplesArray();
		for (int i = 0; i < getNumElements(); i++) {
			assertTrue("toLongArray(overSizedE[]) should contain element " + expectedSubArray[i], subArray.contains(expectedSubArray[i]));
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", false, array[getNumElements()]);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_oversizedArray_ordered() {
		boolean[] array = new boolean[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toBooleanArray(array));

		BooleanList expected = getOrderedElements();
		for (int i = 0; i < getNumElements(); i++) {
			assertEquals(expected.getBoolean(i), array[i]);
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", false, array[getNumElements()]);
	}

	private void expectArrayContentsAnyOrder(boolean[] expected, boolean[] actual) {
		BooleanHelpers.assertEqualIgnoringOrder(BooleanArrayList.wrap(expected), BooleanArrayList.wrap(actual));
	}

	private void expectArrayContentsInOrder(BooleanList expected, boolean[] actual) {
		assertEquals("toBooleanArray() ordered contents: ", expected, BooleanArrayList.wrap(actual));
	}
}