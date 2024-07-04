
run_01:
	mpicxx 01_2_biggest/01_a_2_biggest.cpp -o ./build/01_a_2_biggest.exe
	mpirun -n 4 ./build/01_a_2_biggest.exe

run_02:
	mpicxx 02_prime_sieve/02_prime_sieve.cpp -o ./build/02_prime_sieve.exe
	mpirun -n 4 ./build/02_prime_sieve.exe