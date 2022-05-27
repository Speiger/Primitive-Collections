package speiger.src.testers.objects.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestObject2FloatMapGenerator<T> extends TestMapGenerator<T, Float> {
	public ObjectSamples<Object2FloatMap.Entry<T>> getSamples();
	public ObjectIterable<Object2FloatMap.Entry<T>> order(ObjectList<Object2FloatMap.Entry<T>> insertionOrder);
	public Object2FloatMap<T> create(Object2FloatMap.Entry<T>... elements);
	@Override
	default Object2FloatMap<T> create(Object... elements) {
		Object2FloatMap.Entry<T>[] result = new Object2FloatMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Object2FloatMap.Entry<T>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default T[] createKeyArray(int length) { return (T[])new Object[length]; }
	@Override
	default Float[] createValueArray(int length) { return new Float[length]; }
	@Override
	default Object2FloatMap.Entry<T>[] createArray(int length) { return new Object2FloatMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<T, Float>> samples() {
		ObjectSamples<Object2FloatMap.Entry<T>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getKey(), samples.e0().getFloatValue()), 
			Helpers.mapEntry(samples.e1().getKey(), samples.e1().getFloatValue()), 
			Helpers.mapEntry(samples.e2().getKey(), samples.e2().getFloatValue()), 
			Helpers.mapEntry(samples.e3().getKey(), samples.e3().getFloatValue()), 
			Helpers.mapEntry(samples.e4().getKey(), samples.e4().getFloatValue())
		);
	}
}