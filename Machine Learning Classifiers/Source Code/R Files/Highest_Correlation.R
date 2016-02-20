args <- commandArgs(TRUE)
fileName <- args[1]
diabetes_data<-read.csv(fileName,header = FALSE)
maxCorrelationVal<-0
maxFirstAttributeIndex<-0
maxSecondAttributeIndex<-0
corrVal<-0
for (i in 1:7) 
{ 
  a<-i+1 
  for (j in a:8)
  {
    corrVal<-cor(diabetes_data[[i]],diabetes_data[[j]])
    if(corrVal>maxCorrelationVal)
    {
      maxCorrelationVal<-corrVal
      maxFirstAttributeIndex<-i
      maxSecondAttributeIndex<-j
    }
  }
 
}
paste("Maximum correlation value is: ",maxCorrelationVal,sep="")
paste("The attributes are: ","V",maxFirstAttributeIndex," and ","V",maxSecondAttributeIndex,sep="")
