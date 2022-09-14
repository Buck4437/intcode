from Intcode_d17.ascii import ASCII

with open("Day 21.txt") as f:
    program = f.readline()

computer = ASCII(program, ascii_printing=True)
output = computer.input("""OR E J
OR H J
AND D J
OR A T
AND B T
AND C T
NOT T T
AND T J
NOT A T
OR T J
RUN
""")

"""NOT A J
NOT C T
AND D T
OR T J
WALK"""
while output == ASCII.AWAIT_INPUT:
    output = computer.input(input("") + "\n")

# print(computer.outputs)
print(computer.outputs[-1])
