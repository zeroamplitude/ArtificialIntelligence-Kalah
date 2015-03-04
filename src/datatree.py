__author__ = 'Nicholas De Souza'
# 

class Game:
    def __init__(self):
        self.pl1_score = 0
        self.pl2_score = 0
        self.board = {}
        self.turn = 1

    def play(self, piece):

        return self.pl1_score, self.pl2_score, self.board, self.turn


class Node:

    game = Game()

    def __init__(self, pl1_score, pl2_score, board, turn, parent):
        # Pl1 score
        self.pl1 = pl1_score
        # Pl2 score
        self.pl2 = pl2_score
        # State of the board
        self.board = board
        # who makes the choice
        self.turn = turn
        # the previous choice
        self.parent = parent
        # the choices available based on
        # board state
        self.choices = {}

        temp_choice = []
        if turn == 1:
            for i in range(1, 7):
                if self.board[i] != 0:
                    temp_choice.append(i)
        else:
            for i in range(7, 14):
                if self.board[i] != 0:
                    temp_choice.append(i)

        # if there are no choices - gameover
        if not temp_choice:
            # indicate end of branch by setting children to none
            self.choices = None

            # set the winner of the game
            if self.pl1 > self.pl2:
                self.winner = 1  # player 1 wins
            elif self.pl2 > self.pl1:
                self.winner = 2  # player 2 wins
            elif self.pl1 == self.pl2:
                self.winner = 3  # tie
        else:
            # play out all choices available
            for choice in temp_choice:
                # play move and return game data
                pl1_score, pl2_score, board, turn = Node.game.play(choice)
                # create a child with game data
                # this will recurse until there are no choices

#### This needs to store wins and losses and set a win/lose %################

                self.winner = self.set_child(choice, pl1_score,
                                             pl2_score, board, turn)

#############################################################################

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
    def __init__(self, board, turn):
        # root is the initial node of the tree
        self.root = Node(board, turn, self)
        # cur is the node in focus
        self.cur = self.root

    def set_move(self, choice, board_state, turn):
        self.cur.set_child(choice, board_state, turn)

    def move(self, choice):
        # switch cur to the selected choice
        self.cur = self.cur.get_child(choice)

    def back(self):
        self.cur = self.cur.get_parent()