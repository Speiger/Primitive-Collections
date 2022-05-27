package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.maps.impl.hash.Long2LongLinkedOpenHashMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2LongMapToStringTester extends AbstractLong2LongMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().long2LongEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Long2LongMap.Entry> entries) {
		Long2LongMap reference = new Long2LongLinkedOpenHashMap();
		for (Long2LongMap.Entry entry : entries) {
			reference.put(entry.getLongKey(), entry.getLongValue());
		}
		return reference.toString();
	}
}