sudo umount -l /media/ephemeral0/
sudo mkfs.ext4 -L hadoop /dev/xvdb

sudo mkdir -p /mnt/raid
sudo mount /dev/xvdb /mnt/raid

sudo chmod 777 /mnt
sudo chmod 777 /mnt/raid
mkdir -p /mnt/raid/hdfs
sudo chmod 777 /mnt/raid/hdfs/
mkdir -p /mnt/raid/hdfs/namenode
mkdir -p /mnt/raid/hdfs/datanode
mkdir -p /mnt/raid/tmp/hadoop-ec2-user
