#!/bin/bash

cd target

# solve easy cases

# output columns
column="FILE,OP_STATES,OP_TRANS,OP_ALPHABET_SIZE";
column="$column,ALGORITHM,RESULT_STATES, RESULT_TRANS,RUNTIME(ms)";

time=120; # time bound

echo "$column" > result-complement-easy2.csv
for case in easy/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -complement easy$NUM.ba $case >> result-complement-easy2.csv";
	echo $command
	eval $command
	# optimized 1
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt1 -complement easy$NUM-opt1.ba $case >> result-complement-easy2.csv";
	echo $command
	eval $command
	# optimized 2
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt2 -complement easy$NUM-opt2.ba $case >> result-complement-easy2.csv";
	echo $command
	eval $command
done


echo "$column" > result-complement-normal2.csv
# solve normal cases
for case in normal/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -complement normal$NUM.ba $case -set 3 >> result-complement-normal2.csv";
	echo $command
	eval $command
	# optimized
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt1 -complement normal$NUM-opt1.ba $case -set 3 >> result-complement-normal2.csv";
	echo $command
	eval $command
	# optimized
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt2 -complement normal$NUM-opt2.ba $case -set 3 >> result-complement-normal2.csv";
	echo $command
	eval $command
done



echo "$column" > result-complement-diff2.csv
# solve difficult cases
for case in difficult/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -complement diff$NUM.ba $case -set 3 >> result-complement-diff2.csv";
	echo $command
	eval $command
	# optimized
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt1 -complement diff$NUM-opt1.ba $case -set 3 >> result-complement-diff2.csv";
	echo $command
	eval $command
	# optimized
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt2 -complement diff$NUM-opt2.ba $case -set 3 >> result-complement-diff2.csv";
	echo $command
	eval $command
done
