![build](https://github.com/Speiger/Primitive-Collections/actions/workflows/build_validator.yml/badge.svg)
[![Latest Release](https://jitpack.io/v/Speiger/Primitive-Collections.svg)](https://jitpack.io/#Speiger/Primitive-Collections)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/Speiger/Primitive-Collections)
# Primitive-Collections
This is a Simple Primitive Collections Library aimed to outperform Java's Collection Library and FastUtil.  
Both in Performance and Quality of Life Features.  

## Benchmarks
Benchmarks can be found here: [[Charts]](https://github.com/Speiger/Primitive-Collections-Benchmarks/blob/master/BENCHMARKS-CHARTS.md), [[Tables]](https://github.com/Speiger/Primitive-Collections-Benchmarks/blob/master/BENCHMARKS.md)

## Special Features
[Here](features.md) you find a set of features added to Primitive Collections.   
These are designed to improve performance or to provide Quality of Life.  

## Main Features:      
- ArrayLists / LinkedLists / CopyOnWriteLists
- HashSets/Maps (Linked & HashControl)
- TreeSets/Maps (RB & AVL)
- EnumMaps
- Immutable Maps/Lists/Sets
- ConcurrentHashMaps
- Priority Queues
- Streams & Functional Queries
- Split/Iterators
- Pairs
- Unary/Functions
- Suppliers
- Bi/Consumers
- AsyncBuilders  

# Notes about Versions
Any 0.x.0 version (Minor) can be reason for massive changes including API.     
To ensure that problems can be dealt with even if it is breaking the current API.     

# How to install
Using Gradle:
```gradle
repositories {
    maven {
        url = "https://maven.speiger.com/repository/main"
    }
}
dependencies {
	implementation 'de.speiger:Primitive-Collections:0.6.0'
}
```
Using Jitpack Gradle
```gradle
repositories {
    maven {
        url = "https://jitpack.io"
    }
}
dependencies {
	implementation 'com.github.Speiger:Primitive-Collections:0.6.0'
}
```

<details>
<summary>Direct: </summary>
<p>

| Version 	| Jar                                                                                                                          	| Sources                                                                                                                              	| Java Doc                                                                                                                             	|
|---------	|------------------------------------------------------------------------------------------------------------------------------	|--------------------------------------------------------------------------------------------------------------------------------------	|--------------------------------------------------------------------------------------------------------------------------------------	|
| 0.6.0   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.6.0/Primitive-Collections-0.6.0.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.6.0/Primitive-Collections-0.6.0-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.6.0/Primitive-Collections-0.6.0-javadoc.jar) 	|
| 0.5.3   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.3/Primitive-Collections-0.5.3.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.3/Primitive-Collections-0.5.3-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.3/Primitive-Collections-0.5.3-javadoc.jar) 	|
| 0.5.2   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.2/Primitive-Collections-0.5.2.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.2/Primitive-Collections-0.5.2-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.2/Primitive-Collections-0.5.2-javadoc.jar) 	|
| 0.5.1   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.1/Primitive-Collections-0.5.1.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.1/Primitive-Collections-0.5.1-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.1/Primitive-Collections-0.5.1-javadoc.jar) 	|
| 0.5.0   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.0/Primitive-Collections-0.5.0.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.0/Primitive-Collections-0.5.0-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.5.0/Primitive-Collections-0.5.0-javadoc.jar) 	|
| 0.4.5   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.5/Primitive-Collections-0.4.5.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.5/Primitive-Collections-0.4.5-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.5/Primitive-Collections-0.4.5-javadoc.jar) 	|
| 0.4.4   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.4/Primitive-Collections-0.4.4.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.4/Primitive-Collections-0.4.4-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.4/Primitive-Collections-0.4.4-javadoc.jar) 	|
| 0.4.3   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.3/Primitive-Collections-0.4.3.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.3/Primitive-Collections-0.4.3-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.3/Primitive-Collections-0.4.3-javadoc.jar) 	|
| 0.4.2   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.2/Primitive-Collections-0.4.2.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.2/Primitive-Collections-0.4.2-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.2/Primitive-Collections-0.4.2-javadoc.jar) 	|
| 0.4.1   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.1/Primitive-Collections-0.4.1.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.1/Primitive-Collections-0.4.1-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.1/Primitive-Collections-0.4.1-javadoc.jar) 	|
| 0.4.0   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.0/Primitive-Collections-0.4.0.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.0/Primitive-Collections-0.4.0-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.4.0/Primitive-Collections-0.4.0-javadoc.jar) 	|
| 0.3.6   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.6/Primitive-Collections-0.3.6.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.6/Primitive-Collections-0.3.6-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.6/Primitive-Collections-0.3.6-javadoc.jar) 	|

</p>
</details>

# Contributing
If you want to contribute.      
This project is created using gradle and java and my Template Library only. Nothing extra.      
If you setup gradle the library will be downloaded automatically.      

Where is everything stored?
- Variables and ClassNames are define [here](src/builder/java/speiger/src/builder/GlobalVariables.java)
- Templates are stored [here](src/builder/resources/speiger/assets/collections/templates)
- Tests can be found [here](src/test/java/speiger/src/collections)

Please if you want to contribute follow the [Rule-Sheet](RuleSheet.md). It keeps everything in line.


# How to Build

The SourceCode can be generated via:     
/gradlew.bat generateSource      

to build the jar:           
/gradlew.bat build      
do not combine the commands because they can not be executed at the same time.      
