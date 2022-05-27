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
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2DoubleMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleSortedMap;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleSortedSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleNavigableSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2DoubleMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2DoubleSortedMapGenerator;
import speiger.src.testers.doubles.utils.DoubleSamples;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedDouble2DoubleMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestDouble2DoubleSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Double2DoubleNavigableMap createSubMap(Double2DoubleSortedMap sortedMap, double firstExclusive, double lastExclusive) {
	    	Double2DoubleNavigableMap map = (Double2DoubleNavigableMap) sortedMap;
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
	        return (Double2DoubleNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestDouble2DoubleSortedMapGenerator {
		TestDouble2DoubleSortedMapGenerator parent;
		Bound to;
		Bound from;
		double firstInclusive;
		double lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestDouble2DoubleSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Double2DoubleSortedMap map = parent.create();
			entryComparator = DerivedDouble2DoubleMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getDoubleKey();
			lastInclusive = samples.get(samples.size() - 1).getDoubleKey();
		}
		
		@Override
		public Double2DoubleSortedMap create(Entry... elements) {
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
		
		Double2DoubleSortedMap createSubMap(Double2DoubleSortedMap map, double firstExclusive, double lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestDouble2DoubleSortedMapGenerator
	{
		TestDouble2DoubleSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestDouble2DoubleSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Double2DoubleNavigableMap create(Entry... elements) {
			return ((Double2DoubleNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Double, Double>> order(List<Map.Entry<Double, Double>> insertionOrder) {
			ObjectList<Map.Entry<Double, Double>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestDouble2DoubleMapGenerator
	{
		TestDouble2DoubleMapGenerator parent;
		
		public MapGenerator(TestDouble2DoubleMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Double, Double>> order(List<Map.Entry<Double, Double>> insertionOrder) {
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
		public Double2DoubleMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Double2DoubleMap.Entry> {
		TestDouble2DoubleMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Double, Double>, Map.Entry<Double, Double>> inner) {
			generator = (TestDouble2DoubleMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).double2DoubleEntrySet();
		}
	}

	public static TestDoubleSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Double, Double>, Map.Entry<Double, Double>> inner) {
		if (inner.getInnerGenerator() instanceof TestDouble2DoubleSortedMapGenerator) {
			DoubleSet set = ((TestDouble2DoubleSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof DoubleNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof DoubleSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestDoubleNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Double>, Map.Entry<Double, Double>> inner) {
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
		TestDouble2DoubleSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Double>, Map.Entry<Double, Double>> inner) {
			super(inner);
			generator = (TestDouble2DoubleSortedMapGenerator) inner.getInnerGenerator();
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
		TestDouble2DoubleMapGenerator generator;
		DoubleSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Double>, Map.Entry<Double, Double>> inner) {
			generator = (TestDouble2DoubleMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Double2DoubleMap.Entry> samples = generator.getSamples();
			this.samples = new DoubleSamples(samples.e0().getDoubleKey(), samples.e1().getDoubleKey(), samples.e2().getDoubleKey(), samples.e3().getDoubleKey(), samples.e4().getDoubleKey());
		}

		@Override
		public DoubleSamples getSamples() {
			return samples;
		}

		@Override
		public DoubleIterable order(DoubleList insertionOrder) {
			double value = generator.getSamples().e0().getDoubleValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (double key : insertionOrder) {
				entries.add(new AbstractDouble2DoubleMap.BasicEntry(key, value));
			}
			DoubleList list = new DoubleArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getDoubleKey());
			}
			return list;
		}

		@Override
		public Iterable<Double> order(List<Double> insertionOrder) {
			double value = generator.getSamples().e0().getDoubleValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (double key : insertionOrder) {
				entries.add(new AbstractDouble2DoubleMap.BasicEntry(key, value));
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
				result[index] = new AbstractDouble2DoubleMap.BasicEntry(elements[index++], entry.getDoubleValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public DoubleSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2DoubleMap.BasicEntry((Double) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestDoubleCollectionGenerator {
		TestDouble2DoubleMapGenerator generator;
		DoubleSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Double, Double>, Map.Entry<Double, Double>> inner) {
			generator = (TestDouble2DoubleMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Double2DoubleMap.Entry> samples = generator.getSamples();
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
					throw new IllegalArgumentException("Double2DoubleMap.values generator can order only sample values");
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
					throw new IllegalArgumentException("Double2DoubleMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public DoubleCollection create(double... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2DoubleMap.BasicEntry(entry.getDoubleKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public DoubleCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2DoubleMap.BasicEntry(entry.getKey(), (Double)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Double, Double>> entryObjectComparator(Comparator<Double> keyComparator) {
		return new Comparator<Map.Entry<Double, Double>>() {
			@Override
			public int compare(Map.Entry<Double, Double> a, Map.Entry<Double, Double> b) {
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