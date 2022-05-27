package speiger.src.testers.shorts.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.maps.impl.hash.Short2BooleanOpenHashMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2BooleanMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class Short2BooleanMapEqualsTester extends AbstractShort2BooleanMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Short2BooleanMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Short2BooleanMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Short2BooleanMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().short2BooleanEntrySet())));
	}

	private static Short2BooleanMap newHashMap(ObjectCollection<? extends Short2BooleanMap.Entry> entries) {
		Short2BooleanMap map = new Short2BooleanOpenHashMap();
		for (Short2BooleanMap.Entry entry : entries) {
			map.put(entry.getShortKey(), entry.getBooleanValue());
		}
		return map;
	}
}