# MatrixBot
GEAR NETWORK

### Input Files  
1. Papers database: paper.json  
2. Ciation database: cocitation.txt  

### Output Files  
1. coAuthorMatrix.txt  
2. muCitationMatrix.txt  
3. coCitationMatrix.txt  
4. finalMatrix.txt (combinations of three matrices)  

finalMatrix[i][j] = a x coAuthorMatrix[i][j]+ b x muCitationMatrix[i][j] + c x coCitationMatrix[i][j];  
(a,b,c can be changed in the main function)  

### Environment  
Netbeans and Java  
Go to Oracle website [Link](http://www.oracle.com/us/technologies/java/jdk-7-netbeans-download-432126.html), select the file that corresponds to the operating system. Download it and install.  

### Code file  
Go to Github [Link](https://github.com/sidewallme/MatrixBot/tree/master) and click "Download ZIP". Use Netbeans to open project and run MatrixBot.java in src folder.  
