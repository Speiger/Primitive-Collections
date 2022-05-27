package speiger.src.testers.chars.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.maps.impl.hash.Char2ShortOpenHashMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2ShortMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Char2ShortMapEqualsTester extends AbstractChar2ShortMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Char2ShortMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Char2ShortMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Char2ShortMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().char2ShortEntrySet())));
	}

	private static Char2ShortMap newHashMap(ObjectCollection<? extends Char2ShortMap.Entry> entries) {
		Char2ShortMap map = new Char2ShortOpenHashMap();
		for (Char2ShortMap.Entry entry : entries) {
			map.put(entry.getCharKey(), entry.getShortValue());
		}
		return map;
	}
}