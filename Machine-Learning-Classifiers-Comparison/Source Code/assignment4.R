args <- commandArgs(TRUE) 
dataURL<-as.character(args[1]) 
header<-as.logical(args[2])
d<-read.csv(dataURL,header = header)
int<-as.integer(args[3])

library(rpart) 
library(e1071)  
library(class)
library("neuralnet")
library(mlbench) 
library(ada)
library(randomForest) 
library(neuralnet)
library(nnet)
library(ipred)
sumtree<-0
sumsvm <-0
sumnb <-0
sumlog <-0
sumrd <-0
sumneu <-0
sumboost <-0
sumbag <-0
sumknn <-0


# create 10 samples
set.seed(123)
for(i in 1:10) {
  cat("Running sample ",i,"\n")
  sampleInstances<-sample(1:nrow(d),size = 0.9*nrow(d))
  trainingData<-d[sampleInstances,]
  trainingData <-na.omit(trainingData)
  testData<-d[-sampleInstances,]
  testData <-na.omit(testData)
  
  # which one is the class attribute
  Class<-as.factor(as.integer(args[3]))
  # now create all the classifiers and output accuracy values:
  class1 <- as.integer(args[3])
  testClass <- testData[,as.integer(int)]
  
  # now create all the classifiers and output accuracy values:
  #Decision TREE
  method <-"DecisionTree"
  modeldecision <- rpart(as.formula(paste0("as.factor(", colnames(d)[int], ") ~ .")),data=trainingData,parms = list(split = "information"), method = "class", minsplit = 1)
  prunedTree <- prune(modeldecision, cp = 0.010000)
  predictTree <- predict(prunedTree,testData,type="class")
  treeacctable <- table(predictTree,testClass)
  treeaccuValue <- sum(diag(treeacctable))/sum(treeacctable) *100
  sumtree <- sumtree +treeaccuValue
  cat("Method = ", method,", accuracy= ", treeaccuValue,"\n")
  
  #SVM
  method <- 'SVM'
  #modelsvm <- svm(classform, data = trainingData)
  modelsvm <- svm(as.formula(paste0("as.factor(", colnames(d)[int], ") ~ .")),data = trainingData)
  predsvm <- predict(modelsvm, testData, type = "class")
  #Acuuracy
  svmtable <- table(predsvm,testClass)
  svmaccuracy <- sum(diag(svmtable))/sum(svmtable) *100
  sumsvm <- sumsvm +svmaccuracy
  cat("Method = ", method,", accuracy= ", svmaccuracy,"\n")
  
  #NB
  method <- 'NaiveBayes'
  nbmodel <- naiveBayes(as.formula(paste0("as.factor(", colnames(d)[int], ") ~ .")), data = trainingData)
  prednb <- predict(nbmodel, testData, type = "class")
  nbacctable <- table(prednb,testClass)
  nbaccvalue <- sum(diag(nbacctable))/sum(nbacctable) *100
  sumnb <- sumnb + nbaccvalue
  cat("Method = ", method,", accuracy= ", nbaccvalue,"\n")
  
  
 
  
  
  #Logistic Regression
  method <- 'Logistic Regression'
  logisticModel <- glm(as.formula(paste0("as.factor(", colnames(d)[int], ") ~ .")), data = trainingData, family = "binomial")
  predlogistic<-predict(logisticModel, newdata=testData, type="response")
  threshold=0.65
  prediction<-sapply(predlogistic, FUN=function(x) if (x>threshold) 1 else 0)
  actual<-d[,as.integer(int)]
  LRaccvalue <- sum(actual==prediction)/length(actual) *100
  sumlog <- sumlog+LRaccvalue
  cat("Method = ", method,", accuracy= ", LRaccvalue,"\n")
  
  
  #random forest
  
  method <- "random"
  rfModel <- randomForest(as.formula(paste0("as.factor(", colnames(d)[int], ") ~ .")), data=trainingData, importance=TRUE, proximity=TRUE, ntree=500)
  RFpred <- predict(rfModel,testData,type='response')
  predTable <- table(RFpred,testClass)
  accuracy <- sum(diag(predTable))/sum(predTable)*100
  sumrd <- sumrd+accuracy
  cat("Method = ", method,", accuracy= ", accuracy,"\n")
  
  
  #neural
  method <- 'neural'
  nnetmodel <- nnet(as.formula(paste0("as.factor(", colnames(d)[int], ") ~ .")), trainingData,size=1)
  pred <- predict(nnetmodel, testData)
  neural <- table(pred,testClass)
  accuracy <- sum(diag(neural))/sum(neural) *100
  sumneu <- sumneu+accuracy
  cat("Method = ", method,", accuracy= ", accuracy,"\n")
  
  #boosting
  method <- 'boosting'
  model <- ada(as.formula(paste0("as.factor(", colnames(d)[int], ") ~ .")), data = trainingData, iter=20, nu=1, type="discrete")
  p=predict(model,testData)
  # accuracy
  acc <- table(p,testClass)
  accuracy <- sum(diag(acc))/sum(acc) *100
  sumboost <- sumboost+accuracy
   cat("Method = ", method,", error= ", accuracy,"\n")
  
   
}

sumtree <- sumtree/10
sumsvm <- sumsvm/10
sumnb <- sumnb/10
sumlog <- sumlog/10
sumrd <- sumrd/10
sumneu <- sumneu/10
sumboost <- sumboost/10
sumbag <- sumbag/10
cat("average after 10 samples for decision tree  ",sumtree,"\n")
cat("average after 10 samples for svm  ",sumsvm,"\n")
cat("average after 10 samples for naive bayse  ",sumnb,"\n")
cat("average after 10 samples for logistic regresssion  ",sumlog,"\n")
cat("average after 10 samples for random forest  ",sumrd,"\n")
cat("average after 10 samples for neural network  ",sumneu,"\n")
cat("average after 10 samples for boosting  ",sumboost,"\n")
cat("average after 10 samples for bagging  ",sumbag,"\n")
