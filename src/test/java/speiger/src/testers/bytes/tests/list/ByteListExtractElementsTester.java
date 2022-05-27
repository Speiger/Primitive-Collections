package speiger.src.testers.bytes.tests.list;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import org.junit.Ignore;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.testers.bytes.tests.base.AbstractByteListTester;

@Ignore
@SuppressWarnings("javadoc")
public class ByteListExtractElementsTester extends AbstractByteListTester
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemoveElements() {
		assertArrayEquals("The Extracted Elements should be present", getSampleElements(1).toByteArray(), getList().extractElements(0, 1));
		expectMissing(e0());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemove_FromToLow() {
		try {
			getList().extractElements(-1, 1);
			fail("extractElements(toLow, high) should throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemove_toToHigh() {
		try {
			getList().extractElements(0, getNumElements()+1);
			fail("extractElements(low, toHigh) should throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemove_mixedUp() {
		try {
			getList().extractElements(1, 0);
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
	}
	
	private static void assertArrayEquals(String message, byte[] expected, byte[] actual) {
		assertEquals(message, ByteArrayList.wrap(expected), ByteArrayList.wrap(actual));
	}
}