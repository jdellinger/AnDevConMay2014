#!/bin/sh

SCRIPT_DIR=`dirname $0`
sleep 10

sudo python $SCRIPT_DIR/../python/rover_service.py > $SCRIPT_DIR/../logs/service.log 2>&1
