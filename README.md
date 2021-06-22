# Primitive-Collections (To be Renamed)

This is a Simple Primitive Collections Library i started as a hobby Project.     
It is based on Java's Collection Library and FastUtil.     
But its focus is a different one.     

## Main Features:     
ArraysList, HashSet/Map (Linked & HashControl), TreeSet/Map (RB & AVL), Priority Queue.     


# How to install
Using Gradle:
```gradle
repositories {
    maven {
        url = "https://maven.speiger.com/repository/main"
    }
}
dependencies {
	compile 'de.speiger:Primitive-Collections:0.2.0'
}
```

# How to Build

The SourceCode can be already generated via: 
/gradlew.bat generateSource      
to build the jar     
/gradlew.bat build    
do not combine the commands because they can not be executed at the same time.    

## Current Down Sides (Random order)
- Testing for Sub Maps/Sets/Lists are only in a very basic way tested
- Documentation is only present at the lowest level for most cases and needs a typo fixing.