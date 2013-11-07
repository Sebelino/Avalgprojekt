set terminal pngcairo enhanced
set output "output.png"
set title "Benchmarks"
set xlabel "Number"
set ylabel "Time (ns)"
#set nokey
set grid
set logscale x
set logscale y
set xtics rotate by -90
plot [1:*] [*:*] 'naive.dat' with lines, \
     'erato.dat' with lines, \
     'pollard.dat' with lines, \
     'combiner.dat' with lines
