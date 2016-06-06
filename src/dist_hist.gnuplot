if (!exists("datafile")) datafile='tripDistances.data'
set terminal pdf
set output 'out.pdf'
set key off
set boxwidth 0.8 absolute
set xrange [-0.5:35]
set style fill solid 0.5
set xlabel "Distance in kilometers"
set ylabel "Number of trips"
bin(x) = floor(x+0.5)
set table 'foobar.table'
plot datafile using (bin($1)):(1) smooth frequency with boxes
