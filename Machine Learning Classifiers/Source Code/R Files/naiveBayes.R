dataset <- read.csv("https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data")
library("e1071")
n <- nrow(dataset)
sum=0.00;
for(i in 1:10){
trainRow <- sort(sample(1:n, floor(n*0.9)))
dataset.train <- dataset[trainRow,]
dataset.test <- dataset[-trainRow,]
m <- naiveBayes(as.factor(X1) ~ ., data = dataset.train, type="raw")
pred <- predict(m, dataset.test[,-9])
accuracy <- (mean(pred == t(dataset.test[9])))*100;
print(accuracy)
sum=sum+accuracy
}
overallAcc <- sum/10;

print(overallAcc)