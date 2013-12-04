all:
	javac tsp/src/*.java -d tsp/bin
test1:
	java -cp tsp/bin Run < input/input
test:
	java -cp tsp/bin Run
random:
	java -cp tsp/bin Run random 10
sa:
	java -cp tsp/bin SA < input/input
genmap:
	java -cp tsp/bin GenMap 10
