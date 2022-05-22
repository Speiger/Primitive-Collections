package speiger.src.testers.floats.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;
import speiger.src.testers.floats.utils.MinimalFloatCollection;

@Ignore
public class FloatCollectionContainsAnyTester extends AbstractFloatCollectionTester {
	
	public void testContainsAny_empty() {
		assertFalse("containsAny(empty) should return false", collection.containsAny(MinimalFloatCollection.of()));
	}
	@CollectionSize.Require(absent = ZERO)
	public void testContainsAny_subset() {
		assertTrue("containsAny(subset) should return true", collection.containsAny(MinimalFloatCollection.of(e0())));
	}
	
	@CollectionSize.Require(ONE)
	public void testContainsAny_subSetElement() {
		assertFalse("containsAny(empty) should return false", collection.containsAny(MinimalFloatCollection.of()));
	}
	
	@CollectionSize.Require(ONE)
	public void testContainsAny_subSetElements() {
		assertTrue("containsAny(subset) should return true", collection.containsAny(getSampleElements(5)));
	}
}