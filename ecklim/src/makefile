all:
	javac *.java
input1:
	java Run < input/input1
test:
	java Tester
stats:
	java Tester > all.dat
	awk '/Results/{x="results"++i".dat";next}{print > x;}' all.dat
	mv results1.dat Naive.dat
	mv results2.dat Eratosthenes.dat
	mv results3.dat Pollard.dat
	mv results4.dat Combiner.dat
	gnuplot diagram.gp
	feh output.png
