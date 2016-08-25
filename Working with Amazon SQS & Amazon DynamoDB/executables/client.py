import threading
import time
import Queue
import argparse
import os
import boto.sqs
from boto.sqs.message import Message, RawMessage
from credential import AWS_ACCESS_KEY,AWS_KEY
import random
import boto.dynamodb

class LocalWorker (threading.Thread):
    def __init__(self,tID,name):
        threading.Thread.__init__(self)
        self.tID = tID
        self.name = name

    def run(self):
        print self.tID, self.name
        for i in f:
            q.put(i)
        while not q.empty():
            data = q.get()
            delay = data[6:]
            # task(self.name,data)
            task(self.name,int(delay)/1000.0)

def remoteWorker(queueName,worker,WorkFile):

    connection_ob = boto.connect_sqs(AWS_KEY,AWS_ACCESS_KEY)
    connection = boto.connect_dynamodb(AWS_KEY,AWS_ACCESS_KEY)
    myschema=connection.create_schema(hash_key_name='task_id',hash_key_proto_value='S')
    queue = connection_ob.create_queue(queueName)
    try:
        print "Creating Table.."
        table=connection.create_table(name='task_table', schema=myschema, read_units=100, write_units=100)
        print "Table Created Successfully...."
    except:
        print "Table already exist"

    msg = RawMessage()
    f = open(WorkFile)

    for line in iter(f):
        rand = random.randrange(0,9999)
        msg.set_body(line)
        msg.message_attributes = {"Values": {
                                            "data_type":"String",
                                             "string_value":str(rand)
                                             }
                                        }
        queue.write(msg)
    f.close()
    print "Data Inserted into Queue"

def task(tname,delay):
        # os.system(delay)
        time.sleep(delay)



parser = argparse.ArgumentParser()
parser.add_argument('-s','--queue',help='Local Worker')
parser.add_argument('-t',"--thread",default=1,help='Number of Thread/Worker')
parser.add_argument('-w',"--Work_File",help='Worker File Name')

arg = parser.parse_args()

if(arg.queue == 'LOCAL'):

    q = Queue.Queue()
    f = open(arg.Work_File)
    Start_time = time.time()

    threads = []
    for i in range(0,int(arg.thread)):
        t = LocalWorker(i,"thread"+str(i))
        t.start()
        threads.append(t)

    for k in threads:
        k.join()

    end_time = time.time() - Start_time
    print "Execution Time: " + str(round(end_time,3)) + " Sec"
else:
    Start_time = time.time()
    filename = arg.Work_File
    queue_name = arg.queue
    worker_no = arg.thread
    # obj = LocalWorker()
    remoteWorker(queue_name,worker_no,filename)
    end_time = time.time() - Start_time
    print "Execution Time: " + str(round(end_time,3)) + " Sec"