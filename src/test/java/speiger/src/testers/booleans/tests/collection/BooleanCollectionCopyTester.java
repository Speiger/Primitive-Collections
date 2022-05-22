package speiger.src.testers.booleans.tests.collection;

import org.junit.Assert;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.testers.booleans.tests.base.AbstractBooleanCollectionTester;

public class BooleanCollectionCopyTester extends AbstractBooleanCollectionTester {
	
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	public void testEquals() {
		BooleanCollection copy = collection.copy();
		Assert.assertFalse("Copied Collection shouldn't match", copy == collection);
		Assert.assertTrue("Copied Collection contents should match", copy.equals(collection));
	}
}