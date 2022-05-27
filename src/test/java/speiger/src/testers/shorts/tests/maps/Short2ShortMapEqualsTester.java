package speiger.src.testers.shorts.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.maps.impl.hash.Short2ShortOpenHashMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2ShortMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Short2ShortMapEqualsTester extends AbstractShort2ShortMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Short2ShortMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Short2ShortMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Short2ShortMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().short2ShortEntrySet())));
	}

	private static Short2ShortMap newHashMap(ObjectCollection<? extends Short2ShortMap.Entry> entries) {
		Short2ShortMap map = new Short2ShortOpenHashMap();
		for (Short2ShortMap.Entry entry : entries) {
			map.put(entry.getShortKey(), entry.getShortValue());
		}
		return map;
	}
}