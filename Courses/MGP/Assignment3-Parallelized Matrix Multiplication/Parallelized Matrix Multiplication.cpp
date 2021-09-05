#include "matmul.h"

void matmul_ref(const int* const matrixA, const int* const matrixB,
                int* const matrixC, const int n) {
  // You can assume matrixC is initialized with zero
  for (int i = 0; i < n; i++)
    for (int j = 0; j < n; j++)
      for (int k = 0; k < n; k++)
        matrixC[i * n + j] += matrixA[i * n + k] * matrixB[k * n + j];
}

void matmul_optimized(const int* const matrixA, const int* const matrixB,
                      int* const matrixC, const int n) {
  // TODO: Implement your code

  // transpose B
  int* B = new int[n*n]; // transposed matrixB
  #pragma omp parallel for collapse(2)
  for(int i=0; i<n; i++){
    for(int j=0; j<n; j++){
      B[j * n + i] = matrixB[i * n + j];
    }
  }

  // blocked multiplication with loop reordering
  int block_size = 32;
  int cutoff = n - (n%block_size); // last block index
  #pragma omp parallel for collapse(4)
  for(int i=0; i<cutoff; i+=block_size){
    for(int j=0; j<cutoff; j+=block_size){
      for(int ii=0; ii<block_size; ii++){
        for(int jj=0; jj<block_size; jj++){
	  int temp = 0;
	  for(int k=0; k<n; k++){
	    temp += matrixA[(ii+i)*n+k] * B[(jj+j)*n+k];
	  }
	  matrixC[(i+ii)*n+(jj+j)] = temp;
	}
      }
    }
  }

  // remainder of blocked multiplication
  // incase of n is not fit to block size
  if(n % block_size > 0){
    // remainder block 1, right block	
    #pragma omp parallel for collapse(2)  
    for(int i=cutoff; i<n; i++){
      for(int j=0; j<n; j++){
	int temp = 0;
	for(int k=0; k<n; k++){
	  temp += matrixA[i*n+k] * B[j*n+k];
	}
	matrixC[i*n+j] = temp;
      }
    }
    // remainder block 2, lower block
    #pragma omp parallel for collapse(2)
    for(int i=0; i<cutoff; i++){
      for(int j=cutoff; j<n; j++){
	int temp=0;
	for(int k=0; k<n; k++){
          temp += matrixA[i*n+k] * B[j*n+k];
	}
	matrixC[i*n+j] = temp;
      }
    }    
  }
  delete B;
  B = NULL;

}
