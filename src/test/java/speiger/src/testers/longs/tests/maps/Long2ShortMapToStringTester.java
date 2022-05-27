package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.maps.impl.hash.Long2ShortLinkedOpenHashMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2ShortMapToStringTester extends AbstractLong2ShortMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().long2ShortEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Long2ShortMap.Entry> entries) {
		Long2ShortMap reference = new Long2ShortLinkedOpenHashMap();
		for (Long2ShortMap.Entry entry : entries) {
			reference.put(entry.getLongKey(), entry.getShortValue());
		}
		return reference.toString();
	}
}