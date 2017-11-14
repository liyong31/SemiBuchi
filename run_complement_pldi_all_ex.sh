#!/bin/bash

cd target

# output columns
column="FILE,NONUSE, OP_STATES,OP_TRANS,OP_ALPHABET_SIZE";
column="$column,RESULT_STATES, RESULT_TRANS,ALGORITHM,RUNTIME(ms),STATUS";

time=305; # time bound

echo "$column" > result-complement-all-eo.csv
for case in semis/*.ats
do
	# NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -complement easy.ba $case -set 3 >> result-complement-all-eo.csv";
	echo $command
	eval $command
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -complement easye.ba $case -eo -set 3 >> result-complement-all-eo.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -lazys -complement easy-opt.ba $case -set 3 >> result-complement-all-eo.csv";
	echo $command
	eval $command
		# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -lazys -complement easy-opte.ba $case -eo -set 3 >> result-complement-all-eo.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -lazys -lazyb -complement easy-opt1.ba $case -set 3 >> result-complement-all-eo.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -to 300 -lazys -lazyb -complement easy-opte1.ba $case -eo -set 3 >> result-complement-all-eo.csv";
	echo $command
	eval $command
done
