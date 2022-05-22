package speiger.src.testers.shorts.tests.list;

import org.junit.Ignore;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_ADD_WITH_INDEX;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.testers.shorts.tests.base.AbstractShortListTester;

@Ignore
public class ShortListAddAllArrayAtIndexTester extends AbstractShortListTester {
	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllArrayAtIndex_supportedAllPresent() {
		getList().addElements(0, new short[]{e0()});
		expectAddedIndex(0, e0());
	}

	@ListFeature.Require(absent = SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllArrayAtIndex_unsupportedAllPresent() {
		try {
			getList().addElements(0, new short[]{e0()});
			fail("addAll(n, allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllArrayAtIndex_supportedSomePresent() {
		getList().addElements(0, new short[]{e0(), e3()});
		expectAddedIndex(0, e0(), e3());
	}

	@ListFeature.Require(absent = SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllArrayAtIndex_unsupportedSomePresent() {
		try {
			getList().addElements(0, new short[]{e0(), e3()});
			fail("addAll(n, allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllArrayAtIndex_supportedNothing() {
		getList().addElements(0, emptyArray());
		expectUnchanged();
	}

	@ListFeature.Require(absent = SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllArrayAtIndex_unsupportedNothing() {
		try {
			getList().addElements(0, emptyArray());
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllArrayAtIndex_withDuplicates() {
		getList().addElements(0, new short[]{e0(), e1(), e0(), e1()});
		expectAddedIndex(0, e0(), e1(), e0(), e1());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testAddAllArrayAtIndex_middle() {
		getList().addElements(getNumElements() / 2, createDisjointArray());
		expectAdded(getNumElements() / 2, createDisjointCollection());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllArrayAtIndex_end() {
		getList().addElements(getNumElements(), createDisjointArray());
		expectAdded(getNumElements(), createDisjointCollection());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllArrayAtIndex_nullCollectionReference() {
		try {
			getList().addElements(0, null);
			fail("addElements(n, null) should throw");
		} catch (NullPointerException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllArrayAtIndex_negative() {
		try {
			getList().addElements(-1, new short[]{e3()});
			fail("addElements(-1, e) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllArrayAtIndex_tooLarge() {
		try {
			getList().addElements(getNumElements() + 1, new short[]{e3()});
			fail("addElements(size + 1, e) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}
		@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllArray_supportedToLargeOffset() {
		try {
			getList().addElements(0, createDisjointArray(), 5, 2);
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllArray_supportedToSmallOffset() {
		try {
			getList().addElements(0, createDisjointArray(), -1, 2);
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	public void testAddAllArray_supportedAddSubArray() {
		getList().addElements(0, createDisjointArray(), 0, 1);
		expectAddedIndex(0, e3());
		expectMissing(e4());
	}
}