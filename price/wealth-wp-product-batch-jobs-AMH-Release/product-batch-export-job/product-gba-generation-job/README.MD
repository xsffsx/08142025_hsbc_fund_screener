## Generate GBA interface file job

## Job Description

Generate a file for GBA.

This job need three paramters: ctryRecCde, grpMembrRecCde, prodTypeCde.

### Process

1. Filter products that match these conditions:

```json
{
  "ctryRecCde": "${ctryRecCde}",
  "grpMembrRecCde": "${grpMembrRecCde}",
  "prodTypeCde": "${prodTypeCde}",
  "prodStatCde": {
    "$in": [
      "P",
      "A",
      "T",
      "C"
    ]
  }
}
````
2. Take prodAltPrimNum, gbaAcctTrdb, gnrAcctTrdb from product, and union into string. Finally export the string to the file: ${ctryRecCde}${grpMembrRecCde}GBA${prodTypeCde}${date}.txt. 
