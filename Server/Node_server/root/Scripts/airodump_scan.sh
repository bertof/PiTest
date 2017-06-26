#!/usr/bin/env bash

docker exec BlackArch sudo airmon-ng start wlan1 > /dev/null;
docker exec BlackArch sudo timeout --foreground 15 airodump-ng -w /tmp/airodump_scan.txt wlan1mon 2>/dev/null;
docker exec BlackArch sudo airmon-ng stop wlan1mon > /dev/null;
file=$(docker exec BlackArch sudo ls /tmp/ | grep -o ".*[^kismet]\.csv" | tail -1);
docker exec BlackArch sudo cat "/tmp/$file";
docker exec BlackArch sudo rm -rf "/tmp/airodump_scan*"
