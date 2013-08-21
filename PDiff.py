__author__ = 'rquinn'
import os


if __name__ == "__main__":
    #l = len(sys.argv)

    commandLine = 'perceptualdiff %s %s' % ('a.jpg', 'b.jpg')
    o = os.popen(commandLine)
    print o.readline()[0:4]
    o.close()

    # if l == 4:
    #     runTest(sys.argv[0], sys.argv[1], sys.argv[2], sys.argv[3])
    # else:
    #     print "Something"
