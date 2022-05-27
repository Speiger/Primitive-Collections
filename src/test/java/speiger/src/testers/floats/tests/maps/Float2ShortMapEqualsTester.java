package speiger.src.testers.floats.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.maps.impl.hash.Float2ShortOpenHashMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ShortMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Float2ShortMapEqualsTester extends AbstractFloat2ShortMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Float2ShortMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Float2ShortMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Float2ShortMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().float2ShortEntrySet())));
	}

	private static Float2ShortMap newHashMap(ObjectCollection<? extends Float2ShortMap.Entry> entries) {
		Float2ShortMap map = new Float2ShortOpenHashMap();
		for (Float2ShortMap.Entry entry : entries) {
			map.put(entry.getFloatKey(), entry.getShortValue());
		}
		return map;
	}
}