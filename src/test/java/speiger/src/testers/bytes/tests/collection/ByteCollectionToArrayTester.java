package speiger.src.testers.bytes.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;
import speiger.src.testers.bytes.utils.ByteHelpers;

public class ByteCollectionToArrayTester extends AbstractByteCollectionTester{
	public void testToArray_noArgs() {
		byte[] array = collection.toByteArray();
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_emptyArray() {
		byte[] empty = emptyArray();
		byte[] array = collection.toByteArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_emptyArray_ordered() {
		byte[] empty = emptyArray();
		byte[] array = collection.toByteArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_emptyArrayOfObject() {
		byte[] in = emptyArray();
		byte[] array = collection.toByteArray(in);
		assertEquals("toLongArray(emptyObject[]).length", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_rightSizedArray() {
		byte[] array = new byte[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toByteArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArray_ordered() {
		byte[] array = new byte[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toByteArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_rightSizedArrayOfObject() {
		byte[] array = new byte[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toByteArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArrayOfObject_ordered() {
		byte[] array = new byte[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toByteArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_oversizedArray() {
		byte[] array = new byte[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toByteArray(array));

		ByteList subArray = ByteArrayList.wrap(array).subList(0, getNumElements());
		byte[] expectedSubArray = createSamplesArray();
		for (int i = 0; i < getNumElements(); i++) {
			assertTrue("toLongArray(overSizedE[]) should contain element " + expectedSubArray[i], subArray.contains(expectedSubArray[i]));
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", (byte)0, array[getNumElements()]);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_oversizedArray_ordered() {
		byte[] array = new byte[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toByteArray(array));

		ByteList expected = getOrderedElements();
		for (int i = 0; i < getNumElements(); i++) {
			assertEquals(expected.getByte(i), array[i]);
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", (byte)0, array[getNumElements()]);
	}

	private void expectArrayContentsAnyOrder(byte[] expected, byte[] actual) {
		ByteHelpers.assertEqualIgnoringOrder(ByteArrayList.wrap(expected), ByteArrayList.wrap(actual));
	}

	private void expectArrayContentsInOrder(ByteList expected, byte[] actual) {
		assertEquals("toByteArray() ordered contents: ", expected, ByteArrayList.wrap(actual));
	}
}