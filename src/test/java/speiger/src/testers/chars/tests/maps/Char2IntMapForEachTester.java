package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import java.util.List;

import org.junit.Ignore;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2IntMapForEachTester extends AbstractChar2IntMapTester
{
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		ObjectList<Char2IntMap.Entry> entries = new ObjectArrayList<>();
		getMap().forEach((k, v) -> entries.add(entry(k, v)));
		assertEquals(getOrderedElements(), entries);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		List<Char2IntMap.Entry> entries = new ObjectArrayList<>();
		getMap().forEach((k, v) -> entries.add(entry(k, v)));
		Helpers.assertEqualIgnoringOrder(getSampleEntries(), entries);
	}
}