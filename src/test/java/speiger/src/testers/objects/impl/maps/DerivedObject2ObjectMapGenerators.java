package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectSortedMap;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.testers.objects.generators.TestObjectSortedSetGenerator;
import speiger.src.testers.objects.generators.TestObjectNavigableSetGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2ObjectMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2ObjectSortedMapGenerator;
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

public class DerivedObject2ObjectMapGenerators {
	public static class NavigableMapGenerator<T, V> extends SortedMapGenerator<T, V> {
		public NavigableMapGenerator(TestObject2ObjectSortedMapGenerator<T, V> parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Object2ObjectNavigableMap<T, V> createSubMap(Object2ObjectSortedMap<T, V> sortedMap, T firstExclusive, T lastExclusive) {
	    	Object2ObjectNavigableMap<T, V> map = (Object2ObjectNavigableMap<T, V>) sortedMap;
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
	        return (Object2ObjectNavigableMap<T, V>) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator<T, V> extends MapGenerator<T, V> implements TestObject2ObjectSortedMapGenerator<T, V> {
		TestObject2ObjectSortedMapGenerator<T, V> parent;
		Bound to;
		Bound from;
		T firstInclusive;
		T lastInclusive;
		Comparator<Entry<T, V>> entryComparator;

		public SortedMapGenerator(TestObject2ObjectSortedMapGenerator<T, V> parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Object2ObjectSortedMap<T, V> map = parent.create();
			entryComparator = DerivedObject2ObjectMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry<T, V>> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getKey();
			lastInclusive = samples.get(samples.size() - 1).getKey();
		}
		
		@Override
		public Object2ObjectSortedMap<T, V> create(Entry<T, V>... elements) {
			ObjectList<Entry<T, V>> entries = new ObjectArrayList<>();
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
		
		Object2ObjectSortedMap<T, V> createSubMap(Object2ObjectSortedMap<T, V> map, T firstExclusive, T lastExclusive) {
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
		public Entry<T, V> belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<T, V> belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<T, V> aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<T, V> aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator<T, V> extends MapGenerator<T, V> implements TestObject2ObjectSortedMapGenerator<T, V>
	{
		TestObject2ObjectSortedMapGenerator<T, V> parent;
		
		public DescendingTestMapGenerator(TestObject2ObjectSortedMapGenerator<T, V> parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Object2ObjectNavigableMap<T, V> create(Entry<T, V>... elements) {
			return ((Object2ObjectNavigableMap<T, V>)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<T, V>> order(List<Map.Entry<T, V>> insertionOrder) {
			ObjectList<Map.Entry<T, V>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry<T, V>> order(ObjectList<Entry<T, V>> insertionOrder) {
			ObjectList<Entry<T, V>> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry<T, V> belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry<T, V> belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry<T, V> aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry<T, V> aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator<T, V> implements TestObject2ObjectMapGenerator<T, V>
	{
		TestObject2ObjectMapGenerator<T, V> parent;
		
		public MapGenerator(TestObject2ObjectMapGenerator<T, V> parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<T, V>> order(List<Map.Entry<T, V>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry<T, V>> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry<T, V>> order(ObjectList<Entry<T, V>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Object2ObjectMap<T, V> create(Entry<T, V>... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator<T, V> implements TestObjectSetGenerator<Object2ObjectMap.Entry<T, V>> {
		TestObject2ObjectMapGenerator<T, V> generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<T, V>, Map.Entry<T, V>> inner) {
			generator = (TestObject2ObjectMapGenerator<T, V>) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry<T, V>> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry<T, V>> order(ObjectList<Entry<T, V>> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry<T, V>> order(List<Entry<T, V>> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry<T, V>>(insertionOrder));
		}

		@Override
		public Entry<T, V>[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry<T, V>> create(Object... elements) {
			return generator.create(elements).object2ObjectEntrySet();
		}
	}

	public static <T, V> TestObjectSetGenerator<T> keySetGenerator(OneSizeTestContainerGenerator<Map<T, V>, Map.Entry<T, V>> inner) {
		if (inner.getInnerGenerator() instanceof TestObject2ObjectSortedMapGenerator) {
			ObjectSet<T> set = ((TestObject2ObjectSortedMapGenerator<T, V>) inner.getInnerGenerator()).create().keySet();
			if(set instanceof ObjectNavigableSet) return new MapNavigableKeySetGenerator<>(inner);
			if(set instanceof ObjectSortedSet) return new MapSortedKeySetGenerator<>(inner);
		}
		return new MapKeySetGenerator<>(inner);
	}
	
	public static class MapNavigableKeySetGenerator<T, V> extends MapSortedKeySetGenerator<T, V> implements TestObjectNavigableSetGenerator<T> {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<T, V>, Map.Entry<T, V>> inner) {
			super(inner);
		}
		
		@Override
		public ObjectNavigableSet<T> create(Object... elements) {
			return (ObjectNavigableSet<T>) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator<T, V> extends MapKeySetGenerator<T, V> implements TestObjectSortedSetGenerator<T> {
		TestObject2ObjectSortedMapGenerator<T, V> generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<T, V>, Map.Entry<T, V>> inner) {
			super(inner);
			generator = (TestObject2ObjectSortedMapGenerator<T, V>) inner.getInnerGenerator();
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

	public static class MapKeySetGenerator<T, V> implements TestObjectSetGenerator<T> {
		TestObject2ObjectMapGenerator<T, V> generator;
		ObjectSamples<T> samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<T, V>, Map.Entry<T, V>> inner) {
			generator = (TestObject2ObjectMapGenerator<T, V>) inner.getInnerGenerator();
			ObjectSamples<Object2ObjectMap.Entry<T, V>> samples = generator.getSamples();
			this.samples = new ObjectSamples<>(samples.e0().getKey(), samples.e1().getKey(), samples.e2().getKey(), samples.e3().getKey(), samples.e4().getKey());
		}

		@Override
		public ObjectSamples<T> getSamples() {
			return samples;
		}

		@Override
		public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<T, V>> entries = new ObjectArrayList<>();
			for (T key : insertionOrder) {
				entries.add(new AbstractObject2ObjectMap.BasicEntry<>(key, value));
			}
			ObjectList<T> list = new ObjectArrayList<>();
			for (Entry<T, V> entry : generator.order(entries)) {
				list.add(entry.getKey());
			}
			return list;
		}

		@Override
		public Iterable<T> order(List<T> insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<T, V>> entries = new ObjectArrayList<>();
			for (T key : insertionOrder) {
				entries.add(new AbstractObject2ObjectMap.BasicEntry<>(key, value));
			}
			ObjectList<T> list = new ObjectArrayList<>();
			for (Entry<T, V> entry : generator.order(entries)) {
				list.add(entry.getKey());
			}
			return list;
		}
		
		@Override
		public ObjectSet<T> create(Object... elements) {
			Entry<T, V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<T, V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractObject2ObjectMap.BasicEntry<>((T) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator<T, V> implements TestObjectCollectionGenerator<V> {
		TestObject2ObjectMapGenerator<T, V> generator;
		ObjectSamples<V> samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<T, V>, Map.Entry<T, V>> inner) {
			generator = (TestObject2ObjectMapGenerator<T, V>) inner.getInnerGenerator();
			ObjectSamples<Object2ObjectMap.Entry<T, V>> samples = generator.getSamples();
			this.samples = new ObjectSamples<>(samples.e0().getValue(), samples.e1().getValue(), samples.e2().getValue(), samples.e3().getValue(), samples.e4().getValue());
		}
		
		@Override
		public ObjectSamples<V> getSamples() {
			return samples;
		}

		@Override
		public ObjectIterable<V> order(ObjectList<V> insertionOrder) {
			ObjectList<Entry<T, V>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<V>() {
				@Override
				public int compare(V key, V value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(V entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Objects.equals(list.get(i).getValue(), entry)) return i;
					}
					throw new IllegalArgumentException("Object2ObjectMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<V> order(List<V> insertionOrder) {
			ObjectList<Entry<T, V>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<V>() {
				@Override
				public int compare(V key, V value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(V entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Objects.equals(list.get(i).getValue(), entry)) return i;
					}
					throw new IllegalArgumentException("Object2ObjectMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ObjectCollection<V> create(Object... elements) {
			Entry<T, V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<T, V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractObject2ObjectMap.BasicEntry<>(entry.getKey(), (V)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static<T, V> Comparator<Map.Entry<T, V>> entryObjectComparator(Comparator<T> keyComparator) {
		return new Comparator<Map.Entry<T, V>>() {
			@Override
			public int compare(Map.Entry<T, V> a, Map.Entry<T, V> b) {
				if(keyComparator == null) {
					return ((Comparable<T>)a.getKey()).compareTo((T)b.getKey());
				}
				return keyComparator.compare(a.getKey(), b.getKey());
			}
		};
	}
	
	public static<T, V> Comparator<Entry<T, V>> entryComparator(Comparator<T> keyComparator) {
		return new Comparator<Entry<T, V>>() {
			@Override
			public int compare(Entry<T, V> a, Entry<T, V> b) {
				if(keyComparator == null) {
					return ((Comparable<T>)a.getKey()).compareTo((T)b.getKey());
				}
				return keyComparator.compare(a.getKey(), b.getKey());
			}
		};
	}
}