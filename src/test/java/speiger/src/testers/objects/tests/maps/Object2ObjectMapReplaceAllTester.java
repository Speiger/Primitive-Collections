package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ObjectMapTester;

@Ignore
public class Object2ObjectMapReplaceAllTester<T, V> extends AbstractObject2ObjectMapTester<T, V>
{
	private ObjectSamples<T> keys() {
		return new ObjectSamples<>(k0(), k1(), k2(), k3(), k4());
	}

	private ObjectSamples<V> values() {
		return new ObjectSamples<>(v0(), v1(), v2(), v3(), v4());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testReplaceAllRotate() {
		getMap().replaceObjects((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().get(index + 1);
		});
		ObjectList<Object2ObjectMap.Entry<T, V>> expectedEntries = new ObjectArrayList<>();
		for (Object2ObjectMap.Entry<T, V> entry : getSampleEntries()) {
			int index = keys().asList().indexOf(entry.getKey());
			expectedEntries.add(entry(entry.getKey(), values().asList().get(index + 1)));
		}
		expectContents(expectedEntries);
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testReplaceAllPreservesOrder() {
		getMap().replaceObjects((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().get(index + 1);
		});
		ObjectList<Object2ObjectMap.Entry<T, V>> orderedEntries = getOrderedElements();
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
			getMap().replaceObjects((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().get(index + 1);
			});
			fail("replaceObjects() should throw UnsupportedOperation if a map does " + "not support it and is not empty.");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(ZERO)
	public void testReplaceAll_unsupportedByEmptyCollection() {
		try {
			getMap().replaceObjects((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().get(index + 1);
			});
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testReplaceAll_unsupportedNoOpFunction() {
		try {
			getMap().replaceObjects((k, v) -> v);
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}