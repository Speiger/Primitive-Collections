package speiger.src.testers.bytes.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;
import speiger.src.testers.bytes.utils.MinimalByteCollection;

@Ignore
public class ByteCollectionContainsAllTester extends AbstractByteCollectionTester {
	public void testContainsAll_empty() {
		assertTrue("containsAll(empty) should return true", collection.containsAll(MinimalByteCollection.of()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testContainsAll_subset() {
		assertTrue("containsAll(subset) should return true", collection.containsAll(MinimalByteCollection.of(e0())));
	}

	public void testContainsAll_sameElements() {
		assertTrue("containsAll(sameElements) should return true", collection.containsAll(MinimalByteCollection.of(createSamplesArray())));
	}
	
	public void testContainsAll_self() {
		assertTrue("containsAll(this) should return true", collection.containsAll(collection));
	}

	public void testContainsAll_partialOverlap() {
		assertFalse("containsAll(partialOverlap) should return false", collection.containsAll(MinimalByteCollection.of(e0(), e3())));
	}

	public void testContainsAll_disjoint() {
		assertFalse("containsAll(disjoint) should return false", collection.containsAll(MinimalByteCollection.of(e3())));
	}
	
}