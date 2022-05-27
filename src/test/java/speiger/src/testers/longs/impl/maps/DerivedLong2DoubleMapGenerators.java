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
import speiger.src.collections.longs.maps.abstracts.AbstractLong2DoubleMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleSortedMap;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.TestLongSortedSetGenerator;
import speiger.src.testers.longs.generators.TestLongNavigableSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2DoubleMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2DoubleSortedMapGenerator;
import speiger.src.testers.longs.utils.LongSamples;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.doubles.utils.DoubleSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedLong2DoubleMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestLong2DoubleSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Long2DoubleNavigableMap createSubMap(Long2DoubleSortedMap sortedMap, long firstExclusive, long lastExclusive) {
	    	Long2DoubleNavigableMap map = (Long2DoubleNavigableMap) sortedMap;
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
	        return (Long2DoubleNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestLong2DoubleSortedMapGenerator {
		TestLong2DoubleSortedMapGenerator parent;
		Bound to;
		Bound from;
		long firstInclusive;
		long lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestLong2DoubleSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Long2DoubleSortedMap map = parent.create();
			entryComparator = DerivedLong2DoubleMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getLongKey();
			lastInclusive = samples.get(samples.size() - 1).getLongKey();
		}
		
		@Override
		public Long2DoubleSortedMap create(Entry... elements) {
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
		
		Long2DoubleSortedMap createSubMap(Long2DoubleSortedMap map, long firstExclusive, long lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestLong2DoubleSortedMapGenerator
	{
		TestLong2DoubleSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestLong2DoubleSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Long2DoubleNavigableMap create(Entry... elements) {
			return ((Long2DoubleNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Long, Double>> order(List<Map.Entry<Long, Double>> insertionOrder) {
			ObjectList<Map.Entry<Long, Double>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestLong2DoubleMapGenerator
	{
		TestLong2DoubleMapGenerator parent;
		
		public MapGenerator(TestLong2DoubleMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Long, Double>> order(List<Map.Entry<Long, Double>> insertionOrder) {
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
		public Long2DoubleMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Long2DoubleMap.Entry> {
		TestLong2DoubleMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Long, Double>, Map.Entry<Long, Double>> inner) {
			generator = (TestLong2DoubleMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).long2DoubleEntrySet();
		}
	}

	public static TestLongSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Long, Double>, Map.Entry<Long, Double>> inner) {
		if (inner.getInnerGenerator() instanceof TestLong2DoubleSortedMapGenerator) {
			LongSet set = ((TestLong2DoubleSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof LongNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof LongSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestLongNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Double>, Map.Entry<Long, Double>> inner) {
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
		TestLong2DoubleSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Double>, Map.Entry<Long, Double>> inner) {
			super(inner);
			generator = (TestLong2DoubleSortedMapGenerator) inner.getInnerGenerator();
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
		TestLong2DoubleMapGenerator generator;
		LongSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Long, Double>, Map.Entry<Long, Double>> inner) {
			generator = (TestLong2DoubleMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Long2DoubleMap.Entry> samples = generator.getSamples();
			this.samples = new LongSamples(samples.e0().getLongKey(), samples.e1().getLongKey(), samples.e2().getLongKey(), samples.e3().getLongKey(), samples.e4().getLongKey());
		}

		@Override
		public LongSamples getSamples() {
			return samples;
		}

		@Override
		public LongIterable order(LongList insertionOrder) {
			double value = generator.getSamples().e0().getDoubleValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (long key : insertionOrder) {
				entries.add(new AbstractLong2DoubleMap.BasicEntry(key, value));
			}
			LongList list = new LongArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getLongKey());
			}
			return list;
		}

		@Override
		public Iterable<Long> order(List<Long> insertionOrder) {
			double value = generator.getSamples().e0().getDoubleValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (long key : insertionOrder) {
				entries.add(new AbstractLong2DoubleMap.BasicEntry(key, value));
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
				result[index] = new AbstractLong2DoubleMap.BasicEntry(elements[index++], entry.getDoubleValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public LongSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2DoubleMap.BasicEntry((Long) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestDoubleCollectionGenerator {
		TestLong2DoubleMapGenerator generator;
		DoubleSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Long, Double>, Map.Entry<Long, Double>> inner) {
			generator = (TestLong2DoubleMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Long2DoubleMap.Entry> samples = generator.getSamples();
			this.samples = new DoubleSamples(samples.e0().getDoubleValue(), samples.e1().getDoubleValue(), samples.e2().getDoubleValue(), samples.e3().getDoubleValue(), samples.e4().getDoubleValue());
		}
		
		@Override
		public DoubleSamples getSamples() {
			return samples;
		}

		@Override
		public DoubleIterable order(DoubleList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new DoubleComparator() {
				@Override
				public int compare(double key, double value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(double entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Double.doubleToLongBits(list.get(i).getDoubleValue()) == Double.doubleToLongBits(entry)) return i;
					}
					throw new IllegalArgumentException("Long2DoubleMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Double> order(List<Double> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Double>() {
				@Override
				public int compare(Double key, Double value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Double entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Double.doubleToLongBits(list.get(i).getDoubleValue()) == Double.doubleToLongBits(entry.doubleValue())) return i;
					}
					throw new IllegalArgumentException("Long2DoubleMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public DoubleCollection create(double... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2DoubleMap.BasicEntry(entry.getLongKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public DoubleCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractLong2DoubleMap.BasicEntry(entry.getKey(), (Double)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Long, Double>> entryObjectComparator(Comparator<Long> keyComparator) {
		return new Comparator<Map.Entry<Long, Double>>() {
			@Override
			public int compare(Map.Entry<Long, Double> a, Map.Entry<Long, Double> b) {
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