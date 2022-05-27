package speiger.src.testers.ints.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectSortedMap;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.TestIntSortedSetGenerator;
import speiger.src.testers.ints.generators.TestIntNavigableSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2ObjectMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2ObjectSortedMapGenerator;
import speiger.src.testers.ints.utils.IntSamples;
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

public class DerivedInt2ObjectMapGenerators {
	public static class NavigableMapGenerator<V> extends SortedMapGenerator<V> {
		public NavigableMapGenerator(TestInt2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Int2ObjectNavigableMap<V> createSubMap(Int2ObjectSortedMap<V> sortedMap, int firstExclusive, int lastExclusive) {
	    	Int2ObjectNavigableMap<V> map = (Int2ObjectNavigableMap<V>) sortedMap;
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
	        return (Int2ObjectNavigableMap<V>) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator<V> extends MapGenerator<V> implements TestInt2ObjectSortedMapGenerator<V> {
		TestInt2ObjectSortedMapGenerator<V> parent;
		Bound to;
		Bound from;
		int firstInclusive;
		int lastInclusive;
		Comparator<Entry<V>> entryComparator;

		public SortedMapGenerator(TestInt2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Int2ObjectSortedMap<V> map = parent.create();
			entryComparator = DerivedInt2ObjectMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry<V>> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getIntKey();
			lastInclusive = samples.get(samples.size() - 1).getIntKey();
		}
		
		@Override
		public Int2ObjectSortedMap<V> create(Entry<V>... elements) {
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
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getIntKey(), parent.aboveSamplesLesser().getIntKey());
		}
		
		Int2ObjectSortedMap<V> createSubMap(Int2ObjectSortedMap<V> map, int firstExclusive, int lastExclusive) {
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
	
	public static class DescendingTestMapGenerator<V> extends MapGenerator<V> implements TestInt2ObjectSortedMapGenerator<V>
	{
		TestInt2ObjectSortedMapGenerator<V> parent;
		
		public DescendingTestMapGenerator(TestInt2ObjectSortedMapGenerator<V> parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Int2ObjectNavigableMap<V> create(Entry<V>... elements) {
			return ((Int2ObjectNavigableMap<V>)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Integer, V>> order(List<Map.Entry<Integer, V>> insertionOrder) {
			ObjectList<Map.Entry<Integer, V>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator<V> implements TestInt2ObjectMapGenerator<V>
	{
		TestInt2ObjectMapGenerator<V> parent;
		
		public MapGenerator(TestInt2ObjectMapGenerator<V> parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Integer, V>> order(List<Map.Entry<Integer, V>> insertionOrder) {
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
		public Int2ObjectMap<V> create(Entry<V>... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator<V> implements TestObjectSetGenerator<Int2ObjectMap.Entry<V>> {
		TestInt2ObjectMapGenerator<V> generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Integer, V>, Map.Entry<Integer, V>> inner) {
			generator = (TestInt2ObjectMapGenerator<V>) inner.getInnerGenerator();
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
			return generator.create(elements).int2ObjectEntrySet();
		}
	}

	public static <V> TestIntSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Integer, V>, Map.Entry<Integer, V>> inner) {
		if (inner.getInnerGenerator() instanceof TestInt2ObjectSortedMapGenerator) {
			IntSet set = ((TestInt2ObjectSortedMapGenerator<V>) inner.getInnerGenerator()).create().keySet();
			if(set instanceof IntNavigableSet) return new MapNavigableKeySetGenerator<>(inner);
			if(set instanceof IntSortedSet) return new MapSortedKeySetGenerator<>(inner);
		}
		return new MapKeySetGenerator<>(inner);
	}
	
	public static class MapNavigableKeySetGenerator<V> extends MapSortedKeySetGenerator<V> implements TestIntNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, V>, Map.Entry<Integer, V>> inner) {
			super(inner);
		}
		
		@Override
		public IntNavigableSet create(int... elements) {
			return (IntNavigableSet) super.create(elements);
		}

		@Override
		public IntNavigableSet create(Object... elements) {
			return (IntNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator<V> extends MapKeySetGenerator<V> implements TestIntSortedSetGenerator {
		TestInt2ObjectSortedMapGenerator<V> generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, V>, Map.Entry<Integer, V>> inner) {
			super(inner);
			generator = (TestInt2ObjectSortedMapGenerator<V>) inner.getInnerGenerator();
		}

		@Override
		public IntSortedSet create(int... elements) {
			return (IntSortedSet) super.create(elements);
		}

		@Override
		public IntSortedSet create(Object... elements) {
			return (IntSortedSet) super.create(elements);
		}

		@Override
		public int belowSamplesLesser() {
			return generator.belowSamplesLesser().getIntKey();
		}

		@Override
		public int belowSamplesGreater() {
			return generator.belowSamplesGreater().getIntKey();
		}

		@Override
		public int aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getIntKey();
		}

		@Override
		public int aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getIntKey();
		}
	}

	public static class MapKeySetGenerator<V> implements TestIntSetGenerator {
		TestInt2ObjectMapGenerator<V> generator;
		IntSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, V>, Map.Entry<Integer, V>> inner) {
			generator = (TestInt2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Int2ObjectMap.Entry<V>> samples = generator.getSamples();
			this.samples = new IntSamples(samples.e0().getIntKey(), samples.e1().getIntKey(), samples.e2().getIntKey(), samples.e3().getIntKey(), samples.e4().getIntKey());
		}

		@Override
		public IntSamples getSamples() {
			return samples;
		}

		@Override
		public IntIterable order(IntList insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (int key : insertionOrder) {
				entries.add(new AbstractInt2ObjectMap.BasicEntry<>(key, value));
			}
			IntList list = new IntArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getIntKey());
			}
			return list;
		}

		@Override
		public Iterable<Integer> order(List<Integer> insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (int key : insertionOrder) {
				entries.add(new AbstractInt2ObjectMap.BasicEntry<>(key, value));
			}
			IntList list = new IntArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getIntKey());
			}
			return list;
		}
		
		@Override
		public IntSet create(int... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2ObjectMap.BasicEntry<>(elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public IntSet create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2ObjectMap.BasicEntry<>((Integer) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator<V> implements TestObjectCollectionGenerator<V> {
		TestInt2ObjectMapGenerator<V> generator;
		ObjectSamples<V> samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Integer, V>, Map.Entry<Integer, V>> inner) {
			generator = (TestInt2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Int2ObjectMap.Entry<V>> samples = generator.getSamples();
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
					throw new IllegalArgumentException("Int2ObjectMap.values generator can order only sample values");
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
					throw new IllegalArgumentException("Int2ObjectMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ObjectCollection<V> create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2ObjectMap.BasicEntry<>(entry.getKey(), (V)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static<V> Comparator<Map.Entry<Integer, V>> entryObjectComparator(Comparator<Integer> keyComparator) {
		return new Comparator<Map.Entry<Integer, V>>() {
			@Override
			public int compare(Map.Entry<Integer, V> a, Map.Entry<Integer, V> b) {
				if(keyComparator == null) {
					return Integer.compare(a.getKey().intValue(), b.getKey().intValue());
				}
				return keyComparator.compare(a.getKey().intValue(), b.getKey().intValue());
			}
		};
	}
	
	public static<V> Comparator<Entry<V>> entryComparator(IntComparator keyComparator) {
		return new Comparator<Entry<V>>() {
			@Override
			public int compare(Entry<V> a, Entry<V> b) {
				if(keyComparator == null) {
					return Integer.compare(a.getIntKey(), b.getIntKey());
				}
				return keyComparator.compare(a.getIntKey(), b.getIntKey());
			}
		};
	}
}