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
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ShortMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortSortedMap;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleSortedSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleNavigableSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ShortMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ShortSortedMapGenerator;
import speiger.src.testers.doubles.utils.DoubleSamples;
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

public class DerivedDouble2ShortMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestDouble2ShortSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Double2ShortNavigableMap createSubMap(Double2ShortSortedMap sortedMap, double firstExclusive, double lastExclusive) {
	    	Double2ShortNavigableMap map = (Double2ShortNavigableMap) sortedMap;
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
	        return (Double2ShortNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestDouble2ShortSortedMapGenerator {
		TestDouble2ShortSortedMapGenerator parent;
		Bound to;
		Bound from;
		double firstInclusive;
		double lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestDouble2ShortSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Double2ShortSortedMap map = parent.create();
			entryComparator = DerivedDouble2ShortMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getDoubleKey();
			lastInclusive = samples.get(samples.size() - 1).getDoubleKey();
		}
		
		@Override
		public Double2ShortSortedMap create(Entry... elements) {
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
		
		Double2ShortSortedMap createSubMap(Double2ShortSortedMap map, double firstExclusive, double lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestDouble2ShortSortedMapGenerator
	{
		TestDouble2ShortSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestDouble2ShortSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Double2ShortNavigableMap create(Entry... elements) {
			return ((Double2ShortNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Double, Short>> order(List<Map.Entry<Double, Short>> insertionOrder) {
			ObjectList<Map.Entry<Double, Short>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestDouble2ShortMapGenerator
	{
		TestDouble2ShortMapGenerator parent;
		
		public MapGenerator(TestDouble2ShortMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Double, Short>> order(List<Map.Entry<Double, Short>> insertionOrder) {
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
		public Double2ShortMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Double2ShortMap.Entry> {
		TestDouble2ShortMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Double, Short>, Map.Entry<Double, Short>> inner) {
			generator = (TestDouble2ShortMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).double2ShortEntrySet();
		}
	}

	public static TestDoubleSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Double, Short>, Map.Entry<Double, Short>> inner) {
		if (inner.getInnerGenerator() instanceof TestDouble2ShortSortedMapGenerator) {
			DoubleSet set = ((TestDouble2ShortSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof DoubleNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof DoubleSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestDoubleNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Short>, Map.Entry<Double, Short>> inner) {
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
		TestDouble2ShortSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Short>, Map.Entry<Double, Short>> inner) {
			super(inner);
			generator = (TestDouble2ShortSortedMapGenerator) inner.getInnerGenerator();
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
		TestDouble2ShortMapGenerator generator;
		DoubleSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Short>, Map.Entry<Double, Short>> inner) {
			generator = (TestDouble2ShortMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Double2ShortMap.Entry> samples = generator.getSamples();
			this.samples = new DoubleSamples(samples.e0().getDoubleKey(), samples.e1().getDoubleKey(), samples.e2().getDoubleKey(), samples.e3().getDoubleKey(), samples.e4().getDoubleKey());
		}

		@Override
		public DoubleSamples getSamples() {
			return samples;
		}

		@Override
		public DoubleIterable order(DoubleList insertionOrder) {
			short value = generator.getSamples().e0().getShortValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (double key : insertionOrder) {
				entries.add(new AbstractDouble2ShortMap.BasicEntry(key, value));
			}
			DoubleList list = new DoubleArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getDoubleKey());
			}
			return list;
		}

		@Override
		public Iterable<Double> order(List<Double> insertionOrder) {
			short value = generator.getSamples().e0().getShortValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (double key : insertionOrder) {
				entries.add(new AbstractDouble2ShortMap.BasicEntry(key, value));
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
				result[index] = new AbstractDouble2ShortMap.BasicEntry(elements[index++], entry.getShortValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public DoubleSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2ShortMap.BasicEntry((Double) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestShortCollectionGenerator {
		TestDouble2ShortMapGenerator generator;
		ShortSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Double, Short>, Map.Entry<Double, Short>> inner) {
			generator = (TestDouble2ShortMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Double2ShortMap.Entry> samples = generator.getSamples();
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
					throw new IllegalArgumentException("Double2ShortMap.values generator can order only sample values");
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
					throw new IllegalArgumentException("Double2ShortMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ShortCollection create(short... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2ShortMap.BasicEntry(entry.getDoubleKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public ShortCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2ShortMap.BasicEntry(entry.getKey(), (Short)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Double, Short>> entryObjectComparator(Comparator<Double> keyComparator) {
		return new Comparator<Map.Entry<Double, Short>>() {
			@Override
			public int compare(Map.Entry<Double, Short> a, Map.Entry<Double, Short> b) {
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