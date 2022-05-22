package speiger.src.testers.shorts.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.utils.SanityChecks;
import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;
import speiger.src.testers.shorts.utils.ShortHelpers;

public class ShortCollectionStreamTester extends AbstractShortCollectionTester {
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testStreamToArrayUnknownOrder() {
		synchronized (collection) {
			ShortHelpers.assertContentsAnyOrder(getSampleElements(), unwrap(collection.primitiveStream().toArray()));
		}
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testStreamToArrayKnownOrder() {
		synchronized (collection) {
			assertEquals(getOrderedElements(), ShortArrayList.wrap(unwrap(collection.primitiveStream().toArray())));
		}
	}

	public void testStreamCount() {
		synchronized (collection) {
			assertEquals(getNumElements(), collection.primitiveStream().count());
		}
	}
	
	public short[] unwrap(int[] input) {
		short[] other = new short[input.length];
		for(int i = 0,m=input.length;i<m;i++) {
			other[i] = SanityChecks.castToShort(input[i]);
		}
		return other;
	}
}