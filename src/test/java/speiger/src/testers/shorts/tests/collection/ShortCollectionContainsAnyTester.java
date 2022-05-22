package speiger.src.testers.shorts.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;
import speiger.src.testers.shorts.utils.MinimalShortCollection;

@Ignore
public class ShortCollectionContainsAnyTester extends AbstractShortCollectionTester {
	
	public void testContainsAny_empty() {
		assertFalse("containsAny(empty) should return false", collection.containsAny(MinimalShortCollection.of()));
	}
	@CollectionSize.Require(absent = ZERO)
	public void testContainsAny_subset() {
		assertTrue("containsAny(subset) should return true", collection.containsAny(MinimalShortCollection.of(e0())));
	}
	
	@CollectionSize.Require(ONE)
	public void testContainsAny_subSetElement() {
		assertFalse("containsAny(empty) should return false", collection.containsAny(MinimalShortCollection.of()));
	}
	
	@CollectionSize.Require(ONE)
	public void testContainsAny_subSetElements() {
		assertTrue("containsAny(subset) should return true", collection.containsAny(getSampleElements(5)));
	}
}