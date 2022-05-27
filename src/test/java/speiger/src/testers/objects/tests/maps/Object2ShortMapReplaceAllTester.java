package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.utils.ShortSamples;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ShortMapTester;
import speiger.src.testers.objects.utils.ObjectSamples;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ShortMapReplaceAllTester<T> extends AbstractObject2ShortMapTester<T>
{
	private ObjectSamples<T> keys() {
		return new ObjectSamples<>(k0(), k1(), k2(), k3(), k4());
	}

	private ShortSamples values() {
		return new ShortSamples(v0(), v1(), v2(), v3(), v4());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testReplaceAllRotate() {
		getMap().replaceShorts((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getShort(index + 1);
		});
		ObjectList<Object2ShortMap.Entry<T>> expectedEntries = new ObjectArrayList<>();
		for (Object2ShortMap.Entry<T> entry : getSampleEntries()) {
			int index = keys().asList().indexOf(entry.getKey());
			expectedEntries.add(entry(entry.getKey(), values().asList().getShort(index + 1)));
		}
		expectContents(expectedEntries);
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testReplaceAllPreservesOrder() {
		getMap().replaceShorts((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getShort(index + 1);
		});
		ObjectList<Object2ShortMap.Entry<T>> orderedEntries = getOrderedElements();
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
			getMap().replaceShorts((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getShort(index + 1);
			});
			fail("replaceShorts() should throw UnsupportedOperation if a map does " + "not support it and is not empty.");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(ZERO)
	public void testReplaceAll_unsupportedByEmptyCollection() {
		try {
			getMap().replaceShorts((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getShort(index + 1);
			});
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testReplaceAll_unsupportedNoOpFunction() {
		try {
			getMap().replaceShorts((k, v) -> v);
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}