::non-uniform unlabelled
call python undir.py putida_non-uni_both-ends -n
call python undir.py putida_non-uni_start -n 
call python undir.py putida_non-uni_end -n 

::non-uniform labelled
call python undir.py putida_non-uni_both-ends -l -n 
call python undir.py putida_non-uni_start -l -n
call python undir.py putida_non-uni_end -l -n

::uniform unlabelled
call python undir.py putida_uni_both-ends
call python undir.py putida_uni_start  
call python undir.py putida_uni_end 

::uniform labelled
call python undir.py putida_uni_both-ends -l
call python undir.py putida_uni_start -l 
call python undir.py putida_uni_end -l 


