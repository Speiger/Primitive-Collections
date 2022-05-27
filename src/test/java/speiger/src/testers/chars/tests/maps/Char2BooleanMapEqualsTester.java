package speiger.src.testers.chars.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.maps.impl.hash.Char2BooleanOpenHashMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2BooleanMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class Char2BooleanMapEqualsTester extends AbstractChar2BooleanMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Char2BooleanMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Char2BooleanMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Char2BooleanMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().char2BooleanEntrySet())));
	}

	private static Char2BooleanMap newHashMap(ObjectCollection<? extends Char2BooleanMap.Entry> entries) {
		Char2BooleanMap map = new Char2BooleanOpenHashMap();
		for (Char2BooleanMap.Entry entry : entries) {
			map.put(entry.getCharKey(), entry.getBooleanValue());
		}
		return map;
	}
}