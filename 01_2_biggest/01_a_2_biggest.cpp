#include <iostream>
#include <mpi.h>
#include <ctime>

int main(int argc, char** argv) {
    srand(time(NULL));
    clock_t begin;
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    int n; // Number of integers in the set
    int* numbers; // Array to store the numbers

    if (rank == 0) {
        std::cout << "Enter the number of integers: ";
        std::cin >> n;
        //n = 500000000;

        numbers = new int[n]; // Allocate memory for the numbers array

        for (int i = 0; i < n; i++) {
            numbers[i] = rand() % 10000;
        }
        begin = clock();
    }

    // Broadcast the number of integers to all processes
    MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD);

    // Calculate the number of integers per process
    int chunkSize = n / size;
    int remainder = n % size;

    // Allocate memory for the local numbers array
    int localSize = (rank < remainder) ? chunkSize + 1 : chunkSize;
    int* localNumbers = new int[localSize];

    // Scatter the numbers to all processes
    MPI_Scatter(numbers, chunkSize, MPI_INT, localNumbers, localSize, MPI_INT, 0, MPI_COMM_WORLD);

    // Find the local maximum and second maximum
    int localMax = localNumbers[0];
    int localSecondMax = localNumbers[0];
    for (int i = 1; i < localSize; i++) {
        if (localNumbers[i] > localMax) {
            localSecondMax = localMax;
            localMax = localNumbers[i];
        } else if (localNumbers[i] > localSecondMax) {
            localSecondMax = localNumbers[i];
        }
    }

    // Gather the local maximum and second maximum to process 0
    int* maxValues = nullptr;
    int* secondMaxValues = nullptr;
    if (rank == 0) {
        maxValues = new int[size];
        secondMaxValues = new int[size];
    }
    MPI_Gather(&localMax, 1, MPI_INT, maxValues, 1, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Gather(&localSecondMax, 1, MPI_INT, secondMaxValues, 1, MPI_INT, 0, MPI_COMM_WORLD);

    // Process 0 finds the global maximum and second maximum
    if (rank == 0) {
        int globalMax = maxValues[0];
        int globalSecondMax = secondMaxValues[0];
        for (int i = 1; i < size; i++) {
            if (maxValues[i] > globalMax) {
                globalSecondMax = globalMax;
                globalMax = maxValues[i];
            } else if (maxValues[i] > globalSecondMax) {
                globalSecondMax = maxValues[i];
            }
        }

        // Print the results
        std::cout << "The two largest numbers are: " << globalMax << " and " << globalSecondMax << std::endl;
        std::cout << "The time taken is: " << (double)(clock() - begin) / (double)CLOCKS_PER_SEC << " seconds" << std::endl;
        // Deallocate memory
        delete[] numbers;
        delete[] maxValues;
        delete[] secondMaxValues;
    }

    // Deallocate memory
    delete[] localNumbers;

    MPI_Finalize();
    return 0;
}