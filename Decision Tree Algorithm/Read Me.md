This project has been prepared by -
Navaneeth Venugopala Rao
Net ID - nbv140130

1. To execute the code in Linux, following the following steps.
a. Copy the Java files from the Code folder into your workspace.
b. Run this command from your workspace --> $ javac Solution.java
c. Check if the class files are created.
d. Next run the following command -> 
$ java Solution 20 200 /home/data_sets1/training_set.csv /home/data_sets1/validation_set.csv /home/data_sets1/test_set.csv yes
5. This will print the accuracy percentage along with the model.
6. If you want to just print the percentage without the model, run this command -
$ java Solution 20 200 /home/data_sets1/training_set.csv /home/data_sets1/validation_set.csv /home/data_sets1/test_set.csv no
7. You can change the values of K and L with any values you want.
8. Also note that the path I have specified is an example. While running the problem, please provide the correct path.
9. If you want to run data set 2, just change the path.
10. The sample outputs have been provided in both the Report and the Output.txt file.
11. If the user wants to test the application in Eclipse, import the folder Backup into your eclipse and run Solution.java file. Go to Run Configuration and provide the arguments in a similar format as shown above.(Note the datasets have already been added to the Backup folder, so need to provide the absolute path. Also eclipse uses quotes while provide arguments for files and to print yes or no)
12. Note - I have provided a backup of the entire code directory, just in case.

Example Test Cases -
1. java Solution 20 200 /home/data_sets1/training_set.csv /home/data_sets1/validation_set.csv /home/data_sets1/test_set.csv yes
2. java Solution 20 40 /home/data_sets1/training_set.csv /home/data_sets1/validation_set.csv /home/data_sets1/test_set.csv yes
3. java Solution 10 20 /home/data_sets1/training_set.csv /home/data_sets1/validation_set.csv /home/data_sets1/test_set.csv yes
