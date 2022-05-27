package speiger.src.testers.doubles.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2FloatMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestDouble2FloatMapGenerator extends TestMapGenerator<Double, Float> {
	public ObjectSamples<Double2FloatMap.Entry> getSamples();
	public ObjectIterable<Double2FloatMap.Entry> order(ObjectList<Double2FloatMap.Entry> insertionOrder);
	public Double2FloatMap create(Double2FloatMap.Entry... elements);
	@Override
	default Double2FloatMap create(Object... elements) {
		Double2FloatMap.Entry[] result = new Double2FloatMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Double2FloatMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Double[] createKeyArray(int length) { return new Double[length]; }
	@Override
	default Float[] createValueArray(int length) { return new Float[length]; }
	@Override
	default Double2FloatMap.Entry[] createArray(int length) { return new Double2FloatMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Double, Float>> samples() {
		ObjectSamples<Double2FloatMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getDoubleKey(), samples.e0().getFloatValue()), 
			Helpers.mapEntry(samples.e1().getDoubleKey(), samples.e1().getFloatValue()), 
			Helpers.mapEntry(samples.e2().getDoubleKey(), samples.e2().getFloatValue()), 
			Helpers.mapEntry(samples.e3().getDoubleKey(), samples.e3().getFloatValue()), 
			Helpers.mapEntry(samples.e4().getDoubleKey(), samples.e4().getFloatValue())
		);
	}
}