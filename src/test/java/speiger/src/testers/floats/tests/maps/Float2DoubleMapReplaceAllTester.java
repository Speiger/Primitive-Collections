package speiger.src.testers.floats.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.utils.DoubleSamples;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2DoubleMapTester;
import speiger.src.testers.floats.utils.FloatSamples;

@Ignore
@SuppressWarnings("javadoc")
public class Float2DoubleMapReplaceAllTester extends AbstractFloat2DoubleMapTester
{
	private FloatSamples keys() {
		return new FloatSamples(k0(), k1(), k2(), k3(), k4());
	}

	private DoubleSamples values() {
		return new DoubleSamples(v0(), v1(), v2(), v3(), v4());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testReplaceAllRotate() {
		getMap().replaceDoubles((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getDouble(index + 1);
		});
		ObjectList<Float2DoubleMap.Entry> expectedEntries = new ObjectArrayList<>();
		for (Float2DoubleMap.Entry entry : getSampleEntries()) {
			int index = keys().asList().indexOf(entry.getFloatKey());
			expectedEntries.add(entry(entry.getFloatKey(), values().asList().getDouble(index + 1)));
		}
		expectContents(expectedEntries);
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testReplaceAllPreservesOrder() {
		getMap().replaceDoubles((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getDouble(index + 1);
		});
		ObjectList<Float2DoubleMap.Entry> orderedEntries = getOrderedElements();
		int index = 0;
		for (float key : getMap().keySet()) {
			assertEquals(orderedEntries.get(index).getFloatKey(), key);
			index++;
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplaceAll_unsupported() {
		try {
			getMap().replaceDoubles((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getDouble(index + 1);
			});
			fail("replaceDoubles() should throw UnsupportedOperation if a map does " + "not support it and is not empty.");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(ZERO)
	public void testReplaceAll_unsupportedByEmptyCollection() {
		try {
			getMap().replaceDoubles((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getDouble(index + 1);
			});
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testReplaceAll_unsupportedNoOpFunction() {
		try {
			getMap().replaceDoubles((k, v) -> v);
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}