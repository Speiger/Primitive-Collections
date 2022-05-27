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
import speiger.src.collections.longs.maps.abstracts.AbstractLong2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanSortedMap;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.TestLongSortedSetGenerator;
import speiger.src.testers.longs.generators.TestLongNavigableSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2BooleanMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2BooleanSortedMapGenerator;
import speiger.src.testers.longs.utils.LongSamples;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.booleans.utils.BooleanSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedLong2BooleanMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestLong2BooleanSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Long2BooleanNavigableMap createSubMap(Long2BooleanSortedMap sortedMap, long firstExclusive, long lastExclusive) {
	    	Long2BooleanNavigableMap map = (Long2BooleanNavigableMap) sortedMap;
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
	        return (Long2BooleanNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestLong2BooleanSortedMapGenerator {
		TestLong2BooleanSortedMapGenerator parent;
		Bound to;
		Bound from;
		long firstInclusive;
		long lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestLong2BooleanSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Long2BooleanSortedMap map = parent.create();
			entryComparator = DerivedLong2BooleanMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getLongKey();
			lastInclusive = samples.get(samples.size() - 1).getLongKey();
		}
		
		@Override
		public Long2BooleanSortedMap create(Entry... elements) {
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
		
		Long2BooleanSortedMap createSubMap(Long2BooleanSortedMap map, long firstExclusive, long lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestLong2BooleanSortedMapGenerator
	{
		TestLong2BooleanSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestLong2BooleanSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Long2BooleanNavigableMap create(Entry... elements) {
			return ((Long2BooleanNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Long, Boolean>> order(List<Map.Entry<Long, Boolean>> insertionOrder) {
			ObjectList<Map.Entry<Long, Boolean>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestLong2BooleanMapGenerator
	{
		TestLong2BooleanMapGenerator parent;
		
		public MapGenerator(TestLong2BooleanMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Long, Boolean>> order(List<Map.Entry<Long, Boolean>> insertionOrder) {
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
		public Long2BooleanMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Long2BooleanMap.Entry> {
		TestLong2BooleanMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Long, Boolean>, Map.Entry<Long, Boolean>> inner) {
			generator = (TestLong2BooleanMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).long2BooleanEntrySet();
		}
	}

	public static TestLongSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Long, Boolean>, Map.Entry<Long, Boolean>> inner) {
		if (inner.getInnerGenerator() instanceof TestLong2BooleanSortedMapGenerator) {
			LongSet set = ((TestLong2BooleanSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof LongNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof LongSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestLongNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Boolean>, Map.Entry<Long, Boolean>> inner) {
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
		TestLong2BooleanSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Boolean>, Map.Entry<Long, Boolean>> inner) {
			super(inner);
			generator = (TestLong2BooleanSortedMapGenerator) inner.getInnerGenerator();
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
		TestLong2BooleanMapGenerator generator;
		LongSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Boolean>, Map.Entry<Long, Boolean>> inner) {
			generator = (TestLong2BooleanMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Long2BooleanMap.Entry> samples = generator.getSamples();
			this.samples = new LongSamples(samples.e0().getLongKey(), samples.e1().getLongKey(), samples.e2().getLongKey(), samples.e3().getLongKey(), samples.e4().getLongKey());
		}

		@Override
		public LongSamples getSamples() {
			return samples;
		}

		@Override
		public LongIterable order(LongList insertionOrder) {
			boolean value = generator.getSamples().e0().getBooleanValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (long key : insertionOrder) {
				entries.add(new AbstractLong2BooleanMap.BasicEntry(key, value));
			}
			LongList list = new LongArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getLongKey());
			}
			return list;
		}

		@Override
		public Iterable<Long> order(List<Long> insertionOrder) {
			boolean value = generator.getSamples().e0().getBooleanValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (long key : insertionOrder) {
				entries.add(new AbstractLong2BooleanMap.BasicEntry(key, value));
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
				result[index] = new AbstractLong2BooleanMap.BasicEntry(elements[index++], entry.getBooleanValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public LongSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2BooleanMap.BasicEntry((Long) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestBooleanCollectionGenerator {
		TestLong2BooleanMapGenerator generator;
		BooleanSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Long, Boolean>, Map.Entry<Long, Boolean>> inner) {
			generator = (TestLong2BooleanMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Long2BooleanMap.Entry> samples = generator.getSamples();
			this.samples = new BooleanSamples(samples.e0().getBooleanValue(), samples.e1().getBooleanValue(), samples.e2().getBooleanValue(), samples.e3().getBooleanValue(), samples.e4().getBooleanValue());
		}
		
		@Override
		public BooleanSamples getSamples() {
			return samples;
		}

		@Override
		public BooleanIterable order(BooleanList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new BooleanComparator() {
				@Override
				public int compare(boolean key, boolean value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(boolean entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getBooleanValue() == entry) return i;
					}
					throw new IllegalArgumentException("Long2BooleanMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Boolean> order(List<Boolean> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Boolean>() {
				@Override
				public int compare(Boolean key, Boolean value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Boolean entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getBooleanValue() == entry.booleanValue()) return i;
					}
					throw new IllegalArgumentException("Long2BooleanMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public BooleanCollection create(boolean... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2BooleanMap.BasicEntry(entry.getLongKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public BooleanCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2BooleanMap.BasicEntry(entry.getKey(), (Boolean)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Long, Boolean>> entryObjectComparator(Comparator<Long> keyComparator) {
		return new Comparator<Map.Entry<Long, Boolean>>() {
			@Override
			public int compare(Map.Entry<Long, Boolean> a, Map.Entry<Long, Boolean> b) {
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