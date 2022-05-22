package speiger.src.testers.doubles.tests.collection;

import org.junit.Assert;
import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;

@Ignore
public class DoubleCollectionCopyTester extends AbstractDoubleCollectionTester {
	
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	public void testEquals() {
		DoubleCollection copy = collection.copy();
		Assert.assertFalse("Copied Collection shouldn't match", copy == collection);
		Assert.assertTrue("Copied Collection contents should match", copy.equals(collection));
	}
}