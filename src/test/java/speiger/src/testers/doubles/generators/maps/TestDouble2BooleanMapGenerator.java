package speiger.src.testers.doubles.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2BooleanMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestDouble2BooleanMapGenerator extends TestMapGenerator<Double, Boolean> {
	public ObjectSamples<Double2BooleanMap.Entry> getSamples();
	public ObjectIterable<Double2BooleanMap.Entry> order(ObjectList<Double2BooleanMap.Entry> insertionOrder);
	public Double2BooleanMap create(Double2BooleanMap.Entry... elements);
	@Override
	default Double2BooleanMap create(Object... elements) {
		Double2BooleanMap.Entry[] result = new Double2BooleanMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Double2BooleanMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Double[] createKeyArray(int length) { return new Double[length]; }
	@Override
	default Boolean[] createValueArray(int length) { return new Boolean[length]; }
	@Override
	default Double2BooleanMap.Entry[] createArray(int length) { return new Double2BooleanMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Double, Boolean>> samples() {
		ObjectSamples<Double2BooleanMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getDoubleKey(), samples.e0().getBooleanValue()), 
			Helpers.mapEntry(samples.e1().getDoubleKey(), samples.e1().getBooleanValue()), 
			Helpers.mapEntry(samples.e2().getDoubleKey(), samples.e2().getBooleanValue()), 
			Helpers.mapEntry(samples.e3().getDoubleKey(), samples.e3().getBooleanValue()), 
			Helpers.mapEntry(samples.e4().getDoubleKey(), samples.e4().getBooleanValue())
		);
	}
}