package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.maps.impl.hash.Long2IntLinkedOpenHashMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2IntMapTester;

@Ignore
public class Long2IntMapToStringTester extends AbstractLong2IntMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().long2IntEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Long2IntMap.Entry> entries) {
		Long2IntMap reference = new Long2IntLinkedOpenHashMap();
		for (Long2IntMap.Entry entry : entries) {
			reference.put(entry.getLongKey(), entry.getIntValue());
		}
		return reference.toString();
	}
}