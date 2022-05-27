package speiger.src.testers.doubles.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;
import speiger.src.testers.doubles.utils.MinimalDoubleCollection;

@Ignore
public class DoubleCollectionContainsAllTester extends AbstractDoubleCollectionTester
{
	public void testContainsAll_empty() {
		assertTrue("containsAll(empty) should return true", collection.containsAll(MinimalDoubleCollection.of()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testContainsAll_subset() {
		assertTrue("containsAll(subset) should return true", collection.containsAll(MinimalDoubleCollection.of(e0())));
	}

	public void testContainsAll_sameElements() {
		assertTrue("containsAll(sameElements) should return true", collection.containsAll(MinimalDoubleCollection.of(createSamplesArray())));
	}
	
	public void testContainsAll_self() {
		assertTrue("containsAll(this) should return true", collection.containsAll(collection));
	}

	public void testContainsAll_partialOverlap() {
		assertFalse("containsAll(partialOverlap) should return false", collection.containsAll(MinimalDoubleCollection.of(e0(), e3())));
	}

	public void testContainsAll_disjoint() {
		assertFalse("containsAll(disjoint) should return false", collection.containsAll(MinimalDoubleCollection.of(e3())));
	}
	
}