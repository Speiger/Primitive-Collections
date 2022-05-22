package speiger.src.testers.longs.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;
import speiger.src.testers.longs.utils.LongHelpers;

@Ignore
public class LongCollectionStreamTester extends AbstractLongCollectionTester {
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testStreamToArrayUnknownOrder() {
		synchronized (collection) {
			LongHelpers.assertContentsAnyOrder(getSampleElements(), unwrap(collection.primitiveStream().toArray()));
		}
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testStreamToArrayKnownOrder() {
		synchronized (collection) {
			assertEquals(getOrderedElements(), LongArrayList.wrap(unwrap(collection.primitiveStream().toArray())));
		}
	}

	public void testStreamCount() {
		synchronized (collection) {
			assertEquals(getNumElements(), collection.primitiveStream().count());
		}
	}
	
	public long[] unwrap(long[] input) {
		return input;
	}
}