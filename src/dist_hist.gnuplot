set key off
set boxwidth 0.9
set style fill solid 0.25 noborder
set xlabel "Distance in kilometers"
set ylabel "Number of trips"
set autotitle
bin(x) = floor((x/10)+0.5)
plot 'data' using (bin($1)):(1) smooth frequency with boxes
