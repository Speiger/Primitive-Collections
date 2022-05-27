package speiger.src.testers.chars.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestChar2ObjectMapGenerator<V> extends TestMapGenerator<Character, V> {
	public ObjectSamples<Char2ObjectMap.Entry<V>> getSamples();
	public ObjectIterable<Char2ObjectMap.Entry<V>> order(ObjectList<Char2ObjectMap.Entry<V>> insertionOrder);
	public Char2ObjectMap<V> create(Char2ObjectMap.Entry<V>... elements);
	@Override
	default Char2ObjectMap<V> create(Object... elements) {
		Char2ObjectMap.Entry<V>[] result = new Char2ObjectMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Char2ObjectMap.Entry<V>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Character[] createKeyArray(int length) { return new Character[length]; }
	@Override
	default V[] createValueArray(int length) { return (V[])new Object[length]; }
	@Override
	default Char2ObjectMap.Entry<V>[] createArray(int length) { return new Char2ObjectMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Character, V>> samples() {
		ObjectSamples<Char2ObjectMap.Entry<V>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getCharKey(), samples.e0().getValue()), 
			Helpers.mapEntry(samples.e1().getCharKey(), samples.e1().getValue()), 
			Helpers.mapEntry(samples.e2().getCharKey(), samples.e2().getValue()), 
			Helpers.mapEntry(samples.e3().getCharKey(), samples.e3().getValue()), 
			Helpers.mapEntry(samples.e4().getCharKey(), samples.e4().getValue())
		);
	}
}