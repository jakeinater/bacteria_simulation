call python dir_to_undir.py "LOOP_optimizedEcoliGraph" -n
call python already_undir.py "optimizedDiff" -n -i
call python test.py "optimizedDiff" -n -i

