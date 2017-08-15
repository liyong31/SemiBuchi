#!/bin/bash

# jump to target folder
cd target

# output columns
column="FILE,LHS_SEMIDETERMINISTIC,RHS_SEMIDETERMINISTIC,LHS_STATES,RHS_STATES,LHS_TRANS,RHS_TRANS,ALPHABET_SIZE";
column="$column,PAIR_REJECT_ANTICHAIN,PAIR_DELETE_ANTICHAIN,PAIR_INGNORE_ANTICHAIN,PAIR_LEFT_ANTICHAIN";
column="$column,TRANS_USED_SND_BUCHI,RESULT_STATES,ALGORITHM,RUNTIME(ms),RESULT";


time=120; # time bound

# solve easy cases
echo "$column" > result-opt-easy.csv
for case in easy/*.ats
do
	command="java -jar SemiBuchi-0.0.1.jar -tarjan -to $time $case -set 3 >> result-opt-easy.csv";
	echo $command
	eval $command
	# optimized
	command="java -jar SemiBuchi-0.0.1.jar -tarjan -opt -to $time $case -set 3 >> result-opt-easy.csv";
	echo $command
	eval $command
done

echo "$column" > result-opt-normal.csv
# solve normal cases
for case in normal/*.ats
do
	command="java -jar SemiBuchi-0.0.1.jar -tarjan -to $time $case -set 3 >> result-opt-normal.csv";
	echo $command
	eval $command
	# optimized
	command="java -jar SemiBuchi-0.0.1.jar -tarjan -opt -to $time $case -set 3 >> result-opt-normal.csv";
	echo $command
	eval $command
done
:<<'END'
Hello
END
# solve difficult cases
echo "$column" > result-opt-diff.csv
for case in difficult/*.ats
do
	command="java -Xms3g -Xms3G -Xmx3g -Xmx3g -jar SemiBuchi-0.0.1.jar -tarjan -to $time $case -set 3 >> result-opt-diff.csv";
	echo $command
	eval $command
	# optimized
	command="java -Xms3g -Xms3G -Xmx3g -Xmx3g -jar SemiBuchi-0.0.1.jar -tarjan -opt -to $time $case -set 3 >> result-opt-diff.csv";
	echo $command
	eval $command
done

