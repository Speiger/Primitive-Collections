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
import speiger.src.collections.longs.maps.abstracts.AbstractLong2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2ShortNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortSortedMap;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.TestLongSortedSetGenerator;
import speiger.src.testers.longs.generators.TestLongNavigableSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2ShortMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2ShortSortedMapGenerator;
import speiger.src.testers.longs.utils.LongSamples;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.shorts.utils.ShortSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedLong2ShortMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestLong2ShortSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Long2ShortNavigableMap createSubMap(Long2ShortSortedMap sortedMap, long firstExclusive, long lastExclusive) {
	    	Long2ShortNavigableMap map = (Long2ShortNavigableMap) sortedMap;
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
	        return (Long2ShortNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestLong2ShortSortedMapGenerator {
		TestLong2ShortSortedMapGenerator parent;
		Bound to;
		Bound from;
		long firstInclusive;
		long lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestLong2ShortSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Long2ShortSortedMap map = parent.create();
			entryComparator = DerivedLong2ShortMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getLongKey();
			lastInclusive = samples.get(samples.size() - 1).getLongKey();
		}
		
		@Override
		public Long2ShortSortedMap create(Entry... elements) {
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
		
		Long2ShortSortedMap createSubMap(Long2ShortSortedMap map, long firstExclusive, long lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestLong2ShortSortedMapGenerator
	{
		TestLong2ShortSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestLong2ShortSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Long2ShortNavigableMap create(Entry... elements) {
			return ((Long2ShortNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Long, Short>> order(List<Map.Entry<Long, Short>> insertionOrder) {
			ObjectList<Map.Entry<Long, Short>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestLong2ShortMapGenerator
	{
		TestLong2ShortMapGenerator parent;
		
		public MapGenerator(TestLong2ShortMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Long, Short>> order(List<Map.Entry<Long, Short>> insertionOrder) {
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
		public Long2ShortMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Long2ShortMap.Entry> {
		TestLong2ShortMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Long, Short>, Map.Entry<Long, Short>> inner) {
			generator = (TestLong2ShortMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).long2ShortEntrySet();
		}
	}

	public static TestLongSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Long, Short>, Map.Entry<Long, Short>> inner) {
		if (inner.getInnerGenerator() instanceof TestLong2ShortSortedMapGenerator) {
			LongSet set = ((TestLong2ShortSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof LongNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof LongSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestLongNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Short>, Map.Entry<Long, Short>> inner) {
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
		TestLong2ShortSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Short>, Map.Entry<Long, Short>> inner) {
			super(inner);
			generator = (TestLong2ShortSortedMapGenerator) inner.getInnerGenerator();
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
		TestLong2ShortMapGenerator generator;
		LongSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Short>, Map.Entry<Long, Short>> inner) {
			generator = (TestLong2ShortMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Long2ShortMap.Entry> samples = generator.getSamples();
			this.samples = new LongSamples(samples.e0().getLongKey(), samples.e1().getLongKey(), samples.e2().getLongKey(), samples.e3().getLongKey(), samples.e4().getLongKey());
		}

		@Override
		public LongSamples getSamples() {
			return samples;
		}

		@Override
		public LongIterable order(LongList insertionOrder) {
			short value = generator.getSamples().e0().getShortValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (long key : insertionOrder) {
				entries.add(new AbstractLong2ShortMap.BasicEntry(key, value));
			}
			LongList list = new LongArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getLongKey());
			}
			return list;
		}

		@Override
		public Iterable<Long> order(List<Long> insertionOrder) {
			short value = generator.getSamples().e0().getShortValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (long key : insertionOrder) {
				entries.add(new AbstractLong2ShortMap.BasicEntry(key, value));
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
				result[index] = new AbstractLong2ShortMap.BasicEntry(elements[index++], entry.getShortValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public LongSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2ShortMap.BasicEntry((Long) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestShortCollectionGenerator {
		TestLong2ShortMapGenerator generator;
		ShortSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Long, Short>, Map.Entry<Long, Short>> inner) {
			generator = (TestLong2ShortMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Long2ShortMap.Entry> samples = generator.getSamples();
			this.samples = new ShortSamples(samples.e0().getShortValue(), samples.e1().getShortValue(), samples.e2().getShortValue(), samples.e3().getShortValue(), samples.e4().getShortValue());
		}
		
		@Override
		public ShortSamples getSamples() {
			return samples;
		}

		@Override
		public ShortIterable order(ShortList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new ShortComparator() {
				@Override
				public int compare(short key, short value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(short entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getShortValue() == entry) return i;
					}
					throw new IllegalArgumentException("Long2ShortMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Short> order(List<Short> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Short>() {
				@Override
				public int compare(Short key, Short value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Short entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getShortValue() == entry.shortValue()) return i;
					}
					throw new IllegalArgumentException("Long2ShortMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ShortCollection create(short... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2ShortMap.BasicEntry(entry.getLongKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public ShortCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2ShortMap.BasicEntry(entry.getKey(), (Short)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Long, Short>> entryObjectComparator(Comparator<Long> keyComparator) {
		return new Comparator<Map.Entry<Long, Short>>() {
			@Override
			public int compare(Map.Entry<Long, Short> a, Map.Entry<Long, Short> b) {
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