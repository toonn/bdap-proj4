if (!exists("datafile")) datafile='aiportrevenue.data'
set terminal pdf
set output 'out.pdf'
set timefmt '%Y-%m-%d'
set format x '%m-%d'
set xdata time
set xrange ["2010-02-28":"2010-04-01"]
set xtics rotate by 45
set xtics out offset -1.5,-1.5
set key off
set style fill solid 0.5
set boxwidth 60*60*20 absolute
set xlabel "Date"
set ylabel "Revenue in USD"
plot datafile using 1:3 smooth frequency with boxes
