package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.maps.impl.hash.Byte2CharLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2CharMapToStringTester extends AbstractByte2CharMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().byte2CharEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Byte2CharMap.Entry> entries) {
		Byte2CharMap reference = new Byte2CharLinkedOpenHashMap();
		for (Byte2CharMap.Entry entry : entries) {
			reference.put(entry.getByteKey(), entry.getCharValue());
		}
		return reference.toString();
	}
}