package speiger.src.testers.objects.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2LongMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestObject2LongMapGenerator<T> extends TestMapGenerator<T, Long> {
	public ObjectSamples<Object2LongMap.Entry<T>> getSamples();
	public ObjectIterable<Object2LongMap.Entry<T>> order(ObjectList<Object2LongMap.Entry<T>> insertionOrder);
	public Object2LongMap<T> create(Object2LongMap.Entry<T>... elements);
	@Override
	default Object2LongMap<T> create(Object... elements) {
		Object2LongMap.Entry<T>[] result = new Object2LongMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Object2LongMap.Entry<T>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default T[] createKeyArray(int length) { return (T[])new Object[length]; }
	@Override
	default Long[] createValueArray(int length) { return new Long[length]; }
	@Override
	default Object2LongMap.Entry<T>[] createArray(int length) { return new Object2LongMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<T, Long>> samples() {
		ObjectSamples<Object2LongMap.Entry<T>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getKey(), samples.e0().getLongValue()), 
			Helpers.mapEntry(samples.e1().getKey(), samples.e1().getLongValue()), 
			Helpers.mapEntry(samples.e2().getKey(), samples.e2().getLongValue()), 
			Helpers.mapEntry(samples.e3().getKey(), samples.e3().getLongValue()), 
			Helpers.mapEntry(samples.e4().getKey(), samples.e4().getLongValue())
		);
	}
}