package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2FloatNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatSortedMap;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.testers.objects.generators.TestObjectSortedSetGenerator;
import speiger.src.testers.objects.generators.TestObjectNavigableSetGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2FloatMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2FloatSortedMapGenerator;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.floats.utils.FloatSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedObject2FloatMapGenerators {
	public static class NavigableMapGenerator<T> extends SortedMapGenerator<T> {
		public NavigableMapGenerator(TestObject2FloatSortedMapGenerator<T> parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Object2FloatNavigableMap<T> createSubMap(Object2FloatSortedMap<T> sortedMap, T firstExclusive, T lastExclusive) {
	    	Object2FloatNavigableMap<T> map = (Object2FloatNavigableMap<T>) sortedMap;
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
	        return (Object2FloatNavigableMap<T>) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator<T> extends MapGenerator<T> implements TestObject2FloatSortedMapGenerator<T> {
		TestObject2FloatSortedMapGenerator<T> parent;
		Bound to;
		Bound from;
		T firstInclusive;
		T lastInclusive;
		Comparator<Entry<T>> entryComparator;

		public SortedMapGenerator(TestObject2FloatSortedMapGenerator<T> parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Object2FloatSortedMap<T> map = parent.create();
			entryComparator = DerivedObject2FloatMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry<T>> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getKey();
			lastInclusive = samples.get(samples.size() - 1).getKey();
		}
		
		@Override
		public Object2FloatSortedMap<T> create(Entry<T>... elements) {
			ObjectList<Entry<T>> entries = new ObjectArrayList<>();
			if (from != Bound.NO_BOUND) {
				entries.add(parent.belowSamplesLesser());
				entries.add(parent.belowSamplesGreater());
			}
			if (to != Bound.NO_BOUND) {
				entries.add(parent.aboveSamplesLesser());
				entries.add(parent.aboveSamplesGreater());
			}
			entries.addAll(elements);
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getKey(), parent.aboveSamplesLesser().getKey());
		}
		
		Object2FloatSortedMap<T> createSubMap(Object2FloatSortedMap<T> map, T firstExclusive, T lastExclusive) {
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
		public Entry<T> belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<T> belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<T> aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<T> aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator<T> extends MapGenerator<T> implements TestObject2FloatSortedMapGenerator<T>
	{
		TestObject2FloatSortedMapGenerator<T> parent;
		
		public DescendingTestMapGenerator(TestObject2FloatSortedMapGenerator<T> parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Object2FloatNavigableMap<T> create(Entry<T>... elements) {
			return ((Object2FloatNavigableMap<T>)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<T, Float>> order(List<Map.Entry<T, Float>> insertionOrder) {
			ObjectList<Map.Entry<T, Float>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry<T>> order(ObjectList<Entry<T>> insertionOrder) {
			ObjectList<Entry<T>> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry<T> belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry<T> belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry<T> aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry<T> aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator<T> implements TestObject2FloatMapGenerator<T>
	{
		TestObject2FloatMapGenerator<T> parent;
		
		public MapGenerator(TestObject2FloatMapGenerator<T> parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<T, Float>> order(List<Map.Entry<T, Float>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry<T>> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry<T>> order(ObjectList<Entry<T>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Object2FloatMap<T> create(Entry<T>... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator<T> implements TestObjectSetGenerator<Object2FloatMap.Entry<T>> {
		TestObject2FloatMapGenerator<T> generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<T, Float>, Map.Entry<T, Float>> inner) {
			generator = (TestObject2FloatMapGenerator<T>) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry<T>> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry<T>> order(ObjectList<Entry<T>> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry<T>> order(List<Entry<T>> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry<T>>(insertionOrder));
		}

		@Override
		public Entry<T>[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry<T>> create(Object... elements) {
			return generator.create(elements).object2FloatEntrySet();
		}
	}

	public static <T> TestObjectSetGenerator<T> keySetGenerator(OneSizeTestContainerGenerator<Map<T, Float>, Map.Entry<T, Float>> inner) {
		if (inner.getInnerGenerator() instanceof TestObject2FloatSortedMapGenerator) {
			ObjectSet<T> set = ((TestObject2FloatSortedMapGenerator<T>) inner.getInnerGenerator()).create().keySet();
			if(set instanceof ObjectNavigableSet) return new MapNavigableKeySetGenerator<>(inner);
			if(set instanceof ObjectSortedSet) return new MapSortedKeySetGenerator<>(inner);
		}
		return new MapKeySetGenerator<>(inner);
	}
	
	public static class MapNavigableKeySetGenerator<T> extends MapSortedKeySetGenerator<T> implements TestObjectNavigableSetGenerator<T> {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<T, Float>, Map.Entry<T, Float>> inner) {
			super(inner);
		}
		
		@Override
		public ObjectNavigableSet<T> create(Object... elements) {
			return (ObjectNavigableSet<T>) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator<T> extends MapKeySetGenerator<T> implements TestObjectSortedSetGenerator<T> {
		TestObject2FloatSortedMapGenerator<T> generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<T, Float>, Map.Entry<T, Float>> inner) {
			super(inner);
			generator = (TestObject2FloatSortedMapGenerator<T>) inner.getInnerGenerator();
		}

		@Override
		public ObjectSortedSet<T> create(Object... elements) {
			return (ObjectSortedSet<T>) super.create(elements);
		}

		@Override
		public T belowSamplesLesser() {
			return generator.belowSamplesLesser().getKey();
		}

		@Override
		public T belowSamplesGreater() {
			return generator.belowSamplesGreater().getKey();
		}

		@Override
		public T aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getKey();
		}

		@Override
		public T aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getKey();
		}
	}

	public static class MapKeySetGenerator<T> implements TestObjectSetGenerator<T> {
		TestObject2FloatMapGenerator<T> generator;
		ObjectSamples<T> samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<T, Float>, Map.Entry<T, Float>> inner) {
			generator = (TestObject2FloatMapGenerator<T>) inner.getInnerGenerator();
			ObjectSamples<Object2FloatMap.Entry<T>> samples = generator.getSamples();
			this.samples = new ObjectSamples<>(samples.e0().getKey(), samples.e1().getKey(), samples.e2().getKey(), samples.e3().getKey(), samples.e4().getKey());
		}

		@Override
		public ObjectSamples<T> getSamples() {
			return samples;
		}

		@Override
		public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
			float value = generator.getSamples().e0().getFloatValue();
			ObjectList<Entry<T>> entries = new ObjectArrayList<>();
			for (T key : insertionOrder) {
				entries.add(new AbstractObject2FloatMap.BasicEntry<>(key, value));
			}
			ObjectList<T> list = new ObjectArrayList<>();
			for (Entry<T> entry : generator.order(entries)) {
				list.add(entry.getKey());
			}
			return list;
		}

		@Override
		public Iterable<T> order(List<T> insertionOrder) {
			float value = generator.getSamples().e0().getFloatValue();
			ObjectList<Entry<T>> entries = new ObjectArrayList<>();
			for (T key : insertionOrder) {
				entries.add(new AbstractObject2FloatMap.BasicEntry<>(key, value));
			}
			ObjectList<T> list = new ObjectArrayList<>();
			for (Entry<T> entry : generator.order(entries)) {
				list.add(entry.getKey());
			}
			return list;
		}
		
		@Override
		public ObjectSet<T> create(Object... elements) {
			Entry<T>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<T> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractObject2FloatMap.BasicEntry<>((T) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator<T> implements TestFloatCollectionGenerator {
		TestObject2FloatMapGenerator<T> generator;
		FloatSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<T, Float>, Map.Entry<T, Float>> inner) {
			generator = (TestObject2FloatMapGenerator<T>) inner.getInnerGenerator();
			ObjectSamples<Object2FloatMap.Entry<T>> samples = generator.getSamples();
			this.samples = new FloatSamples(samples.e0().getFloatValue(), samples.e1().getFloatValue(), samples.e2().getFloatValue(), samples.e3().getFloatValue(), samples.e4().getFloatValue());
		}
		
		@Override
		public FloatSamples getSamples() {
			return samples;
		}

		@Override
		public FloatIterable order(FloatList insertionOrder) {
			ObjectList<Entry<T>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new FloatComparator() {
				@Override
				public int compare(float key, float value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(float entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Float.floatToIntBits(list.get(i).getFloatValue()) == Float.floatToIntBits(entry)) return i;
					}
					throw new IllegalArgumentException("Object2FloatMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Float> order(List<Float> insertionOrder) {
			ObjectList<Entry<T>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Float>() {
				@Override
				public int compare(Float key, Float value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Float entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Float.floatToIntBits(list.get(i).getFloatValue()) == Float.floatToIntBits(entry.floatValue())) return i;
					}
					throw new IllegalArgumentException("Object2FloatMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public FloatCollection create(float... elements) {
			Entry<T>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<T> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractObject2FloatMap.BasicEntry<>(entry.getKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public FloatCollection create(Object... elements) {
			Entry<T>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<T> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractObject2FloatMap.BasicEntry<>(entry.getKey(), (Float)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static<T> Comparator<Map.Entry<T, Float>> entryObjectComparator(Comparator<T> keyComparator) {
		return new Comparator<Map.Entry<T, Float>>() {
			@Override
			public int compare(Map.Entry<T, Float> a, Map.Entry<T, Float> b) {
				if(keyComparator == null) {
					return ((Comparable<T>)a.getKey()).compareTo((T)b.getKey());
				}
				return keyComparator.compare(a.getKey(), b.getKey());
			}
		};
	}
	
	public static<T> Comparator<Entry<T>> entryComparator(Comparator<T> keyComparator) {
		return new Comparator<Entry<T>>() {
			@Override
			public int compare(Entry<T> a, Entry<T> b) {
				if(keyComparator == null) {
					return ((Comparable<T>)a.getKey()).compareTo((T)b.getKey());
				}
				return keyComparator.compare(a.getKey(), b.getKey());
			}
		};
	}
}