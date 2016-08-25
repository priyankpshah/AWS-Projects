for $i in {1..16}
do
	ssh ec2-user@slave$i 'sudo rm -rf /mnt/raid/hdfs/datanode/current'
	scp -p 80 .ssh/config ec2-user@slave$i:~/.ssh/
	cat /etc/hosts | ssh slave$i "sudo sh -c 'cat >/etc/hosts'"
	scp -p 80 loadDisk.sh slave$i:~/
	ssh -tty ec2-user@slave$i ./loadDisk.sh
	scp -p 80 .ssh/known_hosts ec2-user@slave"$i":~/.ssh/
done
