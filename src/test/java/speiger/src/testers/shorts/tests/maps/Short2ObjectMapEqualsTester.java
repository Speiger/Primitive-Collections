package speiger.src.testers.shorts.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.maps.impl.hash.Short2ObjectOpenHashMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2ObjectMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Short2ObjectMapEqualsTester<V> extends AbstractShort2ObjectMapTester<V> 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Short2ObjectMap<V> other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Short2ObjectMap.Entry<V>> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Short2ObjectMap.Entry<V>> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().short2ObjectEntrySet())));
	}

	private static <V> Short2ObjectMap<V> newHashMap(ObjectCollection<? extends Short2ObjectMap.Entry<V>> entries) {
		Short2ObjectMap<V> map = new Short2ObjectOpenHashMap<>();
		for (Short2ObjectMap.Entry<V> entry : entries) {
			map.put(entry.getShortKey(), entry.getValue());
		}
		return map;
	}
}