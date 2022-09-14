# Variable names:
# ip: instruction pointer
# rp: relative pointer
class Intcode:

    AWAIT_INPUT = 3
    TERMINATION = 99
    INVALID_OPCODE = -1
    OUT_OF_BOUND = -2

    def __init__(self, raw_program, ascii_printing=False):
        self.raw_program = raw_program
        self.ascii_printing = ascii_printing
        self.__initiate_variables()

    def __initiate_variables(self):
        self.ip = 0
        self.rp = 0
        self.inputs = []
        self.outputs = []

        self.memory = {}
        parsed_program = list(map(lambda x: int(x), self.raw_program.split(",")))
        for i, op in enumerate(parsed_program):
            self.__write(i, op)

    def __read(self, adr):
        if adr in self.memory.keys():
            return self.memory[adr]
        return 0

    def __write(self, adr, val):
        self.memory[adr] = val

    # translate a parameter value into its actual value using mode
    # returns the actual value of param
    def __transl_param(self, param, mode):
        if mode == 0:
            return self.__read(param)
        if mode == 1:
            return param
        if mode == 2:
            return self.__read(param + self.rp)
        raise Exception("Unknown mode when translating param: " + mode)

    def __transl_adr(self, adr, mode):
        if mode == 0:
            return adr
        if mode == 2:
            return adr + self.rp
        raise Exception("Unknown mode when translating address: " + mode)

    # Read multiple values from the memory
    # adr: first address of values
    # nums: number of values to read
    def __read_vals(self, adr, num):
        return tuple([self.__read(adr + i) for i in range(num)])

    # Convert an opcode to a tuple of length 3, listing the mode of 1st, 2nd and 3rd param
    # Mode syntax: EDCAB, returns C, D, E
    def __parse_mode(self, opcode):
        return opcode % 1000 // 100, opcode % 10000 // 1000, opcode // 10000

    def __pop_input(self):
        val = self.inputs[0]
        del self.inputs[0]
        return val

    def __add_output(self, val):
        self.outputs.append(val)
        if self.ascii_printing and val <= 256:
            print(chr(val), end="")

    def __op_add(self, modes):
        params = self.__read_vals(self.ip + 1, 3)
        result = self.__transl_param(params[0], modes[0]) + self.__transl_param(params[1], modes[1])
        self.__write(self.__transl_adr(params[2], modes[2]), result)
        self.ip += 4

    def __op_mul(self, modes):
        params = self.__read_vals(self.ip + 1, 3)
        result = self.__transl_param(params[0], modes[0]) * self.__transl_param(params[1], modes[1])
        self.__write(self.__transl_adr(params[2], modes[2]), result)
        self.ip += 4

    def __op_in(self, modes):
        param = self.__read(self.ip + 1)
        val = self.__pop_input()
        self.__write(self.__transl_adr(param, modes[0]), val)
        self.ip += 2

    def __op_out(self, modes):
        param = self.__read(self.ip + 1)
        val = self.__transl_param(param, modes[0])
        self.__add_output(val)
        self.ip += 2

    def __op_jp(self, modes, is_non_zero):
        params = self.__read_vals(self.ip + 1, 2)
        val = self.__transl_param(params[0], modes[0])
        if (val != 0) == is_non_zero:
            self.ip = self.__transl_param(params[1], modes[1])
        else:
            self.ip += 3

    def __op_lt(self, modes):
        params = self.__read_vals(self.ip + 1, 3)
        vals = self.__transl_param(params[0], modes[0]), self.__transl_param(params[1], modes[1])
        self.__write(self.__transl_adr(params[2], modes[2]), 1 if vals[0] < vals[1] else 0)
        self.ip += 4

    def __op_eq(self, modes):
        params = self.__read_vals(self.ip + 1, 3)
        vals = self.__transl_param(params[0], modes[0]), self.__transl_param(params[1], modes[1])
        self.__write(self.__transl_adr(params[2], modes[2]), 1 if vals[0] == vals[1] else 0)
        self.ip += 4

    def __op_mp(self, modes):
        params = self.__read(self.ip + 1)
        vals = self.__transl_param(params, modes[0])
        self.rp += vals
        self.ip += 2

    def clear_outputs(self):
        self.outputs.clear()

    def input(self, values):
        self.inputs += list(values)
        return self.run()

    def write_memory(self, address, value):
        self.__write(address, value)

    def reset(self):
        self.__initiate_variables()

    def run(self):
        while self.ip < len(self.memory):
            full_opcode = self.__read(self.ip)
            opcode, modes = full_opcode % 100, self.__parse_mode(full_opcode)
            if opcode == 1:
                self.__op_add(modes)
            elif opcode == 2:
                self.__op_mul(modes)
            elif opcode == 3:
                if len(self.inputs) <= 0:
                    return Intcode.AWAIT_INPUT
                self.__op_in(modes)
            elif opcode == 4:
                self.__op_out(modes)
            elif opcode == 5:
                self.__op_jp(modes, True)
            elif opcode == 6:
                self.__op_jp(modes, False)
            elif opcode == 7:
                self.__op_lt(modes)
            elif opcode == 8:
                self.__op_eq(modes)
            elif opcode == 9:
                self.__op_mp(modes)
            elif opcode == 99:
                return Intcode.TERMINATION
            else:
                return Intcode.INVALID_OPCODE
        return Intcode.OUT_OF_BOUND
