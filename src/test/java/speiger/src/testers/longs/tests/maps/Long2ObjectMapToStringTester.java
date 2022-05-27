package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.maps.impl.hash.Long2ObjectLinkedOpenHashMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ObjectMapTester;

@Ignore
public class Long2ObjectMapToStringTester<V> extends AbstractLong2ObjectMapTester<V>
{
	public void testToString_minimal() {
		assertNotNull("toString() should not return null", getMap().toString());
	}

	@CollectionSize.Require(ZERO)
	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_size0() {
		assertEquals("emptyMap.toString should return {}", "{}", getMap().toString());
	}

	@CollectionSize.Require(ONE)
	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_size1() {
		assertEquals("size1Map.toString should return {entry}", "{" + e0() + "}", getMap().toString());
	}

	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_formatting() {
		assertEquals("map.toString() incorrect", expectedToString(getMap().long2ObjectEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Long2ObjectMap.Entry<V>> entries) {
		Long2ObjectMap<V> reference = new Long2ObjectLinkedOpenHashMap<>();
		for (Long2ObjectMap.Entry<V> entry : entries) {
			reference.put(entry.getLongKey(), entry.getValue());
		}
		return reference.toString();
	}
}