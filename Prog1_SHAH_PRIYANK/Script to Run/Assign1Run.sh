#!/bin/bash

thread=$2
blockSize=$3
hostName=$4

echo -e ""
echo -e "**** Enter Any one of the following option******
echo -e "CPU : ./Assign1Run.sh CPU choice(i,f) NoofThreads(1,2,4)"
echo -e "Disk : ./Assign1Run.sh Disk BlockSize(1,1024,1048576) NoofThreads(1,2)"
echo -e "tcp-Server : ./Assign1Run.sh TCP-Server NoofThreads(1,2) BlockSize(1, 1024, 65536) "
echo -e "tcp-Client : ./Assign1Run.sh TCP-Client NoofThreads(1,2) BlockSize(1, 1024, 65536) Hostname"
echo -e "udp-Server : ./Assign1Run.sh UDP-Server BlockSize(1, 1024, 65507) ThreadNo(1,2) HostName "
echo -e "udp-Client : ./Assign1Run.sh UDP-Client BlockSize(1, 1024, 65507) ThreadNo(1,2) HostName "

echo -e ""
echo -e ""
echo -e ""
echo -e " Output:"

if [ $1 = "CPU" ]
then
	/usr/bin/java -cp CPUBench.jar "CPUBench" $2 $3
elif [ $1 = "Disk" ]
then
	/usr/bin/java -cp Disk.jar "diskbench" $2 $3
elif [ $1 = "tcp-Server" ]
then
	/usr/bin/java -cp TcpThreading.jar "tcpsrvth" $2 $3
elif [ $1 = "tcp-Client" ]
then
        /usr/bin/java -cp TcpThreading.jar "tcpcltth" $2 $3 $4
elif [ $1 = "udp-Server" ]
then
        /usr/bin/java -cp udpthread.jar "udpserver" $2 $3 $4
elif [ $1 = "udp-Client" ]
then
        /usr/bin/java -cp udpthread.jar "udpclient" $2 $3 $4
else
	echo "Invalid Selection"
fi


echo -e ""
