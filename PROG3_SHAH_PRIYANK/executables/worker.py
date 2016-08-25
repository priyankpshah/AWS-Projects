import boto.sqs.queue
from boto.sqs.message import Message,RawMessage
from credential import AWS_ACCESS_KEY,AWS_KEY
import time
import argparse
import boto.dynamodb


parser = argparse.ArgumentParser()
parser.add_argument('-s','--queue',help='Local Worker')
parser.add_argument('-t',"--worker",default=1,help='Number of Thread/Worker')
arg = parser.parse_args()
connection_ob = boto.connect_sqs(AWS_KEY,AWS_ACCESS_KEY)

queue = connection_ob.get_queue(arg.queue)
queue.set_message_class(RawMessage)
i=0

start_time = time.time()
while True:
    dbconnection = boto.connect_dynamodb(AWS_KEY,AWS_ACCESS_KEY)
    rs = queue.get_messages(message_attributes='Values')
    if len(rs) != 0:
        msg = rs[0]
        id = rs[0].message_attributes['Values']['string_value']
        f = msg.get_body()
        # try:
        print "Connecting to Table...."
        table = dbconnection.get_table('task_table')

        if table.has_item(hash_key=id):
            queue.delete_message(msg)
        else:
            task_data={'task':f}
            job=table.new_item(hash_key=id, attrs=task_data)
            job.put()
            print "Data inserted successfully into DynamoDB...."
            duration = f[6:]
            time.sleep(int(duration)*1.0/1000)
            queue.delete_message(msg)

        # except:
        # print "Connection UnSuccessfull!!!"
    else:
        break

end_time = time.time() - start_time
print "Execution Time: " + str(round(end_time,3)) + " Sec"
