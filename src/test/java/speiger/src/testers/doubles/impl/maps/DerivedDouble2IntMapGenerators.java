package speiger.src.testers.doubles.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2IntNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntSortedMap;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleSortedSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleNavigableSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2IntMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2IntSortedMapGenerator;
import speiger.src.testers.doubles.utils.DoubleSamples;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.ints.utils.IntSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class DerivedDouble2IntMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestDouble2IntSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Double2IntNavigableMap createSubMap(Double2IntSortedMap sortedMap, double firstExclusive, double lastExclusive) {
	    	Double2IntNavigableMap map = (Double2IntNavigableMap) sortedMap;
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
	        return (Double2IntNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestDouble2IntSortedMapGenerator {
		TestDouble2IntSortedMapGenerator parent;
		Bound to;
		Bound from;
		double firstInclusive;
		double lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestDouble2IntSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Double2IntSortedMap map = parent.create();
			entryComparator = DerivedDouble2IntMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getDoubleKey();
			lastInclusive = samples.get(samples.size() - 1).getDoubleKey();
		}
		
		@Override
		public Double2IntSortedMap create(Entry... elements) {
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
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getDoubleKey(), parent.aboveSamplesLesser().getDoubleKey());
		}
		
		Double2IntSortedMap createSubMap(Double2IntSortedMap map, double firstExclusive, double lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestDouble2IntSortedMapGenerator
	{
		TestDouble2IntSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestDouble2IntSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Double2IntNavigableMap create(Entry... elements) {
			return ((Double2IntNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Double, Integer>> order(List<Map.Entry<Double, Integer>> insertionOrder) {
			ObjectList<Map.Entry<Double, Integer>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestDouble2IntMapGenerator
	{
		TestDouble2IntMapGenerator parent;
		
		public MapGenerator(TestDouble2IntMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Double, Integer>> order(List<Map.Entry<Double, Integer>> insertionOrder) {
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
		public Double2IntMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Double2IntMap.Entry> {
		TestDouble2IntMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Double, Integer>, Map.Entry<Double, Integer>> inner) {
			generator = (TestDouble2IntMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).double2IntEntrySet();
		}
	}

	public static TestDoubleSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Double, Integer>, Map.Entry<Double, Integer>> inner) {
		if (inner.getInnerGenerator() instanceof TestDouble2IntSortedMapGenerator) {
			DoubleSet set = ((TestDouble2IntSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof DoubleNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof DoubleSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestDoubleNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Integer>, Map.Entry<Double, Integer>> inner) {
			super(inner);
		}
		
		@Override
		public DoubleNavigableSet create(double... elements) {
			return (DoubleNavigableSet) super.create(elements);
		}

		@Override
		public DoubleNavigableSet create(Object... elements) {
			return (DoubleNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestDoubleSortedSetGenerator {
		TestDouble2IntSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Integer>, Map.Entry<Double, Integer>> inner) {
			super(inner);
			generator = (TestDouble2IntSortedMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public DoubleSortedSet create(double... elements) {
			return (DoubleSortedSet) super.create(elements);
		}

		@Override
		public DoubleSortedSet create(Object... elements) {
			return (DoubleSortedSet) super.create(elements);
		}

		@Override
		public double belowSamplesLesser() {
			return generator.belowSamplesLesser().getDoubleKey();
		}

		@Override
		public double belowSamplesGreater() {
			return generator.belowSamplesGreater().getDoubleKey();
		}

		@Override
		public double aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getDoubleKey();
		}

		@Override
		public double aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getDoubleKey();
		}
	}

	public static class MapKeySetGenerator implements TestDoubleSetGenerator {
		TestDouble2IntMapGenerator generator;
		DoubleSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Integer>, Map.Entry<Double, Integer>> inner) {
			generator = (TestDouble2IntMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Double2IntMap.Entry> samples = generator.getSamples();
			this.samples = new DoubleSamples(samples.e0().getDoubleKey(), samples.e1().getDoubleKey(), samples.e2().getDoubleKey(), samples.e3().getDoubleKey(), samples.e4().getDoubleKey());
		}

		@Override
		public DoubleSamples getSamples() {
			return samples;
		}

		@Override
		public DoubleIterable order(DoubleList insertionOrder) {
			int value = generator.getSamples().e0().getIntValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (double key : insertionOrder) {
				entries.add(new AbstractDouble2IntMap.BasicEntry(key, value));
			}
			DoubleList list = new DoubleArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getDoubleKey());
			}
			return list;
		}

		@Override
		public Iterable<Double> order(List<Double> insertionOrder) {
			int value = generator.getSamples().e0().getIntValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (double key : insertionOrder) {
				entries.add(new AbstractDouble2IntMap.BasicEntry(key, value));
			}
			DoubleList list = new DoubleArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getDoubleKey());
			}
			return list;
		}
		
		@Override
		public DoubleSet create(double... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2IntMap.BasicEntry(elements[index++], entry.getIntValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public DoubleSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2IntMap.BasicEntry((Double) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestIntCollectionGenerator {
		TestDouble2IntMapGenerator generator;
		IntSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Double, Integer>, Map.Entry<Double, Integer>> inner) {
			generator = (TestDouble2IntMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Double2IntMap.Entry> samples = generator.getSamples();
			this.samples = new IntSamples(samples.e0().getIntValue(), samples.e1().getIntValue(), samples.e2().getIntValue(), samples.e3().getIntValue(), samples.e4().getIntValue());
		}
		
		@Override
		public IntSamples getSamples() {
			return samples;
		}

		@Override
		public IntIterable order(IntList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new IntComparator() {
				@Override
				public int compare(int key, int value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(int entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getIntValue() == entry) return i;
					}
					throw new IllegalArgumentException("Double2IntMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Integer> order(List<Integer> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Integer>() {
				@Override
				public int compare(Integer key, Integer value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Integer entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getIntValue() == entry.intValue()) return i;
					}
					throw new IllegalArgumentException("Double2IntMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public IntCollection create(int... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2IntMap.BasicEntry(entry.getDoubleKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public IntCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2IntMap.BasicEntry(entry.getKey(), (Integer)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Double, Integer>> entryObjectComparator(Comparator<Double> keyComparator) {
		return new Comparator<Map.Entry<Double, Integer>>() {
			@Override
			public int compare(Map.Entry<Double, Integer> a, Map.Entry<Double, Integer> b) {
				if(keyComparator == null) {
					return Double.compare(a.getKey().doubleValue(), b.getKey().doubleValue());
				}
				return keyComparator.compare(a.getKey().doubleValue(), b.getKey().doubleValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(DoubleComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Double.compare(a.getDoubleKey(), b.getDoubleKey());
				}
				return keyComparator.compare(a.getDoubleKey(), b.getDoubleKey());
			}
		};
	}
}