# UVL Generator

### Dependencies
This UVL generator depends on [uvl-smt](https://github.com/SoftVarE-Group/uvl-smt) for reasoning and [java-fm-metamodel](https://github.com/Universal-Variability-Language/java-fm-metamodel/tree/refactoring_metamodel) for managing the model objects.

UVL-SMT depends on z3 binaries which need to be correctly linked when running the generator. Check their [README]([uvl-smt](https://github.com/SoftVarE-Group/uvl-smt/README.md)) for instructions.

### Usage
The generator expects a path to a config.json file as input. You can find examples for usage in `input_examples`.
The main method can be found in the `Runner.java` class.

The .json contains various configuration options that enable the user to control structural properties of the generated feature models.
The example below generates 10 feature models with 500-600 features and 50--100 constraints. 
Furthermore, the groups in the tree hierarchy are 10% optional, 20% mandatory and so on.
An attribute price is attached to features with a 50% chance and with values between 10 and 1000. 
```json
{
  "general" : {
    "numberModels" : 10,
    "seed" : 42,
    "ensureSAT" : true,
    "generatorVersion" : "0.1"
  },
  "tree" : {
    "features" : {
      "number" : [500,600],
      "distribution" : {
        "boolean" : 0.7,
        "integer" : 0.2,
        "real" : 0.1,
        "string" : 0
      },
      "cardinality" : {
        "min" : [1,2],
        "max" : 5,
        "attachProbability" : 0.2
      }
    },
    "maxTreeDepth" : 5,
    "groups" : {
      "distribution": {
        "optional" : 0.1,
        "mandatory" : 0.2,
        "alternative" : 0.5,
        "or" : 0.2,
        "groupCardinality" : 0
      }
    }
  },
  "constraints" : {
    "number" : [50,100],
    "ecr" : 0.8,
    "variablesPerConstraint" : [2,15],
    "distribution" : {
      "boolean" : 0.9,
      "numeric" : 0.07,
      "aggregate" : 0.03,
      "string" : 0
    }
  },
  "attributes" : [
    {
      "name" : "Price",
      "value" : [10,1000],
      "attachProbability" : 0.5,
      "useInConstraints" : true
    }
  ]
}
```
