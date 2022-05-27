package speiger.src.testers.objects.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.maps.impl.hash.Object2ByteOpenHashMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ByteMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ByteMapEqualsTester<T> extends AbstractObject2ByteMapTester<T> 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Object2ByteMap<T> other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Object2ByteMap.Entry<T>> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Object2ByteMap.Entry<T>> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().object2ByteEntrySet())));
	}

	private static <T> Object2ByteMap<T> newHashMap(ObjectCollection<? extends Object2ByteMap.Entry<T>> entries) {
		Object2ByteMap<T> map = new Object2ByteOpenHashMap<>();
		for (Object2ByteMap.Entry<T> entry : entries) {
			map.put(entry.getKey(), entry.getByteValue());
		}
		return map;
	}
}