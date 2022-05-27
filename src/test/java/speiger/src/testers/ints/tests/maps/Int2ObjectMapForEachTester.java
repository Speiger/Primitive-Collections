package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import java.util.List;

import org.junit.Ignore;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2ObjectMapForEachTester<V> extends AbstractInt2ObjectMapTester<V>
{
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		ObjectList<Int2ObjectMap.Entry<V>> entries = new ObjectArrayList<>();
		getMap().forEach((k, v) -> entries.add(entry(k, v)));
		assertEquals(getOrderedElements(), entries);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		List<Int2ObjectMap.Entry<V>> entries = new ObjectArrayList<>();
		getMap().forEach((k, v) -> entries.add(entry(k, v)));
		Helpers.assertEqualIgnoringOrder(getSampleEntries(), entries);
	}
}