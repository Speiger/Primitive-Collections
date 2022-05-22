package speiger.src.testers.shorts.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;
import speiger.src.testers.shorts.utils.ShortHelpers;

@Ignore
public class ShortCollectionToArrayTester extends AbstractShortCollectionTester{
	public void testToArray_noArgs() {
		short[] array = collection.toShortArray();
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_emptyArray() {
		short[] empty = emptyArray();
		short[] array = collection.toShortArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_emptyArray_ordered() {
		short[] empty = emptyArray();
		short[] array = collection.toShortArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_emptyArrayOfObject() {
		short[] in = emptyArray();
		short[] array = collection.toShortArray(in);
		assertEquals("toLongArray(emptyObject[]).length", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_rightSizedArray() {
		short[] array = new short[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toShortArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArray_ordered() {
		short[] array = new short[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toShortArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_rightSizedArrayOfObject() {
		short[] array = new short[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toShortArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArrayOfObject_ordered() {
		short[] array = new short[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toShortArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_oversizedArray() {
		short[] array = new short[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toShortArray(array));

		ShortList subArray = ShortArrayList.wrap(array).subList(0, getNumElements());
		short[] expectedSubArray = createSamplesArray();
		for (int i = 0; i < getNumElements(); i++) {
			assertTrue("toLongArray(overSizedE[]) should contain element " + expectedSubArray[i], subArray.contains(expectedSubArray[i]));
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", (short)0, array[getNumElements()]);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_oversizedArray_ordered() {
		short[] array = new short[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toShortArray(array));

		ShortList expected = getOrderedElements();
		for (int i = 0; i < getNumElements(); i++) {
			assertEquals(expected.getShort(i), array[i]);
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", (short)0, array[getNumElements()]);
	}

	private void expectArrayContentsAnyOrder(short[] expected, short[] actual) {
		ShortHelpers.assertEqualIgnoringOrder(ShortArrayList.wrap(expected), ShortArrayList.wrap(actual));
	}

	private void expectArrayContentsInOrder(ShortList expected, short[] actual) {
		assertEquals("toShortArray() ordered contents: ", expected, ShortArrayList.wrap(actual));
	}
}