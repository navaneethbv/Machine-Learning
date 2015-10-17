dataset <- read.csv("https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data")
library("class")

n <- nrow(dataset)

kVal=c(3,5,7,9,11)
for(j in 1:5){
  sum=0.00;
for(i in 1:10){
  trainRow <- sort(sample(1:n, floor(n*0.9)))
  dataset.train <- dataset[trainRow,]
  dataset.test <- dataset[-trainRow,]
  pred <- knn(dataset.train[,-9], dataset.test[,-9], dataset.train[,9], k = kVal[j], prob=TRUE)
  accuracy <- (mean(pred == t(dataset.test[9])))*100
  sum=sum+accuracy
}
overallAcc <- sum/10
print (kVal[j])
print(overallAcc)

}

