package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.ints.maps.interfaces.Int2LongMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.utils.LongSamples;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2LongMapTester;
import speiger.src.testers.ints.utils.IntSamples;

@Ignore
public class Int2LongMapReplaceAllTester extends AbstractInt2LongMapTester
{
	private IntSamples keys() {
		return new IntSamples(k0(), k1(), k2(), k3(), k4());
	}

	private LongSamples values() {
		return new LongSamples(v0(), v1(), v2(), v3(), v4());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testReplaceAllRotate() {
		getMap().replaceLongs((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getLong(index + 1);
		});
		ObjectList<Int2LongMap.Entry> expectedEntries = new ObjectArrayList<>();
		for (Int2LongMap.Entry entry : getSampleEntries()) {
			int index = keys().asList().indexOf(entry.getIntKey());
			expectedEntries.add(entry(entry.getIntKey(), values().asList().getLong(index + 1)));
		}
		expectContents(expectedEntries);
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testReplaceAllPreservesOrder() {
		getMap().replaceLongs((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getLong(index + 1);
		});
		ObjectList<Int2LongMap.Entry> orderedEntries = getOrderedElements();
		int index = 0;
		for (int key : getMap().keySet()) {
			assertEquals(orderedEntries.get(index).getIntKey(), key);
			index++;
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplaceAll_unsupported() {
		try {
			getMap().replaceLongs((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getLong(index + 1);
			});
			fail("replaceLongs() should throw UnsupportedOperation if a map does " + "not support it and is not empty.");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(ZERO)
	public void testReplaceAll_unsupportedByEmptyCollection() {
		try {
			getMap().replaceLongs((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getLong(index + 1);
			});
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testReplaceAll_unsupportedNoOpFunction() {
		try {
			getMap().replaceLongs((k, v) -> v);
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}