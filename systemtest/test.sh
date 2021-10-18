#!/bin/sh

PREFIX=test
TESTS='00 01 02 03'
PROG=TestHarness
count=0
DEBUG=0

if [ "$1" == "debug" ]; then
  let "DEBUG=1"
fi

if [ ! -f $PROG.java ]; then
  echo Error: $PROG.java is not in the current directory 
  exit 1
fi

javac $PROG.java 
if [ $? -eq 0 ]; then 
  for T in $TESTS; do
		PRETEST=${PREFIX}${T}
    if [ -f ${PRETEST}.in ]; then  
      echo ===========================================
      echo Test file: ${PRETEST}.in 
      java $PROG < ${PRETEST}.in > ${PRETEST}.out
      if diff ${PRETEST}.out ${PRETEST}.gold > /dev/null; then
        echo " " PASSED
        let "count = count + 1"
      elif [ $DEBUG -eq 1 ]; then
        echo Test $T failed, here are the differences
        echo My program output "                     " Expected program output
        echo ================= "                     " =======================
        diff -W 80 -y ${PRETEST}.out ${PRETEST}.gold | more
        exit 1
      else
        echo " " FAILED
      fi
      # rm ${PRETEST}.out
    else
      echo Oops: file ${PRETEST}.in does not exist!
    fi
  done
fi

echo =============================================
echo $count tests passed
