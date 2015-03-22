import jpype
import os
import pygame
import sys
from thread import start_new_thread, start_new
from pygame.constants import QUIT


class AIControl:
    def __init__(self, me, board, turn):
        print(os.path.abspath("."))
        # connection to Java Virtual Machine
        jpype.startJVM(jpype.getDefaultJVMPath(), "-ea",
            "-Djava.class.path=%s" % os.path.abspath("."))

        # get java class from JVM
        Move = jpype.JClass("kalah.Move")

        tmp = []
        for b in board.values():
            tmp.append(len(b.seeds))
        # python var to represent java class
        self.java_move = Move(me, turn, tmp)

    def get_move(self, board):
        tmp = []
        for b in board.values():
            tmp.append(len(b.seeds))
        select = self.java_move.makeMove(tmp)
        # print(select)
        return select

    def __exit__(self, exc_type, exc_val, exc_tb):
        jpype.shutdownJVM()


# if __name__ == '__main__':
#     ai = AIControl({}, 1)
