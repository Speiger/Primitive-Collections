package speiger.src.testers.chars.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.utils.SanityChecks;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;
import speiger.src.testers.chars.utils.CharHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class CharCollectionStreamTester extends AbstractCharCollectionTester
{
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testStreamToArrayUnknownOrder() {
		synchronized (collection) {
			CharHelpers.assertContentsAnyOrder(getSampleElements(), unwrap(collection.primitiveStream().toArray()));
		}
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testStreamToArrayKnownOrder() {
		synchronized (collection) {
			assertEquals(getOrderedElements(), CharArrayList.wrap(unwrap(collection.primitiveStream().toArray())));
		}
	}

	public void testStreamCount() {
		synchronized (collection) {
			assertEquals(getNumElements(), collection.primitiveStream().count());
		}
	}
	
	public char[] unwrap(int[] input) {
		char[] other = new char[input.length];
		for(int i = 0,m=input.length;i<m;i++) {
			other[i] = SanityChecks.castToChar(input[i]);
		}
		return other;
	}
}