package speiger.src.testers.doubles.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestDouble2DoubleMapGenerator extends TestMapGenerator<Double, Double> {
	public ObjectSamples<Double2DoubleMap.Entry> getSamples();
	public ObjectIterable<Double2DoubleMap.Entry> order(ObjectList<Double2DoubleMap.Entry> insertionOrder);
	public Double2DoubleMap create(Double2DoubleMap.Entry... elements);
	@Override
	default Double2DoubleMap create(Object... elements) {
		Double2DoubleMap.Entry[] result = new Double2DoubleMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Double2DoubleMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Double[] createKeyArray(int length) { return new Double[length]; }
	@Override
	default Double[] createValueArray(int length) { return new Double[length]; }
	@Override
	default Double2DoubleMap.Entry[] createArray(int length) { return new Double2DoubleMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Double, Double>> samples() {
		ObjectSamples<Double2DoubleMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getDoubleKey(), samples.e0().getDoubleValue()), 
			Helpers.mapEntry(samples.e1().getDoubleKey(), samples.e1().getDoubleValue()), 
			Helpers.mapEntry(samples.e2().getDoubleKey(), samples.e2().getDoubleValue()), 
			Helpers.mapEntry(samples.e3().getDoubleKey(), samples.e3().getDoubleValue()), 
			Helpers.mapEntry(samples.e4().getDoubleKey(), samples.e4().getDoubleValue())
		);
	}
}