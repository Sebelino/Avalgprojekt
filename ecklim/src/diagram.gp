set terminal pngcairo enhanced
set output "output.png"
#set title "Benchmarks for trying to factorize a prime number"
set title "Benchmarks for factorizing a product p{\267}q"
#set title "Benchmarks for factorizing a product p{\267}q{\267}r"
set xlabel "Number"
set ylabel "Time (ns)"
set key right bottom
set grid
set logscale x
set logscale y
set xtics rotate by -90
plot [1:*] [*:*] 'Naive.dat' with lines, \
     'Eratosthenes.dat' with lines, \
     'Pollard.dat' with lines, \
     'Combiner.dat' with lines
