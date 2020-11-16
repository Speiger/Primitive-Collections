# Rule Sheet

This file documents all of the formatting rules that will exist in the Library,
to ensure consistency in the Library.

Some Base Rules:

- Packages should be sorted to approbate folders. To Ensure fast finding of classes and not running through hundreds of classes in 1 package.
- Class Header braces always start in new line to help with if a if is needed in the class header.
- Otherwise braces should be in the same line as a function name.
- If a function could be done in a single line it should be.
- Function overloads should be avoided. They are only causing problems.
- If a primitive function would cause a overload then a rename should try to add a shortened primitive name in it. If that destroys the naming flow then a cut version name. If that does not work a new rule has to be made up. Example: "get -> getInt" while "removeIf => removeIfInt" does not work so we default to "removeIf => remIf".
- If a function would break the java interface rules (ArrayList.trimToSize) a dedicated interface should be created for it.
- If a #if is wrapping a function the #if always starts after a empty new line and the #endif always has a empty new line before itself. That way empty lines match up no matter the type

Rules can be added or altered, but should only be so if there is a real reason to be present.