#!/bin/bash

cd target

# solve easy cases

# output columns
column="FILE,OP_STATES,OP_TRANS,OP_ALPHABET_SIZE";
column="$column,ALGORITHM,RESULT_STATES, RESULT_TRANS,RUNTIME(ms)";

time=120; # time bound

echo "$column" > result-complement-easy2.csv
for case in classes/benchmarks/easy/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
	# NCSB
	command="timeout $time java -ea -jar SemiBuchi-0.0.1.jar -complement easy.ba $case -beqc -set 3 >> result-complement-easy2.csv";
	echo $command
	eval $command
	# NCSB dc
	command="timeout $time java -ea -jar SemiBuchi-0.0.1.jar -complement easy1.ba $case -set 3 >> result-complement-easy2.csv";
	echo $command
	eval $command
	# NCSB opt
	command="timeout $time java -ea -jar SemiBuchi-0.0.1.jar -complement easy-opt.ba $case -beqc -opt -set 3 >> result-complement-easy2.csv";
	echo $command
	eval $command
	# NCSB opt dc
	command="timeout $time java -ea -jar SemiBuchi-0.0.1.jar -opt -complement easy-opt1.ba $case -set 3 >> result-complement-easy2.csv";
	echo $command
	eval $command
done


echo "$column" > result-complement-normal2.csv
# solve normal cases
for case in classes/benchmarks/normal/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
    # NCSB
	command="timeout $time java -ea -jar SemiBuchi-0.0.1.jar -complement normal.ba $case -beqc -set 3 >> result-complement-normal2.csv";
	echo $command
	eval $command
	# NCSB dc
	command="timeout $time java -ea -jar SemiBuchi-0.0.1.jar -complement normal1.ba $case -set 3 >> result-complement-normal2.csv";
	echo $command
	eval $command
	# NCSB opt
	command="timeout $time java -ea -jar SemiBuchi-0.0.1.jar -opt -beqc -complement normal-opt.ba $case -set 3 >> result-complement-normal2.csv";
	echo $command
	eval $command
	# NCSB opt dc
	command="timeout $time java -ea -jar SemiBuchi-0.0.1.jar -opt -complement normal-opt1.ba $case -set 3 >> result-complement-normal2.csv";
	echo $command
	eval $command
done


:<<'END'
echo "$column" > result-complement-diff1.csv
# solve difficult cases
for case in difficult/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
    # NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -complement diff$NUM.ba $case -beqc -set 3 >> result-complement-diff1.csv";
	echo $command
	eval $command
    # NCSB dc
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -complement diff$NUM.ba $case -set 3 >> result-complement-diff1.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt -complement diff$NUM-opt.ba $case -set 3 >> result-complement-diff1.csv";
	echo $command
	eval $command
done

END
