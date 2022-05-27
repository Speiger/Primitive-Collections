package speiger.src.testers.floats.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestFloat2BooleanMapGenerator extends TestMapGenerator<Float, Boolean> {
	public ObjectSamples<Float2BooleanMap.Entry> getSamples();
	public ObjectIterable<Float2BooleanMap.Entry> order(ObjectList<Float2BooleanMap.Entry> insertionOrder);
	public Float2BooleanMap create(Float2BooleanMap.Entry... elements);
	@Override
	default Float2BooleanMap create(Object... elements) {
		Float2BooleanMap.Entry[] result = new Float2BooleanMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Float2BooleanMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Float[] createKeyArray(int length) { return new Float[length]; }
	@Override
	default Boolean[] createValueArray(int length) { return new Boolean[length]; }
	@Override
	default Float2BooleanMap.Entry[] createArray(int length) { return new Float2BooleanMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Float, Boolean>> samples() {
		ObjectSamples<Float2BooleanMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getFloatKey(), samples.e0().getBooleanValue()), 
			Helpers.mapEntry(samples.e1().getFloatKey(), samples.e1().getBooleanValue()), 
			Helpers.mapEntry(samples.e2().getFloatKey(), samples.e2().getBooleanValue()), 
			Helpers.mapEntry(samples.e3().getFloatKey(), samples.e3().getBooleanValue()), 
			Helpers.mapEntry(samples.e4().getFloatKey(), samples.e4().getBooleanValue())
		);
	}
}