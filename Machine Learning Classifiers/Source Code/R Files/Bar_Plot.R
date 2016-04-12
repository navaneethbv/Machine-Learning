args <- commandArgs(TRUE)
fileName <- args[1]
diabetes_data<-read.csv(fileName,header = FALSE) 
attach(diabetes_data) 
par(mfrow=c(3,3))  
barplot(diabetes_data$V1)
barplot(diabetes_data$V2)
barplot(diabetes_data$V3)  
barplot(diabetes_data$V4) 
barplot(diabetes_data$V5) 
barplot(diabetes_data$V6)  
barplot(diabetes_data$V7)  
barplot(diabetes_data$V8)
 
