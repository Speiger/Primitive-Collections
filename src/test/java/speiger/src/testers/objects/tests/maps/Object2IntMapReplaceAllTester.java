package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.utils.IntSamples;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2IntMapTester;
import speiger.src.testers.objects.utils.ObjectSamples;

@Ignore
@SuppressWarnings("javadoc")
public class Object2IntMapReplaceAllTester<T> extends AbstractObject2IntMapTester<T>
{
	private ObjectSamples<T> keys() {
		return new ObjectSamples<>(k0(), k1(), k2(), k3(), k4());
	}

	private IntSamples values() {
		return new IntSamples(v0(), v1(), v2(), v3(), v4());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testReplaceAllRotate() {
		getMap().replaceInts((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getInt(index + 1);
		});
		ObjectList<Object2IntMap.Entry<T>> expectedEntries = new ObjectArrayList<>();
		for (Object2IntMap.Entry<T> entry : getSampleEntries()) {
			int index = keys().asList().indexOf(entry.getKey());
			expectedEntries.add(entry(entry.getKey(), values().asList().getInt(index + 1)));
		}
		expectContents(expectedEntries);
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testReplaceAllPreservesOrder() {
		getMap().replaceInts((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getInt(index + 1);
		});
		ObjectList<Object2IntMap.Entry<T>> orderedEntries = getOrderedElements();
		int index = 0;
		for (T key : getMap().keySet()) {
			assertEquals(orderedEntries.get(index).getKey(), key);
			index++;
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplaceAll_unsupported() {
		try {
			getMap().replaceInts((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getInt(index + 1);
			});
			fail("replaceInts() should throw UnsupportedOperation if a map does " + "not support it and is not empty.");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(ZERO)
	public void testReplaceAll_unsupportedByEmptyCollection() {
		try {
			getMap().replaceInts((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getInt(index + 1);
			});
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testReplaceAll_unsupportedNoOpFunction() {
		try {
			getMap().replaceInts((k, v) -> v);
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}