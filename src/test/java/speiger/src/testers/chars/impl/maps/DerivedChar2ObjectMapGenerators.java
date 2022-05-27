package speiger.src.testers.chars.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectSortedMap;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.TestCharSortedSetGenerator;
import speiger.src.testers.chars.generators.TestCharNavigableSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2ObjectMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2ObjectSortedMapGenerator;
import speiger.src.testers.chars.utils.CharSamples;
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

@SuppressWarnings("javadoc")
public class DerivedChar2ObjectMapGenerators {
	public static class NavigableMapGenerator<V> extends SortedMapGenerator<V> {
		public NavigableMapGenerator(TestChar2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Char2ObjectNavigableMap<V> createSubMap(Char2ObjectSortedMap<V> sortedMap, char firstExclusive, char lastExclusive) {
	    	Char2ObjectNavigableMap<V> map = (Char2ObjectNavigableMap<V>) sortedMap;
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
	        return (Char2ObjectNavigableMap<V>) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator<V> extends MapGenerator<V> implements TestChar2ObjectSortedMapGenerator<V> {
		TestChar2ObjectSortedMapGenerator<V> parent;
		Bound to;
		Bound from;
		char firstInclusive;
		char lastInclusive;
		Comparator<Entry<V>> entryComparator;

		public SortedMapGenerator(TestChar2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Char2ObjectSortedMap<V> map = parent.create();
			entryComparator = DerivedChar2ObjectMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry<V>> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getCharKey();
			lastInclusive = samples.get(samples.size() - 1).getCharKey();
		}
		
		@Override
		public Char2ObjectSortedMap<V> create(Entry<V>... elements) {
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
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getCharKey(), parent.aboveSamplesLesser().getCharKey());
		}
		
		Char2ObjectSortedMap<V> createSubMap(Char2ObjectSortedMap<V> map, char firstExclusive, char lastExclusive) {
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
	
	public static class DescendingTestMapGenerator<V> extends MapGenerator<V> implements TestChar2ObjectSortedMapGenerator<V>
	{
		TestChar2ObjectSortedMapGenerator<V> parent;
		
		public DescendingTestMapGenerator(TestChar2ObjectSortedMapGenerator<V> parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Char2ObjectNavigableMap<V> create(Entry<V>... elements) {
			return ((Char2ObjectNavigableMap<V>)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Character, V>> order(List<Map.Entry<Character, V>> insertionOrder) {
			ObjectList<Map.Entry<Character, V>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator<V> implements TestChar2ObjectMapGenerator<V>
	{
		TestChar2ObjectMapGenerator<V> parent;
		
		public MapGenerator(TestChar2ObjectMapGenerator<V> parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Character, V>> order(List<Map.Entry<Character, V>> insertionOrder) {
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
		public Char2ObjectMap<V> create(Entry<V>... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator<V> implements TestObjectSetGenerator<Char2ObjectMap.Entry<V>> {
		TestChar2ObjectMapGenerator<V> generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Character, V>, Map.Entry<Character, V>> inner) {
			generator = (TestChar2ObjectMapGenerator<V>) inner.getInnerGenerator();
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
			return generator.create(elements).char2ObjectEntrySet();
		}
	}

	public static <V> TestCharSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Character, V>, Map.Entry<Character, V>> inner) {
		if (inner.getInnerGenerator() instanceof TestChar2ObjectSortedMapGenerator) {
			CharSet set = ((TestChar2ObjectSortedMapGenerator<V>) inner.getInnerGenerator()).create().keySet();
			if(set instanceof CharNavigableSet) return new MapNavigableKeySetGenerator<>(inner);
			if(set instanceof CharSortedSet) return new MapSortedKeySetGenerator<>(inner);
		}
		return new MapKeySetGenerator<>(inner);
	}
	
	public static class MapNavigableKeySetGenerator<V> extends MapSortedKeySetGenerator<V> implements TestCharNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, V>, Map.Entry<Character, V>> inner) {
			super(inner);
		}
		
		@Override
		public CharNavigableSet create(char... elements) {
			return (CharNavigableSet) super.create(elements);
		}

		@Override
		public CharNavigableSet create(Object... elements) {
			return (CharNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator<V> extends MapKeySetGenerator<V> implements TestCharSortedSetGenerator {
		TestChar2ObjectSortedMapGenerator<V> generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, V>, Map.Entry<Character, V>> inner) {
			super(inner);
			generator = (TestChar2ObjectSortedMapGenerator<V>) inner.getInnerGenerator();
		}

		@Override
		public CharSortedSet create(char... elements) {
			return (CharSortedSet) super.create(elements);
		}

		@Override
		public CharSortedSet create(Object... elements) {
			return (CharSortedSet) super.create(elements);
		}

		@Override
		public char belowSamplesLesser() {
			return generator.belowSamplesLesser().getCharKey();
		}

		@Override
		public char belowSamplesGreater() {
			return generator.belowSamplesGreater().getCharKey();
		}

		@Override
		public char aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getCharKey();
		}

		@Override
		public char aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getCharKey();
		}
	}

	public static class MapKeySetGenerator<V> implements TestCharSetGenerator {
		TestChar2ObjectMapGenerator<V> generator;
		CharSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, V>, Map.Entry<Character, V>> inner) {
			generator = (TestChar2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Char2ObjectMap.Entry<V>> samples = generator.getSamples();
			this.samples = new CharSamples(samples.e0().getCharKey(), samples.e1().getCharKey(), samples.e2().getCharKey(), samples.e3().getCharKey(), samples.e4().getCharKey());
		}

		@Override
		public CharSamples getSamples() {
			return samples;
		}

		@Override
		public CharIterable order(CharList insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (char key : insertionOrder) {
				entries.add(new AbstractChar2ObjectMap.BasicEntry<>(key, value));
			}
			CharList list = new CharArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getCharKey());
			}
			return list;
		}

		@Override
		public Iterable<Character> order(List<Character> insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (char key : insertionOrder) {
				entries.add(new AbstractChar2ObjectMap.BasicEntry<>(key, value));
			}
			CharList list = new CharArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getCharKey());
			}
			return list;
		}
		
		@Override
		public CharSet create(char... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2ObjectMap.BasicEntry<>(elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public CharSet create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2ObjectMap.BasicEntry<>((Character) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator<V> implements TestObjectCollectionGenerator<V> {
		TestChar2ObjectMapGenerator<V> generator;
		ObjectSamples<V> samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Character, V>, Map.Entry<Character, V>> inner) {
			generator = (TestChar2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Char2ObjectMap.Entry<V>> samples = generator.getSamples();
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
					throw new IllegalArgumentException("Char2ObjectMap.values generator can order only sample values");
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
					throw new IllegalArgumentException("Char2ObjectMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ObjectCollection<V> create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2ObjectMap.BasicEntry<>(entry.getKey(), (V)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static<V> Comparator<Map.Entry<Character, V>> entryObjectComparator(Comparator<Character> keyComparator) {
		return new Comparator<Map.Entry<Character, V>>() {
			@Override
			public int compare(Map.Entry<Character, V> a, Map.Entry<Character, V> b) {
				if(keyComparator == null) {
					return Character.compare(a.getKey().charValue(), b.getKey().charValue());
				}
				return keyComparator.compare(a.getKey().charValue(), b.getKey().charValue());
			}
		};
	}
	
	public static<V> Comparator<Entry<V>> entryComparator(CharComparator keyComparator) {
		return new Comparator<Entry<V>>() {
			@Override
			public int compare(Entry<V> a, Entry<V> b) {
				if(keyComparator == null) {
					return Character.compare(a.getCharKey(), b.getCharKey());
				}
				return keyComparator.compare(a.getCharKey(), b.getCharKey());
			}
		};
	}
}