library(e1071)
dataset1 = read.csv("https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data")
str(dataset1)

i = 1
sum = 0
count = 0
kernels = c('','linear','polynomial','radial','sigmoid') 
flag =0
for(kernel1 in kernels){ 
  for (i  in 1:10)
  {
    split_data<- sample(nrow(dataset1), size = nrow(dataset1)* 0.9)
    Train_Data<-dataset1[split_data,]
  
    TestData <- dataset1[-split_data,]
    if(flag==0){
      model_svm <- svm(as.factor(Train_Data[,9]) ~ ., data = Train_Data[,1:8])
      predicted_labels <- predict(model_svm,TestData[,1:8])
  
    }
    else{
      model_svm <- svm(as.factor(Train_Data[,9]) ~ ., data = Train_Data[,1:8],kernal=kernel1)
      predicted_labels <- predict(model_svm,TestData[,1:8])
      }
    
    table(predicted_labels,TestData[,9])
    result <- sum(predicted_labels == TestData$X1)/length(predicted_labels)
    #cat("Experiment ",i,": Accuracy is",result*100,"% \n")
    sum = sum + result*100
    count = count + 1
  }
    avg = sum/count
  if(flag==0){
    cat("Overall Accuracy Default Kernel is =",avg,"%\n")
    flag =1
  }
  else{
    cat("Overall Accuracy =",avg,"% kernel =",kernel1,"\n")  
  }
}
