package speiger.src.testers.bytes.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.utils.SanityChecks;
import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;
import speiger.src.testers.bytes.utils.ByteHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ByteCollectionStreamTester extends AbstractByteCollectionTester
{
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testStreamToArrayUnknownOrder() {
		synchronized (collection) {
			ByteHelpers.assertContentsAnyOrder(getSampleElements(), unwrap(collection.primitiveStream().toArray()));
		}
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testStreamToArrayKnownOrder() {
		synchronized (collection) {
			assertEquals(getOrderedElements(), ByteArrayList.wrap(unwrap(collection.primitiveStream().toArray())));
		}
	}

	public void testStreamCount() {
		synchronized (collection) {
			assertEquals(getNumElements(), collection.primitiveStream().count());
		}
	}
	
	public byte[] unwrap(int[] input) {
		byte[] other = new byte[input.length];
		for(int i = 0,m=input.length;i<m;i++) {
			other[i] = SanityChecks.castToByte(input[i]);
		}
		return other;
	}
}