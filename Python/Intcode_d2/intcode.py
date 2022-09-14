# Variable names:
# ip: instruction pointer
#
class Intcode:

    AWAIT_INPUT = 3
    TERMINATION = 99
    INVALID_OPCODE = -1

    def __init__(self, raw_program):
        self.raw_program = raw_program
        self.__initiate_variables()

    def __initiate_variables(self):
        self.ip = 0
        self.memory = list(map(lambda x: int(x), self.raw_program.split(",")))

    def reset(self):
        self.__initiate_variables()

    def run(self):
        while self.ip < len(self.memory):
            opcode = self.memory[self.ip]
            self.ip += 1
            if opcode == 1:
                param_adrs = [self.memory[self.ip + i] for i in range(3)]
                result = self.memory[param_adrs[0]] + self.memory[param_adrs[1]]
                self.memory[param_adrs[2]] = result
                self.ip += 3
            elif opcode == 2:
                param_adrs = [self.memory[self.ip + i] for i in range(3)]
                result = self.memory[param_adrs[0]] * self.memory[param_adrs[1]]
                self.memory[param_adrs[2]] = result
                self.ip += 3
            elif opcode == 99:
                return Intcode.TERMINATION
            else:
                return Intcode.INVALID_OPCODE
