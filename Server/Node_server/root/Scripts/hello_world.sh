#!/usr/bin/env bash

echo "Hello World from $(hostname)";
echo "----------------------------------";
uptime;
echo "----------------------------------";
echo "NodeJS version: $(node -v)";
echo "Docker version: $(docker -v)";
echo "-----------------------------------";
echo "Docker images running:";
docker ps --format="- {{.Image}} in {{.Names}}";