package speiger.src.testers.longs.tests.collection;

import org.junit.Assert;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;

public class LongCollectionCopyTester extends AbstractLongCollectionTester {
	
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	public void testEquals() {
		LongCollection copy = collection.copy();
		Assert.assertFalse("Copied Collection shouldn't match", copy == collection);
		Assert.assertTrue("Copied Collection contents should match", copy.equals(collection));
	}
}