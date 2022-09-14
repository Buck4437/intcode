from Intcode_d17.ascii import ASCII

with open("Day 25.txt") as f:
    program = f.readline()

computer = ASCII(program, ascii_printing=True)
output = computer.input("""""")

while output == ASCII.AWAIT_INPUT:
    output = computer.input(input("") + "\n")

# print(computer.outputs)
print(computer.outputs[-1])
