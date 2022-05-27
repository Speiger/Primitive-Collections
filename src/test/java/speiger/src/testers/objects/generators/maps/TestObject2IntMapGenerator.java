package speiger.src.testers.objects.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestObject2IntMapGenerator<T> extends TestMapGenerator<T, Integer> {
	public ObjectSamples<Object2IntMap.Entry<T>> getSamples();
	public ObjectIterable<Object2IntMap.Entry<T>> order(ObjectList<Object2IntMap.Entry<T>> insertionOrder);
	public Object2IntMap<T> create(Object2IntMap.Entry<T>... elements);
	@Override
	default Object2IntMap<T> create(Object... elements) {
		Object2IntMap.Entry<T>[] result = new Object2IntMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Object2IntMap.Entry<T>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default T[] createKeyArray(int length) { return (T[])new Object[length]; }
	@Override
	default Integer[] createValueArray(int length) { return new Integer[length]; }
	@Override
	default Object2IntMap.Entry<T>[] createArray(int length) { return new Object2IntMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<T, Integer>> samples() {
		ObjectSamples<Object2IntMap.Entry<T>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getKey(), samples.e0().getIntValue()), 
			Helpers.mapEntry(samples.e1().getKey(), samples.e1().getIntValue()), 
			Helpers.mapEntry(samples.e2().getKey(), samples.e2().getIntValue()), 
			Helpers.mapEntry(samples.e3().getKey(), samples.e3().getIntValue()), 
			Helpers.mapEntry(samples.e4().getKey(), samples.e4().getIntValue())
		);
	}
}