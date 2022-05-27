package speiger.src.testers.shorts.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.utils.FloatSamples;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2FloatMapTester;
import speiger.src.testers.shorts.utils.ShortSamples;

@Ignore
@SuppressWarnings("javadoc")
public class Short2FloatMapReplaceAllTester extends AbstractShort2FloatMapTester
{
	private ShortSamples keys() {
		return new ShortSamples(k0(), k1(), k2(), k3(), k4());
	}

	private FloatSamples values() {
		return new FloatSamples(v0(), v1(), v2(), v3(), v4());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testReplaceAllRotate() {
		getMap().replaceFloats((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getFloat(index + 1);
		});
		ObjectList<Short2FloatMap.Entry> expectedEntries = new ObjectArrayList<>();
		for (Short2FloatMap.Entry entry : getSampleEntries()) {
			int index = keys().asList().indexOf(entry.getShortKey());
			expectedEntries.add(entry(entry.getShortKey(), values().asList().getFloat(index + 1)));
		}
		expectContents(expectedEntries);
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testReplaceAllPreservesOrder() {
		getMap().replaceFloats((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getFloat(index + 1);
		});
		ObjectList<Short2FloatMap.Entry> orderedEntries = getOrderedElements();
		int index = 0;
		for (short key : getMap().keySet()) {
			assertEquals(orderedEntries.get(index).getShortKey(), key);
			index++;
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplaceAll_unsupported() {
		try {
			getMap().replaceFloats((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getFloat(index + 1);
			});
			fail("replaceFloats() should throw UnsupportedOperation if a map does " + "not support it and is not empty.");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(ZERO)
	public void testReplaceAll_unsupportedByEmptyCollection() {
		try {
			getMap().replaceFloats((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getFloat(index + 1);
			});
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testReplaceAll_unsupportedNoOpFunction() {
		try {
			getMap().replaceFloats((k, v) -> v);
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}