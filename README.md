# ibp-sweetpotato-traits

Sweetpotato Trait Ontology is maintained by http://www.cropontology.org 
and https://sweetpotatobase.org

Request new traits/variables by submitting a new issue in this github repo, 
or by filling the trait request form http://submit.rtbbase.org


Focal point for requesting new terms: CIP

The following conventions are being used:

1. ‘Sweetpotato’ is written as one word following CIP style guide
2. When requesting a new trait it should include detailed method and scale 
3. Variables are pre-composed of the trait, method, and scale
4. Trait abbreviation should be a synonym, and is a 5-6 letter abbreviation for Object abbreviation as: [a-zA-Z]{3} plus attribute abbreviation as: [a-zA-Z0-9]{2-3}; so total: [a-zA-Z]{3}[a-zA-Z0-9]{2-3}
5. Variable short synonym is name composed of Trait abbreviation + Method class abbreviation + Scale abbreviation following the IBP format and this regular expression: '[a-zA-Z]{3}[a-zA-Z0-9]{2-3}[\_]{1}[A-Z]{1}{a-z}{1}[\_]{1}[a-zA-Z0-9]{1,12}'
6. Scale abbreviations must not contain special characters (as in regular expression above):
7. Special scale abbreviations: categories with 1:9 should be as: 1to9; % as pct
 
8. Extra columns from cropontology spreadsheet format are mapped to the comment field in the following format: "| 'column name': 'content'". The last pair is followed by a point to comply with the OBO format definition. For the moment no check is done if a comment already exists since there is no comment field in the spreadsheet.
9. The columns 'Curation' and 'Crop' are currently ignored. 'Curation' could be added but unclear to what term level (trait, variable, method, scale?). 'Crop' column content is implied.
10. Formula must not use 'x' to denote multiplication but '*' since everything starting with a letter is interpreted as a variable name for cross-referencing.


The following observations:

1. Identifiers in xrefs must be using only English alphabet characters (no extended characters allowed by OBO-editor)
2. The list of variables corresponds currenlty to those commonly used in sweetpotato trials. It will be expanded shortly to a larger list of 175+ variables.

TODOs in curation:

1. Cross referencing to other ontologies
2. Clearer separation of trait, method and variable definitions.


