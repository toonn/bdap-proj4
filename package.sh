#! /bin/sh
mkdir ToonNoltenAssignment4
cd src/TripLength
mvn install
mv target/Exercise1-0.0.jar ../../ToonNoltenAssignment4/Exercise1.jar
mvn clean
cd ../TripRevenue
mvn install
mv target/Exercise2-0.0.jar ../../ToonNoltenAssignment4/Exercise2.jar
mvn clean
cd ../../ToonNoltenAssignment4
cp ../README .
cp ../report.pdf .
cp ../src/ .
cd ..
tar czf Toon_Nolten-Assignment4.tar.gz ToonNoltenAssignment4
rm -r ToonNoltenAssignment4
