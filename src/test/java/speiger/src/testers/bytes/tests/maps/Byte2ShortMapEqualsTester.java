package speiger.src.testers.bytes.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.maps.impl.hash.Byte2ShortOpenHashMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ShortMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2ShortMapEqualsTester extends AbstractByte2ShortMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Byte2ShortMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Byte2ShortMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Byte2ShortMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().byte2ShortEntrySet())));
	}

	private static Byte2ShortMap newHashMap(ObjectCollection<? extends Byte2ShortMap.Entry> entries) {
		Byte2ShortMap map = new Byte2ShortOpenHashMap();
		for (Byte2ShortMap.Entry entry : entries) {
			map.put(entry.getByteKey(), entry.getShortValue());
		}
		return map;
	}
}