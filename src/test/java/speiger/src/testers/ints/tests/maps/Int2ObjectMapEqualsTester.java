package speiger.src.testers.ints.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.maps.impl.hash.Int2ObjectOpenHashMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2ObjectMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Int2ObjectMapEqualsTester<V> extends AbstractInt2ObjectMapTester<V> 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Int2ObjectMap<V> other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Int2ObjectMap.Entry<V>> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Int2ObjectMap.Entry<V>> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().int2ObjectEntrySet())));
	}

	private static <V> Int2ObjectMap<V> newHashMap(ObjectCollection<? extends Int2ObjectMap.Entry<V>> entries) {
		Int2ObjectMap<V> map = new Int2ObjectOpenHashMap<>();
		for (Int2ObjectMap.Entry<V> entry : entries) {
			map.put(entry.getIntKey(), entry.getValue());
		}
		return map;
	}
}