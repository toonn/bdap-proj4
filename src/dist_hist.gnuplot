if (!exists("datafile")) datafile='tripDistances.data'
set key off
set boxwidth 0.9 absolute
set yrange [1:20]
#set logscale y
set style fill solid 0.25 noborder
set xlabel "Distance in kilometers"
set ylabel "Number of trips"
bin(x) = floor((x/100)+0.5)
plot datafile using (bin($1)):(1) smooth frequency with boxes
