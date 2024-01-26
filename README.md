# JLayer - The Java Implementation of the Layer Concept

The [**JLayer framework**](http://www.jlayer.org/) is an implementation of the layer concept - see [background](http://www.jlayer.org/background.html) - and essentially extends the access options to arrays consisting of object elements.

In a sense, the dot notation that allows access to the components of individual objects is elevated to the array level, and in this way programming tasks involving layers of linked objects can be expressed elegantly and compactly. In particular, this is because the description of the core functionality of the corresponding objects can be clearly separated from the description of the linking structure, the latter being easily expressed by mathematical relations over indices.

With regard to the method call, a side effect of the above-mentioned raising of the dot notation to the array level is that methods can optionally be executed in a loop or, if they are semantically suitable for this, in parallel. 

## The basic idea

Let's start with a look at the existing access options. For an array `arr` consisting of objects of type `Unit`, which in turn contains a field `x` and a method `f()`, there are short terms for accessing the collection of all objects as well as for accessing individual objects and their components: `arr, arr[i], arr[i].x, arr[i].f()`.

But there are no short terms for accessing the entireties of all fields `x` or methods `f()` of this array. And this is where the JLayer framework comes into play. It provides a Java annotation processor, and if the type `Unit` mentioned above is annotated appropiately, a wrapper class for the array `arr` is generated, so that the wrapped array `warr` can be used to access these entireties with the "elevated dot notation" `warr.x` and `warr.f()`. We name the wrapped array `warr` itself a **based layer** or **layer base** and refer to the entities `warr.x` and `warr.f()` as **field layer** or **method layer**, respectively. 

## How to use the JLayer Framework?

The use of the **JLayer Framework** is recommended if the local core functionality of objects can be clearly separated from their linking structure and global functionality. There are then two steps.

- The classes of the core objects to be linked are suitably provided with layer annotations. These classes are called **unit classes** and they lead to the automatic generation of the **wrapper classes**. 

- The wrapper classes then support the creation of global structures and dynamics. In particular, the desired network structure can be created using mathematical relations over indices. 

## What are the advantages?

The clear separation between the description of the local core functionality on the one hand and the linking structure and global functionality on the other leads to compact and concise programming code in many cases.

This is due not only to the fact that object layers can be linked with mathematical relations, but also to the fact that calling method layers allows flexible options for passing parameters.

Last but not least, method layers can be executed either in a loop or in parallel, whereby the parallel implementation attempts to utilise all available processor cores. 

## The JLayer Website

Further details and examples can be found on the website [www.jlayer.org](http://www.jlayer.org/).
