package speiger.src.testers.chars.tests.collection;

import org.junit.Assert;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;

public class CharCollectionCopyTester extends AbstractCharCollectionTester {
	
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	public void testEquals() {
		CharCollection copy = collection.copy();
		Assert.assertFalse("Copied Collection shouldn't match", copy == collection);
		Assert.assertTrue("Copied Collection contents should match", copy.equals(collection));
	}
}