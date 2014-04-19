Butter [![Status](https://rawgit.com/bryan/bryan.github.io/master/images/active.svg)](#)
=======

CSCI 201L Transportation Final Project assigned during Spring 2014 under Professor Jeffrey Miller. The instructions and overview for this final projects can be found [here][1]. The full documentation can be found [here][2].

-------

##Authors
-------
__Bryan Chong__ - https://github.com/bryan <br/>
__Kaitlyn Lee__ - https://github.com/k8lynjlee <br/> 
__Hyungjin Shin__ - https://github.com/hyungjis <br/>
__Lorraine Sposto__ - https://github.com/lorsposto <br/>

-------

##Overview
-------
Butter is a graphical application visualizing flow of traffic in the Los Angeles area. Butter allows users to optimize their commutes by providing routes and times to a specified destination based on the state of traffic flow deduced from information provided by each car. Such capabilities will help a driver avoid traffic or minimize their time spent in traffic, or decide to forgo driving altogether, therefore saving the driver time and gas. Information collected by Butter allows drivers to observe trends in traffic for any given time of the day and also helps them decide the optimal time of day to drive. Butter aims to help reduce traffic congestion and air pollution by dispersing or reducing the cars on the road. This can be achieved by suggesting drivers the optimal time to drive and also by providing distance which ultimately minimizes time driving on the road.

##Features & Requirements
-------
###Graphical
1. Intuitive graphical user interface built ground up from Java Swing class. Such process makes it compatible on different OS featuring nearly identical designs.
2. Vehicles are plotted on the map and is designed to change colors based on the flow of the traffic and speed of the car. Vehicles turn red when its speed is under a threshold of 35 mph, yellow in between 35 and 60, and green when above 60.
3. Vehicles also move along the roadway of its designated freeways.
4. Historical data is plotted as a line graph.
5. Data is also visible in raw table formatted chart.
6. Additionally, users should be able to determine what time would be the best to travel to the user's designated location based on the historical data provided.


###User Input
1. Users are allowed to use export and store data in a CSV formatted document for further and additional use. 
2. Users can select a to / from destination location returning the fastest route to arrive at that destination based on the speed limitations and traffic flow which is calculated using a proportional time algorithm.

###Technical
1. Data is stored in a MySQL database.


-------
##Screenshots
![GUI](https://github.com/bryan/CSCI201-TransportationProject/blob/master/Assets/Images/butter_gui.png?raw=true)
![DATA](https://github.com/bryan/CSCI201-TransportationProject/blob/master/Assets/Images/butter_data.png?raw=true)




[1]: https://github.com/bryan/CSCI201-TransportationProject/blob/master/Assets/Documentation/FinalProject.pdf
[2]: #
[3]: https://github.com/bryan