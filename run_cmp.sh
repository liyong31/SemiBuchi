#!/bin/bash

cd target

# solve easy cases

# output columns
column="FILE,OP_STATES,OP_TRANS,OP_ALPHABET_SIZE";
column="$column,ALGORITHM,RESULT_STATES, RESULT_TRANS,RUNTIME(ms)";

time=120; # time bound

echo "$column" > result-complement-easy.csv
for case in ./classes/benchmarks/easy/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
	# NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -complement easy.ba $case -set 3 >> result-complement-easy.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt -complement easy-opt.ba $case -set 3 >> result-complement-easy.csv";
	echo $command
	eval $command
done


echo "$column" > result-complement-normal.csv
# solve normal cases
for case in ./classes/benchmarks/normal/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
	# NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -complement normal0.ba $case -set 3 >> result-complement-normal.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt -complement normal0-opt.ba $case -set 3 >> result-complement-normal.csv";
	echo $command
	eval $command
done



echo "$column" > result-complement-diff.csv
# solve difficult cases
for case in ./classes/benchmarks/difficult/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
    # NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -complement diff0.ba $case -set 3 >> result-complement-diff.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt -complement diff0-opt.ba $case -set 3 >> result-complement-diff.csv";
	echo $command
	eval $command
done
