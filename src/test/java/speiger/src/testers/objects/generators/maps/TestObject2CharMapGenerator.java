package speiger.src.testers.objects.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestObject2CharMapGenerator<T> extends TestMapGenerator<T, Character> {
	public ObjectSamples<Object2CharMap.Entry<T>> getSamples();
	public ObjectIterable<Object2CharMap.Entry<T>> order(ObjectList<Object2CharMap.Entry<T>> insertionOrder);
	public Object2CharMap<T> create(Object2CharMap.Entry<T>... elements);
	@Override
	default Object2CharMap<T> create(Object... elements) {
		Object2CharMap.Entry<T>[] result = new Object2CharMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Object2CharMap.Entry<T>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default T[] createKeyArray(int length) { return (T[])new Object[length]; }
	@Override
	default Character[] createValueArray(int length) { return new Character[length]; }
	@Override
	default Object2CharMap.Entry<T>[] createArray(int length) { return new Object2CharMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<T, Character>> samples() {
		ObjectSamples<Object2CharMap.Entry<T>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getKey(), samples.e0().getCharValue()), 
			Helpers.mapEntry(samples.e1().getKey(), samples.e1().getCharValue()), 
			Helpers.mapEntry(samples.e2().getKey(), samples.e2().getCharValue()), 
			Helpers.mapEntry(samples.e3().getKey(), samples.e3().getCharValue()), 
			Helpers.mapEntry(samples.e4().getKey(), samples.e4().getCharValue())
		);
	}
}