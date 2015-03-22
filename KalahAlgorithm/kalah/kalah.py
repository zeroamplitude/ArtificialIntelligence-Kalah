import pygame
from time import sleep
from PodSixNet.Connection import ConnectionListener
import sys
from pygame.constants import QUIT
from gui import Board
from controller import AIControl


class Kalah:
    def __init__(self, player_num, pl1=None, pl2=None):
        # initialize game settings
        self.clock = pygame.time.Clock()
        self.me = player_num  # 1 for PL1 or 2 for PL2
        self.turn = 1   # set turn to PL1

        # initialize  gui
        # gui listens and is controlled by the games
        # board representation
        self.gui = Board()

        # sync game board with gui  board
        self.board = self.gui.pieces
        self.turn = 1

        # initialize controllers
        self.ai = AIControl(self.me, self.board, self.turn)

        # sets the player to the control method, which gets
        # called in the update loop
        if pl1 is not None:
            self.pl1 = (self.mouse_controls
                        if pl1 == 1 else  # 1 - Mouse or 2 - ai
                        self.ai_controls)
        if pl2 is not None:
            self.pl2 = (self.mouse_controls
                        if pl2 == 1 else  # 1 - Mouse or 2 - ai
                        self.ai_controls)

        # if pl1 == pl2 == None:  # default controller - mouse
        #     self.move = self.ai_controls
        # else: # set move to pl1 controller
        self.move = self.pl1

    def mouse_controls(self):
        move = self.gui.listen()
        if self.me == 1:
            if move >= 1 and move <= 6:
                print("me", self.me, move)
                return move + 1, self.me
            else:
                return self.move()
        elif self.me == 2:
            if 7 <= move and move <= 12:
                move = move + ((6- move) * 2)
                print("me", self.me, move)
                return move + 1, self.me
            else:
                return self.move()

    def ai_controls(self):
        move = self.ai.get_move(self.board)
        if self.me == 1:
            if move >= 0 and move <= 5:
                print("me", self.me, move)
                return move + 1, self.me
            else:
                return self.move()
        elif self.me == 2:
            if 7 <= move and move <= 12:
                move += (6 - move) * 2
                print("me", self.me, move)
                return move + 1, self.me
            else:
                return self.move()

    def update(self):
        """Game loop"""
        self.clock.tick(60)  # set the frame rate
        self.gui.draw()  # draw any updates to the screen

        # check for quit
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
                sys.exit(0)

        # if 'my' turn
        if self.turn == self.me:
            move = self.move()
            print(move)  # make move
            if move is not None:  # filter invalid types
                # validate move
                self.turn = self.validate_move(move[0], move[1])
                self.move = (self.pl1
                             if self.turn == 1 else
                             self.pl2)
                self.me = self.turn
                print(self.turn)
                if self.gameover():
                    self.gui.clean()
        self.gui.listen()

    def gameover(self):
        pl1_check = True
        pl2_check = True
        for i in range(6):
            if len(self.board[i].seeds) != 0:
                pl1_check = False
                break
            if len(self.board[i+((6-i)*2)].seeds) != 0:
                pl2_check = False
        return pl1_check and pl2_check

    def validate_move(self, piece, player):
        print('piece: ',piece, 'player', player)
        if not piece:
            return self.move()
        if 1 > piece or piece > 6:
            return self.move()

        tmp = (self.board[piece-1]
               if player == 1 else
               self.board[(piece-1) + ((6-(piece-1))*2)])

        if len(tmp.seeds) == 0:
            return self.move()
        else:
            return self.valid_move(piece, player)

    def valid_move(self, piece, player):
        piece -= 1  # adjust piece for board keys
        if player == 2:
            piece = piece + ((6 - piece) * 2)
        num_seeds = len(self.board[piece].seeds)

        skip = 0 # shortcut used if needed to skip over piece
        for i in range(num_seeds):
            dest = piece + i + skip + 1

            if dest > 13:
                dest = dest % 14

            if (dest == 6 and player == 2) or (dest == 13 and player == 1):
                skip = 1
                dest += skip

            # if last piece
            if i == num_seeds - 1:
                 # check if home
                if dest == 6 or dest == 13:
                    self.gui.move(piece, dest)
                    return 1 if self.turn == 1 else 2  # another turn
                # check if cur store is empty
                elif len(self.board[dest].seeds) == 0:
                    # check if store across from cur is empty
                    if len(self.board[dest+((6-dest)*2)].seeds) != 0:
                        # if piece is player 1 and it is player 1's turn
                        if 0 <= dest <= 5 and self.turn == 1:
                            self.gui.move(piece, dest)
                            self.gui.move_all(dest) # special move
                            return 1 if self.turn == 2 else 2
                        elif 7 <= dest <= 12 and self.turn == 2:
                            self.gui.move(piece, dest)
                            self.gui.move_all(dest) # special move
                            return 1 if self.turn == 2 else 2
                self.gui.move(piece, dest)
                return 1 if self.turn == 2 else 2
            # move
            self.gui.move(piece, dest)

    def invalid_move(self):
        print("Invalid move: Try again.")
        return self.turn


class NetworkKalah(ConnectionListener, Kalah):
    def __init__(self, connection, player_num, gameID, pl1=None, pl2=None):
        Kalah.__init__(self, player_num, pl1, pl2)
        # network settings
        self.connection = connection
        self.waiting = False
        self.gameID = gameID

    def listen(self):
        self.waiting = True
        while self.waiting:
            self.Pump()
            self.connection.Pump()
            sleep(0.01)

    def update(self):
        Kalah.update(self)
        self.Pump()
        self.connection.Pump()
        sleep(0.01)

    def validate_move(self, piece, player):
        move, pID = Kalah.validate_move(self, piece, player)
        msg = dict(action='move', move=move, pID=pID, gID=self.gameID)
        self.Send(msg)
        self.listen()

    def Network_ValidMove(self, data):
        self.waiting = False
        piece = data['move']
        pID = data['pID']
        self.turn = data['turn']
        self.valid_move(piece, pID)

    def Network_InvalidMove(self, data):
        self.waiting = False
        self.invalid_move()

if __name__ == '__main__':
    pygame.init()
    screen = pygame.display.set_mode((800, 200))
    game = Kalah(player_num=1, pl1=2, pl2=1)
    while True:
        game.update()