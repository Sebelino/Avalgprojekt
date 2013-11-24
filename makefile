all:
	javac tsp/src/*.java -d tsp/bin
test:
	java -cp tsp/bin Run < input/input
sa:
	java -cp tsp/bin SA < input/input
genmap:
	java -cp tsp/bin GenMap 10
