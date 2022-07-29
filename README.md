# JLayer - The Java Implementation of the Layer Concept

The [**JLayer framework**](http://www.jlayer.org/) is an implementation of the **layer concept** - see [background](http://www.jlayer.org/background.html) - and essentially extends the access options to arrays consisting of object elements. This simplifies the programming of such arrays. 

## The basic idea

Let's start with a look at the existing access options. For an array `arr` consisting of objects of type `Unit`, which in turn contains a field `x` and a method `f()`, there are short terms for accessing the collection of all objects as well as for accessing individual objects and their components: `arr, arr[i], arr[i].x, arr[i].f()`.

But there are no short terms for accessing the entireties of all fields `x` or methods `f()` of this array. And this is where the JLayer framework comes into play. It provides a Java annotation processor, and if the type `Unit` mentioned above is annotated appropiately, a wrapper class for the array `arr` is generated, so that the wrapped array `warr` can be used to access these entireties with the "lifted dot notation" `warr.x` and `warr.f()`. We name such entities **field layers** or **method layers**, respectively. 

## How to use the JLayer Framework?

With the help of **JLayer annotations**, programming tasks that involve the networking of object arrays are solved as follows.

- To cope for networking aspects, the classes that describe the core functionality of the objects, are equipped with JLayer annotations. Such classes are called **unit classes**.
- The **wrapper classes** generated from unit classes then support the establishment of global structures and dynamics.

## What are the advantages?

Specifically, with the help of relations over indices the object elements of arrays or the elements of field layers, respectively, can be linked easily. In addition, when method layers are called, there are flexible options for passing parameters. In many cases this leads to compact and concise programming code.

Lust but not least, the execution of method layers can be done either in a loop or in parallel, where the parallel implementation tries to use all available processor cores. 

## The JLayer Website

Further details and examples can be found on the website [www.jlayer.org](http://www.jlayer.org/).
