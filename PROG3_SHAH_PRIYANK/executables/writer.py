import argparse

parser = argparse.ArgumentParser()
parser.add_argument('-t','--task',help='Number of Tasks')
parser.add_argument('-v',"--value",default=100,help='Sleep Value')
arg = parser.parse_args()


f = open('Worker/data','w')
for i in range(0,arg.task):
    f.write('sleep\n'+arg.value)
f.close()


