package speiger.src.testers.floats.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.utils.SanityChecks;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;
import speiger.src.testers.floats.utils.FloatHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class FloatCollectionStreamTester extends AbstractFloatCollectionTester
{
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testStreamToArrayUnknownOrder() {
		synchronized (collection) {
			FloatHelpers.assertContentsAnyOrder(getSampleElements(), unwrap(collection.primitiveStream().toArray()));
		}
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testStreamToArrayKnownOrder() {
		synchronized (collection) {
			assertEquals(getOrderedElements(), FloatArrayList.wrap(unwrap(collection.primitiveStream().toArray())));
		}
	}

	public void testStreamCount() {
		synchronized (collection) {
			assertEquals(getNumElements(), collection.primitiveStream().count());
		}
	}
	
	public float[] unwrap(double[] input) {
		float[] other = new float[input.length];
		for(int i = 0,m=input.length;i<m;i++) {
			other[i] = SanityChecks.castToFloat(input[i]);
		}
		return other;
	}
}