package speiger.src.testers.shorts.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestShort2BooleanMapGenerator extends TestMapGenerator<Short, Boolean> {
	public ObjectSamples<Short2BooleanMap.Entry> getSamples();
	public ObjectIterable<Short2BooleanMap.Entry> order(ObjectList<Short2BooleanMap.Entry> insertionOrder);
	public Short2BooleanMap create(Short2BooleanMap.Entry... elements);
	@Override
	default Short2BooleanMap create(Object... elements) {
		Short2BooleanMap.Entry[] result = new Short2BooleanMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Short2BooleanMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Short[] createKeyArray(int length) { return new Short[length]; }
	@Override
	default Boolean[] createValueArray(int length) { return new Boolean[length]; }
	@Override
	default Short2BooleanMap.Entry[] createArray(int length) { return new Short2BooleanMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Short, Boolean>> samples() {
		ObjectSamples<Short2BooleanMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getShortKey(), samples.e0().getBooleanValue()), 
			Helpers.mapEntry(samples.e1().getShortKey(), samples.e1().getBooleanValue()), 
			Helpers.mapEntry(samples.e2().getShortKey(), samples.e2().getBooleanValue()), 
			Helpers.mapEntry(samples.e3().getShortKey(), samples.e3().getBooleanValue()), 
			Helpers.mapEntry(samples.e4().getShortKey(), samples.e4().getBooleanValue())
		);
	}
}