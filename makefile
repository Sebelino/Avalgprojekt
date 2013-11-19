all:
	javac tsp/src/*.java -d tsp/bin
test:
	java -cp tsp/bin Run < input/input
