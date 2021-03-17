# COSC320Project

## Dataset.
Include the details of the dataset.
https://data.world/hoytick/2017-jan-ontimeflightdata-usa

This dataset contains flight inofrmation for 450000 domestic US flights from 2017. The relevant data fields are year, month, dayofmonth, carrier, flightnum, originairportid, origincityname, destinationairportid, destinationairportid, deptime, arrtime. There is no ticketprice data feild in this dataset, but using the distance feild, we created reasonable estimates for the price. From the site (Rometorio)[https://www.rome2rio.com/blog/2013/01/02/170779446/], it costs about $50 + 11 cents per mile for a ticket. So, we can get a ```ticket cost = 50 + (distance)(0.11)(0.02 x (Random.nextGaussian+1) )```.  Every flight will cost a $50 plus a normally distributed number with mean 1, std 0.02 around 11 cents per mile.


## Implementation. 
Explain how you implemented the algorithm and tested it. All the subtle details should  be  included.  This  is  just  an  explanation  and  you  do  not  need  to  copy  paste  your implementation here. It can be as short as one paragraph. Include links if required.

## Results. 
Include the plots and the interpretation of the plots as input grows. Compare it to the big O function of the running time. For example, if your algorithm runs in , show the graph for 𝑂(𝑛2)the function  in  same  plot  as  well.  Explain  if  this  is  what  you  expected,  and  how  the 𝑛2implementation of your algorithm might have affected the constant values. How the choice of data structure might have affected this result?

## Unexpected Cases/Difficulties. 
Our dataset had no cost feild. This was resolved by creating a cost function to simulate the cost of tickets. However, due to the inherient properties of random variables, right now any ticket price, however unlikely, could be possible. 

## Task Separation and Responsibilities. 
Who did what for this milestone? Explicitly mention the name of group members and their responsibilities. 
