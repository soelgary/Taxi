#!/usr/bin/python

import json
import requests
import sys, getopt
import time
from datetime import datetime
from time import mktime

LOCAL_HOST = 'http://127.0.0.1:8080'

PATH = '/trip'


def make_request(line):
  split = line.split(',')
  start_time = int(time.mktime(datetime.fromtimestamp(mktime(time.strptime(split[5].strip(), '%Y-%m-%j %H:%M:%S'))).timetuple()))
  end_time = int(time.mktime(datetime.fromtimestamp(mktime(time.strptime(split[6].strip(), '%Y-%m-%j %H:%M:%S'))).timetuple()))
  pickup_lat = float(split[10].strip())
  pickup_lon = float(split[11].strip())
  dropoff_lat = float(split[12].strip())
  dropoff_lon = float(split[13].strip())
  payload = {
    "startTime": start_time,
    "endTime": end_time,
    "startLocation": [pickup_lat, pickup_lon],
    "endLocation": [dropoff_lat, dropoff_lon]
  }
  headers = {'content-type': 'application/json'}
  r = requests.post(LOCAL_HOST + PATH, data=json.dumps(payload), headers=headers)

def load_trip_data(inputfile):
  for line in open(inputfile):
    make_request(line)


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