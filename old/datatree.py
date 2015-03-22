import copy
import sqlite3

__author__ = 'Nicholas De Souza'

con = sqlite3.connect('tree.db')


def across(piece):
    return piece + ((6 - piece) * 2)


class Game:
    """ fake game class to represent build actions """

    # def __init__(self, b, t):
    # self.pl1_score = b[6]
    # self.pl2_score = b[13]
    #     self.board = b
    #     self.board = {
    #         0: 3,
    #         1: 3,
    #         2: 3,
    #         3: 3,
    #         4: 3,
    #         5: 3,
    #         6: 0,
    #         7: 3,
    #         8: 3,
    #         9: 3,
    #         10: 3,
    #         11: 3,
    #         12: 3,
    #         13: 0
    #     }
    #     self.turn = t

    # def get_stats(self):
    #     return self.pl1_score, self.pl2_score, self.board, self.turn

    # def set_state(self, pl1_score, pl2_score, board, turn):
    #     self.pl1_score = pl1_score
    #     self.pl2_score = pl2_score
    #     self.board = board
    #     self.turn = turn

    def print_board(self, board):
        pl1 = [board.get(i) for i in range(7)]
        pl2 = [board.get(i) for i in range(7, 14)]

        pl2.reverse()
        print('    12  11  10   9   8   7')
        print('------------------------------')
        print('%02i  %02i  %02i  %02i  %02i  %02i  %02i'
              % (pl2[0], pl2[1], pl2[2], pl2[3], pl2[4], pl2[5], pl2[6]))

        print('    %02i  %02i  %02i  %02i  %02i  %02i  %02i'
              % (pl1[0], pl1[1], pl1[2], pl1[3], pl1[4], pl1[5], pl1[6]))
        print('------------------------------')

        print('     0   1   2   3   4   5')

    def play(self, board, piece, player):
        turn = player
        piece -= 1
        if player == 1:
            seeds = board.get(piece)
            board[piece] = 0
        else:
            piece = across(piece)
            seeds = board.get(piece)
            board[piece] = 0

        # print('piece', piece)
        for i in range(1, seeds + 1):
            # print('piece + i > 13', (piece + i) > 13)
            if (piece + i) > 13:
                cur = (piece + i) % 14
            else:
                cur = i + piece

            # print('cur', cur)
            if i == seeds:
                if cur == 6 or cur == 13:
                    # print('special move: Extra Turn')
                    turn = 1 if turn == 1 else 2
                else:
                    if board[cur] == 0:
                        if player == 1 and 0 <= cur <= 5:
                            if board[across(cur)] != 0:
                                # print('special move: Steal')
                                board[6] += (board.get(cur) +
                                             board.get(across(cur)))
                                board[cur] = 0
                                board[across(cur)] = 0
                                turn = 1 if turn == 2 else 2
                                break
                        elif player == 2 and (7 <= cur <= 12):
                            if board[across(cur)] != 0:
                                # print('special move: Steal')
                                board[13] += (board.get(cur) +
                                              board.get(
                                                  across(cur)))
                                board[cur] = 0
                                board[across(cur)] = 0
                                turn = 1 if turn == 2 else 2
                                break
                        turn = 1 if turn == 2 else 2
                    else:
                        turn = 1 if turn == 2 else 2
            board[cur] += 1

        if self.gameover(board):
            turn = 3
            self.clean(board)

        pl1_score = board[6]
        pl2_score = board[13]
        return pl1_score, pl2_score, board, turn

    def gameover(self, board):
        p1 = True
        p2 = True
        for i in range(6):
            if board[i] != 0:
                p1 = False
                break
        for i in range(7, 13):
            if board[i] != 0:
                p2 = False
                break
        if p1 or p2:
            # self.print_board(board)
            # print('gameover')
            return True
        else:
            return False

    def clean(self, board):
        for i in range(14):
            if i == 6 or i == 13:
                continue
            if i < 6:
                board[6] += board.get(i)
            else:
                board[13] += board.get(i)

            board[i] = 0
        self.print_board(board)


class Node:
    def __init__(self, pl1_score, pl2_score, board, turn, parent,
                 move=0, choice=1):

        # game.set_state(pl1_score, pl2_score, board, turn)
        self.choice = choice
        self.move = move
        # Pl1 score
        self.pl1_score = pl1_score
        # Pl2 score
        self.pl2_score = pl2_score
        # State of the board
        self.board = board
        # who makes the choice
        self.turn = turn
        # the previous choice
        self.parent = parent
        # the choices available based on board state
        self.choices = {}
        # the winner of the branch
        self.winner = []

        # if there are no choices - gameover

        temp_choice = []
        for i in range(6):
            if self.turn == 1 and self.board[i] != 0:
                temp_choice.append(i + 1)
            if self.turn == 2 and self.board[across(i)] != 0:
                temp_choice.append(i + 1)
        # play out all choices available
        temp_choice.reverse()

        for choice in temp_choice:
            print(self.move, 'player', self.turn, 'choice', choice - 1 if
            self.turn == 1 else across(choice - 1))

            game = Game()
            b = copy.deepcopy(self.board)
            tmp_pl1, tmp_pl2, tmp_board, tmp_turn = game.play(b,
                                                              choice,
                                                              self.turn)

            with con:
                cur = con.cursor()
                cur.execute('INSERT INTO board(move, p1_s0, p1_s1, p1_s2, '
                            'p1_s3, p1_s4, p1_s5, p1_h6, '
                            'p2_s7, p2_s8, p2_s9, '
                            'p2_s10, p2_s11, p2_s12, p2_h13) '
                            'VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)',
                            (move,
                             board[0],
                             board[1],
                             board[2],
                             board[3],
                             board[4],
                             board[5],
                             board[6],
                             board[7],
                             board[8],
                             board[9],
                             board[10],
                             board[11],
                             board[12],
                             board[13]))
            if tmp_turn == 3:

                # indicate end of branch by setting children to none
                # self.choices = None
                # set the winner of the game
                if self.pl1_score > self.pl2_score:
                    print('pl1 win')
                    self.winner.append(1)  # player 1 wins
                elif self.pl2_score > self.pl1_score:
                    print('pl2 win')
                    self.winner.append(2)  # player 2 wins
                else:
                    print('tie')
                    self.winner.append(3)  # tie
            else:
                game.print_board(board)
                self.winner.extend(self.set_child(choice,
                                                  tmp_pl1,
                                                  tmp_pl2,
                                                  tmp_board,
                                                  tmp_turn,
                                                  self.move))
        print(self.winner)

        self.pl1_win = (self.winner.count(1) / len(self.winner))
        self.pl2_win = (self.winner.count(2) / len(self.winner))
        self.tie = (self.winner.count(3) / len(self.winner))
        print(self.pl1_win, self.pl2_win, self.tie)

    def set_child(self, choice, pl1_score, pl2_score, board_state, turn, move):
        """ sets the child and returns winner to the parent """
        move += 1
        self.choices[choice] = Node(pl1_score, pl2_score,
                                    board_state, turn, self, move, choice, )
        win = self.choices[choice].winner
        with con:
            cur = con.cursor()
            cur.execute(
                "INSERT INTO move(move, piece, pl1, pl2, turn)"
                "VALUES(?,?,?,?,?)",
                (move,
                 choice,
                 pl1_score,
                 pl2_score,
                 turn))
        del self.choices[choice]
        return win

    def get_child(self, choice):
        return self.choices[choice]

    def get_parent(self):
        return self.parent


class Tree:
    def __init__(self, pl1, pl2, board, turn):
        self.game = Game()
        # root is the initial node of the tree
        self.root = Node(pl1, pl2, board, turn, self)
        # cur is the node in focus
        self.cur = self.root

    def move(self, choice):
        # switch cur to the selected choice
        self.cur = self.cur.get_child(choice)

    def back(self):
        self.cur = self.cur.get_parent()


if __name__ == '__main__':
    import sys

    # sys.setrecursionlimit(50000)
    board = {
        0: 3,
        1: 3,
        2: 3,
        3: 3,
        4: 3,
        5: 3,
        6: 0,
        7: 3,
        8: 3,
        9: 3,
        10: 3,
        11: 3,
        12: 3,
        13: 0
    }


    tree = Tree(0, 0, board, 1)
