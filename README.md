![build](https://github.com/Speiger/Primitive-Collections/actions/workflows/build_validator.yml/badge.svg)
[![Latest Release](https://jitpack.io/v/Speiger/Primitive-Collections.svg)](https://jitpack.io/#Speiger/Primitive-Collections)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/Speiger/Primitive-Collections)
[![codecov](https://codecov.io/gh/Speiger/Primitive-Collections/branch/debug/graph/badge.svg?token=WSTSNJM0EN)](https://codecov.io/gh/Speiger/Primitive-Collections)
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
Using Jitpack Gradle
```gradle
repositories {
    maven {
        url = "https://jitpack.io"
    }
}
dependencies {
	implementation 'com.github.Speiger:Primitive-Collections:0.7.0'
}
```

# SourceCode
The generated Sourcecode can be automatically build,    
but if you want to just browse around in it.    
Check out the [Debug Branch](https://github.com/Speiger/Primitive-Collections/tree/debug/src/main/java/speiger/src/collections), which has the entire up to date code.

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

to generate SourceCode and build the jar:           
/gradlew.bat build      