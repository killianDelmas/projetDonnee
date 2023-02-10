javac *.java

java Server &
sleep 3

echo "" > ResultatsTestSynchro.txt # fichier texte où apparaitront les résultats du test

java TestSynchro >> ResultatsTestSynchro.txt &

sleep 3

i=0
limit=$((0 + $1))
while [ $i -le $limit ]
do  
    java TestSynchro >> ResultatsTestSynchro.txt &
    ((i++))
done

sleep 20 # Pour éviter que le compteur s'affiche alors que la somme n'est pas finie

echo -e "\nCompteur théorique (somme des valeurs précédentes) :" >> ResultatsTestSynchro.txt

awk '{ sum += $1 } END { print sum }' ResultatsTestSynchro.txt >> ResultatsTestSynchro.txt

echo -e "\nCompteur réel :" >> ResultatsTestSynchro.txt

java LectureFinaleSynchro >> ResultatsTestSynchro.txt

sleep 5

./clean.sh
