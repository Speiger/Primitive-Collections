package speiger.src.testers.floats.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestFloat2ShortMapGenerator extends TestMapGenerator<Float, Short> {
	public ObjectSamples<Float2ShortMap.Entry> getSamples();
	public ObjectIterable<Float2ShortMap.Entry> order(ObjectList<Float2ShortMap.Entry> insertionOrder);
	public Float2ShortMap create(Float2ShortMap.Entry... elements);
	@Override
	default Float2ShortMap create(Object... elements) {
		Float2ShortMap.Entry[] result = new Float2ShortMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Float2ShortMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Float[] createKeyArray(int length) { return new Float[length]; }
	@Override
	default Short[] createValueArray(int length) { return new Short[length]; }
	@Override
	default Float2ShortMap.Entry[] createArray(int length) { return new Float2ShortMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Float, Short>> samples() {
		ObjectSamples<Float2ShortMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getFloatKey(), samples.e0().getShortValue()), 
			Helpers.mapEntry(samples.e1().getFloatKey(), samples.e1().getShortValue()), 
			Helpers.mapEntry(samples.e2().getFloatKey(), samples.e2().getShortValue()), 
			Helpers.mapEntry(samples.e3().getFloatKey(), samples.e3().getShortValue()), 
			Helpers.mapEntry(samples.e4().getFloatKey(), samples.e4().getShortValue())
		);
	}
}