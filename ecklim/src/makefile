all:
	javac *.java
input1:
	java Run < input/input1
test:
	java Tester
stats:
	java Tester > naive.dat
	java Tester EratoFactorizer > erato.dat
	java Tester Pollard > pollard.dat
	java Tester Combiner > combiner.dat
	gnuplot diagram.gp
	feh output.png
