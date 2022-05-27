package speiger.src.testers.floats.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.maps.impl.hash.Float2CharOpenHashMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2CharMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Float2CharMapEqualsTester extends AbstractFloat2CharMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Float2CharMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Float2CharMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Float2CharMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().float2CharEntrySet())));
	}

	private static Float2CharMap newHashMap(ObjectCollection<? extends Float2CharMap.Entry> entries) {
		Float2CharMap map = new Float2CharOpenHashMap();
		for (Float2CharMap.Entry entry : entries) {
			map.put(entry.getFloatKey(), entry.getCharValue());
		}
		return map;
	}
}