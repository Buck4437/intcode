from Intcode_d9.intcode import Intcode

with open("Day 9.txt") as f:
    program = f.readline()

intcode = Intcode(program)
output = intcode.run()
while output == Intcode.AWAIT_INPUT:
    output = intcode.input([int(input(""))])
print(output)
print(intcode.outputs)
