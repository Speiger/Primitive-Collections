package speiger.src.testers.longs.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.maps.impl.hash.Long2ShortOpenHashMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ShortMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Long2ShortMapEqualsTester extends AbstractLong2ShortMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Long2ShortMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Long2ShortMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Long2ShortMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().long2ShortEntrySet())));
	}

	private static Long2ShortMap newHashMap(ObjectCollection<? extends Long2ShortMap.Entry> entries) {
		Long2ShortMap map = new Long2ShortOpenHashMap();
		for (Long2ShortMap.Entry entry : entries) {
			map.put(entry.getLongKey(), entry.getShortValue());
		}
		return map;
	}
}