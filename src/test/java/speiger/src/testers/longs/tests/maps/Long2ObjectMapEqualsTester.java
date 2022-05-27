package speiger.src.testers.longs.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.maps.impl.hash.Long2ObjectOpenHashMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ObjectMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Long2ObjectMapEqualsTester<V> extends AbstractLong2ObjectMapTester<V> 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Long2ObjectMap<V> other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Long2ObjectMap.Entry<V>> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Long2ObjectMap.Entry<V>> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().long2ObjectEntrySet())));
	}

	private static <V> Long2ObjectMap<V> newHashMap(ObjectCollection<? extends Long2ObjectMap.Entry<V>> entries) {
		Long2ObjectMap<V> map = new Long2ObjectOpenHashMap<>();
		for (Long2ObjectMap.Entry<V> entry : entries) {
			map.put(entry.getLongKey(), entry.getValue());
		}
		return map;
	}
}