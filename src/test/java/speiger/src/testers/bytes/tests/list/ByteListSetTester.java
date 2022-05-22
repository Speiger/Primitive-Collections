package speiger.src.testers.bytes.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_SET;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.testers.bytes.tests.base.AbstractByteListTester;

@Ignore
public class ByteListSetTester extends AbstractByteListTester {
	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = ZERO)
	public void testSet() {
		doTestSet(e3());
	}
	
	private void doTestSet(byte newValue) {
		int index = aValidIndex();
		byte initialValue = getList().getByte(index);
		assertEquals("set(i, x) should return the old element at position i.", initialValue, getList().set(index, newValue));
		assertEquals("After set(i, x), get(i) should return x", newValue, getList().getByte(index));
		assertEquals("set() should not change the size of a list.", getNumElements(), getList().size());
	}

	@ListFeature.Require(SUPPORTS_SET)
	public void testSet_indexTooLow() {
		try {
			getList().set(-1, e3());
			fail("set(-1) should throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_SET)
	public void testSet_indexTooHigh() {
		int index = getNumElements();
		try {
			getList().set(index, e3());
			fail("set(size) should throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
	}

	@CollectionSize.Require(absent = ZERO)
	@ListFeature.Require(absent = SUPPORTS_SET)
	public void testSet_unsupported() {
		try {
			getList().set(aValidIndex(), e3());
			fail("set() should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@CollectionSize.Require(ZERO)
	@ListFeature.Require(absent = SUPPORTS_SET)
	public void testSet_unsupportedByEmptyList() {
		try {
			getList().set(0, e3());
			fail("set() should throw UnsupportedOperationException or IndexOutOfBoundsException");
		} catch (UnsupportedOperationException | IndexOutOfBoundsException tolerated) {
		}
		expectUnchanged();
	}

	private int aValidIndex() {
		return getList().size() / 2;
	}
}