# Java-DOM-Parsing

## Programming language
Java

## Files used in project
- data/data.xml -> Input data supplied
The input data is kept in a simple format. It contains a top-level element called "data", underneath there are entries called "city", which can contain a variable number of attributes and child nodes. 

- operations/operations.xml -> All the operations need to be performed are determined.

 The operation elements contain five attributes:

 - name - The name to use for the output.
 - attrib - name of the attribute or child node to be evaluated.
 - type - Whether the selected attribute is a child node or an attribute.
 - func - The function to be evaluated, this can be "min", "max", "sum" or "average".
 - filter - A regular expression to be applied to the “name” element of the “city”. Only those “cities” that match the regular       expression should be included in the evaluation.
- solution/output.xml -> The result of the supplied input data file
  The output also consists of a top-level element "results" and below it individual "result" entries. Each “result” has an  attribute “name”, which is taken from the name of the corresponding “operation”. The text content of each "result" element is the result of the calculation.



