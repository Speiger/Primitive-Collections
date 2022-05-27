package speiger.src.testers.doubles.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.maps.impl.hash.Double2LongOpenHashMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2LongMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Double2LongMapEqualsTester extends AbstractDouble2LongMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Double2LongMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Double2LongMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Double2LongMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().double2LongEntrySet())));
	}

	private static Double2LongMap newHashMap(ObjectCollection<? extends Double2LongMap.Entry> entries) {
		Double2LongMap map = new Double2LongOpenHashMap();
		for (Double2LongMap.Entry entry : entries) {
			map.put(entry.getDoubleKey(), entry.getLongValue());
		}
		return map;
	}
}