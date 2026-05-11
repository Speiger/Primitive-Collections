package speiger.src.collections.booleans.functions;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class OptionalBoolean {
	private static final OptionalBoolean EMPTY = new OptionalBoolean();
	
	private final boolean isPresent;
	private final boolean value;
	
	private OptionalBoolean() {
		this.isPresent = false;
		this.value = false;
	}
	
	private OptionalBoolean(boolean value) {
		this.isPresent = true;
		this.value = value;
	}
	
	public static OptionalBoolean empty() {
		return EMPTY;
	}
	
	public static OptionalBoolean of(boolean value) {
		return new OptionalBoolean(value);
	}
	
	public boolean getAsBoolean() {
		if(!isPresent) throw new NoSuchElementException("No value present");
		return value;
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	
	public boolean isEmpty() {
		return !isPresent;
	}
	
	public void ifPresent(BooleanConsumer consumer) {
		if(isPresent) consumer.accept(value);
	}
	
    public void ifPresentOrElse(BooleanConsumer action, Runnable emptyAction) {
        if (isPresent) action.accept(value);
        else emptyAction.run();
    }
	
	public boolean orElse(boolean other) {
		return isPresent ? value : other;
	}
	
	public boolean orElseGet(BooleanSupplier other) {
		return isPresent ? value : other.getAsBoolean();
	}
	
	public boolean orElseThrow() {
        if (!isPresent) throw new NoSuchElementException("No value present");
		return value;
	}
	
    public <X extends Throwable> boolean orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    	if(isPresent) return value;
    	else throw exceptionSupplier.get();
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(obj == this) return true;
    	if(obj instanceof OptionalBoolean) {
    		OptionalBoolean other = (OptionalBoolean)obj;
    		return (isPresent && other.isPresent ? value == other.value : isPresent == other.isPresent);
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return isPresent ? Boolean.hashCode(value) : 0;
    }
    
    @Override
    public String toString() {
    	return isPresent ? "OptionalBoolean["+value+"]" : "OptionalBoolean.empty";
    }
}