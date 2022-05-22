package speiger.src.testers.floats.tests.collection;

import org.junit.Assert;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;

public class FloatCollectionCopyTester extends AbstractFloatCollectionTester {
	
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	public void testEquals() {
		FloatCollection copy = collection.copy();
		Assert.assertFalse("Copied Collection shouldn't match", copy == collection);
		Assert.assertTrue("Copied Collection contents should match", copy.equals(collection));
	}
}