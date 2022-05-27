package speiger.src.testers.doubles.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.maps.impl.hash.Double2IntOpenHashMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2IntMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Double2IntMapEqualsTester extends AbstractDouble2IntMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Double2IntMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Double2IntMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Double2IntMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().double2IntEntrySet())));
	}

	private static Double2IntMap newHashMap(ObjectCollection<? extends Double2IntMap.Entry> entries) {
		Double2IntMap map = new Double2IntOpenHashMap();
		for (Double2IntMap.Entry entry : entries) {
			map.put(entry.getDoubleKey(), entry.getIntValue());
		}
		return map;
	}
}