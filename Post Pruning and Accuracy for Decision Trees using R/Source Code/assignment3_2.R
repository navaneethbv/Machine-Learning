library(rpart)
data("kyphosis")
str(kyphosis)
floor_val<-floor(0.9*nrow(kyphosis)) 
train<-sample(nrow(kyphosis),size=floor_val) 
trainingData<-kyphosis[train,]
testData<-kyphosis[-train,]
kyphosis_fit<-rpart(Kyphosis~Age+Number+Start,parms=list(split='information'), minsplit=2, minbucket=1,data=trainingData,method='class')
par(mar = rep(0.1, 4))
plot(kyphosis_fit)
text(kyphosis_fit) 
summary(kyphosis_fit)
CPVal<-kyphosis_fit$cptable[which.min(kyphosis_fit$cptable[,"xerror"]),"CP"]
CPVal
pruned_fit<-prune(kyphosis_fit,cp=as.numeric(CPVal))
out<-predict(pruned_fit,newdata=testData,type="vector")
tab <- table(out, testData$Kyphosis)
tab
accuracy<-sum(diag(tab))/sum(tab)
accuracyPercentage<-accuracy*100
accuracyPercentage
