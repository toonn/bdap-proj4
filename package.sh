#! /bin/sh
mkdir Toon_Nolten-Assignment4
cd src/TripLength
mvn install
mv target/Exercise1-0.0.jar ../../Toon_Nolten-Assignment4/Exercise1.jar
mvn clean
cd ../TripRevenue
mvn install
mv target/Exercise2-0.0.jar ../../Toon_Nolten-Assignment4/Exercise2.jar
mvn clean
cd ../../Toon_Nolten-Assignment4
cp ../README .
cp ../report.pdf .
cp -r ../src/ .
cd ..
tar czf Toon_Nolten-Assignment4.tar.gz Toon_Nolten-Assignment4
rm -r Toon_Nolten-Assignment4
