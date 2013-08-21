__author__ = 'rquinn'

import os
import json

def createJSON():
    records = []
    record1 = {"name":"Bob", "email":"bob@email.com"}
    records.append(record1)
    record2 = {"name":"Bob2", "email":"bob2@email.com"}
    records.append(record2)
    return "Hello"

def diffIt():

    commandLine = 'perceptualdiff %s %s' % ('a.jpg', 'b.jpg')
    o = os.popen(commandLine)
    print o.readline()[0:4]
    o.close()

if __name__ == "__main__":

    print "Hello"
    #l = len(sys.argv)



    # if l == 4:
    #     runTest(sys.argv[0], sys.argv[1], sys.argv[2], sys.argv[3])
    # else:
    #     print "Something"
