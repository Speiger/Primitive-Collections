package speiger.src.testers.chars.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.maps.impl.hash.Char2CharOpenHashMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2CharMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class Char2CharMapEqualsTester extends AbstractChar2CharMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Char2CharMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Char2CharMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Char2CharMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().char2CharEntrySet())));
	}

	private static Char2CharMap newHashMap(ObjectCollection<? extends Char2CharMap.Entry> entries) {
		Char2CharMap map = new Char2CharOpenHashMap();
		for (Char2CharMap.Entry entry : entries) {
			map.put(entry.getCharKey(), entry.getCharValue());
		}
		return map;
	}
}