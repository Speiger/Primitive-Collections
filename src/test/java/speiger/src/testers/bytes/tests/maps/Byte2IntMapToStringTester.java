package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.maps.impl.hash.Byte2IntLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2IntMapTester;

@Ignore
public class Byte2IntMapToStringTester extends AbstractByte2IntMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().byte2IntEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Byte2IntMap.Entry> entries) {
		Byte2IntMap reference = new Byte2IntLinkedOpenHashMap();
		for (Byte2IntMap.Entry entry : entries) {
			reference.put(entry.getByteKey(), entry.getIntValue());
		}
		return reference.toString();
	}
}