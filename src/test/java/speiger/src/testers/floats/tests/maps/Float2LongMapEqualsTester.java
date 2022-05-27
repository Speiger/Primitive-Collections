package speiger.src.testers.floats.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.maps.impl.hash.Float2LongOpenHashMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2LongMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Float2LongMapEqualsTester extends AbstractFloat2LongMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Float2LongMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Float2LongMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Float2LongMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().float2LongEntrySet())));
	}

	private static Float2LongMap newHashMap(ObjectCollection<? extends Float2LongMap.Entry> entries) {
		Float2LongMap map = new Float2LongOpenHashMap();
		for (Float2LongMap.Entry entry : entries) {
			map.put(entry.getFloatKey(), entry.getLongValue());
		}
		return map;
	}
}