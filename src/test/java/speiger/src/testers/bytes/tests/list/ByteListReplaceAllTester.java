package speiger.src.testers.bytes.tests.list;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_SET;

import java.util.Arrays;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.testers.bytes.tests.base.AbstractByteListTester;

public class ByteListReplaceAllTester extends AbstractByteListTester {
	@ListFeature.Require(SUPPORTS_SET)
	public void testReplaceAll() {
		getList().replaceBytes(e -> samples.e3());
		byte[] array = new byte[getNumElements()];
		Arrays.fill(array, samples.e3());
		expectContents(array);
	}

	@ListFeature.Require(SUPPORTS_SET)
	public void testReplaceAll_changesSome() {
		getList().replaceBytes(e -> e == samples.e0() ? samples.e3() : e);
		byte[] expected = createSamplesArray();
		for (int i = 0; i < expected.length; i++) {
			if (expected[i] == samples.e0()) {
				expected[i] = samples.e3();
			}
		}
		expectContents(expected);
	}

	@CollectionSize.Require(absent = ZERO)
	@ListFeature.Require(absent = SUPPORTS_SET)
	public void testReplaceAll_unsupported() {
		try {
			getList().replaceBytes(e -> e);
			fail("replaceAll() should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
}