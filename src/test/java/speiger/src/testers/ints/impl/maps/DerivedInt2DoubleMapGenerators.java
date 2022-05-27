package speiger.src.testers.ints.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2DoubleMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleSortedMap;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.TestIntSortedSetGenerator;
import speiger.src.testers.ints.generators.TestIntNavigableSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2DoubleMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2DoubleSortedMapGenerator;
import speiger.src.testers.ints.utils.IntSamples;
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
public class DerivedInt2DoubleMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestInt2DoubleSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Int2DoubleNavigableMap createSubMap(Int2DoubleSortedMap sortedMap, int firstExclusive, int lastExclusive) {
	    	Int2DoubleNavigableMap map = (Int2DoubleNavigableMap) sortedMap;
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
	        return (Int2DoubleNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestInt2DoubleSortedMapGenerator {
		TestInt2DoubleSortedMapGenerator parent;
		Bound to;
		Bound from;
		int firstInclusive;
		int lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestInt2DoubleSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Int2DoubleSortedMap map = parent.create();
			entryComparator = DerivedInt2DoubleMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getIntKey();
			lastInclusive = samples.get(samples.size() - 1).getIntKey();
		}
		
		@Override
		public Int2DoubleSortedMap create(Entry... elements) {
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
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getIntKey(), parent.aboveSamplesLesser().getIntKey());
		}
		
		Int2DoubleSortedMap createSubMap(Int2DoubleSortedMap map, int firstExclusive, int lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestInt2DoubleSortedMapGenerator
	{
		TestInt2DoubleSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestInt2DoubleSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Int2DoubleNavigableMap create(Entry... elements) {
			return ((Int2DoubleNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Integer, Double>> order(List<Map.Entry<Integer, Double>> insertionOrder) {
			ObjectList<Map.Entry<Integer, Double>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestInt2DoubleMapGenerator
	{
		TestInt2DoubleMapGenerator parent;
		
		public MapGenerator(TestInt2DoubleMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Integer, Double>> order(List<Map.Entry<Integer, Double>> insertionOrder) {
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
		public Int2DoubleMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Int2DoubleMap.Entry> {
		TestInt2DoubleMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Double>, Map.Entry<Integer, Double>> inner) {
			generator = (TestInt2DoubleMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).int2DoubleEntrySet();
		}
	}

	public static TestIntSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Double>, Map.Entry<Integer, Double>> inner) {
		if (inner.getInnerGenerator() instanceof TestInt2DoubleSortedMapGenerator) {
			IntSet set = ((TestInt2DoubleSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof IntNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof IntSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestIntNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Double>, Map.Entry<Integer, Double>> inner) {
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
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestIntSortedSetGenerator {
		TestInt2DoubleSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Double>, Map.Entry<Integer, Double>> inner) {
			super(inner);
			generator = (TestInt2DoubleSortedMapGenerator) inner.getInnerGenerator();
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

	public static class MapKeySetGenerator implements TestIntSetGenerator {
		TestInt2DoubleMapGenerator generator;
		IntSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Double>, Map.Entry<Integer, Double>> inner) {
			generator = (TestInt2DoubleMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Int2DoubleMap.Entry> samples = generator.getSamples();
			this.samples = new IntSamples(samples.e0().getIntKey(), samples.e1().getIntKey(), samples.e2().getIntKey(), samples.e3().getIntKey(), samples.e4().getIntKey());
		}

		@Override
		public IntSamples getSamples() {
			return samples;
		}

		@Override
		public IntIterable order(IntList insertionOrder) {
			double value = generator.getSamples().e0().getDoubleValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (int key : insertionOrder) {
				entries.add(new AbstractInt2DoubleMap.BasicEntry(key, value));
			}
			IntList list = new IntArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getIntKey());
			}
			return list;
		}

		@Override
		public Iterable<Integer> order(List<Integer> insertionOrder) {
			double value = generator.getSamples().e0().getDoubleValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (int key : insertionOrder) {
				entries.add(new AbstractInt2DoubleMap.BasicEntry(key, value));
			}
			IntList list = new IntArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getIntKey());
			}
			return list;
		}
		
		@Override
		public IntSet create(int... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2DoubleMap.BasicEntry(elements[index++], entry.getDoubleValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public IntSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2DoubleMap.BasicEntry((Integer) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestDoubleCollectionGenerator {
		TestInt2DoubleMapGenerator generator;
		DoubleSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Integer, Double>, Map.Entry<Integer, Double>> inner) {
			generator = (TestInt2DoubleMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Int2DoubleMap.Entry> samples = generator.getSamples();
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
					throw new IllegalArgumentException("Int2DoubleMap.values generator can order only sample values");
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
					throw new IllegalArgumentException("Int2DoubleMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public DoubleCollection create(double... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2DoubleMap.BasicEntry(entry.getIntKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public DoubleCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2DoubleMap.BasicEntry(entry.getKey(), (Double)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Integer, Double>> entryObjectComparator(Comparator<Integer> keyComparator) {
		return new Comparator<Map.Entry<Integer, Double>>() {
			@Override
			public int compare(Map.Entry<Integer, Double> a, Map.Entry<Integer, Double> b) {
				if(keyComparator == null) {
					return Integer.compare(a.getKey().intValue(), b.getKey().intValue());
				}
				return keyComparator.compare(a.getKey().intValue(), b.getKey().intValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(IntComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Integer.compare(a.getIntKey(), b.getIntKey());
				}
				return keyComparator.compare(a.getIntKey(), b.getIntKey());
			}
		};
	}
}