!/bin/bash

# jump to target folder
cd target

# output columns
column="FILE,LHS_SEMIDETERMINISTIC,RHS_SEMIDETERMINISTIC,LHS_STATES,RHS_STATES,LHS_TRANS,RHS_TRANS,ALPHABET_SIZE,RHS_ALPHABET";
column="$column,PAIR_REJECT_ANTICHAIN,PAIR_DELETE_ANTICHAIN,PAIR_INGNORE_ANTICHAIN,PAIR_LEFT_ANTICHAIN";
column="$column,TRANS_USED_SND_BUCHI,RESULT_STATES,ALGORITHM,RUNTIME(ms),RESULT";
echo "$column" > result.csv

time=60; # time bound

# solve easy cases
for case in classes/benchmarks/easy/*.ats
do
   for method in "-tarjan" "-ascc -to" "-ascc" "-rabit";
   do
        echo "java -jar SemiBuchi-0.0.1.jar $method -to $time $case >> result.csv"
		java -jar SemiBuchi-0.0.1.jar "$method" -to "$time" "$case" >> result.csv
   done
done

# solve normal cases
for case in classes/benchmarks/normal/*.ats
do
   for method in "-tarjan" "-ascc -to" "-ascc" "-rabit";
   do
        echo "java -jar SemiBuchi-0.0.1.jar $method -to $time $case >> result.csv"
		java -jar SemiBuchi-0.0.1.jar "$method" -to "$time" "$case" >> result.csv
   done
done

# solve difficult cases
for case in classes/benchmarks/difficult/*.ats
do
   for method in "-tarjan" "-ascc -to" "-ascc" "-rabit";
   do
        echo "java -jar SemiBuchi-0.0.1.jar $method -to $time $case >> result.csv"
		java -jar SemiBuchi-0.0.1.jar "$method" -to "$time" "$case" >> result.csv
   done
done
