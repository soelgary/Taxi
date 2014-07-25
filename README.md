Taxi
====

A service that predicts where people taking cabs will want to be dropped off in NYC

url ```http://www.taxi.gsoeller.com/trip```

Service
=======

Make a request, get a response

Data
====

1. Pojo's for the data
2. Connect to a database
3. Dao's for the Pojo's

Algorithm
=========

What is the best way to actually make a prediction?


Git
===

1. git clone https://github.com/soelgary/Taxi.git -> downloads all the code and sets up your local git repo
2. git pull origin master -> updates your local repo
3. git checkout -b branch-name
4. git add filename
5. git commit 
6. git push origin branch-name
7. go to github and create a pull request for your branch
8. merge pull request into master
9. git checkout master
10. repeat steps 2-8

Setup
=====

1. Install and run mongodb
2. clone this repo
3. Add this project to eclipse by importing an existing maven project
4. install ```TaxiData``` and ```TaxiAlgorithms``` to your local maven repository by running these commands ```cd TaxiData``` and ```mvn clean install```. Repeat for ```TaxiAlgorithms```
5. Setup the run configuration in eclipse. Set the project to be ```TaxiService```. Set the main class to be ```com.gsoeller.taxi.TaxiService.TaxiServiceApplication```. Set the program arguments to be ```server```.
6. Click run and you can send GET/POST requests to http://127.0.0.1/trip


API Endpoints
=============

This is a list of all the endpoints that you can hit and example payloads.

```
GET /trip?limit=n
```


This endpoint will get you all of the trip objects stored in the database. ```limit``` is an optional query param that sets a limit on the number of trips to query for. The default is set to 100.

```
POST /trip
```

This endpoint will create a new trip and return the trip that you created. An example json payload is below

```json
{
  "endLocation": {
    "type": "Point",
    "coordinates": [1.8761, 2.2874]
  }, 
  "endTime": 1357681356, 
  "startLocation": {
    "type": "Point",
    "coordinates": [1.1, 2.2]
  }, 
  "startTime": 1357680958
}
```

Scripts
=======

```
load_trip_data.py
```

This script is located in ```TaxiData/src/main/scripts``` and will add all the taxi data from a given script. Execute the script by running  ```./load_trip_data.py -i /path/to/file/with/trip/data.txt```


















