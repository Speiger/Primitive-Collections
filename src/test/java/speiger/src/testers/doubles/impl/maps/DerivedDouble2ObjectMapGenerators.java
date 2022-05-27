package speiger.src.testers.doubles.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ObjectMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectSortedMap;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleSortedSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleNavigableSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ObjectMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ObjectSortedMapGenerator;
import speiger.src.testers.doubles.utils.DoubleSamples;
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

public class DerivedDouble2ObjectMapGenerators {
	public static class NavigableMapGenerator<V> extends SortedMapGenerator<V> {
		public NavigableMapGenerator(TestDouble2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Double2ObjectNavigableMap<V> createSubMap(Double2ObjectSortedMap<V> sortedMap, double firstExclusive, double lastExclusive) {
	    	Double2ObjectNavigableMap<V> map = (Double2ObjectNavigableMap<V>) sortedMap;
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
	        return (Double2ObjectNavigableMap<V>) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator<V> extends MapGenerator<V> implements TestDouble2ObjectSortedMapGenerator<V> {
		TestDouble2ObjectSortedMapGenerator<V> parent;
		Bound to;
		Bound from;
		double firstInclusive;
		double lastInclusive;
		Comparator<Entry<V>> entryComparator;

		public SortedMapGenerator(TestDouble2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Double2ObjectSortedMap<V> map = parent.create();
			entryComparator = DerivedDouble2ObjectMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry<V>> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getDoubleKey();
			lastInclusive = samples.get(samples.size() - 1).getDoubleKey();
		}
		
		@Override
		public Double2ObjectSortedMap<V> create(Entry<V>... elements) {
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
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getDoubleKey(), parent.aboveSamplesLesser().getDoubleKey());
		}
		
		Double2ObjectSortedMap<V> createSubMap(Double2ObjectSortedMap<V> map, double firstExclusive, double lastExclusive) {
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
	
	public static class DescendingTestMapGenerator<V> extends MapGenerator<V> implements TestDouble2ObjectSortedMapGenerator<V>
	{
		TestDouble2ObjectSortedMapGenerator<V> parent;
		
		public DescendingTestMapGenerator(TestDouble2ObjectSortedMapGenerator<V> parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Double2ObjectNavigableMap<V> create(Entry<V>... elements) {
			return ((Double2ObjectNavigableMap<V>)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Double, V>> order(List<Map.Entry<Double, V>> insertionOrder) {
			ObjectList<Map.Entry<Double, V>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator<V> implements TestDouble2ObjectMapGenerator<V>
	{
		TestDouble2ObjectMapGenerator<V> parent;
		
		public MapGenerator(TestDouble2ObjectMapGenerator<V> parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Double, V>> order(List<Map.Entry<Double, V>> insertionOrder) {
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
		public Double2ObjectMap<V> create(Entry<V>... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator<V> implements TestObjectSetGenerator<Double2ObjectMap.Entry<V>> {
		TestDouble2ObjectMapGenerator<V> generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Double, V>, Map.Entry<Double, V>> inner) {
			generator = (TestDouble2ObjectMapGenerator<V>) inner.getInnerGenerator();
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
			return generator.create(elements).double2ObjectEntrySet();
		}
	}

	public static <V> TestDoubleSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Double, V>, Map.Entry<Double, V>> inner) {
		if (inner.getInnerGenerator() instanceof TestDouble2ObjectSortedMapGenerator) {
			DoubleSet set = ((TestDouble2ObjectSortedMapGenerator<V>) inner.getInnerGenerator()).create().keySet();
			if(set instanceof DoubleNavigableSet) return new MapNavigableKeySetGenerator<>(inner);
			if(set instanceof DoubleSortedSet) return new MapSortedKeySetGenerator<>(inner);
		}
		return new MapKeySetGenerator<>(inner);
	}
	
	public static class MapNavigableKeySetGenerator<V> extends MapSortedKeySetGenerator<V> implements TestDoubleNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, V>, Map.Entry<Double, V>> inner) {
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
	
	public static class MapSortedKeySetGenerator<V> extends MapKeySetGenerator<V> implements TestDoubleSortedSetGenerator {
		TestDouble2ObjectSortedMapGenerator<V> generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, V>, Map.Entry<Double, V>> inner) {
			super(inner);
			generator = (TestDouble2ObjectSortedMapGenerator<V>) inner.getInnerGenerator();
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

	public static class MapKeySetGenerator<V> implements TestDoubleSetGenerator {
		TestDouble2ObjectMapGenerator<V> generator;
		DoubleSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, V>, Map.Entry<Double, V>> inner) {
			generator = (TestDouble2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Double2ObjectMap.Entry<V>> samples = generator.getSamples();
			this.samples = new DoubleSamples(samples.e0().getDoubleKey(), samples.e1().getDoubleKey(), samples.e2().getDoubleKey(), samples.e3().getDoubleKey(), samples.e4().getDoubleKey());
		}

		@Override
		public DoubleSamples getSamples() {
			return samples;
		}

		@Override
		public DoubleIterable order(DoubleList insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (double key : insertionOrder) {
				entries.add(new AbstractDouble2ObjectMap.BasicEntry<>(key, value));
			}
			DoubleList list = new DoubleArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getDoubleKey());
			}
			return list;
		}

		@Override
		public Iterable<Double> order(List<Double> insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (double key : insertionOrder) {
				entries.add(new AbstractDouble2ObjectMap.BasicEntry<>(key, value));
			}
			DoubleList list = new DoubleArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getDoubleKey());
			}
			return list;
		}
		
		@Override
		public DoubleSet create(double... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2ObjectMap.BasicEntry<>(elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public DoubleSet create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2ObjectMap.BasicEntry<>((Double) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator<V> implements TestObjectCollectionGenerator<V> {
		TestDouble2ObjectMapGenerator<V> generator;
		ObjectSamples<V> samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Double, V>, Map.Entry<Double, V>> inner) {
			generator = (TestDouble2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Double2ObjectMap.Entry<V>> samples = generator.getSamples();
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
					throw new IllegalArgumentException("Double2ObjectMap.values generator can order only sample values");
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
					throw new IllegalArgumentException("Double2ObjectMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ObjectCollection<V> create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2ObjectMap.BasicEntry<>(entry.getKey(), (V)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static<V> Comparator<Map.Entry<Double, V>> entryObjectComparator(Comparator<Double> keyComparator) {
		return new Comparator<Map.Entry<Double, V>>() {
			@Override
			public int compare(Map.Entry<Double, V> a, Map.Entry<Double, V> b) {
				if(keyComparator == null) {
					return Double.compare(a.getKey().doubleValue(), b.getKey().doubleValue());
				}
				return keyComparator.compare(a.getKey().doubleValue(), b.getKey().doubleValue());
			}
		};
	}
	
	public static<V> Comparator<Entry<V>> entryComparator(DoubleComparator keyComparator) {
		return new Comparator<Entry<V>>() {
			@Override
			public int compare(Entry<V> a, Entry<V> b) {
				if(keyComparator == null) {
					return Double.compare(a.getDoubleKey(), b.getDoubleKey());
				}
				return keyComparator.compare(a.getDoubleKey(), b.getDoubleKey());
			}
		};
	}
}