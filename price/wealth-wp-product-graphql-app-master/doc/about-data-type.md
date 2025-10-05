# About data type

Data type in MongoDB, Json Shcema, GraphQL Schema and Java are different. 

## MongoDB Data Types

MongoDB uses BSON type which is a super set of JSON, it supports much more types.

## Json Schema Data Types

## GraphQL Schema Data Types

GraphQL comes with a set of default scalar types out of the box:

- Int: A signed 32‐bit integer.
- Float: A signed double-precision floating-point value.
- String: A UTF‐8 character sequence.
- Boolean: true or false.
- ID: The ID scalar type represents a unique identifier, often used to refetch an object or as the key for a cache. The ID type is serialized in the same way as a String; however, defining it as an ID signifies that it is not intended to be human‐readable.

In most GraphQL service implementations, there is also a way to specify custom scalar types. For example, we defined custom types below:

- Date: ISO Date
- DateTime: ISO DateTime
- JSON: Object like, any key - value pairs

Other than these, there is also object, and enum.

## Java Data Types

## Mapping table

MongoDB | Json Schema | GraphQL Schema | Java
-|-|-|-
