package speiger.src.testers.longs.tests.collection;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;
import speiger.src.testers.longs.utils.MinimalLongCollection;

@Ignore
@SuppressWarnings("javadoc")
public class LongCollectionContainsAnyTester extends AbstractLongCollectionTester
{	
	public void testContainsAny_empty() {
		assertFalse("containsAny(empty) should return false", collection.containsAny(MinimalLongCollection.of()));
	}
	@CollectionSize.Require(absent = ZERO)
	public void testContainsAny_subset() {
		assertTrue("containsAny(subset) should return true", collection.containsAny(MinimalLongCollection.of(e0())));
	}
	
	@CollectionSize.Require(ONE)
	public void testContainsAny_subSetElement() {
		assertFalse("containsAny(empty) should return false", collection.containsAny(MinimalLongCollection.of()));
	}
	
	@CollectionSize.Require(ONE)
	public void testContainsAny_subSetElements() {
		assertTrue("containsAny(subset) should return true", collection.containsAny(getSampleElements(5)));
	}
}