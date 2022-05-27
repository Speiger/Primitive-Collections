package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.maps.impl.hash.Byte2ShortLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2ShortMapToStringTester extends AbstractByte2ShortMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().byte2ShortEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Byte2ShortMap.Entry> entries) {
		Byte2ShortMap reference = new Byte2ShortLinkedOpenHashMap();
		for (Byte2ShortMap.Entry entry : entries) {
			reference.put(entry.getByteKey(), entry.getShortValue());
		}
		return reference.toString();
	}
}