package speiger.src.testers.objects.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestObject2BooleanMapGenerator<T> extends TestMapGenerator<T, Boolean> {
	public ObjectSamples<Object2BooleanMap.Entry<T>> getSamples();
	public ObjectIterable<Object2BooleanMap.Entry<T>> order(ObjectList<Object2BooleanMap.Entry<T>> insertionOrder);
	public Object2BooleanMap<T> create(Object2BooleanMap.Entry<T>... elements);
	@Override
	default Object2BooleanMap<T> create(Object... elements) {
		Object2BooleanMap.Entry<T>[] result = new Object2BooleanMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Object2BooleanMap.Entry<T>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default T[] createKeyArray(int length) { return (T[])new Object[length]; }
	@Override
	default Boolean[] createValueArray(int length) { return new Boolean[length]; }
	@Override
	default Object2BooleanMap.Entry<T>[] createArray(int length) { return new Object2BooleanMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<T, Boolean>> samples() {
		ObjectSamples<Object2BooleanMap.Entry<T>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getKey(), samples.e0().getBooleanValue()), 
			Helpers.mapEntry(samples.e1().getKey(), samples.e1().getBooleanValue()), 
			Helpers.mapEntry(samples.e2().getKey(), samples.e2().getBooleanValue()), 
			Helpers.mapEntry(samples.e3().getKey(), samples.e3().getBooleanValue()), 
			Helpers.mapEntry(samples.e4().getKey(), samples.e4().getBooleanValue())
		);
	}
}