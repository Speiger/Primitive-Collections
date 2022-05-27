package speiger.src.testers.objects.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.maps.impl.hash.Object2ObjectOpenHashMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ObjectMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ObjectMapEqualsTester<T, V> extends AbstractObject2ObjectMapTester<T, V> 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Object2ObjectMap<T, V> other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Object2ObjectMap.Entry<T, V>> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Object2ObjectMap.Entry<T, V>> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().object2ObjectEntrySet())));
	}

	private static <T, V> Object2ObjectMap<T, V> newHashMap(ObjectCollection<? extends Object2ObjectMap.Entry<T, V>> entries) {
		Object2ObjectMap<T, V> map = new Object2ObjectOpenHashMap<>();
		for (Object2ObjectMap.Entry<T, V> entry : entries) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}
}