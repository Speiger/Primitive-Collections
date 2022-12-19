### Extra Features

Primitive Collections comes with a few extra features that are disabled by default.    
These will be enabled as soon they become relevant or never at all.

But some of these can be already unlocked when the target version changes.    

If you compile the library for yourself you will automatically gain access to said features.

### Java17 Exclusive Features    
Java17 has some new features that can sadly not really be back-ported but the library still supports them if it is compiled with java17
 
- RandomGenerator: Java17 has added [RandomGenerator.class](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/random/RandomGenerator.html).
  This allows to use custom random implementations without having to re-implement them yourselves.


### ModuleSettings
Primitive Collections is a huge library.   
But maybe you only use like 5-10 different classes.   
Normally you would use tools like "Proguard" to get rid of classes that you don't use.    
But since a lot of classes have dependencies on each other this would only do so much.    

This is where the [ModuleSettings](ModuleSettings.json) come into play.   
It allows you to turn of implementations as you wish and adjusts the code that everything still works.   

There is 3 layers of control inside of the ModuleSettings.   
- Modules directly at the top that turn off everything.   
- Type Specific configurations, where you can for example turn of everything thats "Long" based.   
- And then there is each type specific module settings.   

Allowing for greater control without having to edit hundreds of lines of code.   
On top of that:   
Any Setting that isn't "Present" is counted as "Enabled".    
So if you want to disable just 1 thing you can keep that 1 thing and delete the rest of the Setting.    
It will still work as the same.    
The default settings just come with everything so you can see what is controllable.   

How to compile the Code with the ModuleSettings enabled:
```
/gradlew.bat generateLimitSource build -x test
```
