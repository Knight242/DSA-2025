Assignment 1
Jossian Garcia
9/20/25

What features do you like about Kotlin?
The user interface of Kotlin is very easy to navigate and is pretty similar to VSCode which I admire. I also like how intuitive the debugger is in Kotlin as I was able to cypher what was going wrong with my code pretty easily as compared to the debugger in Python which I remember having trouble using. 
Are there things you were expecting to find that you haven’t?
Not particularly, I hope to get more experience with Kotlin. 
What questions do you have?
My main concern with Kotlin is how to setup up unit tests in Kotlin (I had a good amount of trouble trying to get it to work). 

Try using the debugger (see the Getting Set with Kotlin page) for some very basic information on the debugger. Do you have experience using interactive debuggers like this one? Were you able to successfully launch the debugger?

I have experience with the Python debugger that I used quite a bit during SoftDes and C++ debuggers for other projects but I feel that the Kotlin debugger was pretty easy to successfully launch and use during this assignment. 

Code Structure
Mad Libs Generator:
Users can input words corresponding to parts of speech which are then inserted into a story template. Working with maps, strings, and user input

Recursive Functions:
Exercises like isPalindrome, factorial, and power allow practice with breaking problems down into base and recursive cases

Higher-Order Functions:
Functions like compose and makeSalesTax shows how functions can be returned and used as first-class objects

Search Algorithms:
The linear and binary search provided practice with iterative and recursive problem-solving techniques (pretty similar to Python)

Unit Testing:
All functions are validated using unit tests to make sure everything is working correctly; floating point and numeraric operations
Reflection on Translating Python to Kotlin

Translating the original Python code to Kotlin: 

1. Kotlin is statically typed all function parameters and return types needed to be explicitly defined

2. Collections: Python dictionaries were translated to MutableMap() in Kotlin. Lists where referred to as List<T> or MutableList<T> when needed. 

3. Recursion: Python recursive functions were translated almost perfectly to Kotlin, just with some small differences with Kotlin’s substring and list operations (drop(1)).

Function Composition: Python inner functions were translated to Kotlin lambda expressions.

Unit Testing: Instead of printing results, Kotlin uses JUnit 5 with assertions to verify the code. 

Overall, the translation process helped reinforce understanding of Kotlin syntax, type safety, functional programming concepts, and testing practices. For the most part, the underlying logic of the code stays pretty consistent which I am satisfied with. 