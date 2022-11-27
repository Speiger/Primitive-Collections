package speiger.src.builder.modules;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import speiger.src.builder.ClassType;
import speiger.src.builder.mappers.IMapper;
import speiger.src.builder.processor.TemplateProcess;

@SuppressWarnings("javadoc")
public class ModulePackage
{
	public static final ClassType[] TYPE = ClassType.values();
	final ClassType keyType;
	final ClassType valueType;
	Set<String> blocked = new HashSet<>();
	Map<String, String> nameRemapper = new HashMap<>();
	Map<String, String> splitters = new HashMap<>();
	List<Predicate<String>> blockedFilters = new ArrayList<>();
	List<IMapper> mappers = new ArrayList<>();
	Set<String> flags = new LinkedHashSet<>();
	BiConsumer<String, RequiredType> requirements = (K, V) -> {};
	
	public ModulePackage(ClassType keyType, ClassType valueType) {
		this.keyType = keyType;
		this.valueType = valueType;
	}
	
	public void finish() {
		mappers.sort(Comparator.comparing(IMapper::getSearchValue, this::sort));
	}
	
	public void setRequirements(BiConsumer<String, RequiredType> requirements) {
		this.requirements = requirements;
	}
	
	public boolean isSame() {
		return keyType == valueType;
	}
	
	public boolean isEnumValid() {
		return keyType == ClassType.OBJECT;
	}
	
	public ClassType getKeyType() {
		return keyType;
	}
	
	public ClassType getValueType() {
		return valueType;
	}
	
	public void addFlag(String flag) {
		flags.add(flag);
	}
	
	public void addRequirement(String fileName, RequiredType type) {
		requirements.accept(fileName, type);
	}
	
	public void addMapper(IMapper mapper) {
		mappers.add(mapper);
	}
	
	public void addBlockedFilter(Predicate<String> filter) {
		blockedFilters.add(filter);
	}
	
	public void addBlockedFile(String name) {
		blocked.add(name);
	}
	
	public void addSplitter(String fileName, String splitter) {
		splitters.put(fileName, splitter);
	}
	
	public void addRemapper(String fileName, String actualName) {
		nameRemapper.put(fileName, actualName);
	}
	
	public void process(String fileName, Consumer<TemplateProcess> result) {
		if(isBlocked(fileName)) return;
		String splitter = String.format(splitters.getOrDefault(fileName, keyType.getFileType()), keyType.getFileType(), valueType.getFileType());
		String newName = String.format(nameRemapper.getOrDefault(fileName, "%s"+fileName), splitter);
		TemplateProcess process = new TemplateProcess(newName);
		process.setPathBuilder(new PathBuilder(keyType.getPathType()));
		process.addFlags(flags);
		process.addMappers(mappers);
		result.accept(process);
	}
	
	private boolean isBlocked(String fileName) {
		if(blocked.contains(fileName)) return true;
		for(int i = 0,m=blockedFilters.size();i<m;i++) {
			if(blockedFilters.get(i).test(fileName)) return true;
		}
		return false;
	}
	
	public static List<ModulePackage> createPackages() {
		List<ModulePackage> list = new ArrayList<>();
		for(ClassType key : TYPE) {
			for(ClassType value : TYPE) {
				list.add(new ModulePackage(key, value));
			}
		}
		return list;
	}
	
	private int sort(String key, String value) {
		if(key.equals(value)) return 0;
		if(value.contains(key)) return 1;
		else if(key.contains(value)) return -1;
		return 0;
	}
	
	class PathBuilder implements UnaryOperator<Path> {
		String before;
		
		public PathBuilder(String before) {
			this.before = before;
		}
		
		@Override
		public Path apply(Path t) {
			return t.subpath(0, 6).resolve(before).resolve(t.subpath(6, t.getNameCount()));
		}
	}
}
