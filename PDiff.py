__author__ = 'rquinn'

import os
#import json


def cropFoto():
        os.popen("sh cropFoto.sh")



def createJSON(spotList):

    #This is hack code, replace later with json code when we have the rest in

    gn1, gn2, gn3, gs1, gs2, gs3, gs4, gs5, gs6, gs7, plot = spotList
    gnos = gn1 + gn2 + gn3
    gsos = gs1 + gs2 + gs3 + gs4 + gs5 + gs6 + gs7

    js = """{ "garage north":
    { "floors": [  {"name": "Floor 1", "spots_available": %s },
                   {"name": "Floor 2", "spots_available": %s },
                   {"name": "Floor 3", "spots_available": %s} ], "open_spots": %s },
    "garage south":
    { "floors": [  {"name": "Floor 1", "spots_available": %s },
                   {"name": "Floor 2", "spots_available": %s },
                   {"name": "Floor 3", "spots_available": %s },
                   {"name": "Floor 4", "spots_available": %s },
                   {"name": "Floor 5", "spots_available": %s },
                   {"name": "Floor 6", "spots_available": %s },
                   {"name": "Floor 7", "spots_available": %s } ], "open_spots": %s },
    "parking lot":
    { "open_spots": %s } }""" % (gn1, gn2, gn3, gnos, gs1, gs2, gs3, gs4, gs5, gs6, gs7, gsos, plot)

    return js


def diffIt(pic1, pic2):

    commandLine = 'perceptualdiff -threshold 150 %s %s' % (pic1, pic2)
    o = os.popen(commandLine)
    response = o.readline()[0:4]
    o.close()
    return response if response == 'FAIL' else 'PASS'


def countSpots():

    #Almost complete function, the lower for loop will need modification if we ever care which exact spots are open

    spots = []
    count = 0

    for x in range(0, 18):
        original = 'org_%s.jpg' % x
        cut = 'cut_%s.jpg' % x
        spots.append(diffIt(original, cut))
        rmCommand = "rm cut_%s.jpg" % x
        os.popen(rmCommand)

    #Add code to the below loop when we want to build a map

    for x in spots:
        count = count + 1 if x == 'PASS' else count

    print count, spots

    return count


def createFile():
    f = open('garages.json', 'w')
    f.write(createJSON([countSpots(), 0, 0, 2, 5, 2, 5, 3, 0, 2, 29]))
    f.close

if __name__ == "__main__":

    cropFoto()
    createFile()
    scpCommand = "scp -o IdentityFile=~/.ssh/teamkey.pem garages.json ubuntu@ec2-54-213-174-112.us-west-2.compute.amazonaws.com:~/garages.json"
    o = os.popen(scpCommand)