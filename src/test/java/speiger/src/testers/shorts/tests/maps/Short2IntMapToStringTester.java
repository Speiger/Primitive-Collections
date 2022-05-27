package speiger.src.testers.shorts.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.maps.impl.hash.Short2IntLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2IntMapTester;

@Ignore
public class Short2IntMapToStringTester extends AbstractShort2IntMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().short2IntEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Short2IntMap.Entry> entries) {
		Short2IntMap reference = new Short2IntLinkedOpenHashMap();
		for (Short2IntMap.Entry entry : entries) {
			reference.put(entry.getShortKey(), entry.getIntValue());
		}
		return reference.toString();
	}
}