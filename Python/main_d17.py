from Intcode_d17.ascii import ASCII

with open("Day 17.txt") as f:
    program = f.readline()

computer = ASCII(program, ascii_printing=True)
computer.write_memory(0, 2)
output = computer.input(
"""A,A,B,C,B,C,B,C,B,A
R,10,L,12,R,6
R,6,R,10,R,12,R,6
R,10,L,12,L,12
n
""")

"""
R10,L12,R6,
R10,L12,R6,
R6,R10,R12,R6,
R10,L12,L12,
R6,R10,R12,R6,
R10,L12,L12,
R6,R10,R12,R6,
R10,L12,L12
R6,R10,R12,R6,
R10,L12,R6"""

while output == ASCII.AWAIT_INPUT:
    output = computer.input(input("") + "\n")

# print(computer.outputs)
print(computer.outputs[-1])
