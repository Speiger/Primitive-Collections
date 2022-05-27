package speiger.src.testers.bytes.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.maps.impl.hash.Byte2LongOpenHashMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2LongMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2LongMapEqualsTester extends AbstractByte2LongMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Byte2LongMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Byte2LongMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Byte2LongMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().byte2LongEntrySet())));
	}

	private static Byte2LongMap newHashMap(ObjectCollection<? extends Byte2LongMap.Entry> entries) {
		Byte2LongMap map = new Byte2LongOpenHashMap();
		for (Byte2LongMap.Entry entry : entries) {
			map.put(entry.getByteKey(), entry.getLongValue());
		}
		return map;
	}
}