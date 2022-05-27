package speiger.src.testers.doubles.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;
import speiger.src.testers.doubles.utils.DoubleHelpers;

@Ignore
public class DoubleCollectionStreamTester extends AbstractDoubleCollectionTester
{
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testStreamToArrayUnknownOrder() {
		synchronized (collection) {
			DoubleHelpers.assertContentsAnyOrder(getSampleElements(), unwrap(collection.primitiveStream().toArray()));
		}
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testStreamToArrayKnownOrder() {
		synchronized (collection) {
			assertEquals(getOrderedElements(), DoubleArrayList.wrap(unwrap(collection.primitiveStream().toArray())));
		}
	}

	public void testStreamCount() {
		synchronized (collection) {
			assertEquals(getNumElements(), collection.primitiveStream().count());
		}
	}
	
	public double[] unwrap(double[] input) {
		return input;
	}
}