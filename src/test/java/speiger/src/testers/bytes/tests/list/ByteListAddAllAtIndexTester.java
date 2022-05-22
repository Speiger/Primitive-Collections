package speiger.src.testers.bytes.tests.list;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_ADD_WITH_INDEX;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.tests.base.AbstractByteListTester;
import speiger.src.testers.bytes.utils.MinimalByteCollection;

public class ByteListAddAllAtIndexTester extends AbstractByteListTester {
	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllAtIndex_supportedAllPresent() {
		assertTrue("addAll(n, allPresent) should return true", getList().addAll(0, MinimalByteCollection.of(e0())));
		expectAddedIndex(0, e0());
	}

	@ListFeature.Require(absent = SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllAtIndex_unsupportedAllPresent() {
		try {
			getList().addAll(0, MinimalByteCollection.of(e0()));
			fail("addAll(n, allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllAtIndex_supportedSomePresent() {
		assertTrue("addAll(n, allPresent) should return true", getList().addAll(0, MinimalByteCollection.of(e0(), e3())));
		expectAddedIndex(0, e0(), e3());
	}

	@ListFeature.Require(absent = SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllAtIndex_unsupportedSomePresent() {
		try {
			getList().addAll(0, MinimalByteCollection.of(e0(), e3()));
			fail("addAll(n, allPresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllAtIndex_supportedNothing() {
		assertFalse("addAll(n, nothing) should return false", getList().addAll(0, emptyCollection()));
		expectUnchanged();
	}

	@ListFeature.Require(absent = SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllAtIndex_unsupportedNothing() {
		try {
			assertFalse("addAll(n, nothing) should return false or throw", getList().addAll(0, emptyCollection()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllAtIndex_withDuplicates() {
		MinimalByteCollection elementsToAdd = MinimalByteCollection.of(e0(), e1(), e0(), e1());
		assertTrue("addAll(n, hasDuplicates) should return true", getList().addAll(0, elementsToAdd));
		expectAddedIndex(0, e0(), e1(), e0(), e1());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testAddAllAtIndex_middle() {
		assertTrue("addAll(middle, disjoint) should return true", getList().addAll(getNumElements() / 2, createDisjointCollection()));
		expectAdded(getNumElements() / 2, createDisjointCollection());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testAddAllAtIndex_end() {
		assertTrue("addAll(end, disjoint) should return true", getList().addAll(getNumElements(), createDisjointCollection()));
		expectAdded(getNumElements(), createDisjointCollection());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllAtIndex_nullCollectionReference() {
		try {
			getList().addAll(0, (ByteCollection)null);
			fail("addAll(n, null) should throw");
		} catch (NullPointerException expected) {
		}
		expectUnchanged();
	}
	
	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllAtIndex_nullListReference() {
		try {
			getList().addAll(0, (ByteList)null);
			fail("addAll(n, null) should throw");
		} catch (NullPointerException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllAtIndex_negative() {
		try {
			getList().addAll(-1, MinimalByteCollection.of(e3()));
			fail("addAll(-1, e) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testAddAllAtIndex_tooLarge() {
		try {
			getList().addAll(getNumElements() + 1, MinimalByteCollection.of(e3()));
			fail("addAll(size + 1, e) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}
}