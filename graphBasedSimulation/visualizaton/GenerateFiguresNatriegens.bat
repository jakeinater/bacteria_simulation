::non-uniform unlabelled
call python dir_to_undir.py natriegens_non-uni_both-ends -n
call python dir_to_undir.py natriegens_non-uni_start -n 
call python dir_to_undir.py natriegens_non-uni_end -n 

::non-uniform labelled
call python dir_to_undir.py natriegens_non-uni_both-ends -l -n 
call python dir_to_undir.py natriegens_non-uni_start -l -n
call python dir_to_undir.py natriegens_non-uni_end -l -n

::uniform unlabelled
call python dir_to_undir.py natriegens_uni_both-ends
call python dir_to_undir.py natriegens_uni_start  
call python dir_to_undir.py natriegens_uni_end 

::uniform labelled
call python dir_to_undir.py natriegens_uni_both-ends -l
call python dir_to_undir.py natriegens_uni_start -l 
call python dir_to_undir.py natriegens_uni_end -l 


