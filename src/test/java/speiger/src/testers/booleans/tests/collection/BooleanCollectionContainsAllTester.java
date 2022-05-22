package speiger.src.testers.booleans.tests.collection;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.booleans.tests.base.AbstractBooleanCollectionTester;
import speiger.src.testers.booleans.utils.MinimalBooleanCollection;

public class BooleanCollectionContainsAllTester extends AbstractBooleanCollectionTester {
	public void testContainsAll_empty() {
		assertTrue("containsAll(empty) should return true", collection.containsAll(MinimalBooleanCollection.of()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testContainsAll_subset() {
		assertTrue("containsAll(subset) should return true", collection.containsAll(MinimalBooleanCollection.of(e0())));
	}

	public void testContainsAll_sameElements() {
		assertTrue("containsAll(sameElements) should return true", collection.containsAll(MinimalBooleanCollection.of(createSamplesArray())));
	}
	
	public void testContainsAll_self() {
		assertTrue("containsAll(this) should return true", collection.containsAll(collection));
	}

	public void testContainsAll_partialOverlap() {
		assertFalse("containsAll(partialOverlap) should return false", collection.containsAll(MinimalBooleanCollection.of(e0(), e3())));
	}

	public void testContainsAll_disjoint() {
		assertFalse("containsAll(disjoint) should return false", collection.containsAll(MinimalBooleanCollection.of(e3())));
	}
	
}