This repository aims at showing the effectiveness of functional programming techniques 
in the development of common web-based applications, such as meetup.com.

The project includes a functional implementation of a typical microservice (actually, two implementations: one using scalaz, and the other implemented from scratch), and another one using object-oriented patterns. 

The branch [slick 3.0](https://github.com/hablapps/meetapp/tree/slick_3.0) includes a refactoring of both solutions when the database layer is migrated to slick 3.0.

The branch [reified-flatmap](https://github.com/hablapps/meetapp/tree/reified-flatmap) offers an alternative implementation of the `StoreProgram` monad, more in line with the implementation of Free monads that you'll find in scalaz/cats.

Some presentations based upon this code repository: 

* [Lambda.World](http://lambda.world). [Slides](https://docs.google.com/presentation/d/1RsCnD7tVOxlCrhpqbdmqpksHs7vHstpgVxEIxvSnl1Y/edit?usp=sharing)

* [Madrid Scala Meetup](http://www.meetup.com/Scala-Programming-Madrid/). [Slides](https://docs.google.com/presentation/d/172dThBWx8Y5pyLJn0zYKpF9gzU3NOkVmHw04BR4aTKg/edit?usp=sharing). The same presentation, but for a few jokes that, most probably, you can only understand if you live in Spain.