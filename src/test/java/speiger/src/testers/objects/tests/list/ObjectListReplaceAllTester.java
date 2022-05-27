package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_SET;

import java.util.Arrays;
import java.util.Objects;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.testers.objects.tests.base.AbstractObjectListTester;

@Ignore
public class ObjectListReplaceAllTester<T> extends AbstractObjectListTester<T>
{
	@ListFeature.Require(SUPPORTS_SET)
	public void testReplaceAll() {
		getList().replaceAll(e -> samples.e3());
		T[] array = (T[])new Object[getNumElements()];
		Arrays.fill(array, samples.e3());
		expectContents(array);
	}

	@ListFeature.Require(SUPPORTS_SET)
	public void testReplaceAll_changesSome() {
		getList().replaceAll(e -> Objects.equals(e, samples.e0()) ? samples.e3() : e);
		T[] expected = createSamplesArray();
		for (int i = 0; i < expected.length; i++) {
			if (Objects.equals(expected[i], samples.e0())) {
				expected[i] = samples.e3();
			}
		}
		expectContents(expected);
	}

	@CollectionSize.Require(absent = ZERO)
	@ListFeature.Require(absent = SUPPORTS_SET)
	public void testReplaceAll_unsupported() {
		try {
			getList().replaceAll(e -> e);
			fail("replaceAll() should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
}