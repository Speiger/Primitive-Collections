package speiger.src.testers.floats.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2FloatMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestFloat2FloatMapGenerator extends TestMapGenerator<Float, Float> {
	public ObjectSamples<Float2FloatMap.Entry> getSamples();
	public ObjectIterable<Float2FloatMap.Entry> order(ObjectList<Float2FloatMap.Entry> insertionOrder);
	public Float2FloatMap create(Float2FloatMap.Entry... elements);
	@Override
	default Float2FloatMap create(Object... elements) {
		Float2FloatMap.Entry[] result = new Float2FloatMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Float2FloatMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Float[] createKeyArray(int length) { return new Float[length]; }
	@Override
	default Float[] createValueArray(int length) { return new Float[length]; }
	@Override
	default Float2FloatMap.Entry[] createArray(int length) { return new Float2FloatMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Float, Float>> samples() {
		ObjectSamples<Float2FloatMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getFloatKey(), samples.e0().getFloatValue()), 
			Helpers.mapEntry(samples.e1().getFloatKey(), samples.e1().getFloatValue()), 
			Helpers.mapEntry(samples.e2().getFloatKey(), samples.e2().getFloatValue()), 
			Helpers.mapEntry(samples.e3().getFloatKey(), samples.e3().getFloatValue()), 
			Helpers.mapEntry(samples.e4().getFloatKey(), samples.e4().getFloatValue())
		);
	}
}