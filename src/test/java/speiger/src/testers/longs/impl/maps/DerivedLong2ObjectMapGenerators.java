package speiger.src.testers.longs.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectSortedMap;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.TestLongSortedSetGenerator;
import speiger.src.testers.longs.generators.TestLongNavigableSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2ObjectMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2ObjectSortedMapGenerator;
import speiger.src.testers.longs.utils.LongSamples;
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
public class DerivedLong2ObjectMapGenerators {
	public static class NavigableMapGenerator<V> extends SortedMapGenerator<V> {
		public NavigableMapGenerator(TestLong2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Long2ObjectNavigableMap<V> createSubMap(Long2ObjectSortedMap<V> sortedMap, long firstExclusive, long lastExclusive) {
	    	Long2ObjectNavigableMap<V> map = (Long2ObjectNavigableMap<V>) sortedMap;
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
	        return (Long2ObjectNavigableMap<V>) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator<V> extends MapGenerator<V> implements TestLong2ObjectSortedMapGenerator<V> {
		TestLong2ObjectSortedMapGenerator<V> parent;
		Bound to;
		Bound from;
		long firstInclusive;
		long lastInclusive;
		Comparator<Entry<V>> entryComparator;

		public SortedMapGenerator(TestLong2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Long2ObjectSortedMap<V> map = parent.create();
			entryComparator = DerivedLong2ObjectMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry<V>> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getLongKey();
			lastInclusive = samples.get(samples.size() - 1).getLongKey();
		}
		
		@Override
		public Long2ObjectSortedMap<V> create(Entry<V>... elements) {
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
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getLongKey(), parent.aboveSamplesLesser().getLongKey());
		}
		
		Long2ObjectSortedMap<V> createSubMap(Long2ObjectSortedMap<V> map, long firstExclusive, long lastExclusive) {
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
	
	public static class DescendingTestMapGenerator<V> extends MapGenerator<V> implements TestLong2ObjectSortedMapGenerator<V>
	{
		TestLong2ObjectSortedMapGenerator<V> parent;
		
		public DescendingTestMapGenerator(TestLong2ObjectSortedMapGenerator<V> parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Long2ObjectNavigableMap<V> create(Entry<V>... elements) {
			return ((Long2ObjectNavigableMap<V>)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Long, V>> order(List<Map.Entry<Long, V>> insertionOrder) {
			ObjectList<Map.Entry<Long, V>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator<V> implements TestLong2ObjectMapGenerator<V>
	{
		TestLong2ObjectMapGenerator<V> parent;
		
		public MapGenerator(TestLong2ObjectMapGenerator<V> parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Long, V>> order(List<Map.Entry<Long, V>> insertionOrder) {
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
		public Long2ObjectMap<V> create(Entry<V>... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator<V> implements TestObjectSetGenerator<Long2ObjectMap.Entry<V>> {
		TestLong2ObjectMapGenerator<V> generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Long, V>, Map.Entry<Long, V>> inner) {
			generator = (TestLong2ObjectMapGenerator<V>) inner.getInnerGenerator();
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
			return generator.create(elements).long2ObjectEntrySet();
		}
	}

	public static <V> TestLongSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Long, V>, Map.Entry<Long, V>> inner) {
		if (inner.getInnerGenerator() instanceof TestLong2ObjectSortedMapGenerator) {
			LongSet set = ((TestLong2ObjectSortedMapGenerator<V>) inner.getInnerGenerator()).create().keySet();
			if(set instanceof LongNavigableSet) return new MapNavigableKeySetGenerator<>(inner);
			if(set instanceof LongSortedSet) return new MapSortedKeySetGenerator<>(inner);
		}
		return new MapKeySetGenerator<>(inner);
	}
	
	public static class MapNavigableKeySetGenerator<V> extends MapSortedKeySetGenerator<V> implements TestLongNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, V>, Map.Entry<Long, V>> inner) {
			super(inner);
		}
		
		@Override
		public LongNavigableSet create(long... elements) {
			return (LongNavigableSet) super.create(elements);
		}

		@Override
		public LongNavigableSet create(Object... elements) {
			return (LongNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator<V> extends MapKeySetGenerator<V> implements TestLongSortedSetGenerator {
		TestLong2ObjectSortedMapGenerator<V> generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, V>, Map.Entry<Long, V>> inner) {
			super(inner);
			generator = (TestLong2ObjectSortedMapGenerator<V>) inner.getInnerGenerator();
		}

		@Override
		public LongSortedSet create(long... elements) {
			return (LongSortedSet) super.create(elements);
		}

		@Override
		public LongSortedSet create(Object... elements) {
			return (LongSortedSet) super.create(elements);
		}

		@Override
		public long belowSamplesLesser() {
			return generator.belowSamplesLesser().getLongKey();
		}

		@Override
		public long belowSamplesGreater() {
			return generator.belowSamplesGreater().getLongKey();
		}

		@Override
		public long aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getLongKey();
		}

		@Override
		public long aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getLongKey();
		}
	}

	public static class MapKeySetGenerator<V> implements TestLongSetGenerator {
		TestLong2ObjectMapGenerator<V> generator;
		LongSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, V>, Map.Entry<Long, V>> inner) {
			generator = (TestLong2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Long2ObjectMap.Entry<V>> samples = generator.getSamples();
			this.samples = new LongSamples(samples.e0().getLongKey(), samples.e1().getLongKey(), samples.e2().getLongKey(), samples.e3().getLongKey(), samples.e4().getLongKey());
		}

		@Override
		public LongSamples getSamples() {
			return samples;
		}

		@Override
		public LongIterable order(LongList insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (long key : insertionOrder) {
				entries.add(new AbstractLong2ObjectMap.BasicEntry<>(key, value));
			}
			LongList list = new LongArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getLongKey());
			}
			return list;
		}

		@Override
		public Iterable<Long> order(List<Long> insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (long key : insertionOrder) {
				entries.add(new AbstractLong2ObjectMap.BasicEntry<>(key, value));
			}
			LongList list = new LongArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getLongKey());
			}
			return list;
		}
		
		@Override
		public LongSet create(long... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2ObjectMap.BasicEntry<>(elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public LongSet create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2ObjectMap.BasicEntry<>((Long) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator<V> implements TestObjectCollectionGenerator<V> {
		TestLong2ObjectMapGenerator<V> generator;
		ObjectSamples<V> samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Long, V>, Map.Entry<Long, V>> inner) {
			generator = (TestLong2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Long2ObjectMap.Entry<V>> samples = generator.getSamples();
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
					throw new IllegalArgumentException("Long2ObjectMap.values generator can order only sample values");
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
					throw new IllegalArgumentException("Long2ObjectMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ObjectCollection<V> create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2ObjectMap.BasicEntry<>(entry.getKey(), (V)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static<V> Comparator<Map.Entry<Long, V>> entryObjectComparator(Comparator<Long> keyComparator) {
		return new Comparator<Map.Entry<Long, V>>() {
			@Override
			public int compare(Map.Entry<Long, V> a, Map.Entry<Long, V> b) {
				if(keyComparator == null) {
					return Long.compare(a.getKey().longValue(), b.getKey().longValue());
				}
				return keyComparator.compare(a.getKey().longValue(), b.getKey().longValue());
			}
		};
	}
	
	public static<V> Comparator<Entry<V>> entryComparator(LongComparator keyComparator) {
		return new Comparator<Entry<V>>() {
			@Override
			public int compare(Entry<V> a, Entry<V> b) {
				if(keyComparator == null) {
					return Long.compare(a.getLongKey(), b.getLongKey());
				}
				return keyComparator.compare(a.getLongKey(), b.getLongKey());
			}
		};
	}
}