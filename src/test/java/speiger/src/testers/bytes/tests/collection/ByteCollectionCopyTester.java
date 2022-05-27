package speiger.src.testers.bytes.tests.collection;

import org.junit.Assert;
import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.testers.utils.SpecialFeature;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ByteCollectionCopyTester extends AbstractByteCollectionTester
{
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	@SpecialFeature.Require(SpecialFeature.COPYING)
	public void testEquals() {
		ByteCollection copy = collection.copy();
		Assert.assertFalse("Copied Collection shouldn't match", copy == collection);
		Assert.assertTrue("Copied Collection contents should match", copy.equals(collection));
	}
}