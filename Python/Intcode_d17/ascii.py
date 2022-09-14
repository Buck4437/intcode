from .intcode import Intcode


class ASCII(Intcode):

    def input(self, inputs):
        ints = [ord(char) for char in inputs]
        return super().input(ints)
