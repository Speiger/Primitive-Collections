package speiger.src.testers.chars.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import org.junit.Ignore;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.chars.utils.MinimalCharCollection;

@Ignore
public class CharCollectionRetainAllTester extends AbstractCharCollectionTester {

	private class Target {
		private final CharCollection toRetain;
		private final CharCollection inverse = CharArrayList.wrap(createSamplesArray());
		private final String description;

		private Target(CharCollection toRetain, String description) {
			this.toRetain = toRetain;
			inverse.removeAll(toRetain);
			this.description = description;
		}

		@Override
		public String toString() {
			return description;
		}
	}

	private Target empty;
	private Target disjoint;
	private Target superset;
	private Target nonEmptyProperSubset;
	private Target sameElements;
	private Target partialOverlap;
	private Target containsDuplicates;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		empty = new Target(emptyCollection(), "empty");
		disjoint = new Target(CharArrayList.wrap(e3(), e4()), "disjoint");
		superset = new Target(MinimalCharCollection.of(e0(), e1(), e2(), e3(), e4()), "superset");
		nonEmptyProperSubset = new Target(MinimalCharCollection.of(e1()), "subset");
		sameElements = new Target(CharArrayList.wrap(createSamplesArray()), "sameElements");
		containsDuplicates = new Target(MinimalCharCollection.of(e0(), e0(), e3(), e3()), "containsDuplicates");
		partialOverlap = new Target(MinimalCharCollection.of(e2(), e3()), "partialOverlap");
	}

	// retainAll(empty)
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testRetainAll_emptyPreviouslyEmpty() {
		expectReturnsFalse(empty);
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testRetainAll_emptyPreviouslyEmptyUnsupported() {
		expectReturnsFalseOrThrows(empty);
		expectUnchanged();
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRetainAll_emptyPreviouslyNonEmpty() {
		expectReturnsTrue(empty);
		expectContents();
		expectMissing(e0(), e1(), e2());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRetainAllExtra_emptyPreviouslyNonEmpty() {
		expectReturnsTrueExtra(empty);
		expectContents();
		expectMissing(e0(), e1(), e2());
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRetainAll_emptyPreviouslyNonEmptyUnsupported() {
		expectThrows(empty);
		expectUnchanged();
	}

	// retainAll(disjoint)

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testRetainAll_disjointPreviouslyEmpty() {
		expectReturnsFalse(disjoint);
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testRetainAll_disjointPreviouslyEmptyUnsupported() {
		expectReturnsFalseOrThrows(disjoint);
		expectUnchanged();
	}	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRetainAll_disjointPreviouslyNonEmpty() {
		expectReturnsTrue(disjoint);
		expectContents();
		expectMissing(e0(), e1(), e2());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRetainAllExtra_disjointPreviouslyNonEmpty() {
		expectReturnsTrueExtra(disjoint);
		expectContents();
		expectMissing(e0(), e1(), e2());
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRetainAll_disjointPreviouslyNonEmptyUnsupported() {
		expectThrows(disjoint);
		expectUnchanged();
	}

	// retainAll(superset)

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRetainAll_superset() {
		expectReturnsFalse(superset);
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRetainAll_supersetUnsupported() {
		expectReturnsFalseOrThrows(superset);
		expectUnchanged();
	}

	// retainAll(subset)

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAll_subset() {
		expectReturnsTrue(nonEmptyProperSubset);
		expectContents(nonEmptyProperSubset.toRetain);
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAllExtra_subset() {
		expectReturnsTrueExtra(nonEmptyProperSubset);
		expectContents(nonEmptyProperSubset.toRetain);
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAll_subsetUnsupported() {
		expectThrows(nonEmptyProperSubset);
		expectUnchanged();
	}

	// retainAll(sameElements)

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	public void testRetainAll_sameElements() {
		expectReturnsFalse(sameElements);
		expectUnchanged();
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRetainAll_sameElementsUnsupported() {
		expectReturnsFalseOrThrows(sameElements);
		expectUnchanged();
	}

	// retainAll(partialOverlap)

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAll_partialOverlap() {
		expectReturnsTrue(partialOverlap);
		expectContents(e2());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAllExtra_partialOverlap() {
		expectReturnsTrueExtra(partialOverlap);
		expectContents(e2());
	}

	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAll_partialOverlapUnsupported() {
		expectThrows(partialOverlap);
		expectUnchanged();
	}

	// retainAll(containsDuplicates)

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testRetainAll_containsDuplicatesSizeOne() {
		expectReturnsFalse(containsDuplicates);
		expectContents(e0());
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAll_containsDuplicatesSizeSeveral() {
		expectReturnsTrue(containsDuplicates);
		expectContents(e0());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRetainAllExtra_containsDuplicatesSizeSeveral() {
		expectReturnsTrueExtra(containsDuplicates);
		expectContents(e0());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testRetainAll_nullCollectionReferenceEmptySubject() {
		try {
			collection.retainAll(null);
			// Returning successfully is not ideal, but tolerated.
		} catch (NullPointerException tolerated) {
		}
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRetainAll_nullCollectionReferenceNonEmptySubject() {
		try {
			collection.retainAll(null);
			fail("retainAll(null) should throw NullPointerException");
		} catch (NullPointerException expected) {
		}
	}
	
	private void expectReturnsTrue(Target target) {
		String message = String.format("retainAll(%s) should return true", target);
		assertTrue(message, collection.retainAll(target.toRetain));
	}
	
	private void expectReturnsTrueExtra(Target target) {
		String message = String.format("retainAll(%s, Removed) should return true", target);
		CharList list = new CharArrayList();
		assertTrue(message, collection.retainAll(target.toRetain, list::add));
		CharHelpers.assertEqualIgnoringOrder(target.inverse, list);
	}

	private void expectReturnsFalse(Target target) {
		String message = String.format("retainAll(%s) should return false", target);
		assertFalse(message, collection.retainAll(target.toRetain));
	}

	private void expectThrows(Target target) {
		try {
			collection.retainAll(target.toRetain);
			fail(String.format("retainAll(%s) should throw", target));
		} catch (UnsupportedOperationException expected) {
		}
	}

	private void expectReturnsFalseOrThrows(Target target) {
		String message = String.format("retainAll(%s) should return false or throw", target);
		try {
			assertFalse(message, collection.retainAll(target.toRetain));
		} catch (UnsupportedOperationException tolerated) {
		}
	}
}