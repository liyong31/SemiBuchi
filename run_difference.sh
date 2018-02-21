#!/bin/bash

cd target

# solve easy cases

# output columns
column="FILE,LHS_SEMIDETERMINISTIC,RHS_SEMIDETERMINISTIC,LHS_STATES,RHS_STATES,LHS_TRANS,RHS_TRANS,ALPHABET_SIZE";
column="$column,PAIR_REJECT_ANTICHAIN,PAIR_DELETE_ANTICHAIN,PAIR_INGNORE_ANTICHAIN,PAIR_LEFT_ANTICHAIN";
column="$column,TRANS_USED_SND_BUCHI,RESULT_STATES,ALGORITHM,RUNTIME(ms),RESULT";

time=120; # time bound

echo "$column" > result-diff-easy.csv
for case in easy/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
	# NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -diff -tarjan $case -set 3 >> result-diff-easy.csv";
	echo $command
	eval $command
	# optimized NCSB
#	command="timeout $time java -jar SemiBuchi-0.0.1.jar -diff -lazy -complement easy$NUM-opt.ba $case -set 3 >> result-complement-easy.csv";
#	echo $command
#	eval $command
done
:<<'END'

echo "$column" > result-complement-normal.csv
# solve normal cases
for case in normal/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
	# NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -complement normal$NUM.ba $case -set 3 >> result-complement-normal.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt -complement normal$NUM-opt.ba $case -set 3 >> result-complement-normal.csv";
	echo $command
	eval $command
done



echo "$column" > result-complement-diff.csv
# solve difficult cases
for case in difficult/*.ats
do
    NUM=$(echo $case | sed 's/[^0-9]*//g')
    echo "$NUM"
    # NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -complement diff$NUM.ba $case -set 3 >> result-complement-diff.csv";
	echo $command
	eval $command
	# optimized NCSB
	command="timeout $time java -jar SemiBuchi-0.0.1.jar -opt -complement diff$NUM-opt.ba $case -set 3 >> result-complement-diff.csv";
	echo $command
	eval $command
done
END
