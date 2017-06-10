#!/usr/bin/env bash

docker exec BlackArch sudo airmon-ng start wlo1 > /dev/null;
docker exec BlackArch sudo timeout --foreground 15 airodump-ng -w /tmp/airodump_scan.txt wlo1mon 2>/dev/null;
docker exec BlackArch sudo airmon-ng stop wlo1mon > /dev/null;
file=$(docker exec BlackArch sudo ls /tmp/ | grep -o ".*[^kismet]\.csv" | tail -1);
docker exec BlackArch sudo cat "/tmp/$file";
docker exec BlackArch sudo rm -rf "/tmp/airodump_scan*"
