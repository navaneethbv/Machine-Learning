
1. To run the R programs, use the saved CSV file in the same path as rest of the R files or specify the absolute path.
2. Histograms and Bar Plots are generated in the form of PDF's.
3. Commands are as follows -  

Rscript Histogram.R pima-indians-diabetes.data.csv 
Rscript Bar_Plot.R pima-indians-diabetes.data.csv
Rscript Highest_Correlation.R pima-indians-diabetes.data.csv
Rscript Correlation.R pima-indians-diabetes.data.csv
Rscript naivebayes.R
Rscript knn.R 
Rscript SVM.R

Note: I have used the URL directly for nivebayes, knn and SVM. Histogram, Bar_Plot, Correlation and Highest_Correlation needs the csv file in the arguments.

4. The same format works for the rest of the R files.
5. Naive Bayesian and k Nearest Neighbor has been created using Python.
6. To run the Python code, install latest version of Python and type the file name -

knearestneighbor_3.py
knearestneighbor_3.py
knearestneighbor_3.py
knearestneighbor_3.py
knearestneighbor_3.py
Naive_Bayesian.py

Note: The csv file has to be placed in the same path as the Python file.

7. A report has been included as part of the files.

Note: The default SVM is the best classifier for this Dataset. Naïve Bayesian’s accuracy is close enough but it’s value deviates a lot and hence not considered.
