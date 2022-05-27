package speiger.src.testers.objects.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestObject2ShortMapGenerator<T> extends TestMapGenerator<T, Short> {
	public ObjectSamples<Object2ShortMap.Entry<T>> getSamples();
	public ObjectIterable<Object2ShortMap.Entry<T>> order(ObjectList<Object2ShortMap.Entry<T>> insertionOrder);
	public Object2ShortMap<T> create(Object2ShortMap.Entry<T>... elements);
	@Override
	default Object2ShortMap<T> create(Object... elements) {
		Object2ShortMap.Entry<T>[] result = new Object2ShortMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Object2ShortMap.Entry<T>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default T[] createKeyArray(int length) { return (T[])new Object[length]; }
	@Override
	default Short[] createValueArray(int length) { return new Short[length]; }
	@Override
	default Object2ShortMap.Entry<T>[] createArray(int length) { return new Object2ShortMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<T, Short>> samples() {
		ObjectSamples<Object2ShortMap.Entry<T>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getKey(), samples.e0().getShortValue()), 
			Helpers.mapEntry(samples.e1().getKey(), samples.e1().getShortValue()), 
			Helpers.mapEntry(samples.e2().getKey(), samples.e2().getShortValue()), 
			Helpers.mapEntry(samples.e3().getKey(), samples.e3().getShortValue()), 
			Helpers.mapEntry(samples.e4().getKey(), samples.e4().getShortValue())
		);
	}
}