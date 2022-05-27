package speiger.src.testers.floats.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestFloat2DoubleMapGenerator extends TestMapGenerator<Float, Double> {
	public ObjectSamples<Float2DoubleMap.Entry> getSamples();
	public ObjectIterable<Float2DoubleMap.Entry> order(ObjectList<Float2DoubleMap.Entry> insertionOrder);
	public Float2DoubleMap create(Float2DoubleMap.Entry... elements);
	@Override
	default Float2DoubleMap create(Object... elements) {
		Float2DoubleMap.Entry[] result = new Float2DoubleMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Float2DoubleMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Float[] createKeyArray(int length) { return new Float[length]; }
	@Override
	default Double[] createValueArray(int length) { return new Double[length]; }
	@Override
	default Float2DoubleMap.Entry[] createArray(int length) { return new Float2DoubleMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Float, Double>> samples() {
		ObjectSamples<Float2DoubleMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getFloatKey(), samples.e0().getDoubleValue()), 
			Helpers.mapEntry(samples.e1().getFloatKey(), samples.e1().getDoubleValue()), 
			Helpers.mapEntry(samples.e2().getFloatKey(), samples.e2().getDoubleValue()), 
			Helpers.mapEntry(samples.e3().getFloatKey(), samples.e3().getDoubleValue()), 
			Helpers.mapEntry(samples.e4().getFloatKey(), samples.e4().getDoubleValue())
		);
	}
}