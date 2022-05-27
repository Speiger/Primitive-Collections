package speiger.src.testers.chars.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;
import speiger.src.testers.chars.utils.MinimalCharCollection;

@Ignore
@SuppressWarnings("javadoc")
public class CharCollectionContainsAnyTester extends AbstractCharCollectionTester
{	
	public void testContainsAny_empty() {
		assertFalse("containsAny(empty) should return false", collection.containsAny(MinimalCharCollection.of()));
	}
	@CollectionSize.Require(absent = ZERO)
	public void testContainsAny_subset() {
		assertTrue("containsAny(subset) should return true", collection.containsAny(MinimalCharCollection.of(e0())));
	}
	
	@CollectionSize.Require(ONE)
	public void testContainsAny_subSetElement() {
		assertFalse("containsAny(empty) should return false", collection.containsAny(MinimalCharCollection.of()));
	}
	
	@CollectionSize.Require(ONE)
	public void testContainsAny_subSetElements() {
		assertTrue("containsAny(subset) should return true", collection.containsAny(getSampleElements(5)));
	}
}