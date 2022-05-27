package speiger.src.testers.longs.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2LongMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2LongNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongSortedMap;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.TestLongSortedSetGenerator;
import speiger.src.testers.longs.generators.TestLongNavigableSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2LongMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2LongSortedMapGenerator;
import speiger.src.testers.longs.utils.LongSamples;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedLong2LongMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestLong2LongSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Long2LongNavigableMap createSubMap(Long2LongSortedMap sortedMap, long firstExclusive, long lastExclusive) {
	    	Long2LongNavigableMap map = (Long2LongNavigableMap) sortedMap;
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
	        return (Long2LongNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestLong2LongSortedMapGenerator {
		TestLong2LongSortedMapGenerator parent;
		Bound to;
		Bound from;
		long firstInclusive;
		long lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestLong2LongSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Long2LongSortedMap map = parent.create();
			entryComparator = DerivedLong2LongMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getLongKey();
			lastInclusive = samples.get(samples.size() - 1).getLongKey();
		}
		
		@Override
		public Long2LongSortedMap create(Entry... elements) {
			ObjectList<Entry> entries = new ObjectArrayList<>();
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
		
		Long2LongSortedMap createSubMap(Long2LongSortedMap map, long firstExclusive, long lastExclusive) {
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
		public Entry belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestLong2LongSortedMapGenerator
	{
		TestLong2LongSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestLong2LongSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Long2LongNavigableMap create(Entry... elements) {
			return ((Long2LongNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Long, Long>> order(List<Map.Entry<Long, Long>> insertionOrder) {
			ObjectList<Map.Entry<Long, Long>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			ObjectList<Entry> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator implements TestLong2LongMapGenerator
	{
		TestLong2LongMapGenerator parent;
		
		public MapGenerator(TestLong2LongMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Long, Long>> order(List<Map.Entry<Long, Long>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Long2LongMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Long2LongMap.Entry> {
		TestLong2LongMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Long, Long>, Map.Entry<Long, Long>> inner) {
			generator = (TestLong2LongMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry> order(List<Entry> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry>(insertionOrder));
		}

		@Override
		public Entry[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry> create(Object... elements) {
			return generator.create(elements).long2LongEntrySet();
		}
	}

	public static TestLongSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Long, Long>, Map.Entry<Long, Long>> inner) {
		if (inner.getInnerGenerator() instanceof TestLong2LongSortedMapGenerator) {
			LongSet set = ((TestLong2LongSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof LongNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof LongSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestLongNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Long>, Map.Entry<Long, Long>> inner) {
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
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestLongSortedSetGenerator {
		TestLong2LongSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Long>, Map.Entry<Long, Long>> inner) {
			super(inner);
			generator = (TestLong2LongSortedMapGenerator) inner.getInnerGenerator();
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

	public static class MapKeySetGenerator implements TestLongSetGenerator {
		TestLong2LongMapGenerator generator;
		LongSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Long>, Map.Entry<Long, Long>> inner) {
			generator = (TestLong2LongMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Long2LongMap.Entry> samples = generator.getSamples();
			this.samples = new LongSamples(samples.e0().getLongKey(), samples.e1().getLongKey(), samples.e2().getLongKey(), samples.e3().getLongKey(), samples.e4().getLongKey());
		}

		@Override
		public LongSamples getSamples() {
			return samples;
		}

		@Override
		public LongIterable order(LongList insertionOrder) {
			long value = generator.getSamples().e0().getLongValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (long key : insertionOrder) {
				entries.add(new AbstractLong2LongMap.BasicEntry(key, value));
			}
			LongList list = new LongArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getLongKey());
			}
			return list;
		}

		@Override
		public Iterable<Long> order(List<Long> insertionOrder) {
			long value = generator.getSamples().e0().getLongValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (long key : insertionOrder) {
				entries.add(new AbstractLong2LongMap.BasicEntry(key, value));
			}
			LongList list = new LongArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getLongKey());
			}
			return list;
		}
		
		@Override
		public LongSet create(long... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2LongMap.BasicEntry(elements[index++], entry.getLongValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public LongSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2LongMap.BasicEntry((Long) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestLongCollectionGenerator {
		TestLong2LongMapGenerator generator;
		LongSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Long, Long>, Map.Entry<Long, Long>> inner) {
			generator = (TestLong2LongMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Long2LongMap.Entry> samples = generator.getSamples();
			this.samples = new LongSamples(samples.e0().getLongValue(), samples.e1().getLongValue(), samples.e2().getLongValue(), samples.e3().getLongValue(), samples.e4().getLongValue());
		}
		
		@Override
		public LongSamples getSamples() {
			return samples;
		}

		@Override
		public LongIterable order(LongList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new LongComparator() {
				@Override
				public int compare(long key, long value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(long entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getLongValue() == entry) return i;
					}
					throw new IllegalArgumentException("Long2LongMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Long> order(List<Long> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Long>() {
				@Override
				public int compare(Long key, Long value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Long entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getLongValue() == entry.longValue()) return i;
					}
					throw new IllegalArgumentException("Long2LongMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public LongCollection create(long... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2LongMap.BasicEntry(entry.getLongKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public LongCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2LongMap.BasicEntry(entry.getKey(), (Long)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Long, Long>> entryObjectComparator(Comparator<Long> keyComparator) {
		return new Comparator<Map.Entry<Long, Long>>() {
			@Override
			public int compare(Map.Entry<Long, Long> a, Map.Entry<Long, Long> b) {
				if(keyComparator == null) {
					return Long.compare(a.getKey().longValue(), b.getKey().longValue());
				}
				return keyComparator.compare(a.getKey().longValue(), b.getKey().longValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(LongComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Long.compare(a.getLongKey(), b.getLongKey());
				}
				return keyComparator.compare(a.getLongKey(), b.getLongKey());
			}
		};
	}
}