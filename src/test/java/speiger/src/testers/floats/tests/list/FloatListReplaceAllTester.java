package speiger.src.testers.floats.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_SET;

import java.util.Arrays;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.testers.floats.tests.base.AbstractFloatListTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatListReplaceAllTester extends AbstractFloatListTester
{
	@ListFeature.Require(SUPPORTS_SET)
	public void testReplaceAll() {
		getList().replaceFloats(e -> samples.e3());
		float[] array = new float[getNumElements()];
		Arrays.fill(array, samples.e3());
		expectContents(array);
	}

	@ListFeature.Require(SUPPORTS_SET)
	public void testReplaceAll_changesSome() {
		getList().replaceFloats(e -> e == samples.e0() ? samples.e3() : e);
		float[] expected = createSamplesArray();
		for (int i = 0; i < expected.length; i++) {
			if (Float.floatToIntBits(expected[i]) == Float.floatToIntBits(samples.e0())) {
				expected[i] = samples.e3();
			}
		}
		expectContents(expected);
	}

	@CollectionSize.Require(absent = ZERO)
	@ListFeature.Require(absent = SUPPORTS_SET)
	public void testReplaceAll_unsupported() {
		try {
			getList().replaceFloats(e -> e);
			fail("replaceAll() should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
}