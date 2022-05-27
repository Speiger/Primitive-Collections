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
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ByteMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteNavigableMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteSortedMap;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleSortedSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleNavigableSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ByteMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ByteSortedMapGenerator;
import speiger.src.testers.doubles.utils.DoubleSamples;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.bytes.utils.ByteSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedDouble2ByteMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestDouble2ByteSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Double2ByteNavigableMap createSubMap(Double2ByteSortedMap sortedMap, double firstExclusive, double lastExclusive) {
	    	Double2ByteNavigableMap map = (Double2ByteNavigableMap) sortedMap;
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
	        return (Double2ByteNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestDouble2ByteSortedMapGenerator {
		TestDouble2ByteSortedMapGenerator parent;
		Bound to;
		Bound from;
		double firstInclusive;
		double lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestDouble2ByteSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Double2ByteSortedMap map = parent.create();
			entryComparator = DerivedDouble2ByteMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getDoubleKey();
			lastInclusive = samples.get(samples.size() - 1).getDoubleKey();
		}
		
		@Override
		public Double2ByteSortedMap create(Entry... elements) {
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
		
		Double2ByteSortedMap createSubMap(Double2ByteSortedMap map, double firstExclusive, double lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestDouble2ByteSortedMapGenerator
	{
		TestDouble2ByteSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestDouble2ByteSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Double2ByteNavigableMap create(Entry... elements) {
			return ((Double2ByteNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Double, Byte>> order(List<Map.Entry<Double, Byte>> insertionOrder) {
			ObjectList<Map.Entry<Double, Byte>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestDouble2ByteMapGenerator
	{
		TestDouble2ByteMapGenerator parent;
		
		public MapGenerator(TestDouble2ByteMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Double, Byte>> order(List<Map.Entry<Double, Byte>> insertionOrder) {
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
		public Double2ByteMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Double2ByteMap.Entry> {
		TestDouble2ByteMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Double, Byte>, Map.Entry<Double, Byte>> inner) {
			generator = (TestDouble2ByteMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).double2ByteEntrySet();
		}
	}

	public static TestDoubleSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Double, Byte>, Map.Entry<Double, Byte>> inner) {
		if (inner.getInnerGenerator() instanceof TestDouble2ByteSortedMapGenerator) {
			DoubleSet set = ((TestDouble2ByteSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof DoubleNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof DoubleSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestDoubleNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Byte>, Map.Entry<Double, Byte>> inner) {
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
		TestDouble2ByteSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Byte>, Map.Entry<Double, Byte>> inner) {
			super(inner);
			generator = (TestDouble2ByteSortedMapGenerator) inner.getInnerGenerator();
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
		TestDouble2ByteMapGenerator generator;
		DoubleSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Double, Byte>, Map.Entry<Double, Byte>> inner) {
			generator = (TestDouble2ByteMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Double2ByteMap.Entry> samples = generator.getSamples();
			this.samples = new DoubleSamples(samples.e0().getDoubleKey(), samples.e1().getDoubleKey(), samples.e2().getDoubleKey(), samples.e3().getDoubleKey(), samples.e4().getDoubleKey());
		}

		@Override
		public DoubleSamples getSamples() {
			return samples;
		}

		@Override
		public DoubleIterable order(DoubleList insertionOrder) {
			byte value = generator.getSamples().e0().getByteValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (double key : insertionOrder) {
				entries.add(new AbstractDouble2ByteMap.BasicEntry(key, value));
			}
			DoubleList list = new DoubleArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getDoubleKey());
			}
			return list;
		}

		@Override
		public Iterable<Double> order(List<Double> insertionOrder) {
			byte value = generator.getSamples().e0().getByteValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (double key : insertionOrder) {
				entries.add(new AbstractDouble2ByteMap.BasicEntry(key, value));
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
				result[index] = new AbstractDouble2ByteMap.BasicEntry(elements[index++], entry.getByteValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public DoubleSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2ByteMap.BasicEntry((Double) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestByteCollectionGenerator {
		TestDouble2ByteMapGenerator generator;
		ByteSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Double, Byte>, Map.Entry<Double, Byte>> inner) {
			generator = (TestDouble2ByteMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Double2ByteMap.Entry> samples = generator.getSamples();
			this.samples = new ByteSamples(samples.e0().getByteValue(), samples.e1().getByteValue(), samples.e2().getByteValue(), samples.e3().getByteValue(), samples.e4().getByteValue());
		}
		
		@Override
		public ByteSamples getSamples() {
			return samples;
		}

		@Override
		public ByteIterable order(ByteList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new ByteComparator() {
				@Override
				public int compare(byte key, byte value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(byte entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getByteValue() == entry) return i;
					}
					throw new IllegalArgumentException("Double2ByteMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Byte> order(List<Byte> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Byte>() {
				@Override
				public int compare(Byte key, Byte value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Byte entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getByteValue() == entry.byteValue()) return i;
					}
					throw new IllegalArgumentException("Double2ByteMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ByteCollection create(byte... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2ByteMap.BasicEntry(entry.getDoubleKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public ByteCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractDouble2ByteMap.BasicEntry(entry.getKey(), (Byte)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Double, Byte>> entryObjectComparator(Comparator<Double> keyComparator) {
		return new Comparator<Map.Entry<Double, Byte>>() {
			@Override
			public int compare(Map.Entry<Double, Byte> a, Map.Entry<Double, Byte> b) {
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