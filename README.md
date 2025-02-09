# camunda-BpmnPathFinder



### Pseudo: 

Let's find the path between two points (Start,end)  

Input :  two node ids , the start node and the end node as java arguments 

Output: to printout the shortest path between the start and end node with no loops and the result should be with no line break 

The flow and the components  

BPMN XML which can be fetched from a shared aws api-gateway, i would like to understand the xml structure so i need a sample python to directly parse it , i know it is not needed, but i am more familiar with python and i need to double check if my jave code is working fine 

I need to parse the xml , https://www.baeldung.com/jackson-convert-xml-json  

 i will use the BPMN model api from Camunda in https://docs.camunda.org/manual/7.22/user-guide/model-api/bpmn-model-api/read-a-model   to take raw XML and turns it into Java objects and then i will convert it to graph structure and store nodes and edges in list also i need to handle prefixes of the namspaces like for example : bpmn: 

Implement the graph traversals in Java (same as in python) can be done by DFS & BFS, i have to choose between one of these algorithms but for me bfs is straight forward, to find the most correct path, a very nice article here https://siddosamith.medium.com/graph-traversals-in-java-dfs-bfs-a91910f6b9f9#:~:text=In%20bfs%20we%20start%20from,of%20zero%20first%20and%20move  ,also i need to track visited nodes to avoid loops and build the path as we go when finding the edges + nodes 

i need to extract the start and end nodes from the parsed model using their IDs provided as command-line arguments , in that case it should be start node “approveInvoice” and enpoint invoiceProcessed” 

Error handling (Errors and no found path must quit your program with exit code –1) - https://www.geeksforgeeks.org/difference-between-system-out-println-and-system-err-println-in-java/ 

Output the result: Print the found path or exit with an error if no path exists. 
