-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------Hadoop Steps----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
   
1. Upload your security key on AWS instance. Do the following procedure to grant access and add indentity key.
			eval `ssh-agent -s`
			chmod 600 Hadoop.pem
			ssh-add Hadoop.pem 

2. Mount additional drives:
	lsblk - to check disk to be mounted
	sudo mkfs.ext4 -L hadoop /dev/xvdb
	sudo mkdir -p /mnt/raid
	sudo mount /dev/xvdb /mnt/raid
3.Format the Hadoop namenode.
 	hadoop namenode -format

4.Start all the services from the hadoop/sbin:
	./start-dfs.sh
	./start-yarn.sh
->  In case nodemanger won't start, start it manually:
	./yarn-deamon.sh start nodemanager
->  Check on master and slave both for the active running services:
	jps
->  Check online for total running datanodes on slave and status:
       MASTER_PUBLIC_DNS:50070

5. Upload your program and make jar file, add class file to jar.
	hadoop com.sun.tools.javac.Main YOUR_FILE 
	jar cf JAR_NAME.jar PROGRAM_NAME*.class

6. Generate data file with gensort.
	./gensort -a size file_path/file_name

7. Put generated data on hadoop file system.
	hadoop fs -mkdir /user
	hadoop fs -mkdir /user/ec2-user
	hadoop fs -mkdir /user/ec2-user/input
	Hadoop fs -put file_path/file_name hadoop_input_path

8. Run the Program:
	hadoop jar ts.jar Terasort input_path output_path

9. Store output from hadoop fs to local fs and validate it:
	hadoop fs -get output/* .
	mv generated_outputname output_file_name
	
10.	convert output file with following command:
	sudo yum install unix2dos ( to install application in System)
	unix2dos output_file_name	

11.	validate it with valsort:
	./valsort output_file_name

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------Spark Steps------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


1. by default drive is mounted on /media/ephermal0 which I prefered to /mnt/raid , so Did following changes:
	umount -l /dev/xvds  (To unmount current dir)
	lsblk (list all blocks including those yet not mounted)
	sudo mkfs -t ext4 /dev/sds (prepare drive to be mounter)
	sudo mkdir -p /mnt/raid (create path for drive to mount)
	sudo mount /dev/sds /mnt/raid (mount drive to path)
	df -h (list all mounted drives)

2. Put Scala Program in same folder as Spark-shell.
   Compile scalac Programname.scala ; it will generate class file.
2. Slave nodes are come with drive mounted, to run program go to following path:
	cd spark/spark-shell
->   Open Spark Shell:
	./spark-shell
3. type "scala program_name" ; it will generate output.

4. To checck the output data:
	./hadoop fs -ls /user/root/output/

5. To fetch data from output folder, run following commands:

	./hadoop fs -get /user/root/output/File_number /mnt/raid/

-> It will fetch data from hadoop file system and store it into drive we mounted at /mnt/raid


-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------Shared Memory----------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

Pre-Req:
	Generate the data with gensort at location /mnt/raid with file name "input"

1. Compile the program:
	javac SharedMemory.java

2. Run program:
	java SharedMemory [thread_no}

3. Fetch output file from "/mnt/raid/" , filename: output

4. Convert output file with: unix2dos /mnt/raid/output

5. Check with valsort: ./valsort /mnt/raid/output
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------->>>>>>E.O.F.>>>>>>------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

