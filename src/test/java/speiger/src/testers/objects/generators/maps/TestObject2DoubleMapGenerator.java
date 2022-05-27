package speiger.src.testers.objects.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestObject2DoubleMapGenerator<T> extends TestMapGenerator<T, Double> {
	public ObjectSamples<Object2DoubleMap.Entry<T>> getSamples();
	public ObjectIterable<Object2DoubleMap.Entry<T>> order(ObjectList<Object2DoubleMap.Entry<T>> insertionOrder);
	public Object2DoubleMap<T> create(Object2DoubleMap.Entry<T>... elements);
	@Override
	default Object2DoubleMap<T> create(Object... elements) {
		Object2DoubleMap.Entry<T>[] result = new Object2DoubleMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Object2DoubleMap.Entry<T>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default T[] createKeyArray(int length) { return (T[])new Object[length]; }
	@Override
	default Double[] createValueArray(int length) { return new Double[length]; }
	@Override
	default Object2DoubleMap.Entry<T>[] createArray(int length) { return new Object2DoubleMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<T, Double>> samples() {
		ObjectSamples<Object2DoubleMap.Entry<T>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getKey(), samples.e0().getDoubleValue()), 
			Helpers.mapEntry(samples.e1().getKey(), samples.e1().getDoubleValue()), 
			Helpers.mapEntry(samples.e2().getKey(), samples.e2().getDoubleValue()), 
			Helpers.mapEntry(samples.e3().getKey(), samples.e3().getDoubleValue()), 
			Helpers.mapEntry(samples.e4().getKey(), samples.e4().getDoubleValue())
		);
	}
}