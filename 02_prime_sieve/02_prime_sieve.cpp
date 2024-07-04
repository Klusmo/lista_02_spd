#include <iostream>
#include <cmath>
#include <mpi.h>

void parallelSieve(int n, int rank, int size) {
    int blockSize = (n - 1) / size + 1;
    int start = rank * blockSize + 2;
    int end = std::min(start + blockSize, n + 1);

    std::cout << "Rank " << rank << " has range " << start << " to " << end << std::endl;

    bool *isPrime = new bool[blockSize];
    for (int i = 0; i < blockSize; i++) {
        isPrime[i] = true;
    }

    for (int p = 2; p * p <= n; p++) {
        int firstMultiple;
        if (p * p > start) {
            firstMultiple = p * p - start;
        } else {
            int offset = start % p;
            firstMultiple = offset == 0 ? 0 : p - offset;
        }
        for (int i = firstMultiple; i < blockSize; i += p) {
            isPrime[i] = false;
        }
    }

    int *counts = new int[size];
    int *displacements = new int[size];
    MPI_Allgather(&blockSize, 1, MPI_INT, counts, 1, MPI_INT, MPI_COMM_WORLD);

    int totalPrimes = 0;
    for (int i = 0; i < size; i++) {
        displacements[i] = totalPrimes;
        totalPrimes += counts[i];
    }

    bool *allPrimes = new bool[totalPrimes];
    MPI_Allgatherv(isPrime, blockSize, MPI_BYTE, allPrimes, counts, displacements, MPI_BYTE, MPI_COMM_WORLD);

    if (rank == 0) {
        std::cout << "Prime numbers up to " << n << " are:\n";
        for (int i = 0; i < totalPrimes; i++) {
            if (allPrimes[i]) {
                std::cout << i + 2 << " ";
            }
        }
        std::cout << std::endl;
    }

    delete[] isPrime;
    delete[] counts;
    delete[] displacements;
    delete[] allPrimes;
}

int main(int argc, char *argv[]) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    int n = 100; // Define the maximum value to find prime numbers

    parallelSieve(n, rank, size);

    MPI_Finalize();
    return 0;
}