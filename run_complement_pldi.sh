#!/bin/bash

cd target

# output columns
column="FILE,NONUSE, OP_STATES,OP_TRANS,OP_ALPHABET_SIZE";
column="$column,RESULT_STATES, RESULT_TRANS,ALGORITHM,RUNTIME(ms),STATUS";

time=305; # time bound

echo "$column" > result-complement.csv
for case in classes/benchmarks/easy/*.ats
do
	# NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -complement easy.ba $case -set 3 >> result-complement.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -lazys -complement easy-opt.ba $case -set 3 >> result-complement.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -lazys -lazyb -complement easy-opt1.ba $case -set 3 >> result-complement.csv";
	echo $command
	eval $command
done


# solve normal cases
for case in classes/benchmarks/normal/*.ats
do
	# NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -complement normal.ba $case -set 3 >> result-complement.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -lazys -complement normal-opt.ba $case -set 3 >> result-complement.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -lazys -lazyb -complement normal-opt1.ba $case -set 3 >> result-complement.csv";
	echo $command
	eval $command
done



# solve difficult cases
for case in classes/benchmarks/difficult/*.ats
do
    # NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -complement diff.ba $case -set 3 >> result-complement.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -lazys -complement diff-opt.ba $case -set 3 >> result-complement.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -lazys -lazyb -complement diff-opt1.ba $case -set 3 >> result-complement.csv";
	echo $command
	eval $command
done
