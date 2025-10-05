# Import Global Asset Classification and Valitility Job

## Job Description

This job is used to process the Global Asset Classification json file from BlackRock, load data, transform and then store in a product attribute "gac".

## Reference page
https://wpb-confluence.systems.uk.dummy/display/WWS/AMH+Global+Asset+Classification+Solution

## Step 

### Reader
1. Read the batch incoming path configuration and scan files whose file names match this format:
   Begin with "RBW"
2. Parser json string base on "SECURITY"

### Processor
Acquire and transform gac data.

### Writer
Update gac data to mongoDB base on productId.