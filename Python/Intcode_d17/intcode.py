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
        self._initiate_variables()

    def _initiate_variables(self):
        self.ip = 0
        self.rp = 0
        self.inputs = []
        self.outputs = []

        self.memory = {}
        parsed_program = list(map(lambda x: int(x), self.raw_program.split(",")))
        for i, op in enumerate(parsed_program):
            self._write(i, op)

    def _read(self, adr):
        if adr in self.memory.keys():
            return self.memory[adr]
        return 0

    def _write(self, adr, val):
        self.memory[adr] = val

    # translate a parameter value into its actual value using mode
    # returns the actual value of param
    def _transl_param(self, param, mode):
        if mode == 0:
            return self._read(param)
        if mode == 1:
            return param
        if mode == 2:
            return self._read(param + self.rp)
        raise Exception("Unknown mode when translating param: " + mode)

    def _transl_adr(self, adr, mode):
        if mode == 0:
            return adr
        if mode == 2:
            return adr + self.rp
        raise Exception("Unknown mode when translating address: " + mode)

    # Read multiple values from the memory
    # adr: first address of values
    # nums: number of values to read
    def _read_vals(self, adr, num):
        return tuple([self._read(adr + i) for i in range(num)])

    # Convert an opcode to a tuple of length 3, listing the mode of 1st, 2nd and 3rd param
    # Mode syntax: EDCAB, returns C, D, E
    def _parse_mode(self, opcode):
        return opcode % 1000 // 100, opcode % 10000 // 1000, opcode // 10000

    def _pop_input(self):
        val = self.inputs[0]
        del self.inputs[0]
        return val

    def _add_output(self, val):
        self.outputs.append(val)

    def _op_add(self, modes):
        params = self._read_vals(self.ip + 1, 3)
        result = self._transl_param(params[0], modes[0]) + self._transl_param(params[1], modes[1])
        self._write(self._transl_adr(params[2], modes[2]), result)
        self.ip += 4

    def _op_mul(self, modes):
        params = self._read_vals(self.ip + 1, 3)
        result = self._transl_param(params[0], modes[0]) * self._transl_param(params[1], modes[1])
        self._write(self._transl_adr(params[2], modes[2]), result)
        self.ip += 4

    def _op_in(self, modes):
        param = self._read(self.ip + 1)
        val = self._pop_input()
        self._write(self._transl_adr(param, modes[0]), val)
        self.ip += 2

    def _op_out(self, modes):
        param = self._read(self.ip + 1)
        val = self._transl_param(param, modes[0])
        self._add_output(val)
        self.ip += 2

    def _op_jp(self, modes, is_non_zero):
        params = self._read_vals(self.ip + 1, 2)
        val = self._transl_param(params[0], modes[0])
        if (val != 0) == is_non_zero:
            self.ip = self._transl_param(params[1], modes[1])
        else:
            self.ip += 3

    def _op_lt(self, modes):
        params = self._read_vals(self.ip + 1, 3)
        vals = self._transl_param(params[0], modes[0]), self._transl_param(params[1], modes[1])
        self._write(self._transl_adr(params[2], modes[2]), 1 if vals[0] < vals[1] else 0)
        self.ip += 4

    def _op_eq(self, modes):
        params = self._read_vals(self.ip + 1, 3)
        vals = self._transl_param(params[0], modes[0]), self._transl_param(params[1], modes[1])
        self._write(self._transl_adr(params[2], modes[2]), 1 if vals[0] == vals[1] else 0)
        self.ip += 4

    def _op_mp(self, modes):
        params = self._read(self.ip + 1)
        vals = self._transl_param(params, modes[0])
        self.rp += vals
        self.ip += 2

    def clear_outputs(self):
        self.outputs.clear()

    def input(self, values):
        self.inputs += list(values)
        return self.run()

    def write_memory(self, address, value):
        self._write(address, value)

    def reset(self):
        self._initiate_variables()

    def run(self):
        while self.ip < len(self.memory):
            full_opcode = self._read(self.ip)
            opcode, modes = full_opcode % 100, self._parse_mode(full_opcode)
            if opcode == 1:
                self._op_add(modes)
            elif opcode == 2:
                self._op_mul(modes)
            elif opcode == 3:
                if len(self.inputs) <= 0:
                    return Intcode.AWAIT_INPUT
                self._op_in(modes)
            elif opcode == 4:
                self._op_out(modes)
            elif opcode == 5:
                self._op_jp(modes, True)
            elif opcode == 6:
                self._op_jp(modes, False)
            elif opcode == 7:
                self._op_lt(modes)
            elif opcode == 8:
                self._op_eq(modes)
            elif opcode == 9:
                self._op_mp(modes)
            elif opcode == 99:
                return Intcode.TERMINATION
            else:
                return Intcode.INVALID_OPCODE
        return Intcode.OUT_OF_BOUND
