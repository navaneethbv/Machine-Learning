library(rpart)
data("solder")
str(solder)
floor_val<-floor(0.9*nrow(solder))
train<-sample(nrow(solder),size=floor_val)
trainingData<-solder[train,]
testData<-solder[-train,]
solder_fit<-rpart(Solder~.,parms=list(split='information'), minsplit=2, minbucket=1,data=trainingData,method='class')
par(mar = rep(0.1, 4))
plot(solder_fit)
text(solder_fit)
summary(solder_fit)
CPVal<-solder_fit$cptable[which.min(solder_fit$cptable[,"xerror"]),"CP"]
CPVal
pruned_fit<-prune(solder_fit,cp=as.numeric(CPVal))
out<-predict(pruned_fit,newdata=testData,type="vector")
tab <- table(out, testData$Solder)
tab
accuracy<-sum(diag(tab))/sum(tab)
accuracyPercentage<-accuracy*100
accuracyPercentage