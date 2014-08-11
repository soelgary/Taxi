#!/usr/bin/python

import json
import requests
import sys, getopt
import time
from datetime import datetime
from time import mktime

LOCAL_HOST = 'http://127.0.0.1:9095'

PATH = '/api/trip'


def make_request(line):
  split = line.split(',')
  start_time = int(time.mktime(datetime.fromtimestamp(mktime(time.strptime(split[5].strip(), '%Y-%m-%j %H:%M:%S'))).timetuple()))
  end_time = int(time.mktime(datetime.fromtimestamp(mktime(time.strptime(split[6].strip(), '%Y-%m-%j %H:%M:%S'))).timetuple()))
  num_passengers = split[7]
  #return int(num_passengers)
  pickup_lat = float(split[10].strip())
  pickup_lon = float(split[11].strip())
  dropoff_lat = float(split[12].strip())
  dropoff_lon = float(split[13].strip())
  if pickup_lat < 90 and pickup_lat > -90 and dropoff_lat < 90 and dropoff_lat > -90 and pickup_lon < 180 and dropoff_lon > -180:
    payload = {
      "startTime": start_time,
      "endTime": end_time,
      "startLocation": {
        "type" : "Point",
        "coordinates": [pickup_lat, pickup_lon]
      },
      "endLocation": {
        "type" : "Point",
        "coordinates": [dropoff_lat, dropoff_lon]
      }
    }
    headers = {'content-type': 'application/json'}
    r = requests.post(LOCAL_HOST + PATH, data=json.dumps(payload), headers=headers)
  else:
    print "invalid lat/long " + str(pickup_lat) + " " + str(dropoff_lat) + " " + str(pickup_lon) + " " + str(dropoff_lon)

def load_trip_data(inputfile):
  skippedFirstLine = False
  for line in open(inputfile):
    if skippedFirstLine:
      make_request(line)
    else:
      skippedFirstLine = True


def main(argv):
  inputfile = ''
  try:
    opts, args = getopt.getopt(argv,"hi:",["ifile="])
  except getopt.GetoptError:
    print 'test.py -i <inputfile>'
    sys.exit(2)
  for opt, arg in opts:
    if opt == '-h':
      print 'test.py -i <inputfile>'
      sys.exit()
    elif opt in ("-i", "--ifile"):
      inputfile = arg
  print 'Input file is ', inputfile
  load_trip_data(inputfile)

if __name__ == "__main__":
   main(sys.argv[1:])