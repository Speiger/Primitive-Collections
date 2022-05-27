package speiger.src.testers.shorts.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.maps.impl.hash.Short2DoubleOpenHashMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2DoubleMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class Short2DoubleMapEqualsTester extends AbstractShort2DoubleMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Short2DoubleMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Short2DoubleMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Short2DoubleMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().short2DoubleEntrySet())));
	}

	private static Short2DoubleMap newHashMap(ObjectCollection<? extends Short2DoubleMap.Entry> entries) {
		Short2DoubleMap map = new Short2DoubleOpenHashMap();
		for (Short2DoubleMap.Entry entry : entries) {
			map.put(entry.getShortKey(), entry.getDoubleValue());
		}
		return map;
	}
}