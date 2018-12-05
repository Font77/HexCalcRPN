# HexCalcRPN &mdash; Calculator App For Programmers

## Overview

There are two operating modes of the calculator: unconstrained (&#8734; mode) and the various bit-size modes (8-, 16-, 32- and 64-bit).

In unconstrained mode values entered into the calculator can be of any size, and negative values are represented with a minus sign, like an ordinary calculator.

In the various constrained modes, the values in the calculator's stack are constrained to the number of bits specified by the mode. Any bits that exceed that length are silently dropped. Negative values are expressed by standard [two's complement](https://en.wikipedia.org/wiki/Two's_complement) representation.

If it's not obvious, constrained modes simulate how data is actually stored and manipulated in the registers or memory of a computer. Unconstrained mode &hellip; well, it's useful when you need to work with larger amounts of data for some reason.

## User Guide
