package speiger.src.testers.doubles.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_SET;

import java.util.Arrays;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.testers.doubles.tests.base.AbstractDoubleListTester;

@Ignore
public class DoubleListReplaceAllTester extends AbstractDoubleListTester
{
	@ListFeature.Require(SUPPORTS_SET)
	public void testReplaceAll() {
		getList().replaceDoubles(e -> samples.e3());
		double[] array = new double[getNumElements()];
		Arrays.fill(array, samples.e3());
		expectContents(array);
	}

	@ListFeature.Require(SUPPORTS_SET)
	public void testReplaceAll_changesSome() {
		getList().replaceDoubles(e -> e == samples.e0() ? samples.e3() : e);
		double[] expected = createSamplesArray();
		for (int i = 0; i < expected.length; i++) {
			if (Double.doubleToLongBits(expected[i]) == Double.doubleToLongBits(samples.e0())) {
				expected[i] = samples.e3();
			}
		}
		expectContents(expected);
	}

	@CollectionSize.Require(absent = ZERO)
	@ListFeature.Require(absent = SUPPORTS_SET)
	public void testReplaceAll_unsupported() {
		try {
			getList().replaceDoubles(e -> e);
			fail("replaceAll() should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
}