package speiger.src.testers.booleans.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.booleans.tests.base.AbstractBooleanCollectionTester;
import speiger.src.testers.booleans.utils.MinimalBooleanCollection;

@Ignore
public class BooleanCollectionContainsAnyTester extends AbstractBooleanCollectionTester {
	
	public void testContainsAny_empty() {
		assertFalse("containsAny(empty) should return false", collection.containsAny(MinimalBooleanCollection.of()));
	}
	@CollectionSize.Require(absent = ZERO)
	public void testContainsAny_subset() {
		assertTrue("containsAny(subset) should return true", collection.containsAny(MinimalBooleanCollection.of(e0())));
	}
	
	@CollectionSize.Require(ONE)
	public void testContainsAny_subSetElement() {
		assertFalse("containsAny(empty) should return false", collection.containsAny(MinimalBooleanCollection.of()));
	}
	
	@CollectionSize.Require(ONE)
	public void testContainsAny_subSetElements() {
		assertTrue("containsAny(subset) should return true", collection.containsAny(getSampleElements(5)));
	}
}