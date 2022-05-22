package speiger.src.testers.ints.tests.collection;

import org.junit.Assert;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.testers.ints.tests.base.AbstractIntCollectionTester;

public class IntCollectionCopyTester extends AbstractIntCollectionTester {
	
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	public void testEquals() {
		IntCollection copy = collection.copy();
		Assert.assertFalse("Copied Collection shouldn't match", copy == collection);
		Assert.assertTrue("Copied Collection contents should match", copy.equals(collection));
	}
}