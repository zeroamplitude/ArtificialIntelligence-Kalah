import pygame
from math import floor, ceil
import sys
from time import sleep

__author__ = 'Nicholas De Souza'

CENTER = 'center'


class Gui:
    def __init__(self, size, color, bk_color=(0,0,0), pos=(0, 0), padding=0):
        self.size = size
        self.bk_color = bk_color
        self.color = color
        self.pos = pos
        self.padding = padding
        self.outer = None
        self.inner = None

    def draw(self):

        screen = pygame.display.get_surface()

        self.outer = pygame.draw.rect(screen,
                                      self.bk_color,
                                      (self.pos, self.size))

        self.inner = self.draw_inner(screen)

    def refreash(self):
        pygame.display.flip()

    def listen(self):
        event = pygame.event.wait()
        if event.type == pygame.QUIT:
            sys.exit()
        return event

    def draw_inner(self, screen):
        tmp_size, tmp_pos = self.calc_padding()
        return pygame.draw.rect(screen, self.color, (tmp_pos, tmp_size))

    def calc_padding(self):
        tmp_size = self.size[0] - 2*self.padding, self.size[1] - 2*self.padding
        tmp_pos = self.pos[0] + self.padding, self.pos[1] + self.padding
        return tmp_size, tmp_pos

    def new_pos(self, pos):
        self.pos = pos

    def check(self, x, y):
        x_check = self.pos[0] <= x <= self.pos[0] + self.size[0]
        y_check = self.pos[1] <= y <= self.pos[1] + self.size[1]
        return True == x_check == y_check


class Board(Gui):
    def __init__(self, size=(800, 200), color=(0,0,0),
                 bk_color=(0,0,0), pos=(0,0), padding=10):
        Gui.__init__(self, size=size, color=color, bk_color=bk_color,
                     pos= pos, padding=padding)
        self.pieces = {}

        h1 = House(size=(100, 200), color=(255, 255, 255), padding=10)

        self.insert_piece(13, h1, (0, 0))
        for i in range(1, 8):
            if i == 7:
                h = House(size=(100, 200), color=(255, 255, 255), padding=10)
                self.insert_piece(i-1, h, (h1.pos[0] + (h1.size[0] * i),
                                           h1.pos[1]))
                break

            s1 = Store(size=(100, 100), color=(255, 255, 255), padding=10)
            s2 = Store(size=(100, 100), color=(255, 255, 255), padding=10)

            self.insert_piece(i-1, s1, (h1.pos[0] + (h1.size[0] * i),
                                h1.pos[1] + int(h1.size[1])//2))

            self.insert_piece(i-1
                              + ((6-(i-1))*2), s2, (h1.pos[0]
                                                    + (h1.size[0]* i),h1.pos[1]))

        for i in range(6):
            for x in range(3):
                self.pieces[i].add_seed(Seed())
                self.pieces[i +((6-i)*2)].add_seed(Seed())

    def insert_piece(self, id, piece, pos):
        x_n = self.pos[0] + pos[0]
        y_n = self.pos[1] + pos[1]
        piece.new_pos((x_n, y_n))
        self.pieces[id] = piece

    def draw(self):
        Gui.draw(self)
        for piece in self.pieces.values():
            piece.draw()

        self.refreash()

    def listen(self):
        event = Gui.listen(self)
        if event.type == pygame.MOUSEBUTTONDOWN:
            mouse = pygame.mouse.get_pos()
            for i in range(6):
                if self.pieces[i].check(mouse[0], mouse[1]):
                    return i
                if self.pieces[i + ((6 - i) * 2)].check(mouse[0],mouse[1]):
                    return  (i + ((6 - i) * 2))

    def move(self, piece, dest):
            self.pieces[piece].move_seed(self.pieces[dest])
            self.draw()
            sleep(0.25)

    def move_all(self, piece):
        for i in range(len(self.pieces[piece].seeds)):
            if piece < 6:
                self.pieces[piece].move_seed(self.pieces[6])
            else:
                self.pieces[piece].move_seed(self.pieces[13])
            self.draw()

        for i in range(len(self.pieces[piece + ((6-piece) * 2)].seeds)):
            if piece < 6:
                self.pieces[piece + ((6 - piece) * 2)].move_seed(
                    self.pieces[6])
            else:
                self.pieces[piece + ((6 - piece) * 2)].move_seed(
                    self.pieces[13])
            self.draw()

    def clean(self):
        for i in range(6):
            for s in self.pieces[i].seeds:
                self.pieces[i].move_seed(self.pieces[6])
            for s in self.pieces[i+((6-i)*2)].seeds:
                self.pieces[i+((6-i)*2)].move_seed(self.pieces[13])


class Seed(Gui):
    def __init__(self, size=(15, 15), color=(0,0,0), pos=(0, 0), padding=1):
        Gui.__init__(self, size=size, color=color, bk_color=(255,255,255),
                     pos=pos, padding=2)
        self.target = None

    def set_target(self, pos):
        self.target = pos

    def draw(self):
        self.pos = self.target
        Gui.draw(self)

    def draw_inner(self, screen):
        tmp_pos = self.pos[0] + self.padding, self.pos[1] + self.padding
        tmp_size = int(floor(float(self.size[0]//2) - self.padding))
        return pygame.draw.circle(screen, (0,0,0), tmp_pos, tmp_size)



class House(Gui):
    def __init__(self, size, color, pos=(0,0), bk_color=(0,0,0), padding=0):
        Gui.__init__(self, size, color, bk_color, pos, padding)
        self.seeds = []

    def draw(self):
        Gui.draw(self)
        for seed in self.seeds:
            seed.draw()

    def add_seed(self, seed):
        self.seeds.append(seed)
        self.organize()

    def organize(self):
        num_items = len(self.seeds)

        x_items = int(ceil(self.size[0] // ((ceil(num_items / 2.0) *
                                                    self.seeds[0].size[0]))))
        y_items = int(ceil(self.size[1] // ((ceil(num_items / 2.0) *
                                                    self.seeds[0].size[1]))))

        if num_items != 0:
            x = 1
            y = 1
            for seed in self.seeds:
                seed.set_target((self.pos[0] + (x * (seed.size[0])),
                                 self.pos[1] + (y * (seed.size[1]))
                ))
                x += 1
                if x > x_items + 1:
                    x = 1
                    y += 1

    def move_seed(self, destination):
        if len(self.seeds) != 0:
            destination.add_seed(self.seeds.pop())


class Store(House):
    def __init__(self, size, color, bk_color=(0,0,0), pos=(0, 0), padding=0):
        House.__init__(self, size, color,pos, bk_color, padding)


if __name__ == '__main__':
    pygame.init()
    clock = pygame.time.Clock()
    s = pygame.display.set_mode((800, 500))

    b = Board(size=(800, 500), bk_color=(0, 0, 0), color=(255, 255, 255),
              padding=0)

    while True:
        clock.tick(60)
        b.draw()
        b.listen()