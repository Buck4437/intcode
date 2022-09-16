from .intcode import Intcode


class ASCII(Intcode):

    def input(self, inputs):
        ints = [ord(char) for char in inputs]
        return super().input(ints)

    def _add_output(self, val):
        super()._add_output(val)
        if self.ascii_printing and val <= 256:
            print(chr(val), end="")
