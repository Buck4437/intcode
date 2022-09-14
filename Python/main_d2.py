from Intcode_d2.intcode import *

with open("Day 2.txt") as f:
    program = f.readline()

intcode = Intcode(program)
intcode.memory[1] = 12
intcode.memory[2] = 2
intcode.run()
print(intcode.memory[0])
