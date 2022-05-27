package speiger.src.testers.floats.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectSortedMap;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.TestFloatSortedSetGenerator;
import speiger.src.testers.floats.generators.TestFloatNavigableSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2ObjectMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2ObjectSortedMapGenerator;
import speiger.src.testers.floats.utils.FloatSamples;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedFloat2ObjectMapGenerators {
	public static class NavigableMapGenerator<V> extends SortedMapGenerator<V> {
		public NavigableMapGenerator(TestFloat2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Float2ObjectNavigableMap<V> createSubMap(Float2ObjectSortedMap<V> sortedMap, float firstExclusive, float lastExclusive) {
	    	Float2ObjectNavigableMap<V> map = (Float2ObjectNavigableMap<V>) sortedMap;
	      if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) {
	        return map.headMap(lastInclusive, true);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) {
	        return map.tailMap(firstExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastInclusive, true);
	      } else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstInclusive, true, lastInclusive, true);
	      } else {
	        return (Float2ObjectNavigableMap<V>) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator<V> extends MapGenerator<V> implements TestFloat2ObjectSortedMapGenerator<V> {
		TestFloat2ObjectSortedMapGenerator<V> parent;
		Bound to;
		Bound from;
		float firstInclusive;
		float lastInclusive;
		Comparator<Entry<V>> entryComparator;

		public SortedMapGenerator(TestFloat2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Float2ObjectSortedMap<V> map = parent.create();
			entryComparator = DerivedFloat2ObjectMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry<V>> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getFloatKey();
			lastInclusive = samples.get(samples.size() - 1).getFloatKey();
		}
		
		@Override
		public Float2ObjectSortedMap<V> create(Entry<V>... elements) {
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			if (from != Bound.NO_BOUND) {
				entries.add(parent.belowSamplesLesser());
				entries.add(parent.belowSamplesGreater());
			}
			if (to != Bound.NO_BOUND) {
				entries.add(parent.aboveSamplesLesser());
				entries.add(parent.aboveSamplesGreater());
			}
			entries.addAll(elements);
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getFloatKey(), parent.aboveSamplesLesser().getFloatKey());
		}
		
		Float2ObjectSortedMap<V> createSubMap(Float2ObjectSortedMap<V> map, float firstExclusive, float lastExclusive) {
			if (from == Bound.NO_BOUND && to == Bound.EXCLUSIVE) {
				return map.headMap(lastExclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.NO_BOUND) {
				return map.tailMap(firstInclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.EXCLUSIVE) {
				return map.subMap(firstInclusive, lastExclusive);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public Entry<V> belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<V> belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<V> aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<V> aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator<V> extends MapGenerator<V> implements TestFloat2ObjectSortedMapGenerator<V>
	{
		TestFloat2ObjectSortedMapGenerator<V> parent;
		
		public DescendingTestMapGenerator(TestFloat2ObjectSortedMapGenerator<V> parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Float2ObjectNavigableMap<V> create(Entry<V>... elements) {
			return ((Float2ObjectNavigableMap<V>)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Float, V>> order(List<Map.Entry<Float, V>> insertionOrder) {
			ObjectList<Map.Entry<Float, V>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry<V>> order(ObjectList<Entry<V>> insertionOrder) {
			ObjectList<Entry<V>> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry<V> belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry<V> belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry<V> aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry<V> aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator<V> implements TestFloat2ObjectMapGenerator<V>
	{
		TestFloat2ObjectMapGenerator<V> parent;
		
		public MapGenerator(TestFloat2ObjectMapGenerator<V> parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Float, V>> order(List<Map.Entry<Float, V>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry<V>> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry<V>> order(ObjectList<Entry<V>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Float2ObjectMap<V> create(Entry<V>... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator<V> implements TestObjectSetGenerator<Float2ObjectMap.Entry<V>> {
		TestFloat2ObjectMapGenerator<V> generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Float, V>, Map.Entry<Float, V>> inner) {
			generator = (TestFloat2ObjectMapGenerator<V>) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry<V>> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry<V>> order(ObjectList<Entry<V>> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry<V>> order(List<Entry<V>> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry<V>>(insertionOrder));
		}

		@Override
		public Entry<V>[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry<V>> create(Object... elements) {
			return generator.create(elements).float2ObjectEntrySet();
		}
	}

	public static <V> TestFloatSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Float, V>, Map.Entry<Float, V>> inner) {
		if (inner.getInnerGenerator() instanceof TestFloat2ObjectSortedMapGenerator) {
			FloatSet set = ((TestFloat2ObjectSortedMapGenerator<V>) inner.getInnerGenerator()).create().keySet();
			if(set instanceof FloatNavigableSet) return new MapNavigableKeySetGenerator<>(inner);
			if(set instanceof FloatSortedSet) return new MapSortedKeySetGenerator<>(inner);
		}
		return new MapKeySetGenerator<>(inner);
	}
	
	public static class MapNavigableKeySetGenerator<V> extends MapSortedKeySetGenerator<V> implements TestFloatNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Float, V>, Map.Entry<Float, V>> inner) {
			super(inner);
		}
		
		@Override
		public FloatNavigableSet create(float... elements) {
			return (FloatNavigableSet) super.create(elements);
		}

		@Override
		public FloatNavigableSet create(Object... elements) {
			return (FloatNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator<V> extends MapKeySetGenerator<V> implements TestFloatSortedSetGenerator {
		TestFloat2ObjectSortedMapGenerator<V> generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Float, V>, Map.Entry<Float, V>> inner) {
			super(inner);
			generator = (TestFloat2ObjectSortedMapGenerator<V>) inner.getInnerGenerator();
		}

		@Override
		public FloatSortedSet create(float... elements) {
			return (FloatSortedSet) super.create(elements);
		}

		@Override
		public FloatSortedSet create(Object... elements) {
			return (FloatSortedSet) super.create(elements);
		}

		@Override
		public float belowSamplesLesser() {
			return generator.belowSamplesLesser().getFloatKey();
		}

		@Override
		public float belowSamplesGreater() {
			return generator.belowSamplesGreater().getFloatKey();
		}

		@Override
		public float aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getFloatKey();
		}

		@Override
		public float aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getFloatKey();
		}
	}

	public static class MapKeySetGenerator<V> implements TestFloatSetGenerator {
		TestFloat2ObjectMapGenerator<V> generator;
		FloatSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Float, V>, Map.Entry<Float, V>> inner) {
			generator = (TestFloat2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Float2ObjectMap.Entry<V>> samples = generator.getSamples();
			this.samples = new FloatSamples(samples.e0().getFloatKey(), samples.e1().getFloatKey(), samples.e2().getFloatKey(), samples.e3().getFloatKey(), samples.e4().getFloatKey());
		}

		@Override
		public FloatSamples getSamples() {
			return samples;
		}

		@Override
		public FloatIterable order(FloatList insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (float key : insertionOrder) {
				entries.add(new AbstractFloat2ObjectMap.BasicEntry<>(key, value));
			}
			FloatList list = new FloatArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getFloatKey());
			}
			return list;
		}

		@Override
		public Iterable<Float> order(List<Float> insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (float key : insertionOrder) {
				entries.add(new AbstractFloat2ObjectMap.BasicEntry<>(key, value));
			}
			FloatList list = new FloatArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getFloatKey());
			}
			return list;
		}
		
		@Override
		public FloatSet create(float... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2ObjectMap.BasicEntry<>(elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public FloatSet create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2ObjectMap.BasicEntry<>((Float) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator<V> implements TestObjectCollectionGenerator<V> {
		TestFloat2ObjectMapGenerator<V> generator;
		ObjectSamples<V> samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Float, V>, Map.Entry<Float, V>> inner) {
			generator = (TestFloat2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Float2ObjectMap.Entry<V>> samples = generator.getSamples();
			this.samples = new ObjectSamples<>(samples.e0().getValue(), samples.e1().getValue(), samples.e2().getValue(), samples.e3().getValue(), samples.e4().getValue());
		}
		
		@Override
		public ObjectSamples<V> getSamples() {
			return samples;
		}

		@Override
		public ObjectIterable<V> order(ObjectList<V> insertionOrder) {
			ObjectList<Entry<V>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<V>() {
				@Override
				public int compare(V key, V value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(V entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Objects.equals(list.get(i).getValue(), entry)) return i;
					}
					throw new IllegalArgumentException("Float2ObjectMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<V> order(List<V> insertionOrder) {
			ObjectList<Entry<V>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<V>() {
				@Override
				public int compare(V key, V value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(V entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Objects.equals(list.get(i).getValue(), entry)) return i;
					}
					throw new IllegalArgumentException("Float2ObjectMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ObjectCollection<V> create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2ObjectMap.BasicEntry<>(entry.getKey(), (V)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static<V> Comparator<Map.Entry<Float, V>> entryObjectComparator(Comparator<Float> keyComparator) {
		return new Comparator<Map.Entry<Float, V>>() {
			@Override
			public int compare(Map.Entry<Float, V> a, Map.Entry<Float, V> b) {
				if(keyComparator == null) {
					return Float.compare(a.getKey().floatValue(), b.getKey().floatValue());
				}
				return keyComparator.compare(a.getKey().floatValue(), b.getKey().floatValue());
			}
		};
	}
	
	public static<V> Comparator<Entry<V>> entryComparator(FloatComparator keyComparator) {
		return new Comparator<Entry<V>>() {
			@Override
			public int compare(Entry<V> a, Entry<V> b) {
				if(keyComparator == null) {
					return Float.compare(a.getFloatKey(), b.getFloatKey());
				}
				return keyComparator.compare(a.getFloatKey(), b.getFloatKey());
			}
		};
	}
}