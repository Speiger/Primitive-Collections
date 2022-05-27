package speiger.src.testers.doubles.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.maps.impl.hash.Double2FloatOpenHashMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2FloatMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Double2FloatMapEqualsTester extends AbstractDouble2FloatMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Double2FloatMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Double2FloatMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Double2FloatMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().double2FloatEntrySet())));
	}

	private static Double2FloatMap newHashMap(ObjectCollection<? extends Double2FloatMap.Entry> entries) {
		Double2FloatMap map = new Double2FloatOpenHashMap();
		for (Double2FloatMap.Entry entry : entries) {
			map.put(entry.getDoubleKey(), entry.getFloatValue());
		}
		return map;
	}
}