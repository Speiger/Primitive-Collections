package speiger.src.testers.objects.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;
import speiger.src.testers.objects.utils.MinimalObjectCollection;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectCollectionContainsAllTester<T> extends AbstractObjectCollectionTester<T>
{
	public void testContainsAll_empty() {
		assertTrue("containsAll(empty) should return true", collection.containsAll(MinimalObjectCollection.of()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testContainsAll_subset() {
		assertTrue("containsAll(subset) should return true", collection.containsAll(MinimalObjectCollection.of(e0())));
	}

	public void testContainsAll_sameElements() {
		assertTrue("containsAll(sameElements) should return true", collection.containsAll(MinimalObjectCollection.of(createSamplesArray())));
	}
	
	public void testContainsAll_self() {
		assertTrue("containsAll(this) should return true", collection.containsAll(collection));
	}

	public void testContainsAll_partialOverlap() {
		assertFalse("containsAll(partialOverlap) should return false", collection.containsAll(MinimalObjectCollection.of(e0(), e3())));
	}

	public void testContainsAll_disjoint() {
		assertFalse("containsAll(disjoint) should return false", collection.containsAll(MinimalObjectCollection.of(e3())));
	}
	
}