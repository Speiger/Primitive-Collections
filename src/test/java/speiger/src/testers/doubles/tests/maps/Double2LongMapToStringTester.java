package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.maps.impl.hash.Double2LongLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2LongMapToStringTester extends AbstractDouble2LongMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().double2LongEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Double2LongMap.Entry> entries) {
		Double2LongMap reference = new Double2LongLinkedOpenHashMap();
		for (Double2LongMap.Entry entry : entries) {
			reference.put(entry.getDoubleKey(), entry.getLongValue());
		}
		return reference.toString();
	}
}