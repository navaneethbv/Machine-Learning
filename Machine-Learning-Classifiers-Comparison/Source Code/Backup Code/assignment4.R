args <- commandArgs(TRUE)
dataURL<-as.character(args[1])
header<-as.logical(args[2])
d<-read.csv(dataURL,header = header)
d[d=="?"] <- 0
classvariable <- as.integer(args[3])
d[,classvariable]<-as.factor(d[,classvariable])
levels(d[,classvariable]) <- c(0,1)
set.seed(123)
library(rpart)
library(e1071)
library(class)
library(adabag)
library(randomForest)
library(mlbench)
library(ada)
library("ipred")
library("MASS")
library("nnet")
options(warn=-1)
for(i in 1:10) {
  cat("Running sample ",i,"\n")
  sampleInstances<-sample(1:nrow(d),size = 0.9*nrow(d))
  trainingData<-d[sampleInstances,]
  testData<-d[-sampleInstances,]
  
  #Decision Tree
  fit  <- rpart( trainingData[,classvariable] ~ ., data = trainingData[,-classvariable],parms = list(split = 'information'),control = rpart.control(minsplit = 1,minbucket = 1),method = 'class')
  prntr <- prune(fit, cp = 0.01)
  calcaccuracy <- table(predict(prntr, testData,type = "class"),testData[,classvariable])
  accuracy <- ((calcaccuracy[1,1] + calcaccuracy[2,2]) / (calcaccuracy[1,1] + calcaccuracy[1,2] + calcaccuracy[2,1] + calcaccuracy[2,2]))
  accuracy <- accuracy*100
  cat("Method = Decision Tree"," Accuracy = ",accuracy,"\n")
  
  
  
  #SVM
  fit <- svm(trainingData[,classvariable] ~ ., data = trainingData[,-classvariable])
  calcaccuracy <- table(predict(fit, testData), testData[,classvariable])
  accuracy <- ((calcaccuracy[1,1] + calcaccuracy[2,2]) / (calcaccuracy[1,1] + calcaccuracy[1,2] + calcaccuracy[2,1] + calcaccuracy[2,2]))
  accuracy <- accuracy*100
  cat("Method = SVM"," and Accuracy = ",accuracy,"\n")
  
  
  #Naive Bayessian
  fit <- naiveBayes(trainingData[,classvariable] ~ ., data = trainingData[,-classvariable])
  calcaccuracy <- table(predict(fit, testData), testData[,classvariable])
  accuracy <- ((calcaccuracy[1,1] + calcaccuracy[2,2]) / (calcaccuracy[1,1] + calcaccuracy[1,2] + calcaccuracy[2,1] + calcaccuracy[2,2]))
  accuracy <- accuracy*100
  cat("Method = Naive Baye's"," and Accuracy = ",accuracy,"\n")
  
  
  #KNN
  fctrd <- factor(trainingData[,classvariable])
  fit <- knn(train = trainingData[,-classvariable], test = testData[,-classvariable], fctrd ,prob = TRUE)
  calcaccuracy <- table(fit, testData[,classvariable])
  accuracy <- ((calcaccuracy[1,1] + calcaccuracy[2,2]) / (calcaccuracy[1,1] + calcaccuracy[1,2] + calcaccuracy[2,1] + calcaccuracy[2,2]))
  accuracy <- accuracy*100
  cat("Method = KNN"," and Accuracy = ",accuracy,"\n")
  
  
  #Logistic Regression
  fit <- glm(trainingData[,classvariable] ~ .,  data = trainingData[,-classvariable], family = "binomial")
  doprediction<-predict(fit, newdata=testData, type="response")
  threshold=0.65
  prdvalue<-sapply(doprediction, FUN=function(x) if (x>threshold) 1 else 0)
  testlength <- testData[,classvariable]
  accuracy <- sum(testlength == prdvalue) / length(testlength)
  accuracy <- accuracy*100
  cat("Method = Logistic Regression"," and Accuracy = ",accuracy,"\n")
  
  
  #Neural Networks
  fit <- nnet( trainingData[,classvariable] ~ ., data=trainingData[,-classvariable],size = 4,maxit=1000,decay=.01)
  doprediction <- predict(fit,newdata = testData)
  doprediction <- round(doprediction)
  accuracy <- sum(doprediction == testData[,classvariable] ) / length(doprediction)
  accuracy <- accuracy*100
  cat("Method = Neural Networks"," and Accuracy = ",accuracy,"\n")
  
  #Random Forest
  fit <- randomForest(trainingData[,classvariable] ~ ., data=trainingData[,-classvariable], importance=TRUE,proximity=TRUE)
  calcaccuracy <- table(predict(fit, newdata=testData,), testData[,classvariable])
  accuracy <- ((calcaccuracy[1,1] + calcaccuracy[2,2]) / (calcaccuracy[1,1] + calcaccuracy[1,2] + calcaccuracy[2,1] + calcaccuracy[2,2]))
  accuracy <- accuracy*100
  cat("Method = Random Forest"," and Accuracy = ",accuracy,"\n")
  
  
  #Bagging
  fit <- bagging(trainingData[,classvariable] ~ ., data=trainingData[,-classvariable], coob=TRUE)
  calcaccuracy <- table(predict(fit,testData),testData[,classvariable])
  accuracy <- ((calcaccuracy[1,1] + calcaccuracy[2,2]) / (calcaccuracy[1,1] + calcaccuracy[1,2] + calcaccuracy[2,1] + calcaccuracy[2,2]))
  accuracy <- accuracy*100
  cat("Method = Bagging"," and Accuracy = ",accuracy,"\n")
  
  
  #Boosting
  fit <- ada(trainingData[,classvariable] ~ ., data = trainingData[,-classvariable], iter=20, nu=1, type="discrete")
  calcaccuracy <- table(predict(fit,testData),testData[,classvariable])
  accuracy <- ((calcaccuracy[1,1] + calcaccuracy[2,2]) / (calcaccuracy[1,1] + calcaccuracy[1,2] + calcaccuracy[2,1] + calcaccuracy[2,2]))
  accuracy <- accuracy*100
  cat("Method = Boosting"," and Accuracy = ",accuracy,"\n")
  
  
}
options(warn=0)
