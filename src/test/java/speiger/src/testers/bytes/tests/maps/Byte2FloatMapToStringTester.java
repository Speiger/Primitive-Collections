package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.maps.impl.hash.Byte2FloatLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2FloatMapTester;

@Ignore
public class Byte2FloatMapToStringTester extends AbstractByte2FloatMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().byte2FloatEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Byte2FloatMap.Entry> entries) {
		Byte2FloatMap reference = new Byte2FloatLinkedOpenHashMap();
		for (Byte2FloatMap.Entry entry : entries) {
			reference.put(entry.getByteKey(), entry.getFloatValue());
		}
		return reference.toString();
	}
}