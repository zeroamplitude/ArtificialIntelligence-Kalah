__author__ = 'Nicholas De Souza'


def across(piece):
    return piece + ((6 - piece) * 2)


class Game:
    """ fake game class to represent build actions """
    def __init__(self):
        self.pl1_score = 0
        self.pl2_score = 0
        self.board = {
            0:  3,
            1:  3,
            2:  3,
            3:  3,
            4:  3,
            5:  3,
            6:  0,
            7:  3,
            8:  3,
            9:  3,
            10: 3,
            11: 3,
            12: 3,
            13: 0
        }
        self.turn = 1

    def get_stats(self):
        return self.pl1_score, self.pl2_score, self.board, self.turn

    def set_state(self, pl1_score, pl2_score, board, turn):
        self.pl1_score = pl1_score
        self.pl2_score = pl2_score
        self.board = board
        self.turn = turn

    def print_board(self):
        pl1 = [self.board.get(i) for i in range(7)]
        pl2 = [self.board.get(i) for i in range(7, 14)]

        pl2.reverse()
        print(pl2)
        print('  ', pl1)

    def play(self, piece, player):
        piece -= 1
        if player == 1:
            seeds = self.board.get(piece)
            self.board[piece] = 0
        else:
            piece = across(piece)
            seeds = self.board.get(across(piece))
            self.board[piece] = 0

        # print('piece', piece)
        for i in range(1, seeds + 1):
            # print('piece + i > 13', (piece + i) > 13)
            if (piece + i) > 13:
                cur = (piece + i) % 14
            else:
                cur = i + piece

            # print('cur', cur)
            # print('i == seeds:', i == seeds)
            if i == seeds:
                # print('cur != 6 or 13:', cur != 6 and cur != 13)
                if cur != 6 and cur != 13:
                    # print('self.board[cur] == 0', self.board[cur] == 0)
                    if self.board[cur] == 0:
                        # print('pl = 1 && 0 <= cur <= 5',
                        #       player == 1 and 0 <= cur <= 5)
                        # print('pl = 2 && 7 <= cur <= 12',
                        #       player == 2 and 7 <= cur <= 12)
                        if player == 1 and 0 <= cur <= 5:
                            # print('board[across(cur)] != 0:',
                            #       self.board[across(cur)] != 0)
                            if self.board[across(cur)] != 0:
                                self.board[6] += (self.board.get(cur) +
                                                  self.board.get(across(cur)))
                                self.board[cur] = 0
                                self.board[across(cur)] = 0
                                self.turn = 1 if player == 2 else 2
                                break
                        elif player == 2 and (7 <= cur <= 12):

                            # print('board[back_across(cur)] != 0',
                            #       self.board[across(cur)] != 0)
                            if self.board[across(cur)] != 0:
                                self.board[13] += (self.board.get(cur) +
                                                   self.board.get(
                                                       across(cur)))
                                self.board[cur] = 0
                                self.board[across(cur)] = 0
                                self.turn = 1 if player == 2 else 2
                                break
                else:
                    self.board[cur] += 1
                    self.turn = 1 if player == 1 else 2
                    break
            self.board[cur] += 1
            self.turn = 1 if player == 2 else 2

        return self.pl1_score, self.pl2_score, self.board, self.turn


class Node:

    game = Game()

    def __init__(self, pl1_score, pl2_score, board, turn, parent):

        Node.game.set_state(pl1_score, pl2_score, board, turn)

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

        temp_choice = []
        if turn == 1:
            for i in range(6):
                if self.board[i] != 0:
                    temp_choice.append(i+1)
        else:
            for i in range(7, 14):
                if self.board[i] != 0:
                    temp_choice.append(i+1)

        # if there are no choices - gameover
        if not temp_choice:
            # indicate end of branch by setting children to none
            self.choices = None

            # set the winner of the game
            if self.pl1_score > self.pl2_score:
                self.winner.append(1)  # player 1 wins
            elif self.pl2_score > self.pl1_score:
                self.winner.append(2)  # player 2 wins
            elif self.pl1_score == self.pl2_score:
                self.winner.append(3)  # tie
        else:
            # play out all choices available
            for choice in temp_choice:
                # play move and return game data
                pl1_score, pl2_score, board, turn = Node.game.play(choice,
                                                                   turn)
                # create a child with game data
                # this will recurse until there are no choices
                # the return of set_child is the winners of the the
                # branch
                self.winner.extend(self.set_child(choice, pl1_score,
                                                  pl2_score, board, turn))

                #############################################################################

        self.pl1_win = (self.winner.count(1) / len(self.winner))
        self.pl2_win = (self.winner.count(2) / len(self.winner))
        self.tie = (self.winner.count(3) / len(self.winner))

    def set_child(self, choice, pl1_score, pl2_score, board_state, turn):
        """ sets the child and returns winner to the parent """
        self.choices[choice] = Node(pl1_score, pl2_score,
                                    board_state, turn, self)
        return self.choices[choice].winner

    def get_child(self, choice):
        return self.choices[choice]

    def get_parent(self):
        return self.parent


class Tree:
    def __init__(self, pl1, pl2, board, turn):
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
    # print(across(7))
    game = Game()
    pl1, pl2, board, turn = game.get_stats()
    tree = Tree(pl1, pl2, board, turn)
    tree