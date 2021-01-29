# Primitive-Collections (To be Renamed)

This is a Simple Primitive Collections Library i started as a hobby Project.     
It is based on Java's Collection Library and FastUtil.     
But its focus is a different one.     

Main Features:     
ArraysList, HashSet/Map (Linked & HashControl), TreeSet/Map (RB & AVL), Priority Queue.     

# Guide

The SourceCode can be already generated via: 
/gradlew.bat generateSource      
to build the jar     
/gradlew.bat build    
do not combine the commands because they can not be executed at the same time.    

Current Problem:     
EnumMaps use sun.misc package.     
Some overhauls or missing implementations have to be added. (A couple passes)     
But technically Lists/Sets/PriorityQueues are fully usable, Maps are untested and just recently added.     

Documentation & Tests are going to be added very soon.