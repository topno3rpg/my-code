#!/bin/bash
	echo "Starting my-code ..."
	nohup java -Xbootclasspath/a:`echo lib/*.jar| tr ' ' ':'`: -jar lib/my-code*.jar > /root/logs/stdout.log 2>&1 &
	echo "Started my-code ..."