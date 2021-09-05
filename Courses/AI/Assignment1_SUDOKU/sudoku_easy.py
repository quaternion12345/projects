#############################
univ_id = "0000"           ##
#############################

from tkinter import Tk, Canvas, Frame, Button, BOTH, TOP, LEFT, RIGHT, BOTTOM
from Problem import Problem
import random
import sudoku_50 as problem_set

MARGIN = 20  # Pixels around the board
SIDE = 50  # Width of every board cell.
WIDTH = HEIGHT = MARGIN * 2 + SIDE * 9  # Width and height of the whole board
DELAY = 0.05  # the delay time makes the changing number visible
#PROBLEM_NUM = problem_set.problem_num

class SudokuUI(Frame):
    def __init__(self, parent):
        Frame.__init__(self, parent)
        self.parent = parent
        self.__initUI()

    def __initUI(self):
        self.parent.title("AI: Sudoku Solver")
        self.pack(fill=BOTH)
        self.canvas = Canvas(self, width=WIDTH, height=HEIGHT + 10)
        self.easy_problem = Problem(self.canvas, 0, DELAY, random.randint(0, 1), setting = "easy")
        self.hard_problem = Problem(self.canvas, 0, DELAY, random.randint(0, 1), setting = "hard")

        self.problem = None # Set this by click button
        
        
        # Initialize each cell in the puzzle
        for i in range(1, 10):
            for j in range(1, 10):
                self.item = self.canvas.create_text(
                    MARGIN + (j - 1) * SIDE + SIDE / 2, MARGIN + (i - 1) * SIDE + SIDE / 2,
                    text='', tags="numbers", fill="black", font=("Helvetica", 12)
                )
        self.item = self.canvas.create_text(40, 490, text='Count :', fill="black", font=("Helvetica", 13))
        self.item = self.canvas.create_text(95, 490, text='', fill="black", font=("Helvetica", 13))
        self.item = self.canvas.create_text(170, 490, text='Average :', fill="black", font=("Helvetica", 13))
        self.item = self.canvas.create_text(225, 490, text='',fill="black", font=("Helvetica", 13))
        self.item = self.canvas.create_text(320, 490, text='Ranking :',fill="black", font=("Helvetica", 13))
        self.item = self.canvas.create_text(370, 490, text='', fill="black", font=("Helvetica", 13))
        self.item = self.canvas.create_text(420, 490, text='Total :', fill="black", font=("Helvetica", 13))
        self.item = self.canvas.create_text(460, 490, text='', fill="black", font=("Helvetica", 13))
        self.canvas.pack(fill=BOTH, side=TOP)
        self.start_button1 = Button(self, text="__Hard__", command=self.__start_hard_solver)
        self.start_button2 = Button(self, text="__Easy__", command=self.__start_easy_solver)
        
        self.start_button2.pack(side=LEFT)
        self.start_button1.pack(side=RIGHT)
        #self.start_button2.config(state="disabled")
        self.__draw_grid()

    # Draws 9x9 grid
    def __draw_grid(self):
        for i in range(10):
            width = 3 if i % 3 == 0 else 1
            x0 = MARGIN + i * SIDE
            y0 = MARGIN
            x1 = MARGIN + i * SIDE
            y1 = HEIGHT - MARGIN
            self.canvas.create_line(x0, y0, x1, y1, fill="black", width=width)
            x0 = MARGIN
            y0 = MARGIN + i * SIDE
            x1 = WIDTH - MARGIN
            y1 = MARGIN + i * SIDE
            self.canvas.create_line(x0, y0, x1, y1, fill="black", width=width)
    def __start_easy_solver(self):
      self.problem = self.easy_problem
      self.__start_solver()
      
    def __start_hard_solver(self):
      self.problem = self.hard_problem
      self.__start_solver()
      
      
    def __start_solver(self):
        self.start_button1.config(state="disabled")
        self.start_button2.config(state="disabled")
        for i in range(self.problem.problem_num):
            for m in range(1, 10):
                for n in range(1, 10):
                    if(self.problem.given_number[m-1][n-1] != 0): self.canvas.itemconfig(9 * (m - 1) + n, text=self.problem.given_number[m-1][n-1], tags="numbers", fill="blue")
                    else:self.canvas.itemconfig(9 * (m - 1) + n, text='', tags="numbers", fill="black")
                    
            self.solver_class = solver_class(self.problem)
            self.solver_class.solver()
            if self.problem.finished==0:
                self.problem.fail()
                return
            self.canvas.update()
            
            
            if(i != self.problem.problem_num -1):  self.problem = Problem(self.canvas, self.problem.tk, 0.0, self.problem.temp, setting = self.problem.setting)
        self.problem.update_a()
        #self.start_button2.config(state="active")
        
        #If the problem has finished, this function will display "Finished!"
        self.problem.is_done()
        
    def __submit(self):
        request=self.problem.submit(univ_id, "")
        message=request.split(',')
        if int(message[0]) == 100:
            self.problem.fail_10min()
        elif int(message[0]) == 101:
            self.canvas.update()
            self.canvas.itemconfig(87, text=int(message[1]), tags="numbers", fill="blue")
            self.canvas.itemconfig(89, text=int(message[2]), tags="numbers", fill="blue")
            self.problem.already_done()
        elif int(message[0]) == 102:
            self.canvas.update()
            self.canvas.itemconfig(87, text=int(message[1]), tags="numbers", fill="blue")
            self.canvas.itemconfig(89, text=int(message[2]), tags="numbers", fill="blue")
            self.problem.is_done()
        elif int(message[0]) == 501:
            #   print "501"
            self.problem.wrong_id_pw()

class solver_class():
    def __init__(self, problem):
        self.problem = problem
        self.given_number = problem.given_number

    def solver(self):
        self.puzzle = []
        new = []
        for i in range(0, 9):
            for j in range(0, 9):
                if(self.given_number[i][j] != 0): new.append(self.given_number[i][j])
                else: new.append(0)
            self.puzzle.append(new)
            new = []


        # TO DO: need to write solver
        # Your code goes here...
        # this is example using for loop
        # you have to improve this code
        # puzzle[y][x] == 0 then it is ungiven number else it is given number
        # given_number[y][x] == 0 then it is ungiven number else it is given number

        hint = self.check_rule()
        self.upgrade_hint(hint)
        order = self.make_order(hint)
        # x coordinate is order[0][0] and y coordinate is order[0][1]
        while(len(order) != 0):
            i = order[0][1]
            j = order[0][0]
            for k in hint[9 * (i-1) + (j-1)]:
                output = self.problem.checker(i, j, k)  # Try to input 'K' & This increases the number of attempts
                if output == 1:  # if the value is correct, checker will output 1. Otherwise, output is 0.
                    self.puzzle[i-1][j-1]=k
                    self.update_hint(i-1, j-1, hint)
                    self.upgrade_hint(hint)
                    order = self.make_order(hint)
                    break



    def check_rule(self):
        # 2-dimension list for saving possible values for each node
        self.possible = []
        new = [1,2,3,4,5,6,7,8,9]
        # x coordinate is j and y coordinate is i
        # puzzle[y][x]
        for i in range(1,10):
            for j in range(1,10):
                if self.puzzle[i-1][j-1] == 0:    # if it is ungiven number
                    # append constraint numbers, standard coordinate index is (i-1, j-1)
                    # append horizontal and vertical constraints
                    # temp is list for impossible numbers
                    # new is list for possible numbers
                    temp = []
                    new = [1,2,3,4,5,6,7,8,9]
                    for k in range(1,10):
                        if self.puzzle[i-1][k-1] != 0 and self.puzzle[i-1][k-1] not in temp:
                            temp.append(self.puzzle[i-1][k-1])
                        if self.puzzle[k-1][j-1] != 0 and self.puzzle[k-1][j-1] not in temp:
                            temp.append(self.puzzle[k-1][j-1])
                    # append near constraints
                    if i % 3 == 1:
                        if j % 3 == 1: # case 1
                            if self.puzzle[i - 0][j - 0] != 0 and self.puzzle[i - 0][j - 0] not in temp:
                                temp.append(self.puzzle[i-0][j-0])
                            if self.puzzle[i - 0][j + 1] != 0 and self.puzzle[i - 0][j + 1] not in temp:
                                temp.append(self.puzzle[i-0][j+1])
                            if self.puzzle[i + 1][j - 0] != 0 and self.puzzle[i + 1][j - 0] not in temp:
                                temp.append(self.puzzle[i+1][j-0])
                            if self.puzzle[i + 1][j + 1] != 0 and self.puzzle[i + 1][j + 1] not in temp:
                                temp.append(self.puzzle[i+1][j+1])

                        elif j % 3 == 2: # case 2
                            if self.puzzle[i - 0][j - 2] != 0 and self.puzzle[i - 0][j - 2] not in temp:
                                temp.append(self.puzzle[i-0][j-2])
                            if self.puzzle[i - 0][j - 0] != 0 and self.puzzle[i - 0][j - 0] not in temp:
                                temp.append(self.puzzle[i-0][j-0])
                            if self.puzzle[i + 1][j - 2] != 0 and self.puzzle[i + 1][j - 2] not in temp:
                                temp.append(self.puzzle[i+1][j-2])
                            if self.puzzle[i + 1][j - 0] != 0 and self.puzzle[i + 1][j - 0] not in temp:
                                temp.append(self.puzzle[i+1][j-0])

                        else: # case 3
                            if self.puzzle[i - 0][j - 3] != 0 and self.puzzle[i - 0][j - 3] not in temp:
                                temp.append(self.puzzle[i-0][j-3])
                            if self.puzzle[i - 0][j - 2] != 0 and self.puzzle[i - 0][j - 2] not in temp:
                                temp.append(self.puzzle[i-0][j-2])
                            if self.puzzle[i + 1][j - 3] != 0 and self.puzzle[i + 1][j - 3] not in temp:
                                temp.append(self.puzzle[i+1][j-3])
                            if self.puzzle[i + 1][j - 2] != 0 and self.puzzle[i + 1][j - 2] not in temp:
                                temp.append(self.puzzle[i+1][j-2])

                    elif i % 3 == 2:
                        if j % 3 == 1: # case 4
                            if self.puzzle[i - 2][j - 0] != 0 and self.puzzle[i - 2][j - 0] not in temp:
                                temp.append(self.puzzle[i-2][j-0])
                            if self.puzzle[i - 2][j + 1] != 0 and self.puzzle[i - 2][j + 1] not in temp:
                                temp.append(self.puzzle[i-2][j+1])
                            if self.puzzle[i - 0][j - 0] != 0 and self.puzzle[i - 0][j - 0] not in temp:
                                temp.append(self.puzzle[i-0][j-0])
                            if self.puzzle[i - 0][j + 1] != 0 and self.puzzle[i - 0][j + 1] not in temp:
                                temp.append(self.puzzle[i-0][j+1])

                        elif j % 3 == 2: # case 5
                            if self.puzzle[i - 2][j - 2] != 0 and self.puzzle[i - 2][j - 2] not in temp:
                                temp.append(self.puzzle[i-2][j-2])
                            if self.puzzle[i - 2][j - 0] != 0 and self.puzzle[i - 2][j - 0] not in temp:
                                temp.append(self.puzzle[i-2][j-0])
                            if self.puzzle[i - 0][j - 2] != 0 and self.puzzle[i - 0][j - 2] not in temp:
                                temp.append(self.puzzle[i-0][j-2])
                            if self.puzzle[i - 0][j - 0] != 0 and self.puzzle[i - 0][j - 0] not in temp:
                                temp.append(self.puzzle[i-0][j-0])

                        else: # case 6
                            if self.puzzle[i - 2][j - 3] != 0 and self.puzzle[i - 2][j - 3] not in temp:
                                temp.append(self.puzzle[i-2][j-3])
                            if self.puzzle[i - 2][j - 2] != 0 and self.puzzle[i - 2][j - 2] not in temp:
                                temp.append(self.puzzle[i-2][j-2])
                            if self.puzzle[i - 0][j - 3] != 0 and self.puzzle[i - 0][j - 3] not in temp:
                                temp.append(self.puzzle[i-0][j-3])
                            if self.puzzle[i - 0][j - 2] != 0 and self.puzzle[i - 0][j - 2] not in temp:
                                temp.append(self.puzzle[i-0][j-2])
                    else:
                        if j % 3 == 1: # case 7
                            if self.puzzle[i - 3][j - 0] != 0 and self.puzzle[i - 3][j - 0] not in temp:
                                temp.append(self.puzzle[i-3][j-0])
                            if self.puzzle[i - 3][j + 1] != 0 and self.puzzle[i - 3][j + 1] not in temp:
                                temp.append(self.puzzle[i-3][j+1])
                            if self.puzzle[i - 2][j - 0] != 0 and self.puzzle[i - 2][j - 0] not in temp:
                                temp.append(self.puzzle[i-2][j-0])
                            if self.puzzle[i - 2][j + 1] != 0 and self.puzzle[i - 2][j + 1] not in temp:
                                temp.append(self.puzzle[i-2][j+1])

                        elif j % 3 == 2: # case 8
                            if self.puzzle[i - 3][j - 2] != 0 and self.puzzle[i - 3][j - 2] not in temp:
                                temp.append(self.puzzle[i-3][j-2])
                            if self.puzzle[i - 3][j - 0] != 0 and self.puzzle[i - 3][j - 0] not in temp:
                                temp.append(self.puzzle[i-3][j-0])
                            if self.puzzle[i - 2][j - 2] != 0 and self.puzzle[i - 2][j - 2] not in temp:
                                temp.append(self.puzzle[i-2][j-2])
                            if self.puzzle[i - 2][j - 0] != 0 and self.puzzle[i - 2][j - 0] not in temp:
                                temp.append(self.puzzle[i-2][j-0])

                        else: # case 9
                            if self.puzzle[i - 3][j - 3] != 0 and self.puzzle[i - 3][j - 3] not in temp:
                                temp.append(self.puzzle[i-3][j-3])
                            if self.puzzle[i - 3][j - 2] != 0 and self.puzzle[i - 3][j - 2] not in temp:
                                temp.append(self.puzzle[i-3][j-2])
                            if self.puzzle[i - 2][j - 3] != 0 and self.puzzle[i - 2][j - 3] not in temp:
                                temp.append(self.puzzle[i-2][j-3])
                            if self.puzzle[i - 2][j - 2] != 0 and self.puzzle[i - 2][j - 2] not in temp:
                                temp.append(self.puzzle[i-2][j-2])

                    # make possible list for (i-1,j-1)
                    new = [x for x in new if x not in temp]
                else: # if it is given number then make possible list empty
                    new = []
                # append possible list to whole possible list container
                self.possible.append(new)
        return self.possible

    def update_hint(self, y, x, lst):
        # update possible list
        # x is j-1 and y is i-1
        number = self.puzzle[y][x]
        # update vertical and horizontal
        for i in range(0, 9):
            lst[9 * i + x] = [n for n in lst[9 * i + x] if n is not number]
            lst[9 * y + i] = [n for n in lst[9 * y + i] if n is not number]
        # update near ones
        if (x+1) % 3 == 1:
            if (y+1) % 3 == 1: # case 1
                lst[9 * (y+1) + (x+1)] = [n for n in lst[9 * (y+1) + (x+1)] if n is not number]
                lst[9 * (y+1) + (x+2)] = [n for n in lst[9 * (y+1) + (x+2)] if n is not number]
                lst[9 * (y+2) + (x+1)] = [n for n in lst[9 * (y+2) + (x+1)] if n is not number]
                lst[9 * (y+2) + (x+2)] = [n for n in lst[9 * (y+2) + (x+2)] if n is not number]

            elif (y+1) % 3 == 2: # case 4
                lst[9 * (y - 1) + (x + 1)] = [n for n in lst[9 * (y - 1) + (x + 1)] if n is not number]
                lst[9 * (y + 1) + (x + 1)] = [n for n in lst[9 * (y + 1) + (x + 1)] if n is not number]
                lst[9 * (y - 1) + (x + 2)] = [n for n in lst[9 * (y - 1) + (x + 2)] if n is not number]
                lst[9 * (y + 1) + (x + 2)] = [n for n in lst[9 * (y + 1) + (x + 2)] if n is not number]

            else: # case 7
                lst[9 * (y - 2) + (x + 1)] = [n for n in lst[9 * (y - 2) + (x + 1)] if n is not number]
                lst[9 * (y - 1) + (x + 1)] = [n for n in lst[9 * (y - 1) + (x + 1)] if n is not number]
                lst[9 * (y - 2) + (x + 2)] = [n for n in lst[9 * (y - 2) + (x + 2)] if n is not number]
                lst[9 * (y - 1) + (x + 2)] = [n for n in lst[9 * (y - 1) + (x + 2)] if n is not number]

        elif (x+1) % 3 == 2:
            if (y+1) % 3 == 1:  # case 2
                lst[9 * (y + 1) + (x - 1)] = [n for n in lst[9 * (y + 1) + (x - 1)] if n is not number]
                lst[9 * (y + 2) + (x - 1)] = [n for n in lst[9 * (y + 2) + (x - 1)] if n is not number]
                lst[9 * (y + 1) + (x + 1)] = [n for n in lst[9 * (y + 1) + (x + 1)] if n is not number]
                lst[9 * (y + 2) + (x + 1)] = [n for n in lst[9 * (y + 2) + (x + 1)] if n is not number]

            elif (y+1) % 3 == 2:  # case 5
                lst[9 * (y - 1) + (x - 1)] = [n for n in lst[9 * (y - 1) + (x - 1)] if n is not number]
                lst[9 * (y + 1) + (x - 1)] = [n for n in lst[9 * (y + 1) + (x - 1)] if n is not number]
                lst[9 * (y - 1) + (x + 1)] = [n for n in lst[9 * (y - 1) + (x + 1)] if n is not number]
                lst[9 * (y + 1) + (x + 1)] = [n for n in lst[9 * (y + 1) + (x + 1)] if n is not number]

            else:  # case 8
                lst[9 * (y - 2) + (x - 1)] = [n for n in lst[9 * (y - 2) + (x - 1)] if n is not number]
                lst[9 * (y - 1) + (x - 1)] = [n for n in lst[9 * (y - 1) + (x - 1)] if n is not number]
                lst[9 * (y - 2) + (x + 1)] = [n for n in lst[9 * (y - 2) + (x + 1)] if n is not number]
                lst[9 * (y - 1) + (x + 1)] = [n for n in lst[9 * (y - 1) + (x + 1)] if n is not number]

        else:
            if (y+1) % 3 == 1:  # case 3
                lst[9 * (y + 1) + (x - 1)] = [n for n in lst[9 * (y + 1) + (x - 1)] if n is not number]
                lst[9 * (y + 1) + (x - 2)] = [n for n in lst[9 * (y + 1) + (x - 2)] if n is not number]
                lst[9 * (y + 2) + (x - 1)] = [n for n in lst[9 * (y + 2) + (x - 1)] if n is not number]
                lst[9 * (y + 2) + (x - 2)] = [n for n in lst[9 * (y + 2) + (x - 2)] if n is not number]

            elif (y+1) % 3 == 2:  # case 6
                lst[9 * (y - 1) + (x - 1)] = [n for n in lst[9 * (y - 1) + (x - 1)] if n is not number]
                lst[9 * (y + 1) + (x - 1)] = [n for n in lst[9 * (y + 1) + (x - 1)] if n is not number]
                lst[9 * (y - 1) + (x - 2)] = [n for n in lst[9 * (y - 1) + (x - 2)] if n is not number]
                lst[9 * (y + 1) + (x - 2)] = [n for n in lst[9 * (y + 1) + (x - 2)] if n is not number]

            else:  # case 9
                lst[9 * (y - 2) + (x - 1)] = [n for n in lst[9 * (y - 2) + (x - 1)] if n is not number]
                lst[9 * (y - 1) + (x - 1)] = [n for n in lst[9 * (y - 1) + (x - 1)] if n is not number]
                lst[9 * (y - 2) + (x - 2)] = [n for n in lst[9 * (y - 2) + (x - 2)] if n is not number]
                lst[9 * (y - 1) + (x - 2)] = [n for n in lst[9 * (y - 1) + (x - 2)] if n is not number]
        lst[9 * y + x] = []
        return lst

    def make_order(self, lst):
        # make heuristic list for ordering
        # list consists of x coordinate, y coordinate, length of possible list
        # lst[9 * (y-1) + (x-1)]
        heuristic = []
        new = []
        x = 1
        y = 1
        for i in lst:
            # (1,1) -> (2,1) -> (3,1) -> ...
            if len(i) > 0:
                if(x < 9):
                    new.append(x)
                    new.append(y)
                    x += 1
                else:
                    new.append(x)
                    new.append(y)
                    x = 1
                    y += 1
                new.append(len(i))
                heuristic.append(new)
                new = []
            else:
                if(x < 9):
                    x += 1
                else:
                    x = 1
                    y += 1
        random.shuffle(heuristic)
        sorted_heuristic = sorted(heuristic, key=lambda i:i[2])
        return sorted_heuristic

    def upgrade_hint(self, lst):
        # (j,i)
        # sorting
        for i in range(0,9):
            for j in range(0,9):
                lst[9 * i + j].sort()
        # upgrading
        for i in range(0,9):
            for j in range(0,9):
                # check naked triple
                if len(lst[9 * i + j]) == 3:
                    # check column
                    ccount = 0
                    ctemp = []
                    for k in range(0,9):
                        if (k != i) and (len(lst[9 * k + j]) == 3) and (lst[9 * k + j] == lst[9 * i + j]):
                            ccount += 1
                            ctemp.append(k)
                    if ccount == 2:
                        for k in range(0,9):
                            if (k != i) and (k != ctemp[0]) and (k != ctemp[1]):
                                lst[9 * k + j] = [x for x in lst[9 * k + j] if x is not lst[9 * i + j][0]]
                                lst[9 * k + j] = [x for x in lst[9 * k + j] if x is not lst[9 * i + j][1]]
                                lst[9 * k + j] = [x for x in lst[9 * k + j] if x is not lst[9 * i + j][2]]
                    # check row
                    rcount = 0
                    rtemp = []
                    for k in range(0,9):
                        if (k != j) and (len(lst[9 * i + k]) == 3) and (lst[9 * i + k] == lst[9 * i + j]):
                            rcount += 1
                            rtemp.append(k)
                    if rcount == 2:
                        for k in range(0,9):
                            if (k != j) and (k != rtemp[0]) and (k != rtemp[1]):
                                lst[9 * i + k] = [x for x in lst[9 * i + k] if x is not lst[9 * i + j][0]]
                                lst[9 * i + k] = [x for x in lst[9 * i + k] if x is not lst[9 * i + j][1]]
                                lst[9 * i + k] = [x for x in lst[9 * i + k] if x is not lst[9 * i + j][2]]
                    # check box
                    bcount = 0
                    bctemp = []
                    brtemp = []
                    if (i+1) % 3 == 1:
                        if (j+1) % 3 == 1:
                            # case 1
                            if (len(lst[9 * (i+1) + (j+1)]) == 3) and (lst[9 * (i+1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i+1)
                            elif (len(lst[9 * (i+1) + (j+2)]) == 3) and (lst[9 * (i+1) + (j+2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+2)
                                brtemp.append(i+1)
                            elif (len(lst[9 * (i+2) + (j+1)]) == 3) and (lst[9 * (i+2) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i+2)
                            elif (len(lst[9 * (i+2) + (j+2)]) == 3) and (lst[9 * (i+2) + (j+2)] == lst[9 * i + j]):
                                bcount +=1
                                bctemp.append(j+2)
                                brtemp.append(i+2)
                            if bcount == 2:
                                if (((i+1) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j + 1)] = [x for x in lst[9 * (i + 1) + (j + 1)] if x is not lst[9 * i + j][2]]
                                if (((i+1) != brtemp[0]) or ((j+2) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j+2) != bctemp[1])):
                                    lst[9 * (i+1) + (j+2)] = [x for x in lst[9 * (i+1) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+2)] = [x for x in lst[9 * (i+1) + (j+2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j + 2)] = [x for x in lst[9 * (i + 1) + (j + 2)] if x is not lst[9 * i + j][2]]
                                if (((i+2) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i+2) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i+2) + (j+1)] = [x for x in lst[9 * (i+2) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j+1)] = [x for x in lst[9 * (i+2) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 2) + (j + 1)] = [x for x in lst[9 * (i + 2) + (j + 1)] if x is not lst[9 * i + j][2]]
                                if (((i+2) != brtemp[0]) or ((j+2) != bctemp[0])) and (((i+2) != brtemp[1]) or ((j+2) != bctemp[1])):
                                    lst[9 * (i+2) + (j+2)] = [x for x in lst[9 * (i+2) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j+2)] = [x for x in lst[9 * (i+2) + (j+2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 2) + (j + 2)] = [x for x in lst[9 * (i + 2) + (j + 2)] if x is not lst[9 * i + j][2]]
                        elif (j+1) % 3 == 2:
                            # case 2
                            if (len(lst[9 * (i+1) + (j-1)]) == 3) and (lst[9 * (i+1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i+1)
                            elif (len(lst[9 * (i+2) + (j-1)]) == 3) and (lst[9 * (i+1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i+2)
                            elif (len(lst[9 * (i+1) + (j+1)]) == 3) and (lst[9 * (i+1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i+1)
                            elif (len(lst[9 * (i+2) + (j+1)]) == 3) and (lst[9 * (i+2) + (j+1)] == lst[9 * i + j]):
                                bcount +=1
                                bctemp.append(j+1)
                                brtemp.append(i+2)
                            if bcount == 2:
                                if (((i+1) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j - 1)] = [x for x in lst[9 * (i + 1) + (j - 1)] if x is not lst[9 * i + j][2]]
                                if (((i+2) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i+2) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i+2) + (j-1)] = [x for x in lst[9 * (i+2) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j-1)] = [x for x in lst[9 * (i+2) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 2) + (j - 1)] = [x for x in lst[9 * (i + 2) + (j - 1)] if x is not lst[9 * i + j][2]]
                                if (((i+1) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j + 1)] = [x for x in lst[9 * (i + 1) + (j + 1)] if x is not lst[9 * i + j][2]]
                                if (((i+2) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i+2) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i+2) + (j+1)] = [x for x in lst[9 * (i+2) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j+1)] = [x for x in lst[9 * (i+2) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 2) + (j + 1)] = [x for x in lst[9 * (i + 2) + (j + 1)] if x is not lst[9 * i + j][2]]
                        else:
                            # case 3
                            if (len(lst[9 * (i+1) + (j-2)]) == 3) and (lst[9 * (i+1) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-2)
                                brtemp.append(i+1)
                            elif (len(lst[9 * (i+1) + (j-1)]) == 3) and (lst[9 * (i+1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i+1)
                            elif (len(lst[9 * (i+2) + (j-2)]) == 3) and (lst[9 * (i+2) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-2)
                                brtemp.append(i+2)
                            elif (len(lst[9 * (i+2) + (j-1)]) == 3) and (lst[9 * (i+2) + (j-1)] == lst[9 * i + j]):
                                bcount +=1
                                bctemp.append(j-1)
                                brtemp.append(i+2)
                            if bcount == 2:
                                if (((i+1) != brtemp[0]) or ((j-2) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j-2) != bctemp[1])):
                                    lst[9 * (i+1) + (j-2)] = [x for x in lst[9 * (i+1) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-2)] = [x for x in lst[9 * (i+1) + (j-2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j - 2)] = [x for x in lst[9 * (i + 1) + (j - 2)] if x is not lst[9 * i + j][2]]
                                if (((i+1) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j - 1)] = [x for x in lst[9 * (i + 1) + (j - 1)] if x is not lst[9 * i + j][2]]
                                if (((i+2) != brtemp[0]) or ((j-2) != bctemp[0])) and (((i+2) != brtemp[1]) or ((j-2) != bctemp[1])):
                                    lst[9 * (i+2) + (j-2)] = [x for x in lst[9 * (i+2) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j-2)] = [x for x in lst[9 * (i+2) + (j-2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 2) + (j - 2)] = [x for x in lst[9 * (i + 2) + (j - 2)] if x is not lst[9 * i + j][2]]
                                if (((i+2) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i+2) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i+2) + (j-1)] = [x for x in lst[9 * (i+2) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j-1)] = [x for x in lst[9 * (i+2) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 2) + (j - 1)] = [x for x in lst[9 * (i + 2) + (j - 1)] if x is not lst[9 * i + j][2]]
                    elif (i+1) % 3 == 2:
                        if (j+1) % 3 == 1:
                            # case 4
                            if (len(lst[9 * (i-1) + (j+1)]) == 3) and (lst[9 * (i-1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i-1)
                            elif (len(lst[9 * (i-1) + (j+2)]) == 3) and (lst[9 * (i-1) + (j+2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+2)
                                brtemp.append(i-1)
                            elif (len(lst[9 * (i+1) + (j+1)]) == 3) and (lst[9 * (i+1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i+1)
                            elif (len(lst[9 * (i+1) + (j+2)]) == 3) and (lst[9 * (i+1) + (j+2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+2)
                                brtemp.append(i+1)
                            if bcount == 2:
                                if (((i-1) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j + 1)] = [x for x in lst[9 * (i - 1) + (j + 1)] if x is not lst[9 * i + j][2]]
                                if (((i-1) != brtemp[0]) or ((j+2) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j+2) != bctemp[1])):
                                    lst[9 * (i-1) + (j+2)] = [x for x in lst[9 * (i-1) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+2)] = [x for x in lst[9 * (i-1) + (j+2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j + 2)] = [x for x in lst[9 * (i - 1) + (j + 2)] if x is not lst[9 * i + j][2]]
                                if (((i+1) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j + 1)] = [x for x in lst[9 * (i + 1) + (j + 1)] if x is not lst[9 * i + j][2]]
                                if (((i+1) != brtemp[0]) or ((j+2) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j+2) != bctemp[1])):
                                    lst[9 * (i+1) + (j+2)] = [x for x in lst[9 * (i+1) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+2)] = [x for x in lst[9 * (i+1) + (j+2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j + 2)] = [x for x in lst[9 * (i + 1) + (j + 2)] if x is not lst[9 * i + j][2]]
                        elif (j+1) % 3 == 2:
                            # case 5
                            if (len(lst[9 * (i-1) + (j-1)]) == 3) and (lst[9 * (i-1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i-1)
                            elif (len(lst[9 * (i+1) + (j-1)]) == 3) and (lst[9 * (i+1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i+1)
                            elif (len(lst[9 * (i-1) + (j+1)]) == 3) and (lst[9 * (i-1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i-1)
                            elif (len(lst[9 * (i+1) + (j+1)]) == 3) and (lst[9 * (i+1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i+1)
                            if bcount == 2:
                                if (((i-1) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j - 1)] = [x for x in lst[9 * (i - 1) + (j - 1)] if x is not lst[9 * i + j][2]]
                                if (((i+1) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j - 1)] = [x for x in lst[9 * (i + 1) + (j - 1)] if x is not lst[9 * i + j][2]]
                                if (((i-1) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j + 1)] = [x for x in lst[9 * (i - 1) + (j + 1)] if x is not lst[9 * i + j][2]]
                                if (((i+1) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j + 1)] = [x for x in lst[9 * (i + 1) + (j + 1)] if x is not lst[9 * i + j][2]]
                        else:
                            # case 6
                            if (len(lst[9 * (i-1) + (j-2)]) == 3) and (lst[9 * (i-1) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-2)
                                brtemp.append(i-1)
                            elif (len(lst[9 * (i-1) + (j-1)]) == 3) and (lst[9 * (i-1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i-1)
                            elif (len(lst[9 * (i+1) + (j-2)]) == 3) and (lst[9 * (i+1) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-2)
                                brtemp.append(i+1)
                            elif (len(lst[9 * (i+1) + (j-1)]) == 3) and (lst[9 * (i+1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i+1)
                            if bcount == 2:
                                if (((i-1) != brtemp[0]) or ((j-2) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j-2) != bctemp[1])):
                                    lst[9 * (i-1) + (j-2)] = [x for x in lst[9 * (i-1) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-2)] = [x for x in lst[9 * (i-1) + (j-2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j - 2)] = [x for x in lst[9 * (i - 1) + (j - 2)] if x is not lst[9 * i + j][2]]
                                if (((i-1) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j - 1)] = [x for x in lst[9 * (i - 1) + (j - 1)] if x is not lst[9 * i + j][2]]
                                if (((i+1) != brtemp[0]) or ((j-2) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j-2) != bctemp[1])):
                                    lst[9 * (i+1) + (j-2)] = [x for x in lst[9 * (i+1) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-2)] = [x for x in lst[9 * (i+1) + (j-2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j - 2)] = [x for x in lst[9 * (i + 1) + (j - 2)] if x is not lst[9 * i + j][2]]
                                if (((i+1) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i+1) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i + 1) + (j - 1)] = [x for x in lst[9 * (i + 1) + (j - 1)] if x is not lst[9 * i + j][2]]
                    else:
                        if (j+1) % 3 == 1:
                            # case 7
                            if (len(lst[9 * (i-2) + (j+1)]) == 3) and (lst[9 * (i-2) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i-2)
                            elif (len(lst[9 * (i-2) + (j+2)]) == 3) and (lst[9 * (i-2) + (j+2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+2)
                                brtemp.append(i-2)
                            elif (len(lst[9 * (i-1) + (j+1)]) == 3) and (lst[9 * (i-1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i-1)
                            elif (len(lst[9 * (i-1) + (j+2)]) == 3) and (lst[9 * (i-1) + (j+2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+2)
                                brtemp.append(i-1)
                            if bcount == 2:
                                if (((i-2) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i-2) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i-2) + (j+1)] = [x for x in lst[9 * (i-2) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j+1)] = [x for x in lst[9 * (i-2) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 2) + (j + 1)] = [x for x in lst[9 * (i - 2) + (j + 1)] if x is not lst[9 * i + j][2]]
                                if (((i-2) != brtemp[0]) or ((j+2) != bctemp[0])) and (((i-2) != brtemp[1]) or ((j+2) != bctemp[1])):
                                    lst[9 * (i-2) + (j+2)] = [x for x in lst[9 * (i-2) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j+2)] = [x for x in lst[9 * (i-2) + (j+2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 2) + (j + 2)] = [x for x in lst[9 * (i - 2) + (j + 2)] if x is not lst[9 * i + j][2]]
                                if (((i-1) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j + 1)] = [x for x in lst[9 * (i - 1) + (j + 1)] if x is not lst[9 * i + j][2]]
                                if (((i-1) != brtemp[0]) or ((j+2) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j+2) != bctemp[1])):
                                    lst[9 * (i-1) + (j+2)] = [x for x in lst[9 * (i-1) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+2)] = [x for x in lst[9 * (i-1) + (j+2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j + 2)] = [x for x in lst[9 * (i - 1) + (j + 2)] if x is not lst[9 * i + j][2]]
                        elif (j+1) % 3 == 2:
                            # case 8
                            if (len(lst[9 * (i-2) + (j-1)]) == 3) and (lst[9 * (i-2) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i-2)
                            elif (len(lst[9 * (i-2) + (j+1)]) == 3) and (lst[9 * (i-2) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i-2)
                            elif (len(lst[9 * (i-1) + (j-1)]) == 3) and (lst[9 * (i-1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i-1)
                            elif (len(lst[9 * (i-1) + (j+1)]) == 3) and (lst[9 * (i-1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j+1)
                                brtemp.append(i-1)
                            if bcount == 2:
                                if (((i-2) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i-2) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i-2) + (j-1)] = [x for x in lst[9 * (i-2) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j-1)] = [x for x in lst[9 * (i-2) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 2) + (j - 1)] = [x for x in lst[9 * (i - 2) + (j - 1)] if x is not lst[9 * i + j][2]]
                                if (((i-2) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i-2) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i-2) + (j+1)] = [x for x in lst[9 * (i-2) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j+1)] = [x for x in lst[9 * (i-2) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 2) + (j + 1)] = [x for x in lst[9 * (i - 2) + (j + 1)] if x is not lst[9 * i + j][2]]
                                if (((i-1) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j - 1)] = [x for x in lst[9 * (i - 1) + (j - 1)] if x is not lst[9 * i + j][2]]
                                if (((i-1) != brtemp[0]) or ((j+1) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j+1) != bctemp[1])):
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j + 1)] = [x for x in lst[9 * (i - 1) + (j + 1)] if x is not lst[9 * i + j][2]]
                        else:
                            # case 9
                            if (len(lst[9 * (i-2) + (j-2)]) == 3) and (lst[9 * (i-2) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-2)
                                brtemp.append(i-2)
                            elif (len(lst[9 * (i-2) + (j-1)]) == 3) and (lst[9 * (i-2) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i-2)
                            elif (len(lst[9 * (i-1) + (j-2)]) == 3) and (lst[9 * (i-1) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-2)
                                brtemp.append(i-1)
                            elif (len(lst[9 * (i-1) + (j-1)]) == 3) and (lst[9 * (i-1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp.append(j-1)
                                brtemp.append(i-1)
                            if bcount == 2:
                                if (((i-2) != brtemp[0]) or ((j-2) != bctemp[0])) and (((i-2) != brtemp[1]) or ((j-2) != bctemp[1])):
                                    lst[9 * (i-2) + (j-2)] = [x for x in lst[9 * (i-2) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j-2)] = [x for x in lst[9 * (i-2) + (j-2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 2) + (j - 2)] = [x for x in lst[9 * (i - 2) + (j - 2)] if x is not lst[9 * i + j][2]]
                                if (((i-2) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i-2) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i-2) + (j-1)] = [x for x in lst[9 * (i-2) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j-1)] = [x for x in lst[9 * (i-2) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 2) + (j - 1)] = [x for x in lst[9 * (i - 2) + (j - 1)] if x is not lst[9 * i + j][2]]
                                if (((i-1) != brtemp[0]) or ((j-2) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j-2) != bctemp[1])):
                                    lst[9 * (i-1) + (j-2)] = [x for x in lst[9 * (i-1) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-2)] = [x for x in lst[9 * (i-1) + (j-2)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j - 2)] = [x for x in lst[9 * (i - 1) + (j - 2)] if x is not lst[9 * i + j][2]]
                                if (((i-1) != brtemp[0]) or ((j-1) != bctemp[0])) and (((i-1) != brtemp[1]) or ((j-1) != bctemp[1])):
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][1]]
                                    lst[9 * (i - 1) + (j - 1)] = [x for x in lst[9 * (i - 1) + (j - 1)] if x is not lst[9 * i + j][2]]

                # check naked pair
                elif len(lst[9 * i + j]) == 2:
                    # check column
                    ccount = 0
                    for k in range(0,9):
                        if (k != i) and (len(lst[9 * k + j]) == 2) and (lst[9 * k + j] == lst[9 * i + j]):
                            ccount += 1
                            ctemp = k
                    if ccount == 1:
                        for k in range(0,9):
                            if (k != i) and (k != ctemp):
                                lst[9 * k + j] = [x for x in lst[9 * k + j] if x is not lst[9 * i + j][0]]
                                lst[9 * k + j] = [x for x in lst[9 * k + j] if x is not lst[9 * i + j][1]]
                    # check row
                    rcount = 0
                    for k in range(0,9):
                        if (k != j) and (len(lst[9 * i + k]) == 2) and (lst[9 * i + k] == lst[9 * i + j]):
                            rcount += 1
                            rtemp = k
                    if rcount == 1:
                        for k in range(0,9):
                            if (k != j) and (k != rtemp):
                                lst[9 * i + k] = [x for x in lst[9 * i + k] if x is not lst[9 * i + j][0]]
                                lst[9 * i + k] = [x for x in lst[9 * i + k] if x is not lst[9 * i + j][1]]
                    # check box
                    bcount = 0
                    if (i+1) % 3 == 1:
                        if (j+1) % 3 == 1:
                            # case 1
                            if (len(lst[9 * (i+1) + (j+1)]) == 2) and (lst[9 * (i+1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i+1
                            elif (len(lst[9 * (i+1) + (j+2)]) == 2) and (lst[9 * (i+1) + (j+2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+2
                                brtemp = i+1
                            elif (len(lst[9 * (i+2) + (j+1)]) == 2) and (lst[9 * (i+2) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i+2
                            elif (len(lst[9 * (i+2) + (j+2)]) == 2) and (lst[9 * (i+2) + (j+2)] == lst[9 * i + j]):
                                bcount +=1
                                bctemp = j+2
                                brtemp = i+2
                            if bcount == 1:
                                if ((i+1) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][1]]
                                if ((i+1) != brtemp) or ((j+2) != bctemp):
                                    lst[9 * (i+1) + (j+2)] = [x for x in lst[9 * (i+1) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+2)] = [x for x in lst[9 * (i+1) + (j+2)] if x is not lst[9 * i + j][1]]
                                if ((i+2) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i+2) + (j+1)] = [x for x in lst[9 * (i+2) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j+1)] = [x for x in lst[9 * (i+2) + (j+1)] if x is not lst[9 * i + j][1]]
                                if ((i+2) != brtemp) or ((j+2) != bctemp):
                                    lst[9 * (i+2) + (j+2)] = [x for x in lst[9 * (i+2) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j+2)] = [x for x in lst[9 * (i+2) + (j+2)] if x is not lst[9 * i + j][1]]
                        elif (j+1) % 3 == 2:
                            # case 2
                            if (len(lst[9 * (i+1) + (j-1)]) == 2) and (lst[9 * (i+1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i+1
                            elif (len(lst[9 * (i+2) + (j-1)]) == 2) and (lst[9 * (i+1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i+2
                            elif (len(lst[9 * (i+1) + (j+1)]) == 2) and (lst[9 * (i+1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i+1
                            elif (len(lst[9 * (i+2) + (j+1)]) == 2) and (lst[9 * (i+2) + (j+1)] == lst[9 * i + j]):
                                bcount +=1
                                bctemp = j+1
                                brtemp = i+2
                            if bcount == 1:
                                if ((i+1) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][1]]
                                if ((i+2) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i+2) + (j-1)] = [x for x in lst[9 * (i+2) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j-1)] = [x for x in lst[9 * (i+2) + (j-1)] if x is not lst[9 * i + j][1]]
                                if ((i+1) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][1]]
                                if ((i+2) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i+2) + (j+1)] = [x for x in lst[9 * (i+2) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j+1)] = [x for x in lst[9 * (i+2) + (j+1)] if x is not lst[9 * i + j][1]]
                        else:
                            # case 3
                            if (len(lst[9 * (i+1) + (j-2)]) == 2) and (lst[9 * (i+1) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-2
                                brtemp = i+1
                            elif (len(lst[9 * (i+1) + (j-1)]) == 2) and (lst[9 * (i+1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i+1
                            elif (len(lst[9 * (i+2) + (j-2)]) == 2) and (lst[9 * (i+2) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-2
                                brtemp = i+2
                            elif (len(lst[9 * (i+2) + (j-1)]) == 2) and (lst[9 * (i+2) + (j-1)] == lst[9 * i + j]):
                                bcount +=1
                                bctemp = j-1
                                brtemp = i+2
                            if bcount == 1:
                                if ((i+1) != brtemp) or ((j-2) != bctemp):
                                    lst[9 * (i+1) + (j-2)] = [x for x in lst[9 * (i+1) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-2)] = [x for x in lst[9 * (i+1) + (j-2)] if x is not lst[9 * i + j][1]]
                                if ((i+1) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][1]]
                                if ((i+2) != brtemp) or ((j-2) != bctemp):
                                    lst[9 * (i+2) + (j-2)] = [x for x in lst[9 * (i+2) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j-2)] = [x for x in lst[9 * (i+2) + (j-2)] if x is not lst[9 * i + j][1]]
                                if ((i+2) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i+2) + (j-1)] = [x for x in lst[9 * (i+2) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+2) + (j-1)] = [x for x in lst[9 * (i+2) + (j-1)] if x is not lst[9 * i + j][1]]
                    elif (i+1) % 3 == 2:
                        if (j+1) % 3 == 1:
                            # case 4
                            if (len(lst[9 * (i-1) + (j+1)]) == 2) and (lst[9 * (i-1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i-1
                            elif (len(lst[9 * (i-1) + (j+2)]) == 2) and (lst[9 * (i-1) + (j+2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+2
                                brtemp = i-1
                            elif (len(lst[9 * (i+1) + (j+1)]) == 2) and (lst[9 * (i+1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i+1
                            elif (len(lst[9 * (i+1) + (j+2)]) == 2) and (lst[9 * (i+1) + (j+2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+2
                                brtemp = i+1
                            if bcount == 1:
                                if ((i-1) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][1]]
                                if ((i-1) != brtemp) or ((j+2) != bctemp):
                                    lst[9 * (i-1) + (j+2)] = [x for x in lst[9 * (i-1) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+2)] = [x for x in lst[9 * (i-1) + (j+2)] if x is not lst[9 * i + j][1]]
                                if ((i+1) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][1]]
                                if ((i+1) != brtemp) or ((j+2) != bctemp):
                                    lst[9 * (i+1) + (j+2)] = [x for x in lst[9 * (i+1) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+2)] = [x for x in lst[9 * (i+1) + (j+2)] if x is not lst[9 * i + j][1]]
                        elif (j+1) % 3 == 2:
                            # case 5
                            if (len(lst[9 * (i-1) + (j-1)]) == 2) and (lst[9 * (i-1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i-1
                            elif (len(lst[9 * (i+1) + (j-1)]) == 2) and (lst[9 * (i+1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i+1
                            elif (len(lst[9 * (i-1) + (j+1)]) == 2) and (lst[9 * (i-1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i-1
                            elif (len(lst[9 * (i+1) + (j+1)]) == 2) and (lst[9 * (i+1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i+1
                            if bcount == 1:
                                if ((i-1) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][1]]
                                if ((i+1) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][1]]
                                if ((i-1) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][1]]
                                if ((i+1) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j+1)] = [x for x in lst[9 * (i+1) + (j+1)] if x is not lst[9 * i + j][1]]
                        else:
                            # case 6
                            if (len(lst[9 * (i-1) + (j-2)]) == 2) and (lst[9 * (i-1) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-2
                                brtemp = i-1
                            elif (len(lst[9 * (i-1) + (j-1)]) == 2) and (lst[9 * (i-1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i-1
                            elif (len(lst[9 * (i+1) + (j-2)]) == 2) and (lst[9 * (i+1) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-2
                                brtemp = i+1
                            elif (len(lst[9 * (i+1) + (j-1)]) == 2) and (lst[9 * (i+1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i+1
                            if bcount == 1:
                                if ((i-1) != brtemp) or ((j-2) != bctemp):
                                    lst[9 * (i-1) + (j-2)] = [x for x in lst[9 * (i-1) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-2)] = [x for x in lst[9 * (i-1) + (j-2)] if x is not lst[9 * i + j][1]]
                                if ((i-1) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][1]]
                                if ((i+1) != brtemp) or ((j-2) != bctemp):
                                    lst[9 * (i+1) + (j-2)] = [x for x in lst[9 * (i+1) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-2)] = [x for x in lst[9 * (i+1) + (j-2)] if x is not lst[9 * i + j][1]]
                                if ((i+1) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i+1) + (j-1)] = [x for x in lst[9 * (i+1) + (j-1)] if x is not lst[9 * i + j][1]]
                    else:
                        if (j+1) % 3 == 1:
                            # case 7
                            if (len(lst[9 * (i-2) + (j+1)]) == 2) and (lst[9 * (i-2) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i-2
                            elif (len(lst[9 * (i-2) + (j+2)]) == 2) and (lst[9 * (i-2) + (j+2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+2
                                brtemp = i-2
                            elif (len(lst[9 * (i-1) + (j+1)]) == 2) and (lst[9 * (i-1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i-1
                            elif (len(lst[9 * (i-1) + (j+2)]) == 2) and (lst[9 * (i-1) + (j+2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+2
                                brtemp = i-1
                            if bcount == 1:
                                if ((i-2) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i-2) + (j+1)] = [x for x in lst[9 * (i-2) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j+1)] = [x for x in lst[9 * (i-2) + (j+1)] if x is not lst[9 * i + j][1]]
                                if ((i-2) != brtemp) or ((j+2) != bctemp):
                                    lst[9 * (i-2) + (j+2)] = [x for x in lst[9 * (i-2) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j+2)] = [x for x in lst[9 * (i-2) + (j+2)] if x is not lst[9 * i + j][1]]
                                if ((i-1) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][1]]
                                if ((i-1) != brtemp) or ((j+2) != bctemp):
                                    lst[9 * (i-1) + (j+2)] = [x for x in lst[9 * (i-1) + (j+2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+2)] = [x for x in lst[9 * (i-1) + (j+2)] if x is not lst[9 * i + j][1]]
                        elif (j+1) % 3 == 2:
                            # case 8
                            if (len(lst[9 * (i-2) + (j-1)]) == 2) and (lst[9 * (i-2) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i-2
                            elif (len(lst[9 * (i-2) + (j+1)]) == 2) and (lst[9 * (i-2) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i-2
                            elif (len(lst[9 * (i-1) + (j-1)]) == 2) and (lst[9 * (i-1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i-1
                            elif (len(lst[9 * (i-1) + (j+1)]) == 2) and (lst[9 * (i-1) + (j+1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j+1
                                brtemp = i-1
                            if bcount == 1:
                                if ((i-2) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i-2) + (j-1)] = [x for x in lst[9 * (i-2) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j-1)] = [x for x in lst[9 * (i-2) + (j-1)] if x is not lst[9 * i + j][1]]
                                if ((i-2) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i-2) + (j+1)] = [x for x in lst[9 * (i-2) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j+1)] = [x for x in lst[9 * (i-2) + (j+1)] if x is not lst[9 * i + j][1]]
                                if ((i-1) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][1]]
                                if ((i-1) != brtemp) or ((j+1) != bctemp):
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j+1)] = [x for x in lst[9 * (i-1) + (j+1)] if x is not lst[9 * i + j][1]]
                        else:
                            # case 9
                            if (len(lst[9 * (i-2) + (j-2)]) == 2) and (lst[9 * (i-2) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-2
                                brtemp = i-2
                            elif (len(lst[9 * (i-2) + (j-1)]) == 2) and (lst[9 * (i-2) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i-2
                            elif (len(lst[9 * (i-1) + (j-2)]) == 2) and (lst[9 * (i-1) + (j-2)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-2
                                brtemp = i-1
                            elif (len(lst[9 * (i-1) + (j-1)]) == 2) and (lst[9 * (i-1) + (j-1)] == lst[9 * i + j]):
                                bcount += 1
                                bctemp = j-1
                                brtemp = i-1
                            if bcount == 1:
                                if ((i-2) != brtemp) or ((j-2) != bctemp):
                                    lst[9 * (i-2) + (j-2)] = [x for x in lst[9 * (i-2) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j-2)] = [x for x in lst[9 * (i-2) + (j-2)] if x is not lst[9 * i + j][1]]
                                if ((i-2) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i-2) + (j-1)] = [x for x in lst[9 * (i-2) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-2) + (j-1)] = [x for x in lst[9 * (i-2) + (j-1)] if x is not lst[9 * i + j][1]]
                                if ((i-1) != brtemp) or ((j-2) != bctemp):
                                    lst[9 * (i-1) + (j-2)] = [x for x in lst[9 * (i-1) + (j-2)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-2)] = [x for x in lst[9 * (i-1) + (j-2)] if x is not lst[9 * i + j][1]]
                                if ((i-1) != brtemp) or ((j-1) != bctemp):
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][0]]
                                    lst[9 * (i-1) + (j-1)] = [x for x in lst[9 * (i-1) + (j-1)] if x is not lst[9 * i + j][1]]
        # shuffling
        self.random_shuffle(lst)
        return lst

    def random_shuffle(self, lst):
        for i in range(len(lst)):
            random.shuffle(lst[i])
        return lst

if __name__ == "__main__":
    root = Tk()
    SudokuUI(root)
    root.geometry("%dx%d" % (WIDTH, HEIGHT + 40))
    root.mainloop()
